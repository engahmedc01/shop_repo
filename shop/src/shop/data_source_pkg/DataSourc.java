/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shop.data_source_pkg;

/**
 *
 * @author SHOP
 */
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import shop.data_structures_pkg.Person;
import oracle.sql.StructDescriptor;
import org.apache.poi.util.IOUtils;
import shop.GUI.CustomerDischarge_panel;
import shop.StartWindow;
import static shop.TestMain.EXPORT_DIR;
import shop.data_structures_pkg.Pair;
import shop.data_structures_pkg.Table;
import shop.test.main_test;

public class DataSourc {

    static final String USER = "SHOP";
    static final String PASS = "SHOP";
    static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    public Connection con;
    Map<String, String> NormalizMap;

    public DataSourc() {

        super();

        System.out.println("Connecting to database...");
//        StartWindow.connecting_PB.setValue(12);
//        StartWindow.monitor_label.setText("الأتصال بقاعدة البيانات");
        try {

            Class.forName(JDBC_DRIVER);
            // StartWindow.connecting_PB.setValue(25);
            con = DriverManager.getConnection(DB_URL, USER, PASS);

        } catch (ClassNotFoundException | SQLException e) {
            //    StartWindow.monitor_label.setText("Connection fail :(");
            //   StartWindow.fail(1);
        }
        StartWindow.connecting_PB.setValue(50);
        fillNormalizMap();
        System.out.println("Connection successe :)");
        //StartWindow.monitor_label.setText("تم الأتصال بقاعدة البيانات");

    }

    public Map<String, Integer> getSeasons() {
        Map<String, Integer> data = new HashMap<String, Integer>();
        CallableStatement cs = null;
        DateFormat df = new SimpleDateFormat("MMMMMM-YYYY", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        try {
            cs = con.prepareCall("SELECT\n"
                    + "     to_char(SEASON.END_DATE,'DD-MM-YYYY HH24:MI AM') AS END_DATE,\n"
                    + "     to_char(SEASON.START_DATE,'DD-MM-YYYY HH24:MI AM') AS START_DATE, \n"
                    + "     SEASON.SEASON_ID AS SEASON_ID "
                    + " FROM\n"
                    + "     SEASON SEASON\n"
                    + " WHERE\n"
                    + "     SEASON.CURRENT_SEASON =0 \n"
                    + " ORDER BY\n"
                    + "      SEASON.SEASON_ID asc");

            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("SEASON_ID");
                String start = df.format(StringToDate(rs.getString("START_DATE"), "dd-MM-yyyy"));
                String end = df.format(StringToDate(rs.getString("END_DATE"), "dd-MM-yyyy"));
                String seasonName = (start) + "<===>" + (end);
                data.put(seasonName, id);

            }
            rs.close();
            return data;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void closePurchasesPeriod(List<Map> purchasesPrices) {

        CallableStatement cs = null;

        try {
            Object[] Java_TABLE;
            Java_TABLE = new Object[purchasesPrices.size()];
            for (int i = 0; i < purchasesPrices.size(); i++) {

                Java_TABLE[i] = new oracle.sql.STRUCT(new oracle.sql.StructDescriptor("CONFIRME_PURCHASED_OBJ", con), con, purchasesPrices.get(i));
            }
            oracle.sql.ARRAY SQL_Table = new oracle.sql.ARRAY(new oracle.sql.ArrayDescriptor("PURCHASES_BUY_PRICE_COLL", con), con, Java_TABLE);

            cs = con.prepareCall("{?= call CONFIRM_PURCHASED_PRICE(?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setArray(2, SQL_Table);
            cs.execute();

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Vector<Object> getSellersOrders(String name, java.sql.Date orderDate, int fridageID) {

        Vector<Object> data = new Vector<Object>();
        CallableStatement cs = null;
        ResultSet rs = null;
        DateFormat df = new SimpleDateFormat("EEEEEE dd-MMMMMM-YYYY   ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        try {
            cs = con.prepareCall("SELECT\n"
                    + "     SELLER_ORDER.ORDER_ID AS ORDER_ID,\n"
                    + "     SELLER.SELLER_NAME AS SELLER_NAME,\n"
                    + "     SELLER_ORDER.TOTAL_COST AS TOTAL_COST,\n"
                    + "    to_char( SELLER_ORDER.ORDER_DATE,'DD-MM-YYYY HH24:MI AM') AS ORDER_DATE,\n"
                    + "     FRIDAGE.FRIDAGE_NAME AS FRIDAGE_NAME\n"
                    + "FROM\n"
                    + "     SHOP.SELLER SELLER INNER JOIN SHOP.SELLER_ORDER SELLER_ORDER ON SELLER.SELLER_ID = SELLER_ORDER.SELLER_ID\n"
                    + "     INNER JOIN SHOP.FRIDAGE FRIDAGE ON SELLER_ORDER.FRIDAGE_ID = FRIDAGE.FRIDAGE_ID\n"
                    + "	 WHERE\n"
                    + "     SELLER_NAME LIKE ?\n"
                    + "     and FRIDAGE.FRIDAGE_ID=?     "
                    + "     and to_char( ORDER_DATE,'DD-MM-YYYY') LIKE to_char ( ?,'DD-MM-YYYY')");
            cs.setString(1, name);

            cs.setInt(2, fridageID);
            cs.setDate(3, orderDate);
            rs = cs.executeQuery();
            while (rs.next()) {
                Vector<Object> temp = new Vector<Object>();
                String orderID = rs.getString("ORDER_ID");
                String TOTAL_COST = rs.getString("TOTAL_COST");

                java.util.Date ORDER_DATE = StringToDate(rs.getString("ORDER_DATE"), "dd-MM-yyyy hh:mm");
                String orderdate = df.format(ORDER_DATE);

                String FRIDAGE_NAME = rs.getString("FRIDAGE_NAME");

                temp.add(TOTAL_COST);
                temp.add(FRIDAGE_NAME);
                temp.add(orderdate);
                temp.add(orderID);
                data.add(temp);

            }
            rs.close();
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public void insertDraft(double amount, String report, java.sql.Date date) {

        CallableStatement cs = null;

        try {
            cs = con.prepareCall("INSERT INTO DRAFT (AMOUNT,REPORT,DRAFT_DATE) VALUES (?,?,?)");
            cs.setDouble(1, amount);
            cs.setString(2, report);
            cs.setDate(3, date);
            cs.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Vector<Object> selectDraft() {

        CallableStatement cs = null;
        Vector<Object> data = new Vector<Object>();
        DateFormat df = new SimpleDateFormat("EEEEEE dd-MMMMMM-YYYY   ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        try {
            cs = con.prepareCall("select * from draft");

            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                Vector<Object> temp = new Vector<Object>();
                double amount = rs.getDouble("AMOUNT");
                String notes = rs.getString("REPORT");
                String date = df.format(rs.getDate("DRAFT_DATE"));
                temp.add(notes);
                temp.add((String.valueOf(amount)));
                temp.add((date));

                data.add(temp);
            }

            rs.close();
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {

                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return null;

    }

    public double getPurchasedCustomerTotalInst(String name, int season_id) {
        CallableStatement cs = null;

        ResultSet rs = null;

        try {
            cs = con.prepareCall("SELECT\n"
                    + "    \n"
                    + "   sum(  PURCHASED_CUSTOMRER_INSTS.AMOUNT) AS AMOUNT\n"
                    + "  \n"
                    + "FROM\n"
                    + "     CUSTOMER CUSTOMER INNER JOIN PURCHASED_CUSTOMRER_INSTS PURCHASED_CUSTOMRER_INSTS ON CUSTOMER.CUSTOMER_ID = PURCHASED_CUSTOMRER_INSTS.CUSTOMER_ID\n"
                    + "WHERE\n"
                    + "     CUSTOMER.CUSTOMER_NAME LIKE ? ");//and SEASON_ID =?

            cs.setString(1, name);
          //  cs.setInt(2, season_id);
            rs = cs.executeQuery();
            double amount = 0;
            while (rs.next()) {

                amount = rs.getDouble("AMOUNT");

            }
            rs.close();
            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double getPurchasedCustomerTotalDue(String name, int seasonID) {
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = con.prepareCall("SELECT\n"
                    + "     sum(CUSTOMER_ORDER.BUY_PRICE) AS BUY_PRICE\n"
                    + "    \n"
                    + "FROM\n"
                    + "     CUSTOMER CUSTOMER INNER JOIN CUSTOMER_ORDER CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "WHERE\n"
                    + "     CUSTOMER.CUSTOMER_NAME LIKE ?\n"
                    + "     and  CUSTOMER_ORDER.PERIOD_ID =-1 ");//and season_id =?
            cs.setString(1, name);
        //    cs.setInt(2, seasonID);
            rs = cs.executeQuery();
            double amount = 0;
            while (rs.next()) {

                amount = rs.getDouble("BUY_PRICE");

            }
            rs.close();
            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public Vector<Object> getPurchasesorders(String name, int season_id) {
        CallableStatement cs = null;
        ResultSet rs = null;
        Vector<Object> data = new Vector<Object>();
        DateFormat df = new SimpleDateFormat("dd-MMMMMM EEEEEE hh:mm aaaa ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        try {
            cs = con.prepareCall("SELECT\n"
                    + "     CUSTOMER_ORDER.ORDER_ID AS ORDER_ID,\n"
                    + "    to_char( CUSTOMER_ORDER.ORDER_DATE,'DD-MM HH24:MI')as ORDER_DATE,\n"
                    + "     CUSTOMER_ORDER.GROSS_WEIGHT AS GROSS_WEIGHT,\n"
                    + "     CUSTOMER_ORDER.NOLUN as NOLUN \n"
                    + "FROM\n"
                    + "  CUSTOMER INNER JOIN  CUSTOMER_ORDER "
                    + "ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID "
                    + "WHERE\n"
                    + "      CUSTOMER_ORDER.finished = 0\n"
                    + "     and  CUSTOMER_ORDER.PERIOD_ID =0 "
                    + " and  CUSTOMER.CUSTOMER_NAME  like ?  and season_id=?");

            cs.setString(1, name);
            cs.setInt(2, season_id);
            rs = cs.executeQuery();
            while (rs.next()) {
                Vector<Object> temp = new Vector<Object>();
                java.util.Date orderDate = StringToDate(rs.getString("ORDER_DATE"), "dd-MM hh:mm");
                String order_date = df.format(orderDate);

                String order_id = rs.getString("ORDER_ID");
                String grossWeight = rs.getString("GROSS_WEIGHT");
                String noloun = rs.getString("NOLUN");
                temp.add("");
                temp.add("");
                temp.add(noloun);
                temp.add(grossWeight);
                temp.add(order_date);
                temp.add(order_id);

                data.add(temp);
            }
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

    public void logout() {
        CallableStatement cs = null;
        try {

            cs = con.prepareCall("{?= call LOGOUT()}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.execute();
            int x = cs.getInt(1);

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean isUniqueInstance() {

        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = con.prepareCall("select count(active) active_count from login where active=1");

            rs = cs.executeQuery();
            int x = 0;
            while (rs.next()) {
                x = rs.getInt("active_count");
                System.out.println("x = " + x);
                return (x == 0) ? true : false;

            }
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return false;
    }

    public int getproductIDFormCustomerOrder(int id) {

        CallableStatement cs = null;
        int product_id = 0;
        try {
            cs = con.prepareCall("select product_id from customer_order where order_id=" + id);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {

                product_id = rs.getInt("product_id");

            }
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return product_id;
    }

    public void signup(String newUserName, String newPassword, String oldUserName, String oldPassword) {
        CallableStatement cs = null;

        try {
            cs = con.prepareCall("{?= call SIGNUP(?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, newUserName);
            cs.setString(3, newPassword);
            cs.setString(4, oldUserName);
            cs.setString(5, oldPassword);
            cs.execute();
            int x = cs.getInt(1);
            JOptionPane.showMessageDialog(null, x);
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void fillNormalizMap() {

        NormalizMap = new HashMap<>();

        NormalizMap.put("K_L", "عمالة كريم");
        NormalizMap.put("K_S", "موردين كيماوي");
        NormalizMap.put("K_V", "منوعات كريم");
        NormalizMap.put("labours", "العمال");
        NormalizMap.put("maintaince", "صيانة تلاجات");
        NormalizMap.put("varaity", "نثريات");
        NormalizMap.put("allah", "زكاة وصدقات");
        NormalizMap.put("forgivness", "سماحات");
        NormalizMap.put("ORDER_PAY", "دفع فواتيرالعملاء");

        NormalizMap.put("TIPS", "وهبة");

        NormalizMap.put("NOLOUN", "ناولون");

        NormalizMap.put("INTST_PAY", "تسديد فواتير بياعين");
        NormalizMap.put("PURCHASES", "مشتروات");
        NormalizMap.put("IN_LOAN", "دين ع المحل");
        NormalizMap.put("OUT_LOAN", "دين لصالح الحل");
        NormalizMap.put("OUT_PAY_LOAN", "تسديد دين");
        NormalizMap.put("IN_PAY_LOAN", "تحصيل ديون");
        NormalizMap.put("PURCHASES_WITHDRAWALS", "مشتروات");
        NormalizMap.put("ORDER_TIPS", "وهبة فواتير");
        //PURCHASES_WITHDRAWALS ORDER_tIPS
    }

    public boolean validateSeasonClose(int seasonID) {
        CallableStatement cs = null;

        try {
            cs = con.prepareCall("SELECT\n"
                    + "     count(CUSTOMER_ORDER.ORDER_ID) AS ORDER_ID\n"
                    + "FROM\n"
                    + "      CUSTOMER_ORDER \n"
                    + "WHERE\n"
                    + "CUSTOMER_ORDER.DUED=0\n"
                    + "and  \n"
                    + "CUSTOMER_ORDER.SEASON_ID = ?");
            cs.setInt(1, seasonID);
            ResultSet rs = cs.executeQuery();
            int count = 0;
            while (rs.next()) {
                count = rs.getInt("ORDER_ID");

            }
            rs.close();
            return (count == 0);
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public void purchasesWithdrawalTransaction(double amount, String notes) {

        CallableStatement cs = null;

        try {
            cs = con.prepareCall("{?= call PURCHAS_WITHDRAWAL_TRANSACTION(?,?) }");
            cs.setDouble(2, amount);
            cs.setString(3, notes);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.execute();
            int x = cs.getInt(1);
            JOptionPane.showMessageDialog(null, x);
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public double getSellerLoan(int id, int seasonID) {
        CallableStatement cs = null;

        try {
            cs = con.prepareCall("select CURRENT_LOAN from SELLER_LOAN_BAG  where SELLER_ID=? and SEASON_ID =?");
            cs.setInt(1, id);
            cs.setInt(2, seasonID);
            ResultSet rs = cs.executeQuery();
            double CURRENT_LOAN = -1;
            while (rs.next()) {
                CURRENT_LOAN = rs.getDouble("CURRENT_LOAN");
            }

            rs.close();

            return CURRENT_LOAN;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return 0;

    }

    public Vector<Object> getSellerInstallement(String name, int sciencere, int seasonID) {

        Statement st = null;
        Vector<Object> data = new Vector<Object>();
        DateFormat df = new SimpleDateFormat("EEEEEE dd-MMMMMM  hh:mm aaaa ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        try {
            st = con.createStatement();
            int sellerID = sellerSelectAll(name).getId();
            int sellerLoanBagID = getSellerBagID(sellerID, seasonID);
            String query = "select to_char(INSTALMENT_DATE,'DD-MM HH24:MI AM')AS INST_DATE ,AMOUNT,REPORT  "
                    + "from INSTALMENT where SELLER_BAG_LOAN_ID = " + sellerLoanBagID;
            query += (sciencere == 1) ? "" : "  and SCIENCERE = " + sciencere;
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Vector<Object> temp = new Vector<Object>();
                String report = rs.getString("REPORT");
                String SInstDate = rs.getString("INST_DATE");
                String amount = rs.getString("amount");

                temp.add(report);
                temp.add((amount));
                temp.add((df.format(StringToDate(SInstDate, "dd-MM hh:mm "))));

                data.add(temp);

            }

            rs.close();
            return data;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;

    }

    public boolean sellerHasCurrentLoan(String sellerName, int seasonID) {

        int sellerID = sellerSelectAll(sellerName).getId();
        double loan = getSellerLoan(sellerID, seasonID);
        return (loan > 0);

    }

    public Object[][] getActiveSellersLoans(String type, int seasonID) {

        CallableStatement cs = null;
        List<List<String>> data = new ArrayList<>();

        try {
            cs = con.prepareCall("SELECT\n"
                    + "     SELLER.SELLER_NAME AS SELLER_NAME,\n"
                    + "     SELLER_LOAN_BAG.CURRENT_LOAN AS CURRENT_LOAN\n"
                    + "FROM\n"
                    + "      SELLER INNER JOIN SELLER_LOAN_BAG ON SELLER.SELLER_ID = SELLER_LOAN_BAG.SELLER_ID\n"
                    + "where \n"
                    + " SELLER_LOAN_BAG.SEASON_ID=?  \n"
                    + " and SELLER_LOAN_BAG.CURRENT_LOAN > 0 "
                    + " and SELLER.SELLER_TYPE = ? ");
            cs.setInt(1, seasonID);
            cs.setString(2, type);

            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                List<String> temp = new ArrayList<>();
                String sellerName = rs.getString("SELLER_NAME");
                double totalAmount = getSellerOrderTotalPrice(sellerName, seasonID);
                double loan = rs.getDouble("CURRENT_LOAN");
                temp.add(String.valueOf(totalAmount));
                temp.add((String.valueOf(Math.rint(loan))));
                temp.add(sellerName);
                data.add(temp);

            }

            if (data.size() == 0) {
                return null;
            }

            Object[][] table = new Object[data.size()][3];
            for (int i = 0; i < data.size(); i++) {
                List<String> temp = data.get(i);
                for (int j = 0; j < temp.size(); j++) {

                    table[i][j] = temp.get(j);
                }

            }
            rs.close();
            return table;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    public Object[][] getSellersLoans(String type, int seasonID) {

        CallableStatement cs = null;
        List<List<String>> data = new ArrayList<>();

        try {
            cs = con.prepareCall("SELECT\n"
                    + "     SELLER.SELLER_NAME AS SELLER_NAME,\n"
                    + "     SELLER_LOAN_BAG.CURRENT_LOAN AS CURRENT_LOAN\n"
                    + "FROM\n"
                    + "      SELLER INNER JOIN SELLER_LOAN_BAG ON SELLER.SELLER_ID = SELLER_LOAN_BAG.SELLER_ID\n"
                    + "where \n"
                    + " SELLER_LOAN_BAG.SEASON_ID=?  \n"
                    + " and SELLER.SELLER_TYPE = ? ");
            cs.setInt(1, seasonID);
            cs.setString(2, type);

            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                List<String> temp = new ArrayList<>();
                String sellerName = rs.getString("SELLER_NAME");
                double loan = rs.getDouble("CURRENT_LOAN");
                double totalAmount = getSellerOrderTotalPrice(sellerName, seasonID);
                temp.add(String.valueOf(totalAmount));
                temp.add((String.valueOf(Math.rint(loan))));
                temp.add(sellerName);
                data.add(temp);

            }

            if (data.size() == 0) {
                return null;
            }

            Object[][] table = new Object[data.size()][3];
            for (int i = 0; i < data.size(); i++) {
                List<String> temp = data.get(i);
                for (int j = 0; j < temp.size(); j++) {

                    table[i][j] = temp.get(j);
                }

            }
            rs.close();
            return table;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    public double getPeriodPurchasesWithdrawals() {
        CallableStatement cs = null;

        ResultSet rs = null;
        try {

            cs = con.prepareCall("SELECT\n"
                    + "   \n"
                    + "    sum( PURCHASE_WITHDRAWAL.AMOUNT) AS AMOUNT\n"
                    + "    \n"
                    + "FROM\n"
                    + "     PURCHASE_PERIOD INNER JOIN PURCHASE_WITHDRAWAL ON PURCHASE_PERIOD.PURCHASE_PERIOD_ID = PURCHASE_WITHDRAWAL.PERIOD_ID\n"
                    + "WHERE\n"
                    + "     PURCHASE_PERIOD.finished = 0\n"
                    + "     ");
            rs = cs.executeQuery();
            double amount = 0;
            while (rs.next()) {

                amount = rs.getDouble("AMOUNT");

            }

            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int getPurchasedVechile1Count(int SEASON_ID) {
        CallableStatement cs = null;

        ResultSet rs = null;
        try {

            cs = con.prepareCall("SELECT\n"
                    + "\n"
                    + "     count(CUSTOMER_ORDER.ORDER_ID) AS Vechile1Count \n"
                    + "\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "where   CUSTOMER_ORDER.VECHILE_TYPE like 'دبابه'\n"
                    + "and   CUSTOMER.CUSTOMER_NAME like 'مشتروات%'\n"
                    + "and finished=1\n"
                    + "  and SEASON_ID=? "
                    + "\n"
                    + "");
            cs.setInt(1, SEASON_ID);
            rs = cs.executeQuery();
            int amount = 0;
            while (rs.next()) {

                amount = rs.getInt("Vechile1Count");

            }

            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int getPurchasedVechile2Count(int SEASON_ID) {
        CallableStatement cs = null;

        ResultSet rs = null;
        try {

            cs = con.prepareCall("SELECT\n"
                    + "\n"
                    + "     count(CUSTOMER_ORDER.ORDER_ID) AS Vechile2Count \n"
                    + "\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "where   CUSTOMER_ORDER.VECHILE_TYPE like 'جامبو'\n"
                    + "and   CUSTOMER.CUSTOMER_NAME like 'مشتروات%'\n"
                    + "and finished=1\n"
                    + " and SEASON_ID=?"
                    + "\n"
                    + "");
            cs.setInt(1, SEASON_ID);
            rs = cs.executeQuery();
            int count = 0;
            while (rs.next()) {

                count = rs.getInt("Vechile2Count");

            }

            return count;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int getkareemVechile1Count(int seasonID) {
        CallableStatement cs = null;

        ResultSet rs = null;
        try {

            cs = con.prepareCall("SELECT\n"
                    + "\n"
                    + "     count(CUSTOMER_ORDER.ORDER_ID) AS Vechile1Count\n"
                    + "\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "where   CUSTOMER_ORDER.VECHILE_TYPE like 'دبابه'\n"
                    + "and   CUSTOMER.CUSTOMER_NAME like 'كريم_%'\n"
                    + "and finished=1\n"
                    + "  and CUSTOMER_ORDER.SEASON_ID=?");
            cs.setInt(1, seasonID);
            rs = cs.executeQuery();
            int count = 0;
            while (rs.next()) {

                count = rs.getInt("Vechile1Count");

            }

            return count;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int getkareemVechile2Count(int seasonID) {
        CallableStatement cs = null;

        ResultSet rs = null;
        try {

            cs = con.prepareCall("SELECT\n"
                    + "\n"
                    + "     count(CUSTOMER_ORDER.ORDER_ID) AS Vechile2Count\n"
                    + "\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "where   CUSTOMER_ORDER.VECHILE_TYPE like 'جامبو'\n"
                    + "and   CUSTOMER.CUSTOMER_NAME like 'كريم_%'\n"
                    + "and finished=1\n"
                    + "  and CUSTOMER_ORDER.SEASON_ID=?");

            cs.setInt(1, seasonID);
            rs = cs.executeQuery();
            int count = 0;
            while (rs.next()) {

                count = rs.getInt("Vechile2Count");

            }

            return count;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int getCustomerVechile2Count(int seasonID) {
        CallableStatement cs = null;

        ResultSet rs = null;
        try {

            cs = con.prepareCall("SELECT\n"
                    + "    \n"
                    + "   count(  CUSTOMER_ORDER.ORDER_ID) AS Vechile2Count\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "where   CUSTOMER_ORDER.VECHILE_TYPE like 'جامبو'\n"
                    + "and   CUSTOMER.CUSTOMER_NAME  not like 'مشتروات%'\n"
                    + "AND    CUSTOMER.CUSTOMER_NAME not like 'كريم_%'\n"
                    + "and finished=1\n"
                    + "  and SEASON_ID=?");
            cs.setInt(1, seasonID);
            rs = cs.executeQuery();
            int count = 0;
            while (rs.next()) {

                count = rs.getInt("Vechile2Count");

            }

            return count;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int getCustomerVechile1Count(int seasonID) {
        CallableStatement cs = null;

        ResultSet rs = null;
        try {

            cs = con.prepareCall("SELECT\n"
                    + "    \n"
                    + "   count(  CUSTOMER_ORDER.ORDER_ID) AS Vechile1Count\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "where   CUSTOMER_ORDER.VECHILE_TYPE like 'دبابه'\n"
                    + "and   CUSTOMER.CUSTOMER_NAME  not like 'مشتروات%'\n"
                    + "AND    CUSTOMER.CUSTOMER_NAME not like 'كريم_%'\n"
                    + "and finished=1\n"
                    + "  and SEASON_ID=?");
            cs.setInt(1, seasonID);
            rs = cs.executeQuery();
            int count = 0;
            while (rs.next()) {

                count = rs.getInt("Vechile1Count");

            }

            return count;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public double getPurchasedTipsAmount(int seasonID) {
        CallableStatement cs = null;

        ResultSet rs = null;
        try {

            cs = con.prepareCall("SELECT\n"
                    + "     SUM(OUTCOME_DETAIL.AMOUNT) AS AMOUNT\n"
                    + "\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID,\n"
                    + "    OUTCOME_DETAIL\n"
                    + "WHERE\n"
                    + "\n"
                    + "      CUSTOMER.CUSTOMER_NAME LIKE 'مشتروات%'\n"
                    + "     and OUTCOME_DETAIL.ORDER_ID = CUSTOMER_ORDER.ORDER_ID\n"
                    + "and  OUTCOME_DETAIL.OUTCOME_TYPE like 'TIPS'\n"
                    + "and finished=1 and SEASON_ID=?");
            cs.setInt(1, seasonID);
            rs = cs.executeQuery();
            double amount = 0;
            while (rs.next()) {

                amount = rs.getDouble("AMOUNT");

            }

            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if (cs != null) {
                rs.close();
                cs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public double getKareemTipsAmount(int SEASON_ID) {
        CallableStatement cs = null;

        ResultSet rs = null;
        try {

            cs = con.prepareCall("SELECT\n"
                    + "     SUM(OUTCOME_DETAIL.AMOUNT) AS AMOUNT\n"
                    + "\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID,\n"
                    + "     OUTCOME_DETAIL\n"
                    + "WHERE\n"
                    + "\n"
                    + "      CUSTOMER.CUSTOMER_NAME LIKE 'كريم_%'\n"
                    + "     and OUTCOME_DETAIL.ORDER_ID = CUSTOMER_ORDER.ORDER_ID\n"
                    + "and  OUTCOME_DETAIL.OUTCOME_TYPE like 'TIPS'\n"
                    + "and CUSTOMER_ORDER.finished=1  and CUSTOMER_ORDER.SEASON_ID=? \n");
            cs.setInt(1, SEASON_ID);
            rs = cs.executeQuery();
            double amount = 0;
            while (rs.next()) {

                amount = rs.getDouble("AMOUNT");

            }

            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public double getCustomersTipsAmount(int seasonID) {
        CallableStatement cs = null;

        ResultSet rs = null;
        try {

            cs = con.prepareCall("SELECT\n"
                    + "     SUM(OUTCOME_DETAIL.AMOUNT) AS AMOUNT \n"
                    + "\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID,\n"
                    + "     OUTCOME_DETAIL\n"
                    + "WHERE\n"
                    + "      CUSTOMER.CUSTOMER_NAME not LIKE 'كريم_%'\n"
                    + "      and CUSTOMER.CUSTOMER_NAME not LIKE 'مشتروات%'\n"
                    + "      and OUTCOME_DETAIL.ORDER_ID = CUSTOMER_ORDER.ORDER_ID\n"
                    + "      and  OUTCOME_DETAIL.OUTCOME_TYPE like 'TIPS' \n"
                    + "      and finished=1  \n"
                    + " and CUSTOMER_ORDER.SEASON_ID=? ");
            cs.setInt(1, seasonID);
            rs = cs.executeQuery();

            double amount = 0;
            while (rs.next()) {

                amount = rs.getDouble("AMOUNT");

            }

            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double getCustomersOrdersCommssionAmount(int seasonID) {
        CallableStatement cs = null;

        ResultSet rs = null;
        try {

            cs = con.prepareCall(" SELECT\n"
                    + "    \n"
                    + "    sum( CUSTOMER_ORDER.COMMISION) AS COMMISION_amount\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "WHERE\n"
                    + "     CUSTOMER.CUSTOMER_NAME not LIKE 'مشتروات%'\n"
                    + "   and   CUSTOMER.CUSTOMER_NAME not LIKE 'كريم_%'\n"
                    + "and finished =1 and  SEASON_ID=? ");
            cs.setInt(1, seasonID);
            rs = cs.executeQuery();
            double amount = 0;
            while (rs.next()) {

                amount = rs.getDouble("COMMISION_amount");

            }

            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double getCustomersOrdersNetAmount(int seasonID) {
        CallableStatement cs = null;

        ResultSet rs = null;
        try {

            cs = con.prepareCall(" SELECT\n"
                    + "    \n"
                    + "    sum( CUSTOMER_ORDER.NET_PRICE) AS NET_PRICE\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "WHERE\n"
                    + "     CUSTOMER.CUSTOMER_NAME not LIKE 'مشتروات%'\n"
                    + "   and   CUSTOMER.CUSTOMER_NAME not LIKE 'كريم_%'\n"
                    + "and finished =1  and SEASON_ID=? ");
            cs.setInt(1, seasonID);
            rs = cs.executeQuery();
            double amount = 0;
            while (rs.next()) {

                amount = rs.getDouble("NET_PRICE");

            }

            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double getCustomersOrdersPrice(int orderID) {
        CallableStatement cs = null;

        ResultSet rs = null;
        try {

            cs = con.prepareCall(" SELECT\n"
                    + "    \n"
                    + "     CUSTOMER_ORDER.NET_PRICE AS NET_PRICE\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "WHERE\n"
                    + "  order_id=? ");
            cs.setInt(1, orderID);
            rs = cs.executeQuery();
            double amount = 0;
            while (rs.next()) {

                amount = rs.getDouble("NET_PRICE");

            }

            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double getKareemOrdersCommssionAmount(int seasonID) {
        CallableStatement cs = null;

        ResultSet rs = null;
        try {

            cs = con.prepareCall("SELECT\n"
                    + "    sum( CUSTOMER_ORDER.COMMISION) AS COMMISION_amount\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "WHERE\n"
                    + "     CUSTOMER.CUSTOMER_NAME LIKE 'كريم_%'\n"
                    + "and finished =1 and CUSTOMER_ORDER.SEASON_ID=?");
            cs.setInt(1, seasonID);
            rs = cs.executeQuery();
            double amount = 0;
            while (rs.next()) {

                amount = rs.getDouble("COMMISION_amount");

            }

            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public double getKareemTotalOrdersAmount(int seasonID) {
        CallableStatement cs = null;

        ResultSet rs = null;
        try {

            cs = con.prepareCall("SELECT\n"
                    + "    sum( CUSTOMER_ORDER.NET_PRICE) AS NET_PRICE\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "WHERE\n"
                    + "     CUSTOMER.CUSTOMER_NAME LIKE 'كريم_%'\n"
                    + "and finished =1  and CUSTOMER_ORDER.SEASON_ID=?");
            cs.setInt(1, seasonID);
            rs = cs.executeQuery();
            double amount = 0;
            while (rs.next()) {

                amount = rs.getDouble("NET_PRICE");

            }

            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public double getPurchasesOrdersCommssionAmount(int SEASON_ID) {
        CallableStatement cs = null;

        ResultSet rs = null;
        try {

            cs = con.prepareCall(" SELECT\n"
                    + "    \n"
                    + "    sum( CUSTOMER_ORDER.COMMISION) AS COMMISION_amount\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "WHERE\n"
                    + "     CUSTOMER.CUSTOMER_NAME LIKE 'مشتروات%'\n"
                    + "and finished =1  and SEASON_ID=?");
            cs.setInt(1, SEASON_ID);
            rs = cs.executeQuery();
            double amount = 0;
            while (rs.next()) {

                amount = rs.getDouble("COMMISION_amount");

            }

            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public double getPurchasesOrdersNetAmount(int SEASON_ID) {
        CallableStatement cs = null;

        ResultSet rs = null;
        try {

            cs = con.prepareCall(" SELECT\n"
                    + "    \n"
                    + "    sum( CUSTOMER_ORDER.NET_PRICE) AS NET_PRICE\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "WHERE\n"
                    + "     CUSTOMER.CUSTOMER_NAME LIKE 'مشتروات%'\n"
                    + "and finished =1 and SEASON_ID=?");
            cs.setInt(1, SEASON_ID);
            rs = cs.executeQuery();
            double amount = 0;
            while (rs.next()) {

                amount = rs.getDouble("NET_PRICE");

            }

            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public Vector<Object> getAllOCustomersNames(int fridageID, int seasonID) {
        CallableStatement cs = null;
        Vector<Object> data = new Vector<Object>();
        ResultSet rs = null;
        try {

            cs = con.prepareCall(" SELECT\n"
                    + "   \n"
                    + "     DISTINCT(CUSTOMER.CUSTOMER_NAME) AS CUSTOMER_NAME\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "WHERE\n"
                    + "     CUSTOMER_ORDER.FRIDAGE_ID = ? "
                    + " and CUSTOMER_NAME not like 'كريم_%' and CUSTOMER_NAME not like 'مشتروات%'"
                    + " and CUSTOMER_ORDER.SEASON_ID=? ");
            cs.setInt(1, fridageID);
            cs.setInt(2, seasonID);
            rs = cs.executeQuery();
            while (rs.next()) {
                Vector<Object> temp = new Vector<Object>();
                String name = rs.getString("CUSTOMER_NAME");
                temp.add(name);
                data.add(temp);

            }

            return data;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Vector<Object> getAllKareemNames(int fridageID, int SEASON_ID) {
        CallableStatement cs = null;
        Vector<Object> data = new Vector<Object>();
        ResultSet rs = null;
        try {

            cs = con.prepareCall(" SELECT\n"
                    + "   \n"
                    + "    distinct( CUSTOMER.CUSTOMER_NAME) AS CUSTOMER_NAME\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "WHERE\n"
                    + "     CUSTOMER_ORDER.FRIDAGE_ID = ? "
                    + " AND CUSTOMER_NAME  like 'كريم_%'  and CUSTOMER_ORDER.SEASON_ID=?");
            cs.setInt(1, fridageID);
            cs.setInt(2, SEASON_ID);
            rs = cs.executeQuery();
            while (rs.next()) {
                Vector<Object> temp = new Vector<Object>();
                String name = rs.getString("CUSTOMER_NAME");
                temp.add(name);
                data.add(temp);

            }

            return data;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Vector<Object> getAllPurchasesNames(int season_id) {
        CallableStatement cs = null;
        Vector<Object> data = new Vector<Object>();
        ResultSet rs = null;
        try {

            cs = con.prepareCall(" SELECT\n"
                    + "     CUSTOMER.CUSTOMER_NAME AS CUSTOMER_NAME\n"
                    + "FROM\n"
                    + "      CUSTOMER\n"
                    + "where  CUSTOMER_NAME  like 'مشتروات%'  ");
            rs = cs.executeQuery();
            while (rs.next()) {
                Vector<Object> temp = new Vector<Object>();
                String name = rs.getString("CUSTOMER_NAME");
                double cost = getPurchasedCustomerTotalDue(name, season_id);
                double paid = getPurchasedCustomerTotalInst(name, season_id);
                temp.add((String.valueOf(cost - paid)));

                temp.add((String.valueOf(paid)));
                temp.add((String.valueOf(cost)));
                temp.add(name);
                data.add(temp);

            }

            return data;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Vector<Object> getAllPurchaseCustomers(int fridageID, int seasonID) {
        CallableStatement cs = null;
        Vector<Object> data = new Vector<Object>();
        ResultSet rs = null;
        try {

            cs = con.prepareCall(" SELECT\n"
                    + "   \n"
                    + "     DISTINCT(CUSTOMER.CUSTOMER_NAME) AS CUSTOMER_NAME\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "WHERE\n"
                    + "     CUSTOMER_ORDER.FRIDAGE_ID = ? "
                    + "and  CUSTOMER_NAME  like 'مشتروات%' and CUSTOMER_ORDER.SEASON_ID=? ");
            cs.setInt(1, fridageID);
            cs.setInt(2, seasonID);
            rs = cs.executeQuery();
            while (rs.next()) {
                Vector<Object> temp = new Vector<Object>();
                String name = rs.getString("CUSTOMER_NAME");

                temp.add(name);
                data.add(temp);

            }

            return data;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public double getSumCommision(String name, int seasonID) {
        CallableStatement cs = null;

        ResultSet rs = null;
        try {

            cs = con.prepareCall(" SELECT\n"
                    + "    \n"
                    + "    sum( CUSTOMER_ORDER.COMMISION) AS COMMISION_amount\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "WHERE\n"
                    + "     CUSTOMER.CUSTOMER_NAME LIKE ? \n"
                    + "and finished =1 and CUSTOMER_ORDER.SEASON_ID=? ");
            cs.setString(1, name);
            cs.setInt(2, seasonID);
            rs = cs.executeQuery();
            double amount = 0;
            while (rs.next()) {

                amount = rs.getDouble("COMMISION_amount");

            }

            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return 0;
    }

    public double getSumTotalOrders(String name, int seasonID) {
        CallableStatement cs = null;

        ResultSet rs = null;
        try {

            cs = con.prepareCall(" SELECT\n"
                    + "    \n"
                    + "    sum( CUSTOMER_ORDER.NET_PRICE) AS NET_PRICE\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "WHERE\n"
                    + "     CUSTOMER.CUSTOMER_NAME LIKE ? \n"
                    + "and finished =1  and CUSTOMER_ORDER.SEASON_ID=? ");
            cs.setString(1, name);
            cs.setInt(2, seasonID);
            rs = cs.executeQuery();
            double amount = 0;
            while (rs.next()) {

                amount = rs.getDouble("NET_PRICE");

            }

            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return 0;
    }

    public double getSumTips(String name, int seasonID) {
        CallableStatement cs = null;

        ResultSet rs = null;
        try {

            cs = con.prepareCall("SELECT\n"
                    + "     SUM(OUTCOME_DETAIL.AMOUNT) AS AMOUNT\n"
                    + "\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID,\n"
                    + "    OUTCOME_DETAIL\n"
                    + "WHERE\n"
                    + "\n"
                    + "      CUSTOMER.CUSTOMER_NAME  LIKE ? \n"
                    + "     and OUTCOME_DETAIL.ORDER_ID = CUSTOMER_ORDER.ORDER_ID\n"
                    + "and  OUTCOME_DETAIL.OUTCOME_TYPE like 'TIPS'\n"
                    + "and finished=1  "
                    + "and CUSTOMER_ORDER.SEASON_ID=?\n");
            cs.setString(1, name);
            cs.setInt(2, seasonID);
            rs = cs.executeQuery();
            double amount = 0;
            while (rs.next()) {

                amount = rs.getDouble("AMOUNT");

            }

            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int getVechile1Count(String name, int seasonID) {
        CallableStatement cs = null;

        ResultSet rs = null;
        try {

            cs = con.prepareCall("SELECT\n"
                    + "    \n"
                    + "   count(  CUSTOMER_ORDER.ORDER_ID) AS Vechile1Count\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + " where   CUSTOMER_ORDER.VECHILE_TYPE like 'دبابه' \n"
                    + "  and   CUSTOMER.CUSTOMER_NAME   like ? \n"
                    + "and finished = 1\n"
                    + " and CUSTOMER_ORDER.SEASON_ID=?");
            cs.setString(1, name);
            cs.setInt(2, seasonID);
            rs = cs.executeQuery();
            int count = 0;
            while (rs.next()) {

                count = rs.getInt("Vechile1Count");

            }

            return count;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                rs.close();
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public int getVechile2Count(String name, int seasonID) {
        CallableStatement cs = null;

        ResultSet rs = null;
        try {

            cs = con.prepareCall("SELECT\n"
                    + "    \n"
                    + "   count(  CUSTOMER_ORDER.ORDER_ID) AS Vechile2Count\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "where   CUSTOMER_ORDER.VECHILE_TYPE like 'جامبو'\n"
                    + "and   CUSTOMER.CUSTOMER_NAME   like ? \n"
                    + "and finished=1\n"
                    + " and CUSTOMER_ORDER.SEASON_ID=? ");
            cs.setString(1, name);
            cs.setInt(2, seasonID);
            rs = cs.executeQuery();
            int count = 0;
            while (rs.next()) {

                count = rs.getInt("Vechile2Count");

            }

            return count;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public Vector<Object> getPurchasedCustomerInsts(String name, int seasonID) {

        Vector<Object> data = new Vector<>();
        CallableStatement cs = null;
        DateFormat df = new SimpleDateFormat(" EEEEEE dd-MMMMMM  ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        try {
            cs = con.prepareCall("SELECT\n"
                    + "     to_char(PURCHASED_CUSTOMRER_INSTS.INST_DATE,'DD-MM-YYYY HH24:MI AM') AS INST_DATE,\n"
                    + "     PURCHASED_CUSTOMRER_INSTS.AMOUNT AS AMOUNT,\n"
                    + "     PURCHASED_CUSTOMRER_INSTS.NOTES AS NOTES,\n"
                    + "     CUSTOMER.CUSTOMER_NAME AS CUSTOMER_CUSTOMER_NAME\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN PURCHASED_CUSTOMRER_INSTS ON CUSTOMER.CUSTOMER_ID = PURCHASED_CUSTOMRER_INSTS.CUSTOMER_ID\n"
                    + "where   CUSTOMER.CUSTOMER_NAME like ? and season_id=? order by PURCHASED_CUSTOMRER_INSTS.INST_DATE desc ");

            cs.setString(1, name);
            cs.setInt(2, seasonID);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                Vector<Object> temp = new Vector<>();
                String AMOUNT = rs.getString("AMOUNT");
                String NOTES = rs.getString("NOTES");
                java.util.Date date = StringToDate(rs.getString("INST_DATE"), "dd-MM-yyyy hh:mm ");
                String sDate = df.format(date);

                temp.add(NOTES);
                temp.add(AMOUNT);
                temp.add(sDate);

                data.add(temp);

            }

            rs.close();
            return data;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void purchasedCustomerInstPay(String name, double amount, String notes, int fridageID) {

        CallableStatement cs = null;
        try {
            cs = con.prepareCall("{?=call PURCHASED_PAY_TRANSACTION(?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, name);
            cs.setDouble(3, amount);
            cs.setString(4, notes);
            cs.setInt(5, fridageID);
            cs.execute();
            JOptionPane.showMessageDialog(null, cs.getInt(1));
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Vector<Object> getCustomers(int finished, int dued, int seasonID, int fridageNumber) {
        Vector<Object> data = new Vector<>();
        CallableStatement cs = null;

        try {
            cs = con.prepareCall("SELECT\n"
                    + "\n"
                    + "     Distinct(CUSTOMER.CUSTOMER_NAME) AS CUSTOMER_NAME\n"
                    + "\n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "WHERE\n"
                    + "     CUSTOMER_ORDER.FINISHED = ?\n"
                    + " and CUSTOMER_ORDER.PERIOD_ID=-1"
                    + " and CUSTOMER_ORDER.DUED=?"
                    + " and CUSTOMER_ORDER.SEASON_ID=?"
                    + " and  CUSTOMER_ORDER.FRIDAGE_ID=?"
            );

            cs.setInt(1, finished);
            cs.setInt(2, dued);
            cs.setInt(3, seasonID);
            cs.setInt(4, fridageNumber);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {

                Vector<Object> temp = new Vector<>();
                temp.add(rs.getString("CUSTOMER_NAME"));
                data.add(temp);

            }
            rs.close();
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public Vector<Object> getCustomerOrders(String name, int fridageID, int seasonID) {

        Vector<Object> data = new Vector<>();
        CallableStatement cs = null;
        DateFormat df = new SimpleDateFormat(" EEEEEE dd-MMMMMM  ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        try {
            cs = con.prepareCall("SELECT\n"
                    + "     CUSTOMER_ORDER.ORDER_ID AS ORDER_ID,\n"
                    + "\n"
                    + "     CUSTOMER_ORDER.COMMISION AS COMMISION,\n"
                    + "     CUSTOMER_ORDER.TIPS AS TIPS,\n"
                    + "     CUSTOMER_ORDER.NET_WEIGHT AS NET_WEIGHT,\n"
                    + "     CUSTOMER_ORDER.GROSS_WEIGHT AS GROSS_WEIGHT,\n"
                    + "     to_char(CUSTOMER_ORDER.ORDER_DATE,'DD-MM HH24:MI AM') AS ORDER_DATE,\n"
                    + "     CUSTOMER_ORDER.NOLUN AS NOLUN,\n"
                    + "     PRODUCT.PRODUCT_NAME AS PRODUCT_NAME,\n"
                    + "     CUSTOMER_ORDER.TOTAL_PRICE AS TOTAL_PRICE,\n"
                    + "     CUSTOMER_ORDER.NET_PRICE AS NET_PRICE \n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "     INNER JOIN PRODUCT ON CUSTOMER_ORDER.PRODUCT_ID = PRODUCT.PRODUCT_ID\n"
                    + "where CUSTOMER.CUSTOMER_NAME like ? "
                    + "and finished =1 and FRIDAGE_ID = ? and CUSTOMER_ORDER.SEASON_ID=?  order by CUSTOMER_ORDER.ORDER_DATE desc");
            cs.setString(1, name);
            cs.setInt(2, fridageID);
            cs.setInt(3, seasonID);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Vector<Object> temp = new Vector<>();
                String ORDER_ID = rs.getString("ORDER_ID");

                String COMMISION = rs.getString("COMMISION");
                String TIPS = rs.getString("TIPS");
                String NET_WEIGHT = rs.getString("NET_WEIGHT");
                String GROSS_WEIGHT = rs.getString("GROSS_WEIGHT");
                String NOLUN = rs.getString("NOLUN");
                String PRODUCT_NAME = rs.getString("PRODUCT_NAME");
                String TOTAL_PRICE = rs.getString("TOTAL_PRICE");
                String NET_PRICE = rs.getString("NET_PRICE");
                java.util.Date date = StringToDate(rs.getString("ORDER_DATE"), "dd-MM hh:mm ");
                String ORDER_DATE = df.format(date);

                temp.add((NET_PRICE));
                temp.add((COMMISION));
                temp.add((TIPS));
                temp.add((TOTAL_PRICE));
                temp.add((NOLUN));
                temp.add((NET_WEIGHT));
                temp.add((GROSS_WEIGHT));
                temp.add((PRODUCT_NAME));
                temp.add((ORDER_DATE));
                temp.add((ORDER_ID));

                data.add(temp);

            }
            rs.close();
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return null;

    }

    public Vector<Object> getPurchasedOrders(String name, int fridageID, int seasonID) {

        Vector<Object> data = new Vector<>();
        CallableStatement cs = null;
        DateFormat df = new SimpleDateFormat(" EEEEEE dd-MMMMMM  ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        try {
            cs = con.prepareCall("SELECT\n"
                    + "     CUSTOMER_ORDER.ORDER_ID AS ORDER_ID,\n"
                    + "\n"
                    + "     CUSTOMER_ORDER.COMMISION AS COMMISION,\n"
                    + "     CUSTOMER_ORDER.TIPS AS TIPS,\n"
                    + "     CUSTOMER_ORDER.NET_WEIGHT AS NET_WEIGHT,\n"
                    + "     CUSTOMER_ORDER.GROSS_WEIGHT AS GROSS_WEIGHT,\n"
                    + "     to_char(CUSTOMER_ORDER.ORDER_DATE,'DD-MM HH24:MI AM') AS ORDER_DATE,\n"
                    + "     CUSTOMER_ORDER.NOLUN AS NOLUN,\n"
                    + "     PRODUCT.PRODUCT_NAME AS PRODUCT_NAME,\n"
                    + "     CUSTOMER_ORDER.TOTAL_PRICE AS TOTAL_PRICE,\n"
                    + "     CUSTOMER_ORDER.UNITE_PRICE AS UNITE_PRICE,\n"
                    + "     CUSTOMER_ORDER.NET_PRICE AS NET_PRICE \n"
                    + "FROM\n"
                    + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "     INNER JOIN PRODUCT ON CUSTOMER_ORDER.PRODUCT_ID = PRODUCT.PRODUCT_ID\n"
                    + "where CUSTOMER.CUSTOMER_NAME like ? "
                    + "and finished =1 and FRIDAGE_ID = ? and CUSTOMER_ORDER.SEASON_ID=?  order by ORDER_DATE desc");
            cs.setString(1, name);
            cs.setInt(2, fridageID);
            cs.setInt(3, seasonID);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Vector<Object> temp = new Vector<>();
                String ORDER_ID = rs.getString("ORDER_ID");

                String COMMISION = rs.getString("COMMISION");
                String TIPS = rs.getString("TIPS");
                String NET_WEIGHT = rs.getString("NET_WEIGHT");
                String GROSS_WEIGHT = rs.getString("GROSS_WEIGHT");
                String NOLUN = rs.getString("NOLUN");
                String PRODUCT_NAME = rs.getString("PRODUCT_NAME");
                String TOTAL_PRICE = rs.getString("TOTAL_PRICE");
                String NET_PRICE = rs.getString("NET_PRICE");
                java.util.Date date = StringToDate(rs.getString("ORDER_DATE"), "dd-MM hh:mm ");
                String ORDER_DATE = df.format(date);
                String UNITE_PRICE = rs.getString("UNITE_PRICE");

                temp.add((NET_PRICE));
                temp.add((COMMISION));
                temp.add((TIPS));
                temp.add((UNITE_PRICE));
                temp.add((TOTAL_PRICE));
                temp.add((NOLUN));
                temp.add((NET_WEIGHT));
                temp.add((GROSS_WEIGHT));
                temp.add((PRODUCT_NAME));
                temp.add((ORDER_DATE));
                temp.add((ORDER_ID));

                data.add(temp);

            }
            rs.close();
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return null;

    }

//    public Vector<Object> getCustomerOrders(String name,int fridageNumber,int seasonID) {
//
//        Vector<Object> data = new Vector<>();
//        CallableStatement cs = null;
//        DateFormat df = new SimpleDateFormat(" EEEEEE dd-MMMMMM  ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
//
//        try {
//            cs = con.prepareCall("SELECT\n"
//                    + "     CUSTOMER_ORDER.ORDER_ID AS ORDER_ID,\n"
//                    + "\n"
//                    + "     CUSTOMER_ORDER.COMMISION AS COMMISION,\n"
//                    + "     CUSTOMER_ORDER.TIPS AS TIPS,\n"
//                    + "     CUSTOMER_ORDER.NET_WEIGHT AS NET_WEIGHT,\n"
//                    + "     CUSTOMER_ORDER.GROSS_WEIGHT AS GROSS_WEIGHT,\n"
//                    + "     to_char(CUSTOMER_ORDER.ORDER_DATE,'DD-MM HH24:MI AM') AS ORDER_DATE,\n"
//                    + "     CUSTOMER_ORDER.NOLUN AS NOLUN,\n"
//                    + "     PRODUCT.PRODUCT_NAME AS PRODUCT_NAME,\n"
//                    + "     CUSTOMER_ORDER.TOTAL_PRICE AS TOTAL_PRICE,\n"
//                    + "     CUSTOMER_ORDER.NET_PRICE AS NET_PRICE \n"
//                    + "FROM\n"
//                    + "      CUSTOMER INNER JOIN CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
//                    + "     INNER JOIN PRODUCT ON CUSTOMER_ORDER.PRODUCT_ID = PRODUCT.PRODUCT_ID\n"
//                    + "where CUSTOMER.CUSTOMER_NAME like ? "
//                    + "and finished =1  "
//                    + "and CUSTOMER_ORDER.FRIDAGE_ID=? "
//                    + "and CUSTOMER_ORDER.SEASON_ID=?");
//            cs.setString(1, name);
//            cs.setInt(2, fridageNumber);
//            cs.setInt(3, seasonID);
//            ResultSet rs = cs.executeQuery();
//
//            while (rs.next()) {
//                Vector<Object> temp = new Vector<>();
//                String ORDER_ID = rs.getString("ORDER_ID");
//
//                String COMMISION = rs.getString("COMMISION");
//                String TIPS = rs.getString("TIPS");
//                String NET_WEIGHT = rs.getString("NET_WEIGHT");
//                String GROSS_WEIGHT = rs.getString("GROSS_WEIGHT");
//                String NOLUN = rs.getString("NOLUN");
//                String PRODUCT_NAME = rs.getString("PRODUCT_NAME");
//                String TOTAL_PRICE = rs.getString("TOTAL_PRICE");
//                String NET_PRICE = rs.getString("NET_PRICE");
//                java.util.Date date = StringToDate(rs.getString("ORDER_DATE"), "dd-MM hh:mm ");
//                String ORDER_DATE = df.format(date);
//
//                temp.add((NET_PRICE));
//                temp.add((COMMISION));
//                temp.add((TIPS));
//                temp.add((TOTAL_PRICE));
//                temp.add((NOLUN));
//                temp.add((NET_WEIGHT));
//                temp.add((GROSS_WEIGHT));
//                temp.add((PRODUCT_NAME));
//                temp.add((ORDER_DATE));
//                temp.add((ORDER_ID));
//
//                data.add(temp);
//
//            }
//            rs.close();
//            return data;
//        } catch (SQLException ex) {
//            Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//
//            try {
//                cs.close();
//            } catch (SQLException ex) {
//                Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
//        return null;
//
//    }
    public double getSafeBalance() {

        Statement st = null;

        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select BALANCE FROM MY_SAFE WHERE SAFE_ID =1");
            while (rs.next()) {
                double balance = rs.getDouble("BALANCE");
                rs.close();
                return Math.rint(balance);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return 0;

    }

    public boolean sellerHasPriorLoan(int bagID) {

        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            cs = con.prepareCall("SELECT\n"
                    + "     SELLER_LOAN_BAG.PRIOR_LOAN AS PRIOR_LOAN ,NOTES\n"
                    + "FROM\n"
                    + "     SELLER_LOAN_BAG\n"
                    + "WHERE\n"
                    + "   SELLER_LOAN_BAG.LOAN_BAG_ID=?  ");
            cs.setInt(1, bagID);
            rs = cs.executeQuery();
            double priorLoan = 0;
            while (rs.next()) {
                priorLoan = rs.getDouble("PRIOR_LOAN");
            }
            rs.close();
            if (priorLoan > 0) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;

    }

    Vector<Object> getPriorLoanData(int bagID, int PRIOR_LOAN_SETTELED) {

        CallableStatement cs = null;
        ResultSet rs = null;
        Vector<Object> priorLoanData = new Vector<Object>();
        DateFormat df = new SimpleDateFormat(" EEEEEE YYYY/MM/dd ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        try {
            String query = "SELECT\n"
                    + "     SELLER_LOAN_BAG.PRIOR_LOAN AS PRIOR_LOAN ,NOTES\n"
                    + "FROM\n"
                    + "     SELLER_LOAN_BAG\n"
                    + "WHERE\n"
                    + "   SELLER_LOAN_BAG.LOAN_BAG_ID=? ";
            query += (PRIOR_LOAN_SETTELED == 1) ? "" : "    AND PRIOR_LOAN_SETTELED =" + 0;
            cs = con.prepareCall(query);
            cs.setInt(1, bagID);
            rs = cs.executeQuery();
            double priorLoan = 0;
            String notes = "";
            while (rs.next()) {
                priorLoan = rs.getDouble("PRIOR_LOAN");
                notes = rs.getString("NOTES");
                if (notes == null) {
                    return null;
                }//dd-MM-yyyy
                java.util.Date data = StringToDate(notes, "dd/MM/yyyy");
                priorLoanData.add((String.valueOf(priorLoan)));
                priorLoanData.add("");
                priorLoanData.add(("مديونيه سابقه," + df.format(data)));
                priorLoanData.add("");
            }

            rs.close();
            return priorLoanData;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public Vector<Object> getSellerOrders(String name, int SCIENCERE, int PRIOR_LOAN_SETTELED, int seasonID) {

        CallableStatement cs = null;
        DateFormat df = new SimpleDateFormat(" EEEEEE dd-MMMMMM hh:mm aaaa ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        Vector<Object> data = new Vector<Object>();

        try {
            int sellerID = sellerSelectAll(name).getId();
            int sellerLoanBagID = getSellerBagID(sellerID, seasonID);
            //get prior loan of seller
            if (sellerHasPriorLoan(sellerLoanBagID)) {
                Vector<Object> temp = getPriorLoanData(sellerLoanBagID, PRIOR_LOAN_SETTELED);

                if (temp != null) {
                    data.add(temp);
                }
            }
            //get current seller loans
            String query = "SELECT\n"
                    + "     SELLER_ORDER.ORDER_ID AS ORDER_ID,\n"
                    + "     SELLER_ORDER.TOTAL_COST AS TOTAL_COST,\n"
                    + "     to_char(ORDER_DATE,'DD-MM HH24:MI AM') as ORDER_DATE,\n"
                    + "     FRIDAGE.FRIDAGE_NAME AS FRIDAGE_NAME,\n"
                    + "     SELLER_ORDER.NOTES AS NOTES\n"
                    + "FROM\n"
                    + "     FRIDAGE INNER JOIN SELLER_ORDER ON FRIDAGE.FRIDAGE_ID = SELLER_ORDER.FRIDAGE_ID\n"
                    + "where  SELLER_ORDER.SELLER_LOAN_BAG_ID=?  \n"
                    + "  and SELLER_ORDER.SEASON_ID=?   ";
            query += (SCIENCERE == 1) ? "   " : " and SELLER_ORDER.SCIENCERE=" + SCIENCERE;
            query += "    order by ORDER_DATE desc ";
            cs = con.prepareCall(query);//SELLER_ORDER.SCIENCERE=?

            cs.setInt(1, sellerLoanBagID);
            cs.setInt(2, seasonID);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {

                Vector<Object> temp = new Vector<>();
                double totalCost = rs.getDouble("TOTAL_COST");
                String notes = rs.getString("NOTES");
                String SOrderDate = rs.getString("ORDER_DATE");
                String friadageName = rs.getString("FRIDAGE_NAME");
                java.util.Date orderDate = StringToDate(SOrderDate, "dd-MM hh:mm ");

                // temp.add(notes);
                temp.add((String.valueOf(Math.rint(totalCost))));
                temp.add((friadageName));
                temp.add((df.format(orderDate)));
                temp.add((rs.getString("order_id")));

                data.add(temp);

            }

            return data;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    public int getCustomerIdFromCustomerOrder(int orderID) {

        Statement st = null;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select customer_id from customer_order where order_id = " + orderID);
            while (rs.next()) {
                int id = rs.getInt("CUSTOMER_ID");
                rs.close();
                return id;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;

    }

    public java.util.Date getCustomerOrderDate(int orderID) {

        Statement st = null;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select to_char(order_date,'DD-MM-yyyy HH24:MI AM') as order_date from customer_order where order_id = " + orderID);
            while (rs.next()) {

                String s = rs.getString("order_date");
                rs.close();
                return StringToDate(s, "dd-MM-yyyy hh:mm ");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;

    }

    public Vector<Object> getSellerOrderDetail(int orderID) {

        Statement st = null;
        Vector<Object> data = new Vector<Object>();
        DateFormat df = new SimpleDateFormat("dd-MMMMMM hh:mm aaaa ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        try {
            st = con.createStatement();

            ResultSet rs = st.executeQuery("SELECT\n"
                    + "     SELLER_WEIGHT.GROSS_QUANTITY AS GROSS_QUANTITY,\n"
                    + "     SELLER_WEIGHT.WEIGHT_ID AS WEIGHT_ID,\n"
                    + "     SELLER_WEIGHT.CUSTOMER_ORDER_ID AS CUSTOMER_ORDER_ID,\n"
                    + "     SELLER_WEIGHT.NET_QUANTITY AS NET_QUANTITY,\n"
                    + "     SELLER_WEIGHT.UNITE_PRICE AS UNITE_PRICE,\n"
                    + "     SELLER_WEIGHT.AMOUNT AS TOTAL_COST,\n"
                    + "     SELLER_WEIGHT.CUSTOMER_ORDER_ID AS CUSTOMER_ORDER_ID,\n"
                    + "     SELLER_WEIGHT.PACKAGE_NUMBER AS PACKAGE_NUMBER,\n"
                    + "     PRODUCT.PRODUCT_NAME AS PRODUCT_NAME,\n"
                    + "     CUSTOMER.CUSTOMER_ID AS CUSTOMER_ID,\n"
                    + "     CUSTOMER_ORDER.ORDER_DATE AS ORDER_DATE,\n"
                    + "     CUSTOMER_ORDER.STORE_ID AS STORE_ID\n"
                    + "FROM\n"
                    + "     SHOP.PRODUCT PRODUCT INNER JOIN SHOP.SELLER_WEIGHT SELLER_WEIGHT ON PRODUCT.PRODUCT_ID = SELLER_WEIGHT.PRODUCT_ID\n"
                    + "     INNER JOIN SHOP.CUSTOMER_ORDER CUSTOMER_ORDER ON PRODUCT.PRODUCT_ID = CUSTOMER_ORDER.PRODUCT_ID\n"
                    + "     AND CUSTOMER_ORDER.ORDER_ID = SELLER_WEIGHT.CUSTOMER_ORDER_ID\n"
                    + "     INNER JOIN SHOP.CUSTOMER CUSTOMER ON CUSTOMER_ORDER.CUSTOMER_ID = CUSTOMER.CUSTOMER_ID\n"
                    + "WHERE\n"
                    + "     SELLER_WEIGHT.SELLER_ORDER_ID = " + orderID);
            while (rs.next()) {
                Vector<Object> temp = new Vector<Object>();
                double grossWeight = rs.getDouble("GROSS_QUANTITY");
                double netWeight = rs.getDouble("NET_QUANTITY");
                double unitePrice = rs.getDouble("UNITE_PRICE");
                double totalCost = rs.getDouble("TOTAL_COST");
                String storeID = rs.getString("STORE_ID");

                grossWeight = (grossWeight);
                netWeight = (netWeight);
                unitePrice = (unitePrice);
                totalCost = (totalCost);

                double packagenumber = rs.getDouble("PACKAGE_NUMBER");
                String productName = rs.getString("PRODUCT_NAME");
                String weightID = rs.getString("WEIGHT_ID");

                int customerOrderID = rs.getInt("CUSTOMER_ORDER_ID");
                String customerOrderName = "";
                if ((customerOrderID != -1)) {
                    int customer_id = getCustomerIdFromCustomerOrder(customerOrderID);

                    String customerName = "";

                    customerName = customerSelectAll(customer_id).getName();
                    java.util.Date customerOrderdate = getCustomerOrderDate(customerOrderID);
                    String fprmatedDate = df.format(customerOrderdate);
                    customerOrderName = customerName + ", " + fprmatedDate;
                }
                temp.add((customerOrderName));
                temp.add((String.valueOf(totalCost)));
                temp.add((String.valueOf(unitePrice)));
                temp.add((String.valueOf(packagenumber)));
                temp.add((String.valueOf(netWeight)));
                temp.add((String.valueOf(grossWeight)));
                temp.add(productName);
                temp.add("ثلاجة رقم " + storeID);
                temp.add(weightID);
                data.add(temp);

            }

            rs.close();
            return data;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;

    }

    public Object[][] getKareemWithdrawals() {

        Statement st = null;
        DateFormat df = new SimpleDateFormat(" EEEEEE dd-MMMMMM-yyyy ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        List<List<String>> data = new ArrayList<>();
        Object[][] table;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from OUTCOME_DETAIL where OUTCOME_TYPE like 'K_V'");
            while (rs.next()) {
                List<String> temp = new ArrayList<>();

                int outcome_id = rs.getInt("OUTCOME_ID");
                java.util.Date outcome_date = getOutcomeDate(outcome_id);
                String amount = rs.getString("AMOUNT");
                String report = rs.getString("NOTES");

                temp.add((report));
                temp.add((df.format(outcome_date)));
                temp.add((amount));
                data.add(temp);
            }

            table = new Object[data.size()][3];
            for (int i = 0; i < data.size(); i++) {
                List<String> temp = data.get(i);
                for (int j = 0; j < temp.size(); j++) {
                    table[i][j] = temp.get(j);

                }

            }
            rs.close();
            return table;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    String getProductName(int id) {

        Statement st = null;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select product_name from product where product_id = " + id);
            while (rs.next()) {
                String product_name = rs.getString("product_name");
                rs.close();
                return product_name;

            }
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;

    }

    double getCustomerOrderTotalPrice(int orderID) {

        Statement st = null;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select sum(amount)total_Price from SELLER_WEIGHT where CUSTOMER_ORDER_ID = " + orderID);
            while (rs.next()) {
                double totalPrice = rs.getDouble("total_Price");
                rs.close();
                return totalPrice;

            }
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return 0;

    }

    public double getSellerOrderTotalPrice(String sellerName, int seasonID) {

        Statement st = null;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select sum(TOTAL_COST)total_Price "
                    + "   from SELLER_ORDER SO INNER JOIN SELLER S ON S.SELLER_ID=SO.SELLER_ID"
                    + "  where S.SELLER_NAME  LIKE '" + sellerName + "' and SEASON_ID= " + seasonID);
            while (rs.next()) {
                double totalPrice = rs.getDouble("total_Price");
                rs.close();
                return totalPrice;

            }
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return 0;

    }

    double getCustomerOrderNetWeight(int orderID) {

        Statement st = null;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select sum(net_quantity)net_quantity from seller_weight where customer_order_id = " + orderID);
            while (rs.next()) {
                double netquantity = rs.getDouble("net_quantity");
                rs.close();
                return netquantity;

            }
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;

    }

    public double getCustomerOrderWithdrawaledgrossWeight(int orderID, int productID) {

        Statement st = null;
        String pro1 = "SELECT SUM(  SELLER_WEIGHT.GROSS_QUANTITY) AS GROSS\n"
                + "FROM\n"
                + "      SELLER_WEIGHT\n"
                + "WHERE CUSTOMER_ORDER_ID =" + orderID;
        String pro2 = "select sum(PACKAGE_NUMBER)as GROSS from seller_weight where customer_order_id = " + orderID;
        String sql = (productID == 1) ? pro1 : pro2;

        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                double netquantity = rs.getDouble("GROSS");
                rs.close();
                return netquantity;

            }
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;

    }

    public double getCustomerordergrossweight(int orderID) {

        Statement st = null;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select GROSS_WEIGHT from customer_order where order_id = " + orderID);
            while (rs.next()) {
                double netquantity = rs.getDouble("GROSS_WEIGHT");
                rs.close();
                return netquantity;

            }
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;

    }

    public double getProductavaliableAmount(int orderID, int pro_id) {
        double grossWeight = getCustomerordergrossweight(orderID);
        if (grossWeight == 0) {
            return -10000000;
        }
        double withDrawaled = getCustomerOrderWithdrawaledgrossWeight(orderID, pro_id);

        return (grossWeight - withDrawaled);

    }

    public void editeCustomerOrder(double netWeight, double totalPrice, double netPrice, double tips, double commision, String notes, int orderID, double ratio) {
        CallableStatement cs = null;
        try {
            cs = con.prepareCall("{?= call ORDER_VERSION (?,?,?,?,?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setDouble(2, netWeight);
            cs.setDouble(3, totalPrice);
            cs.setDouble(4, netPrice);
            cs.setDouble(5, tips);
            cs.setDouble(6, commision);
            cs.setString(7, notes);
            cs.setInt(8, orderID);
            cs.setDouble(9, ratio);

            cs.execute();
            JOptionPane.showMessageDialog(null, cs.getInt(1));

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public int getSellerBagID(int sellerID, int seasonID) {
        CallableStatement cs = null;
        try {
            // i
            cs = con.prepareCall("{?= call SEARCH_PKG.FIND_SELLER_LOAN_BAG (?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, sellerID);
            cs.setInt(3, seasonID);

            cs.execute();
            return cs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public int getCurrentSeason() {
        CallableStatement cs = null;
        try {
            cs = con.prepareCall("{?= call SEARCH_PKG.FIND_CURRENT_SEASON ()}");
            cs.registerOutParameter(1, Types.INTEGER);

            cs.execute();
            //   JOptionPane.showMessageDialog(null, );
            return cs.getInt(1);

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public Vector<Object> getPurchasesOrders() {
        CallableStatement cs = null;
        ResultSet rs = null;
        DateFormat df = new SimpleDateFormat(" EEEEEE dd-MMMMMM hh:mm aaaa ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        Vector<Object> data = new Vector<>();

        try {
            cs = con.prepareCall("SELECT\n"
                    + "     CUSTOMER_ORDER.ORDER_ID AS ORDER_ID,\n"
                    + "     CUSTOMER_ORDER.CUSTOMER_ID AS CUSTOMER_ID,\n"
                    + "     CUSTOMER_ORDER.PRODUCT_ID AS PRODUCT_ID,\n"
                    + "     CUSTOMER_ORDER.STORE_ID AS STORE_ID,\n"
                    + "     CUSTOMER_ORDER.NOLUN AS NOLUN,\n"
                    + "     CUSTOMER_ORDER.GROSS_WEIGHT AS GROSS_WEIGHT,\n"
                    + "     CUSTOMER_ORDER.NET_WEIGHT AS NET_WEIGHT,\n"
                    + "     to_char(CUSTOMER_ORDER.ORDER_DATE,'DD-MM HH24:MI AM') AS ORDER_DATE,\n"
                    + "     CUSTOMER_ORDER.TOTAL_PRICE AS TOTAL_PRICE,\n"
                    + "     CUSTOMER_ORDER.NET_PRICE AS NET_PRICE,\n"
                    + "     CUSTOMER_ORDER.EDIT_DATE AS EDIT_DATE,\n"
                    + "     CUSTOMER_ORDER.DUE_DATE AS DUE_DATE,\n"
                    + "     CUSTOMER_ORDER.TIPS AS TIPS,\n"
                    + "     CUSTOMER_ORDER.COMMISION AS COMMISION,\n"
                    + "     CUSTOMER_ORDER.NOTES AS NOTES,\n"
                    + "     CUSTOMER_ORDER.VECHILE_TYPE AS VECHILE_TYPE,\n"
                    + "     CUSTOMER_ORDER.DUED AS DUED,\n"
                    + "     CUSTOMER_ORDER.UNITES AS UNITES,\n"
                    + "     CUSTOMER_ORDER.RATIO AS RATIO,\n"
                    + "     CUSTOMER_ORDER.BUY_PRICE AS BUY_PRICE,\n"
                    + "     CUSTOMER_ORDER.PERIOD_ID AS PERIOD_ID,\n"
                    + "     PURCHASE_PERIOD.PURCHASE_PERIOD_ID AS PURCHASE_PERIOD_ID,\n"
                    + "     PURCHASE_PERIOD.FINISHED AS PURCHASE_PERIOD_FINISHED\n"
                    + "FROM\n"
                    + "     _ORDER CUSTOMER_ORDER,\n"
                    + "     PURCHASE_PERIOD\n"
                    + "WHERE\n"
                    + "     PERIOD_ID = PURCHASE_PERIOD_ID\n"
                    + "     and   PURCHASE_PERIOD.FINISHED  = 1"
                    + "AND CUSTOMER_ORDER.FINISHED = 0");

            rs = cs.executeQuery();
            while (rs.next()) {
                Vector<Object> temp = new Vector<>();
                String orerid = rs.getString("ORDER_ID");

                String GROSS_WEIGHT = rs.getString("GROSS_WEIGHT");
                java.util.Date date = StringToDate(rs.getString("ORDER_DATE"), "dd-MM hh:mm ");

                String sDate = df.format(date);
                String notes = rs.getString("NOTES");
                int customer_id = rs.getInt("CUSTOMER_ID");
                temp.add(GROSS_WEIGHT);
                temp.add(customerSelectAll(customer_id).getName() + sDate);
                temp.add(orerid);

                data.add(temp);

            }
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public double getSeasonStartTotalSellersLoan(int seasonID) {

        CallableStatement cs = null;
        double loan = 0;
        try {
            cs = con.prepareCall("SELECT\n"
                    + "    sum( SELLER_LOAN_BAG.PRIOR_LOAN) AS PRIOR_LOAN\n"
                    + "    \n"
                    + "FROM\n"
                    + "      SELLER_LOAN_BAG\n"
                    + "WHERE\n"
                    + "     SELLER_LOAN_BAG.SEASON_ID  = ?");
            cs.setInt(1, seasonID);
            ResultSet rs = cs.executeQuery();;
            while (rs.next()) {

                loan = rs.getDouble("PRIOR_LOAN");

            }
            rs.close();
            return loan;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return 0;
    }

    public double getSeasonEndTotalSellersLoan(int seasonID) {

        CallableStatement cs = null;
        double loan = 0;
        try {
            cs = con.prepareCall("SELECT\n"
                    + "    sum( SELLER_LOAN_BAG.CURRENT_LOAN) AS CURRENT_LOAN\n"
                    + "    \n"
                    + "FROM\n"
                    + "      SELLER_LOAN_BAG\n"
                    + "WHERE\n"
                    + "     SELLER_LOAN_BAG.SEASON_ID  = ?");
            cs.setInt(1, seasonID);
            ResultSet rs = cs.executeQuery();;
            while (rs.next()) {

                loan = rs.getDouble("CURRENT_LOAN");

            }
            rs.close();
            return loan;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return 0;
    }

    public Object[][] getCustomerOrderData(int order_id, Map<String, String> orderData) {

        Statement st = null;
        DateFormat df = new SimpleDateFormat(" EEEEEE , dd-MMMMMM hh:mm aaaa ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT\n"
                    + "     CUSTOMER_ORDER.ORDER_ID AS ORDER_ID,\n"
                    + "     CUSTOMER_ORDER.CUSTOMER_ID AS CUSTOMER_ID,\n"
                    + "     CUSTOMER_ORDER.PRODUCT_ID AS PRODUCT_ID,\n"
                    + "     CUSTOMER_ORDER.STORE_ID AS STORE_ID,\n"
                    + "     CUSTOMER_ORDER.NOLUN AS NOLUN,\n"
                    + "     CUSTOMER_ORDER.GROSS_WEIGHT AS GROSS_WEIGHT,\n"
                    + "     CUSTOMER_ORDER.NET_WEIGHT AS NET_WEIGHT,\n"
                    + "    to_char( CUSTOMER_ORDER.ORDER_DATE,'DD-MM HH24:MI AM') AS ORDER_DATE,\n"
                    + "     CUSTOMER_ORDER.TOTAL_PRICE AS TOTAL_PRICE,\n"
                    + "     CUSTOMER_ORDER.NET_PRICE AS NET_PRICE,\n"
                    + "     CUSTOMER_ORDER.EDIT_DATE AS EDIT_DATE,\n"
                    + "     CUSTOMER_ORDER.DUE_DATE AS DUE_DATE,\n"
                    + "     CUSTOMER_ORDER.TIPS AS TIPS,\n"
                    + "     CUSTOMER_ORDER.COMMISION AS COMMISION,\n"
                    + "     CUSTOMER_ORDER.NOTES AS NOTES,\n"
                    + "     CUSTOMER_ORDER.FINISHED AS FINISHED,\n"
                    + "     CUSTOMER_ORDER.VECHILE_TYPE AS VECHILE_TYPE,\n"
                    + "     CUSTOMER_ORDER.DUED AS DUED,\n"
                    + "     CUSTOMER_ORDER.UNITES AS UNITES,\n"
                    + "     CUSTOMER_ORDER.RATIO AS RATIO,\n"
                    + "     CUSTOMER_ORDER.BUY_PRICE AS BUY_PRICE,\n"
                    + "     CUSTOMER_ORDER.PERIOD_ID AS PERIOD_ID\n"
                    + "FROM customer_order where order_id =" + order_id + "     order by  CUSTOMER_ORDER.ORDER_DATE desc ");
            while (rs.next()) {
                double netweight = getCustomerOrderNetWeight(order_id);
                double totalPraice = getCustomerOrderTotalPrice(order_id);

                java.util.Date orderDate = StringToDate(rs.getString("ORDER_DATE"), "dd-MM hh:mm ");

                totalPraice = Math.rint(totalPraice);
                orderData.put("orderID", String.valueOf(order_id));
                orderData.put("orderDate", (df.format(orderDate)));
                orderData.put("product", getProductName(rs.getInt("PRODUCT_ID")));
                orderData.put("noloun", rs.getString("NOLUN"));
                orderData.put("grossWeight", rs.getString("GROSS_WEIGHT"));
                orderData.put("netWeight", String.valueOf(netweight));
                orderData.put("unites", rs.getString("UNITES"));
                orderData.put("commision", rs.getString("COMMISION"));
                orderData.put("totalcost", String.valueOf(totalPraice));
                orderData.put("notes", rs.getString("NOTES"));
                orderData.put("BUY_PRICE", rs.getString("BUY_PRICE"));
                orderData.put("vechileType", rs.getString("VECHILE_TYPE"));//TIPS
                orderData.put("tips", rs.getString("TIPS"));//NET_PRICE
                orderData.put("netPrice", rs.getString("NET_PRICE"));

            }
            Object[][] tableData = null;
            List<List<String>> orderDet = new ArrayList<>();
            rs = st.executeQuery("select SUM(net_quantity)AS NET_WEIGHT,SUM(AMOUNT)AS TOTAL_COST,unite_price from seller_weight WHERE customer_order_id = " + order_id + " group by unite_price  ");
            while (rs.next()) {
                List<String> temp = new ArrayList<>();

                temp.add(rs.getString("NET_WEIGHT"));
                temp.add(rs.getString("unite_price"));
                temp.add(rs.getString("TOTAL_COST"));

                orderDet.add(temp);

            }
            tableData = new Object[orderDet.size()][3];

            for (int i = 0; i < orderDet.size(); i++) {
                List<String> temp = orderDet.get(i);
                for (int j = 0; j < temp.size(); j++) {
                    tableData[i][j] = temp.get(j);

                }
            }
            rs.close();
            return tableData;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public Vector<Object> getCustomerOrderSellingDetail(int orderid, int product_id) {

        CallableStatement cs = null;
        Vector<Object> orderDet = new Vector<Object>();
        String sqlpro1 = "select SUM(net_quantity)AS NET_WEIGHT,SUM(AMOUNT)AS TOTAL_COST,unite_price from seller_weight WHERE customer_order_id = ? group by unite_price order by unite_price desc ";
        String sqlpro2 = "select SUM(PACKAGE_NUMBER)AS NET_WEIGHT,SUM(AMOUNT)AS TOTAL_COST,unite_price from seller_weight WHERE customer_order_id = ? group by unite_price order by unite_price desc";
        String sql = (product_id == 1) ? sqlpro1 : sqlpro2;
        try {
            cs = con.prepareCall(sql);
            cs.setInt(1, orderid);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Vector<Object> temp = new Vector<Object>();

                temp.add(rs.getString("NET_WEIGHT"));
                temp.add(rs.getString("unite_price"));
                temp.add(rs.getString("TOTAL_COST"));

                orderDet.add(temp);

            }
            rs.close();
            return orderDet;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;

    }

    public Vector<Object> getCustomerOrders(String name, int finished, int dued, int seasonID) {

        int id = customerSelectAll(name).getId();
        DateFormat df = new SimpleDateFormat("dd-MMMMMM hh:mm aa", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        Vector<Object> data = new Vector<>();
        CallableStatement cs = null;
        try {// + "and DUED=?");
            cs = con.prepareCall("select GROSS_WEIGHT ,TIPS,order_id,notes,to_char (ORDER_DATE,'DD-MM-YYYY HH24:MI AM ') AS ORDER_DATE from customer_order "
                    + "where finished=?"
                    + " and  customer_id = ? "
                    + " and PERIOD_ID  =-1 "
                    + " and DUED =? "
                    + " and SEASON_ID=?  order by  CUSTOMER_ORDER.ORDER_DATE desc");
            cs.setInt(1, finished);
            cs.setInt(2, id);
            cs.setInt(3, dued);
            cs.setInt(4, seasonID);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                Vector<Object> temp = new Vector<>();
                String orderID = rs.getString("order_id");
                String S_OrderDate = rs.getString("ORDER_DATE");
                java.util.Date orderDate = StringToDate(S_OrderDate, "dd-MM-yyyy hh:mm ");
                String notes = rs.getString("NOTES");
                String orderName = name + "," + df.format(orderDate);
                String tips = rs.getString("TIPS");
                String grossWeight = rs.getString("GROSS_WEIGHT");

                temp.add((grossWeight));
                temp.add((orderName));
                temp.add((orderID));

                data.add(temp);
            }

            rs.close();
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public Vector<String> getCustomersNamesByOrdersDate(java.sql.Date orderDate, int storeNumber, int seasonID, int productID, int fridageID) {

        DateFormat df = new SimpleDateFormat("dd-MMMMMM hh:mm aa", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        Vector<String> data = new Vector<String>();
        CallableStatement cs = null;
        try {
            cs = con.prepareCall("select order_tag from customer_order "
                    + " where  "
                    + "to_char(  order_date,'dd-MM-YYYY')= to_char(?,'dd-MM-YYYY') "
                    + "and SEASON_ID=? and STORE_ID=?"
                    + " and PRODUCT_ID=? and fridage_id=?");
            cs.setDate(1, orderDate);
            cs.setInt(2, seasonID);
            cs.setInt(3, storeNumber);
            cs.setInt(4, productID);
            cs.setInt(5, fridageID);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                data.add(rs.getString("order_tag"));
            }

            rs.close();
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    java.util.Date getOutcomeDate(int id) {
        Statement st = null;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select OUTCOME_DATE from OUTCOME where OUTCOME_ID =" + id + "   order by  OUTCOME_DATE desc");
            while (rs.next()) {
                java.util.Date outcomeDate = rs.getDate("OUTCOME_DATE");
                rs.close();
                return outcomeDate;

            }
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public void CustomerOrderDelvery(int order_id, String notes) {

        CallableStatement cs = null;

        try {
            cs = con.prepareCall("{?= call CUSTOMER_ORDER_DELIVERY(?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, order_id);
            cs.setString(3, notes);
            cs.execute();
            int x = cs.getInt(1);
            JOptionPane.showMessageDialog(null, x);

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public Person customerSelectAll(String name) {
        Statement st = null;
        try {
            st = con.createStatement();

            ResultSet rs = st.executeQuery("select* from customer where customer_name = '" + name + "'");
            while (rs.next()) {
                Person p = new Person(rs.getInt("customer_id"), rs.getString("customer_name"), rs.getString("customer_address"), rs.getString("customer_phone"));
                rs.close();
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;

    }
//contreactorSelectAll

    public Person contreactorSelectAll(int id) {
        Statement st = null;
        try {
            st = con.createStatement();

            ResultSet rs = st.executeQuery("select* from contractor where contractor_id = " + id + "");
            while (rs.next()) {
                Person p = new Person(rs.getInt("contractor_id"), rs.getString("contractor_name"), rs.getString("contractor_address"), rs.getString("contractor_phone"));

                rs.close();
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;

    }

    public Person customerSelectAll(int id) {
        Statement st = null;
        try {
            st = con.createStatement();

            ResultSet rs = st.executeQuery("select* from customer where customer_id = " + id + "");
            while (rs.next()) {
                Person p = new Person(rs.getInt("customer_id"), rs.getString("customer_name"), rs.getString("customer_address"), rs.getString("customer_phone"));

                rs.close();
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;

    }

    public Person loanerSelectAll(int id) {
        Statement st = null;
        ResultSet rs = null;
        try {
            st = con.createStatement();
            Person p = null;
            rs = st.executeQuery("select* from LOANERS where LOANER_ID = " + id);
            while (rs.next()) {
                p = new Person(rs.getInt("LOANER_ID"), rs.getString("LOANER_NAME"), "", "");
            }
            return p;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;

    }

    public Person loanerSelectAll(String name) {
        Statement st = null;
        ResultSet rs = null;
        try {
            st = con.createStatement();

            rs = st.executeQuery("select* from LOANERS where LOANER_NAME = '" + name + "'");
            while (rs.next()) {
                Person p = new Person(rs.getInt("LOANER_ID"), rs.getString("LOANER_NAME"), "", "");
                rs.close();
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;

    }

    public int getIncomeID(java.util.Date date) {

        CallableStatement cs = null;
        try {
            cs = con.prepareCall("select INCOME_ID from income where trunc(INCOME_DATE)=trunc(?) ");

            cs.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("INCOME_ID");
                rs.close();
                return id;

            }
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return 0;
    }

    public double getIncomeAmount(java.util.Date date) {

        CallableStatement cs = null;
        try {
            cs = con.prepareCall("select TOTAL_INCOME from income where trunc(INCOME_DATE)=trunc(?) ");
            cs.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                double id = rs.getDouble("TOTAL_INCOME");
                rs.close();
                return id;

            }
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return 0;
    }

    public int getOutcomeID(java.util.Date date) {

        CallableStatement cs = null;
        try {
            cs = con.prepareCall("select OUTCOME_ID from OUTcome where trunc(OUTCOME_DATE)=trunc(?) ");

            cs.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("OUTCOME_ID");
                rs.close();
                return id;

            }
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return 0;
    }

    public double getOutcomeAmount(java.util.Date date) {

        CallableStatement cs = null;
        try {
            cs = con.prepareCall("select TOTAL_OUTCOME from OUTcome where trunc(OUTCOME_DATE)=trunc(?)  ");
            cs.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                double id = rs.getDouble("TOTAL_OUTCOME");
                rs.close();
                return id;

            }
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return 0;
    }

    public Object[][] getOutcomeDailyTranscation(java.util.Date date, Double dayAmount) {

        int outcomeID = getOutcomeID(date);
        double cash = 0;
        double totalAmount = 0;
        List<List<String>> data = new ArrayList<>();
        Object[][] table;
        Statement st = null;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from OUTCOME_DETAIL where OUTCOME_ID= " + outcomeID + "  ORDER BY OUTCOME_DETAIL_ID DESC");
            while (rs.next()) {
                List<String> temp = new ArrayList<>();
                String order_id = (rs.getString("ORDER_ID").equals("-1")) ? "" : rs.getString("ORDER_ID");
                int customer_id = rs.getInt("CUSTOMER_ID");
                String customerName = "";
                temp.add(rs.getString("NOTES"));
                temp.add((getFridageName(rs.getInt("FRIDAGE_ID"))));
                temp.add(order_id);

                String type = rs.getString("OUTCOME_TYPE");
                System.out.println("type = " + type);
                if (customer_id != -1) {
                    if (type.equals("K_L") || type.equals("K_S") || type.equals("K_V")) {
                        customerName = contreactorSelectAll(customer_id).getName();

                    } else if (type.equals("OUT_PAY_LOAN") || type.equals("OUT_LOAN")) {

                        customerName = loanerSelectAll(customer_id).getName();

                    } else {
                        customerName = customerSelectAll(customer_id).getName();

                    }
                }
                type = NormalizMap.get(type);
                temp.add(customerName);
                temp.add(type);
                double amount = Double.parseDouble(rs.getString("AMOUNT"));

                totalAmount += amount;
                temp.add(rs.getString("AMOUNT"));
                data.add(temp);

            }
            List<String> temp = new ArrayList<String>(Arrays.asList("", "نقدي", String.valueOf(cash)));

            table = new Object[data.size()][7];
            for (int i = 0; i < data.size(); i++) {
                temp = data.get(i);
                for (int j = 0; j < temp.size(); j++) {
                    String str = temp.get(j);
                    table[i][j] = (str == null) ? str : (str);

                }
            }
            rs.close();
            dayAmount = new Double(totalAmount);
            return table;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;

    }

    public Object[][] getContractorAccount(String contName, String type, int sort, int seasonID) {
        Statement st = null;
        List<List<Object>> data = new ArrayList<>();
        Object[][] table;
        DateFormat df = new SimpleDateFormat("dd-MMMMMM-yyyy hh:mm aaaa", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        String cond = "";
        switch (sort) {

            case 1:
                cond = " and paid =" + 1;
                break;
            case 0:
                cond = " and paid =" + 0;
                break;

        }
        try {
            int cont_id = getContId(contName, type);
            int account_id = getContAccountId(cont_id);

            st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from CONTRACTOR_ACCOUNT_DETAIL where CONTRACTOR_ACCOUNT_ID = " + account_id + cond + " and season_id = " + seasonID + "order by ACCOUNT_DETAIL_DATE desc");
            while (rs.next()) {
                List<Object> temp = new ArrayList<>();

                String amount = rs.getString("AMOUNT");
                java.util.Date date = rs.getDate("ACCOUNT_DETAIL_DATE");
                String report = rs.getString("REPORT");
                boolean paid = (rs.getInt("PAID") == 1);
                report = (report == null) ? report : (report);
                temp.add(report);
                if (!type.equals("K_V")) {
                    temp.add(paid);
                }
                temp.add(amount);
                temp.add((df.format(date)));

                data.add(temp);
            }
            table = new Object[data.size()][4];
            for (int i = 0; i < data.size(); i++) {
                List<Object> temp = data.get(i);
                for (int j = 0; j < temp.size(); j++) {
                    table[i][j] = temp.get(j);

                }

            }
            rs.close();
            return table;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;

    }

    public int getContId(String name, String type) {
        CallableStatement cs = null;

        try {
            cs = con.prepareCall("{?= call SHOP.SEARCH_PKG.FIND_CONTRACTOR(?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, name);
            cs.setString(3, type);
            cs.execute();
            int id = cs.getInt(1);

            return id;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return 0;
    }

    public int getContAccountId(int id) {
        CallableStatement cs = null;

        try {
            cs = con.prepareCall("{?= call SHOP.SEARCH_PKG.FIND_CONT_ACCOUNT(?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, id);
            cs.execute();
            int account_id = cs.getInt(1);
            cs.close();
            return account_id;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }
    /* P_CONT_NAME IN VARCHAR2
     , P_CONT_ADDRESS IN VARCHAR2
     , P_CONT_PHONE IN VARCHAR2
     , P_CONT_TYPE IN VARCHAR2
     , P_AMOUNT IN NUMBER
     ,P_FRIDAGE_ID IN NUMBER
     , P_REPORT IN VARCHAR2*/

    public void contractorTransaction(String c_name, String c_address, String c_phone,
            String type, double amount, String report, int fridageNumber, int paid, java.sql.Date date, int seasonID) {
        CallableStatement cs = null;

        try {
            cs = con.prepareCall("{?= call CONTRACTOR_TRANSACTION(?,?,?,?,?,?,?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, c_name);
            cs.setString(3, c_address);
            cs.setString(4, c_phone);
            cs.setString(5, type);
            cs.setDouble(6, amount);
            cs.setInt(7, fridageNumber);
            cs.setString(8, report);
            cs.setInt(9, paid);
            cs.setDate(10, date);
            cs.setInt(11, seasonID);
            cs.execute();

            JOptionPane.showMessageDialog(null, cs.getInt(1));
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public double getContractorWithadrawal(String name, int paid, int seasonID) {

        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = con.prepareCall("SELECT\n"
                    + "  SUM(CONTRACTOR_ACCOUNT_DETAIL.AMOUNT) AS AMOUNT\n"
                    + "  FROM\n"
                    + "   CONTRACTOR_ACCOUNT INNER JOIN  CONTRACTOR_ACCOUNT_DETAIL ON CONTRACTOR_ACCOUNT.ACCOUNT_ID = CONTRACTOR_ACCOUNT_DETAIL.CONTRACTOR_ACCOUNT_ID\n"
                    + "  INNER JOIN CONTRACTOR ON CONTRACTOR_ACCOUNT.CONTRACTOR_ID = CONTRACTOR.CONTRACTOR_ID\n"
                    + "    where  \n"
                    + "   CONTRACTOR_ACCOUNT_DETAIL.PAID = ?\n"
                    + "                      and CONTRACTOR.CONTRACTOR_NAME like  ? and CONTRACTOR_ACCOUNT_DETAIL.season_id=? ");
            cs.setInt(1, paid);
            cs.setString(2, name);
            cs.setInt(3, seasonID);
            rs = cs.executeQuery();
            double amount = 0;
            while (rs.next()) {

                amount = rs.getDouble("AMOUNT");

            }
            rs.close();
            return amount;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public Object[][] getIncomeDailyTranscation(java.util.Date date, Double dayAmount) {

        int incomeID = getIncomeID(date);
        double cash = 0;
        double totalAmount = 0;
        List<List<String>> data = new ArrayList<>();
        Object[][] table;
        Statement st = null;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from INCOME_DETAIL where INCOME_ID= " + incomeID + " ORDER BY INCOME_DETAIL_ID desc");
            while (rs.next()) {
                List<String> temp = new ArrayList<>();

                String type = rs.getString("INCOME_DETAIL_TYPE");
                if (type.equals("INTST_PAY")) {
                    int id = rs.getInt("SELLER_ID");
                    Person p = sellerSelectAll(id);
                    String sellerName = p.getName();
                    double amount = rs.getDouble("AMOUNT");
                    String notes = rs.getString("NOTES");

                    temp.add(notes);
                    temp.add(sellerName);
                    temp.add(NormalizMap.get(type));
                    temp.add(String.valueOf(amount));
                    totalAmount += amount;

                    data.add(temp);
                } else if (type.equals("IN_LOAN")) {
                    int id = rs.getInt("SELLER_ID");
                    Person p = loanerSelectAll(id);
                    String sellerName = p.getName();
                    double amount = rs.getDouble("AMOUNT");
                    String notes = rs.getString("NOTES");

                    temp.add(notes);
                    temp.add(sellerName);
                    temp.add(NormalizMap.get(type));
                    temp.add(String.valueOf(amount));
                    totalAmount += amount;

                    data.add(temp);

                } else if (type.equals("IN_PAY_LOAN")) {
                    int id = rs.getInt("SELLER_ID");
                    Person p = loanerSelectAll(id);
                    String sellerName = p.getName();
                    double amount = rs.getDouble("AMOUNT");
                    String notes = rs.getString("NOTES");

                    temp.add(notes);
                    temp.add(sellerName);
                    temp.add(NormalizMap.get(type));
                    temp.add(String.valueOf(amount));
                    totalAmount += amount;

                    data.add(temp);

                } else if (type.equals("CASH")) {
                    double amount = rs.getDouble("AMOUNT");
                    cash += amount;
                    totalAmount += amount;

                }

            }
            //  List<String> temp = new ArrayList<String>(Arrays.asList("", "نقدي", ));
            // data.add(temp);
            table = new Object[data.size() + 1][4];
            table[0][0] = "";
            table[0][1] = "نقدي";
            table[0][3] = (String.valueOf(cash));
            for (int i = 0; i < data.size(); i++) {
                List<String> temp = new ArrayList<String>();
                temp = data.get(i);
                for (int j = 0; j < temp.size(); j++) {

                    table[i + 1][j] = (temp.get(j));

                }
            }
            rs.close();
            dayAmount = new Double(totalAmount);

            return table;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;

    }

    public List<java.util.Date> getOutcomeDates(int seasonID) {

        CallableStatement cs = null;
        List<java.util.Date> dates = new ArrayList<java.util.Date>();
        try {
            cs = con.prepareCall("select to_char(outcomeDate ,'DD-MM-YYYY HH24:MI AM')  as outcomeDate\n"
                    + "from (select DISTINCT((OUTCOME_DATE)) as outcomeDate from OUTCOME where   SEASON_ID =?  order by outcome.OUTCOME_DATE desc)");
            cs.setInt(1, seasonID);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                java.util.Date date = StringToDate(rs.getString("outcomeDate"), "dd-MM-yyyy hh:mm ");

                dates.add(date);

            }
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return dates;

    }

    public List<java.util.Date> getIncomeDates(int seasonID) {

        List<java.util.Date> dates = new ArrayList<java.util.Date>();
        CallableStatement cs = null;
        try {
            cs = con.prepareCall("select to_char(incomeDate ,'DD-MM-YYYY HH24:MI AM')  as incomeDate\n"
                    + "from (select DISTINCT((INCOME_DATE)) as incomeDate from INCOME where   SEASON_ID =?  order by INCOME_DATE desc)\n"
            );
            cs.setInt(1, seasonID);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                dates.add(StringToDate(rs.getString("incomeDate"), "dd-MM-yyyy hh:mm "));

            }
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return dates;

    }

    public Person sellerSelectAll(String name) {
        Statement st = null;
        try {
            st = con.createStatement();

            ResultSet rs = st.executeQuery("select* from seller where seller_name = '" + name + "'");
            while (rs.next()) {
                Person p = new Person(rs.getInt("seller_id"), rs.getString("seller_name"), rs.getString("seller_address"), rs.getString("seller_phone"));
                rs.close();
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;

    }

    public Person sellerSelectAll(int id) {
        Statement st = null;
        try {
            st = con.createStatement();

            ResultSet rs = st.executeQuery("select* from SELLER where SELLER_ID = " + id + "");
            while (rs.next()) {
                Person p = new Person(rs.getInt("seller_id"), rs.getString("seller_name"), rs.getString("seller_address"), rs.getString("seller_phone"));
                rs.close();
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;

    }

    public boolean authenticateUser(String userName, String Password) {
        CallableStatement cs = null;
        try {
            cs = con.prepareCall("{?= call AUTHENTICATE_USER(?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, userName);
            cs.setString(3, Password);
            cs.executeQuery();
            int x = cs.getInt(1);
            return (x == 1);

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;

    }

    public void outcomeTransaction(double amount, String notes, String type, int csutomer_id, int order_id, int firdage_id) {
        /* P_AMOUNT IN NUMBER 
         , P_NOTES IN VARCHAR2 
         ,P_TYPE IN varchar2
         ,P_CUSTOMER_id number*/
        CallableStatement cs = null;
        try {
            cs = con.prepareCall("{?= call OUTCOME_TRANSCATION(?,?,?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setDouble(2, amount);
            cs.setString(3, notes);
            cs.setString(4, type);
            cs.setInt(5, csutomer_id);
            cs.setInt(6, order_id);
            cs.setInt(7, firdage_id);
            cs.execute();
            JOptionPane.showMessageDialog(null, cs.getInt(1));
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void loanPayTransaction(double amount, String name, String notes, String type, int fridageID) {

        CallableStatement cs = null;
        try {

            cs = con.prepareCall("{?= call LOAN_PYING_TRANSACTION (?,?,?,?,?)}");
            cs.setString(2, name);
            cs.setDouble(3, amount);
            cs.setString(4, type);
            cs.setString(5, notes);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(6, fridageID);

            cs.execute();
            int x = cs.getInt(1);

            JOptionPane.showMessageDialog(null, x);

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void loanIncTransaction(double amount, String name, String notes, String type, int fridageID) {

        CallableStatement cs = null;
        try {

            cs = con.prepareCall("{?= call LOAN_INC_TRANSACTION (?,?,?,?,?)}");
            cs.setString(2, name);
            cs.setDouble(3, amount);
            cs.setString(4, type);
            cs.setString(5, notes);
            cs.setInt(6, fridageID);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.execute();
            int x = cs.getInt(1);

            JOptionPane.showMessageDialog(null, x);

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void incomeTransaction(double amount, String notes, String type, int seller_id, int firdage_id) {
        /*  P_INCOMETYPE IN VARCHAR2 
         , P_AMOUNT IN NUMBER 
         , REPORT IN VARCHAR2
         ,P_SELLER_id number
         ) RETURN NUMBER*/
        CallableStatement cs = null;
        try {
            cs = con.prepareCall("{?= call INCOME_TRANSACTION(?,?,?,?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setDouble(3, amount);
            cs.setString(4, notes);
            cs.setString(2, type);
            cs.setInt(5, seller_id);
            cs.setInt(6, firdage_id);
            cs.execute();
            JOptionPane.showMessageDialog(null, cs.getInt(1));
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public Vector<String> customer_order_delvery(int order_id) {
        CallableStatement cs = null;
        DateFormat df = new SimpleDateFormat("EEEEEE dd-MMMMMM hh:mm aa", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        Vector<String> temp = new Vector<>();
        try {
            cs = con.prepareCall("SELECT\n"
                    + "     CUSTOMER_ORDER.ORDER_ID AS ORDER_ID,\n"
                    + "     CUSTOMER_ORDER.PRODUCT_ID AS PRODUCT_ID,\n"
                    + "     CUSTOMER_ORDER.NOLUN AS NOLUN,\n"
                    + "     CUSTOMER_ORDER.GROSS_WEIGHT AS GROSS_WEIGHT,\n"
                    + "     CUSTOMER_ORDER.NET_WEIGHT AS NET_WEIGHT,\n"
                    + "    to_char( CUSTOMER_ORDER.ORDER_DATE,'DD-MM-YY HH:MI AM') AS RDER_DATE,\n"
                    + "     CUSTOMER_ORDER.TOTAL_PRICE AS TOTAL_PRICE,\n"
                    + "     CUSTOMER_ORDER.NET_PRICE AS NET_PRICE,\n"
                    + "     CUSTOMER_ORDER.TIPS AS TIPS,\n"
                    + "     CUSTOMER_ORDER.COMMISION AS COMMISION,\n"
                    + "     CUSTOMER_ORDER.NOTES AS NOTES,\n"
                    + "     CUSTOMER_ORDER.VECHILE_TYPE AS VECHILE_TYPE , \n"
                    + "     customer_order.dued as dued"
                    + " FROM\n"
                    + "     _ORDER CUSTOMER_ORDER"
                    + " where ORDER_ID = ? and  CUSTOMER_ORDER.finished = 1  ");

            cs.setInt(1, order_id);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                java.util.Date orderdate = StringToDate(rs.getString("RDER_DATE"), "dd-MM-yy hh:mm");

                String orderDate = df.format(orderdate);

                String orderid = rs.getString("ORDER_ID");

                String product = getProductName(rs.getInt("PRODUCT_ID"));

                String NOLUN = rs.getString("NOLUN");

                String GROSS_WEIGHT = rs.getString("GROSS_WEIGHT");

                String NET_WEIGHT = rs.getString("NET_WEIGHT");

                String TOTAL_PRICE = rs.getString("TOTAL_PRICE");

                String NET_PRICE = rs.getString("NET_PRICE");

                String TIPS = rs.getString("TIPS");

                String COMMISION = rs.getString("COMMISION");

                String VECHILE_TYPE = rs.getString("VECHILE_TYPE");

                String NOTES = (rs.getString("NOTES") == null) ? "" : rs.getString("NOTES");
                String dued = rs.getString("dued");
                temp.add(NOTES);
                temp.add(VECHILE_TYPE);
                temp.add(TIPS);
                temp.add(NET_PRICE);
                temp.add(TOTAL_PRICE);
                temp.add(COMMISION);
                temp.add(NET_WEIGHT);
                temp.add(GROSS_WEIGHT);
                temp.add(product);
                temp.add(orderid);
                temp.add((orderDate));
                temp.add(NOLUN);
                temp.add(dued);

            }
            rs.close();
            return temp;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public String getLoanerType(String name) {

        CallableStatement cs = null;
        try {
            cs = con.prepareCall("select loaner_type from loaners where loaner_name like ?");
            cs.setString(1, name);

            ResultSet rs = cs.executeQuery();
            String amount = "";
            while (rs.next()) {
                amount = rs.getString("loaner_type");
            }
            rs.close();
            return amount;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
//LOAN_PYING_TRANSACTION

    public Pair<Double, String> getLoanerAmount_type(String name) {

        CallableStatement cs = null;
        Pair<Double, String> p = new Pair<>();
        try {
            cs = con.prepareCall("SELECT\n"
                    + "     LOANERS.LOANER_NAME AS LOANER_NAME,\n"
                    + "\n"
                    + "     LOAN_ACCOUNT.LOAN_TYPE AS LOAN_TYPE,\n"
                    + "     LOAN_ACCOUNT.DUE_AMOUNT AS DUE_AMOUNT,\n"
                    + "     LOAN_ACCOUNT.FINISHED AS FINISHED\n"
                    + "FROM\n"
                    + "     LOANERS INNER JOIN LOAN_ACCOUNT ON LOANERS.LOANER_ID = LOAN_ACCOUNT.LOANER_ID\n"
                    + "\n"
                    + "where FINISHED=0 and LOANER_NAME like ?");
            cs.setString(1, name);

            ResultSet rs = cs.executeQuery();
            double amount = 0;
            String type = "";
            while (rs.next()) {
                amount = rs.getDouble("DUE_AMOUNT");
                type = rs.getString("LOAN_TYPE");

            }
            rs.close();
            return new Pair<>(amount, type);

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object[][] getDailyBookTransaction(java.sql.Date date, List<Pair<int[], int[]>> spanRowsList, List<Integer> GapRows, int FRIDAGE_ID) {

        CallableStatement cs = null;
        final String typeName = "CUSTOMER_DAILY_OBJECT";

        final String DAILY_SALES_ORDER_DET_OBJ = "DAILY_SALES_ORDER_DET_OBJ";
        final String DAILY_SALES_ORDER_DET_COLL = "DAILY_SALES_ORDER_DET_COLL";

        final String DAILY_SALES_ORDER_OBJ = "DAILY_SALES_ORDER_OBJ";
        final String DAILY_SALES_COLL = "DAILY_SALES_COLL";
        final String typeTableName = "CUSTOMER_DAILY_COLLECTION";

        Object[][] table = null;
        try {

            final StructDescriptor structDescriptor = StructDescriptor.createDescriptor(typeName.toUpperCase(), con);

            final StructDescriptor DAILY_SALES_ORDER_DET_OBJ_SD = StructDescriptor.createDescriptor(DAILY_SALES_ORDER_DET_OBJ.toUpperCase(), con);
            final StructDescriptor DAILY_SALES_ORDER_OBJ_SD = StructDescriptor.createDescriptor(DAILY_SALES_ORDER_OBJ.toUpperCase(), con);

//            final ResultSetMetaData metaData = structDescriptor.getMetaData();
//
//            final ResultSetMetaData DAILY_SALES_ORDER_DET_OBJ_MD = DAILY_SALES_ORDER_DET_OBJ_SD.getMetaData();
//            final ResultSetMetaData DAILY_SALES_ORDER_OBJ_MD = DAILY_SALES_ORDER_OBJ_SD.getMetaData();
            cs = con.prepareCall("{?= call DAILY_SALES_BOOK(?,?,?)}");
            cs.registerOutParameter(1, Types.ARRAY, DAILY_SALES_COLL);
            cs.setDate(2, date);
            cs.setInt(4, FRIDAGE_ID);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.execute();
            int dataSize = cs.getInt(3);

            Object[] data = (Object[]) ((Array) cs.getObject(1)).getArray();
            dataSize += data.length * 2;
            table = new Object[dataSize][8];
            int i = 0;
            int j = 7;
            DateFormat df = new SimpleDateFormat("dd-MMMMMM hh:mm aaaa", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

            for (Object tmp : data) {
                Struct row = (Struct) tmp;
                // Attributes are index 1 based...
                Object[] orderData = row.getAttributes();
                String sellerName = orderData[0].toString();
                String orderCoast = orderData[1].toString();
                Object[] orderDetaliArray = (Object[]) ((Array) orderData[2]).getArray();

                table[i][7] = sellerName;
                j = 0;
                int spanrow[] = new int[orderDetaliArray.length];
                int spanRowCounter = 0;
                for (Object cursor : orderDetaliArray) {
                    spanrow[spanRowCounter] = i;
                    spanRowCounter++;
                    Struct orderDetRow = (Struct) cursor;
                    Object[] orderDetailRow = orderDetRow.getAttributes();

                    String productName = orderDetailRow[0].toString();
                    String grossWeight = orderDetailRow[1].toString();
                    String netWeight = orderDetailRow[2].toString();
                    String packageNumber = orderDetailRow[3].toString();
                    String unitePrice = orderDetailRow[4].toString();
                    String Cost = orderDetailRow[5].toString();
                    int productid = getProductID(productName);
                    String customerOrderName = "";
                    String customerOrdeDate = "";
                    if (productid == 1 || productid == 2) {
                        customerOrderName = orderDetailRow[6].toString();
                        customerOrdeDate = orderDetailRow[7].toString();

                        java.util.Date customer_order_date = (java.util.Date) orderDetailRow[7];
                        customerOrderName = customerOrderName + " ," + df.format(customer_order_date);
                    }
                    table[i][0] = (customerOrderName);
                    table[i][1] = (Cost + " $");
                    table[i][2] = (unitePrice + " $");
                    table[i][3] = (netWeight + " ك.ج");
                    table[i][4] = (grossWeight + " ك.ج");
                    table[i][5] = (packageNumber);
                    table[i][6] = (productName);
                    i++;

                }
                Pair<int[], int[]> p = new Pair<>(spanrow, new int[]{7});
                spanRowsList.add(p);

                table[i][0] = ("الإجمالي: " + orderCoast);
                GapRows.add(i + 1);

                i += 2;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return table;
    }

    String NormalizeinArabic(String str) {

        char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        if (str == null) {
            return str;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                builder.append(arabicChars[(int) (str.charAt(i)) - 48]);
            } else {
                builder.append(str.charAt(i));
            }
        }
//        System.out.println("Number in English : " + str);
//        System.out.println("Number In Arabic : " + builder.toString());
        return builder.toString();
    }

    public void instalementTransaction(String sellerName, double amount, String report) {

//SELLER_INSTALLEMENT_PAYING
        CallableStatement cs = null;
        try {
            cs = con.prepareCall("{?= call SELLER_INSTALLEMENT_PAYING(?,?,?) }");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, sellerName);
            cs.setDouble(3, amount);
            cs.setString(4, report);
            cs.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public Object[][] getCustomerDailyTransaction(java.sql.Date date, int FRIDAGE_ID) {

        CallableStatement cs = null;
        final String typeName = "CUSTOMER_DAILY_OBJECT";
        final String typeTableName = "CUSTOMER_DAILY_COLLECTION";
        Object[][] table = null;
        try {
            final StructDescriptor structDescriptor = StructDescriptor.createDescriptor(typeName.toUpperCase(), con);
            final ResultSetMetaData metaData = structDescriptor.getMetaData();

            cs = con.prepareCall("{?= call CUSTOMERS_DAILY_TRANSACTION(?,?,?)}");
            cs.registerOutParameter(1, Types.ARRAY, typeTableName);
            cs.setDate(2, date);
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.setInt(4, FRIDAGE_ID);
            cs.execute();
            Object[] data = (Object[]) ((Array) cs.getObject(1)).getArray();
            table = new Object[data.length][8];
            int i = 0;
            int j = 6;
            for (Object tmp : data) {
                Struct row = (Struct) tmp;
                // Attributes are index 1 based...
                int idx = 1;
                for (Object attribute : row.getAttributes()) {
                    System.out.println(metaData.getColumnName(idx) + " = " + attribute);
                    String Attr_name = metaData.getColumnName(idx);
                    ++idx;
                    switch (Attr_name) {
                        case "CUSTOMER_NAME":
                            String name = attribute.toString();
                            name = (name.equals("PURCHASES")) ? NormalizMap.get("PURCHASES") : name;
                            table[i][7] = name;
                            break;
                        case "PRODUCT_NAME":
                            table[i][6] = (attribute.toString());
                            break;
                        case "NOLOUN":
                            table[i][5] = (attribute.toString());
                            break;
                        case "WEIGHT":
                            table[i][4] = (((Double.parseDouble(attribute.toString()) == 0) ? "بدون" : attribute).toString());
                            break;
                        case "TIPS":
                            table[i][2] = (attribute.toString());
                            break;
                        case "STORE_PLACE":
                            table[i][1] = ("ثلاجة رقم " + attribute);
                            break;
                        case "FINISHED":
                            boolean flag = (Integer.parseInt(attribute.toString()) == 1);
                            table[i][0] = flag;
                            break;
                        case "UNITES":
                            table[i][3] = (attribute.toString());
                            break;

                    }

                }

                i++;
                System.out.println("---");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return table;
    }

    List<Map> getSellerOrderDetailMap(Vector<Object> orderDetailData) {
        List<Map> dataMap = new ArrayList<>();
        for (int i = 0; i < orderDetailData.size(); i++) {
            Vector<Object> rowData = (Vector<Object>) orderDetailData.get(i);
            String[] orderTag = rowData.get(0).toString().replace(" ", "").split("_");
            double cost = Double.parseDouble(rowData.get(1).toString());
            double unitePrice = Double.parseDouble(rowData.get(2).toString());
            double packageNumber = (rowData.get(3).toString().isEmpty()) ? 0 : Double.parseDouble(rowData.get(3).toString());
            double netWeight = (rowData.get(4).toString().isEmpty()) ? 0 : Double.parseDouble(rowData.get(4).toString());
            double grossWeight = (rowData.get(5).toString().isEmpty()) ? 0 : Double.parseDouble(rowData.get(5).toString());
            String productName = rowData.get(6).toString();

            Map<String, Object> temp = new HashMap<>();

            int productID = getProductID(productName);

            temp.put("P_GROSS_WEIGHT", (grossWeight));
            temp.put("P_NET_WEIGHT", (netWeight));
            temp.put("P_UNITE_PRICE", (unitePrice));
            temp.put("P_TOTAL_COST", (cost));
            temp.put("P_PACKAGE_NUMBER", (packageNumber));
            temp.put("P_CUSTOMER_ID", null);
            temp.put("P_PRODUCT_ID", (productID));
            temp.put("CUSTOMER_ORDER_ID", -1);

            if (orderTag.length > 1) {
               // temp.put("P_CUSTOMER_ID", (p.getId()));

                int order_id = Integer.parseInt(orderTag[orderTag.length - 1]);
                temp.put("CUSTOMER_ORDER_ID", order_id);

            }
            dataMap.add(temp);

        }

        return dataMap;
    }

    public void deleteSllerCustomerOrderID(int orderID, int seasonID) {

        CallableStatement cs = null;
        try {
            cs = con.prepareCall("{?= call DELETE_SELLER_ORDER(?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, orderID);
            cs.setInt(3, seasonID);
            cs.execute();
            JOptionPane.showMessageDialog(null, cs.getInt(1));

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void changeSllerCustomerOrderID(int weightID, int Customer_orderID) {

        CallableStatement cs = null;
        try {
            cs = con.prepareCall("{?= call CHANGE_SELLER_CUSTOMER_ORDER(?,?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, weightID);
            cs.setInt(3, Customer_orderID);
            cs.execute();
            JOptionPane.showMessageDialog(null, cs.getInt(1));

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public int getcsutomerOrderIDByDate(String date, int customer_id) {

        CallableStatement cs = null;
        try {
            cs = con.prepareCall("{?= call GET_CUSTOMER_ORDER_ID_BY_DATE (?,?)}");
            cs.setString(2, date);
            cs.setInt(3, customer_id);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.execute();
            int x = cs.getInt(1);
            return x;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;

    }

    public int getCustomerOrderIDByDate(java.util.Date date, int customer_id, int store_id) {

        CallableStatement cs = null;
        try {
            cs = con.prepareCall("select order_id from customer_order where "
                    + "to_char(order_date,'DD-MM-YYYY')=to_char(?,'DD-MM-YYYY') and customer_id=?"
                    + " and store_id=?");
            cs.setDate(1, new java.sql.Date(date.getTime()));
            cs.setInt(2, customer_id);
            cs.setInt(3, store_id);
            ResultSet rs = cs.executeQuery();
            int order_id = 0;
            while (rs.next()) {

                order_id = rs.getInt("order_id");
            }
            rs.close();
            return order_id;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;

    }

    public int getProductID(String name) {

        CallableStatement cs = null;
        try {
            cs = con.prepareCall("{?= call GET_PRODUCT_ID(?) }");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, name);
            cs.executeQuery();
            return cs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return 0;
    }

    public String getFridageName(int id) {

        CallableStatement cs = null;
        try {
            cs = con.prepareCall("select FRIDAGE_NAME from FRIDAGE where FRIDAGE_ID=? ");
            cs.setInt(1, id);
            ResultSet rs = cs.executeQuery();
            String fridageName = "";
            while (rs.next()) {

                fridageName = rs.getString("FRIDAGE_NAME");
            }
            rs.close();
            return fridageName;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return "";
    }

    /**
     *
     * @return
     */
    public List<Pair<java.util.Date, Integer>> getAllCustomersOrdersNames(int id, int storeID, int FRIDAGE_ID) {
        List<Pair<java.util.Date, Integer>> customersOrders = new ArrayList<>();

        Statement st = null;

        try {

            st = con.createStatement();
            ResultSet rs1 = st.executeQuery("select   "
                    + "to_char (ORDER_DATE,'DD-MM-YYYY HH24:MI:SS AM') AS ORDER_DATE ,"
                    + "CUSTOMER_ID  "
                    + "from customer_order WHERE FINISHED = 0 and product_id = " + id + ""
                    + " and  store_id = " + storeID + ""
                    + "  and FRIDAGE_ID =" + FRIDAGE_ID);
            while (rs1.next()) {
                String s = rs1.getString("ORDER_DATE");
                java.util.Date order_date = StringToDate(s, "dd-MM-yyyy HH:mm:ss");
                Integer customer_id = rs1.getInt("CUSTOMER_ID");
                customersOrders.add(new Pair<>(order_date, customer_id));

            }
            rs1.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return customersOrders;
    }

    public List<String> getAllCustomersOrdersTags(int id, int storeID, int FRIDAGE_ID) {
        List<String> customersOrders = new ArrayList<>();

        Statement st = null;

        try {

            st = con.createStatement();
            ResultSet rs1 = st.executeQuery("select  order_tag  "
                    + "from customer_order WHERE FINISHED = 0 and product_id = " + id + ""
                    + " and  store_id = " + storeID + ""
                    + "  and FRIDAGE_ID =" + FRIDAGE_ID);
            while (rs1.next()) {
                String tag = rs1.getString("order_tag");
                customersOrders.add(tag);

            }
            rs1.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return customersOrders;
    }

    java.util.Date StringToDate(String stringDate, String pattern) {
        java.util.Date date = null;
        try {
            date = new SimpleDateFormat(pattern).parse(stringDate);
        } catch (ParseException ex) {
            Logger.getLogger(CustomerDischarge_panel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public String getcustomer_name(int id) {

        Statement st = null;
        try {

            st = con.createStatement();
            ResultSet rs = st.executeQuery("select CUSTOMER_NAME from CUSTOMER where customer_id = " + id + "");
            while (rs.next()) {
                String name = rs.getString("CUSTOMER_NAME");
                rs.close();
                return name;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public List<String> getAllCustomersNames() {
        List<String> customerNmes = new ArrayList<>();
        Statement st = null;
        try {

            st = con.createStatement();
            ResultSet rs = st.executeQuery("select CUSTOMER_NAME from CUSTOMER");
            while (rs.next()) {

                customerNmes.add(rs.getString("CUSTOMER_NAME"));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return customerNmes;
    }

    public int sellerTransaction(String c_name, String c_address, String c_phone, String type, Vector<Object> orderDetailData, double total, double paid, int fridage_id) {

        CallableStatement cs = null;
        List<Map> order_details = getSellerOrderDetailMap(orderDetailData);

        try {

            Object[] Java_TABLE;
            Java_TABLE = new Object[order_details.size()];
            for (int i = 0; i < order_details.size(); i++) {
                Java_TABLE[i] = new oracle.sql.STRUCT(new oracle.sql.StructDescriptor("SELLER_WEIGHT_OBJ", con), con, order_details.get(i));

            }
            oracle.sql.ARRAY SQL_Table = new oracle.sql.ARRAY(new oracle.sql.ArrayDescriptor("SELLER_WEIGHT_COLL", con), con, Java_TABLE);

            cs = con.prepareCall("{? = call SELLER_TRANSACTION (?,?,?,?,?,?,?,?)}");

            cs.registerOutParameter(1, Types.VARCHAR);
            cs.setString(2, c_name);
            cs.setString(3, c_address);
            cs.setString(4, c_phone);
            cs.setString(5, type);
            cs.setDouble(6, total);
            cs.setDouble(7, paid);
            cs.setArray(8, SQL_Table);
            cs.setInt(9, fridage_id);

            cs.executeUpdate();
            int x;
            String message;
            message = cs.getString(1);
            //  String message=(x!=0)?"تم الحفظ ":"لم يتم الحفظ";
            JOptionPane.showMessageDialog(null, message);
            return 0;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public Vector<Object> getAllContractors(String type, int seasonID) {

        CallableStatement cs = null;
        Vector<Object> data = new Vector<>();
        try {
            cs = con.prepareCall("SELECT\n"
                    + "   \n"
                    + "     CONTRACTOR.CONTRACTOR_NAME AS CONTRACTOR_NAME\n"
                    + " FROM \n"
                    + "     CONTRACTOR INNER JOIN CONTRACTOR_ACCOUNT"
                    + " ON CONTRACTOR.CONTRACTOR_ID = CONTRACTOR_ACCOUNT.CONTRACTOR_ID"
                    + "  where CONTRACTOR.CONTRACTOR_TYPE LIKE ? ");
            cs.setString(1, type);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                Vector<Object> temp = new Vector<>();
                String name = rs.getString("CONTRACTOR_NAME");
                String amount = String.valueOf(getContractorAmount(name, seasonID));

                temp.add((amount));
                temp.add(name);
                data.add(temp);

            }
            rs.close();
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;

    }

    public double getContractorAmount(String name, int seasonID) {

        CallableStatement cs = null;
        double amount = 0;
        try {
            cs = con.prepareCall("select sum (AMOUNT) as amount from CONTRACTOR c inner join CONTRACTOR_ACCOUNT co \n"
                    + "on c.CONTRACTOR_ID=co.CONTRACTOR_ID \n"
                    + "inner join CONTRACTOR_ACCOUNT_DETAIL coi\n"
                    + "on co.ACCOUNT_ID=coi.CONTRACTOR_ACCOUNT_ID where SEASON_ID=? and CONTRACTOR_NAME like ?");
            cs.setInt(1, seasonID);
            cs.setString(2, name);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {

                amount = rs.getDouble("amount");
            }
            rs.close();
            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return amount;

    }

    public List<java.util.Date> getDailySalesBookDates(int FRIDAGE_ID, int seasonID) {

        List<java.util.Date> dates = new ArrayList<java.util.Date>();
        CallableStatement cs = null;
        try {
            cs = con.prepareCall("select to_char(orderDate ,'DD-MM-YYYY')  as dates\n"
                    + "from (select DISTINCT(trunc(ORDER_DATE)) as orderDate from seller_order where FRIDAGE_ID = ? and SEASON_ID =?  order by orderDate desc   ) ");
            cs.setInt(1, FRIDAGE_ID);
            cs.setInt(2, seasonID);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                dates.add(StringToDate(rs.getString("dates"), "dd-MM-yyyy"));

            }
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return dates;

    }

    public void closeSeason() {

        CallableStatement cs = null;
        try {
            cs = con.prepareCall("{call CLOSE_SAEASON ()}");
            cs.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public List<java.util.Date> getcustomerDailyDates(int FRIDAGE_ID, int seasonID) {
        CallableStatement cs = null;
        List<java.util.Date> dates = new ArrayList<java.util.Date>();
        try {
            cs = con.prepareCall("select to_char(orderDate ,'DD-MM-YYYY')  as dates\n"
                    + "from (select DISTINCT(trunc(ORDER_DATE)) as orderDate from CUSTOMER_ORDER where FRIDAGE_ID = ? and SEASON_ID =?  order by orderDate desc   )");
            cs.setInt(1, FRIDAGE_ID);
            cs.setInt(2, seasonID);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                dates.add(StringToDate(rs.getString("dates"), "dd-MM-yyyy"));

            }
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return dates;

    }

    public Vector<String> inExactMatchSearchCustomersName(String customerName) {

        Statement st = null;
        Vector<String> customers_list = new Vector<>();
        try {
            st = con.createStatement();

            ResultSet rs = st.executeQuery("select  DISTINCT(CUSTOMER_NAME) from customer where CUSTOMER_NAME like '" + customerName + "%'");
            while (rs.next()) {
                customers_list.add(rs.getString("CUSTOMER_NAME"));

            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return customers_list;

    }

    public Vector<String> inExactMatchSearchLoanersName(String loanerName) {

        Statement st = null;
        Vector<String> customers_list = new Vector<>();
        try {
            st = con.createStatement();

            ResultSet rs = st.executeQuery("select  DISTINCT(loaner_NAME) from Loaners where loaner_NAME like '" + loanerName + "%'");
            while (rs.next()) {
                customers_list.add(rs.getString("loaner_NAME"));

            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return customers_list;

    }

    public Vector<Object> getLoaners(String type) {
        CallableStatement cs = null;
        Vector<Object> data = new Vector<Object>();

        try {
            cs = con.prepareCall("SELECT\n"
                    + "     LOANERS.LOANER_NAME AS LOANER_NAME,\n"
                    + "     LOAN_ACCOUNT.DUE_AMOUNT AS DUE_AMOUNT,\n"
                    + "     LOAN_ACCOUNT.LOAN_TYPE AS LOAN_TYPE\n"
                    + "FROM\n"
                    + "     LOANERS INNER JOIN LOAN_ACCOUNT ON LOANERS.LOANER_ID = LOAN_ACCOUNT.LOANER_ID\n"
                    + "WHERE\n"
                    + "      LOAN_ACCOUNT.FINISHED = 0 and  LOAN_ACCOUNT.LOAN_TYPE like ?");
            cs.setString(1, type);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                Vector<Object> temp = new Vector<Object>();
                temp.add((rs.getString("DUE_AMOUNT")));
                temp.add(rs.getString("LOANER_NAME"));

                data.add(temp);

            }
            rs.close();
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

    public Vector<Object> getLoanerDetail(String name, String type) {
        CallableStatement cs = null;
        Vector<Object> data = new Vector<Object>();
        DateFormat df = new SimpleDateFormat("EEEEE dd-MMMMMM-yyyy hh:mm aaaa", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        try {
            cs = con.prepareCall("SELECT\n"
                    + "     LOANERS.LOANER_NAME AS LOANER_NAME,\n"
                    + "     LOAN_ACCOUNT.LOAN_TYPE AS LOAN_TYPE,\n"
                    + "     to_char(INC_LOAN.INC_LOAN_DATE,'DD-MM-YYYY HH24:MI' )AS INC_LOAN_DATE,\n"
                    + "\n"
                    + "     INC_LOAN.AMOUNT AS AMOUNT,\n"
                    + "     INC_LOAN.NOTES AS NOTES\n"
                    + " FROM\n"
                    + "     LOANERS INNER JOIN LOAN_ACCOUNT ON LOANERS.LOANER_ID = LOAN_ACCOUNT.LOANER_ID\n"
                    + "     INNER JOIN INC_LOAN ON LOAN_ACCOUNT.LOAN_ACCOUNT_ID = INC_LOAN.LOAN_ACCOUNT_ID\n"
                    + "WHERE\n"
                    + "      LOAN_ACCOUNT.FINISHED = 0\n"
                    + "     and LOAN_ACCOUNT.LOAN_TYPE LIKE ? and  LOANERS.LOANER_NAME  like ? ");

            cs.setString(1, type);
            cs.setString(2, name);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {

                Vector<Object> temp = new Vector<Object>();
                java.util.Date date = StringToDate(rs.getString("INC_LOAN_DATE"), "dd-MM-yyyy HH:mm");
                String sdate = df.format(date);
                temp.add(rs.getString("notes"));
                temp.add((rs.getString("amount")));
                temp.add((sdate));

                data.add(temp);

            }
            rs.close();
            return data;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

    public Vector<Object> getLoanerInst(String name, String type) {
        CallableStatement cs = null;
        Vector<Object> data = new Vector<Object>();
        DateFormat df = new SimpleDateFormat("EEEEE dd-MMMMMM-yyyy hh:mm aaaa", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        try {
            cs = con.prepareCall("SELECT\n"
                    + "     LOANERS.LOANER_NAME AS LOANER_NAME,\n"
                    + "     LOAN_ACCOUNT.LOAN_TYPE AS LOAN_TYPE,\n"
                    + "     LOAN_PAYING.PAID_AMOUNT AS LOAN_PAYING_AMOUNT,\n"
                    + "    to_char( LOAN_PAYING.PAYING_DATE,'DD-MM-YYYY HH24:MI' )AS LOAN_PAYING_DATE,\n"
                    + "     LOAN_PAYING.NOTES AS LOAN_PAYING_NOTES\n"
                    + "FROM\n"
                    + "     LOANERS INNER JOIN LOAN_ACCOUNT ON LOANERS.LOANER_ID = LOAN_ACCOUNT.LOANER_ID\n"
                    + "     INNER JOIN LOAN_PAYING ON LOAN_ACCOUNT.LOAN_ACCOUNT_ID = LOAN_PAYING.LOAN_ACCOUNT_ID\n"
                    + "WHERE\n"
                    + "     LOAN_ACCOUNT.FINISHED = 0\n"
                    + "     and LOAN_ACCOUNT.LOAN_TYPE LIKE ? and LOANER_NAME like ?  ");

            cs.setString(1, type);
            cs.setString(2, name);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {

                Vector<Object> temp = new Vector<Object>();
                java.util.Date date = StringToDate(rs.getString("LOAN_PAYING_DATE"), "dd-MM-yyyy HH:mm");
                String sdate = df.format(date);
                temp.add(rs.getString("LOAN_PAYING_NOTES"));
                temp.add((rs.getString("LOAN_PAYING_AMOUNT")));
                temp.add((sdate));

                data.add(temp);

            }
            rs.close();
            return data;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

    public boolean vallidateSafeWithdrawal(double amount) {

        double safeAmount = getSafeBalance();
        if (amount > safeAmount) {

            JOptionPane.showMessageDialog(null, "رصيد الخزنة لايسمح ");
            return false;
        }
        return true;

    }

    public double loanbalance(String type) {

        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            cs = con.prepareCall("SELECT\n"
                    + "    SUM( LOAN_ACCOUNT.DUE_AMOUNT )AS DUE_AMOUNT\n"
                    + "FROM\n"
                    + "     LOAN_ACCOUNT\n"
                    + "WHERE\n"
                    + "     LOAN_ACCOUNT.FINISHED = 0\n"
                    + "     and LOAN_ACCOUNT.LOAN_TYPE LIKE ?");
            cs.setString(1, type);
            double amount = 0;
            rs = cs.executeQuery();
            while (rs.next()) {

                amount = rs.getDouble("DUE_AMOUNT");
            }
            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;

    }

    public Vector<String> inExactMatchSearchContractorName(String ContractorName, String type) {

        Statement st = null;
        Vector<String> customers_list = new Vector<>();
        try {
            st = con.createStatement();

            ResultSet rs = st.executeQuery("select  DISTINCT(CONTRACTOR_NAME) from CONTRACTOR where CONTRACTOR_NAME like '" + ContractorName + "%' and CONTRACTOR_TYPE like '" + type + "'");
            while (rs.next()) {
                customers_list.add(rs.getString("CONTRACTOR_NAME"));

            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return customers_list;

    }

    public Vector<String> inExactMatchSearchCustomersAddress(String customerAddress) {

        Statement st = null;
        Vector<String> customers_address_list = new Vector<>();
        try {
            st = con.createStatement();

            ResultSet rs = st.executeQuery("select  DISTINCT(CUSTOMER_ADDRESS) from customer where CUSTOMER_ADDRESS like '" + customerAddress + "%'");
            while (rs.next()) {
                customers_address_list.add(rs.getString("CUSTOMER_ADDRESS"));

            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return customers_address_list;

    }

    public Vector<String> inExactMatchSearchBuyerName(String BuyerName, String type) {

        Statement st = null;
        Vector<String> supplier_list = new Vector<>();
        try {
            st = con.createStatement();

            ResultSet rs = st.executeQuery("select  DISTINCT(SELLER_NAME) from SELLER where SELLER_NAME like '" + BuyerName + "%' and SELLER_TYPE like '" + type + "'");
            while (rs.next()) {
                supplier_list.add(rs.getString("SELLER_NAME"));

            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return supplier_list;

    }

    public Vector<String> inExactMatchSearchBuyerName(String BuyerName) {

        Statement st = null;
        Vector<String> supplier_list = new Vector<>();
        try {
            st = con.createStatement();

            ResultSet rs = st.executeQuery("select  DISTINCT(SELLER_NAME) from SELLER where SELLER_NAME like '" + BuyerName + "%'");
            while (rs.next()) {
                supplier_list.add(rs.getString("SELLER_NAME"));

            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return supplier_list;

    }

    public Vector<String> inExactMatchSearchloanerName(String loanerName, String type) {

        Statement st = null;
        Vector<String> loaner_list = new Vector<>();
        try {
            st = con.createStatement();

            ResultSet rs = st.executeQuery("  SELECT\n"
                    + "    distinct( LOANERS.LOANER_NAME) AS LOANER_NAME,\n"
                    + "     LOAN_ACCOUNT.LOAN_TYPE AS LOAN_TYPE\n"
                    + "FROM\n"
                    + "     LOANERS INNER JOIN LOAN_ACCOUNT ON LOANERS.LOANER_ID = LOAN_ACCOUNT.LOANER_ID\n"
                    + "where \n"
                    + " LOAN_ACCOUNT.LOAN_TYPE like '" + type + "'\n"
                    + "and  LOANER_NAME like '" + loanerName + "%'");
            while (rs.next()) {
                loaner_list.add(rs.getString("LOANER_NAME"));

            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return loaner_list;

    }

    public Vector<String> inExactMatchSearchBuyerAddress(String BuyerAddress) {

        Statement st = null;
        Vector<String> customers_address_list = new Vector<>();
        try {
            st = con.createStatement();

            ResultSet rs = st.executeQuery("select  DISTINCT(BUYER_ADDRESS) from BUYER where BUYER_ADDRESS like '" + BuyerAddress + "%'");
            while (rs.next()) {
                customers_address_list.add(rs.getString("BUYER_ADDRESS"));

            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return customers_address_list;

    }

    /*
    

     */
    public double customerTransaction(String S_name, String S_address,
            String S_phone, String productName, double NOLOUN,
            int storeID, String vechileType, String notes, double quantity, double tips, int unites, String type, int FRIDAGE_ID, String tag) {
        CallableStatement cs = null;

        try {
            cs = con.prepareCall("{?= call CUSTOMER_DISCHARGE_TRANSACTION (?,?,?,?,?,?,?,?,?,?,?,?,?,?)} ");
            cs.registerOutParameter(1, Types.VARCHAR);
            cs.setString(2, S_name);
            cs.setString(3, S_address);
            cs.setString(4, S_phone);
            cs.setDouble(5, NOLOUN);
            cs.setString(6, productName);
            cs.setInt(7, storeID);
            cs.setString(8, vechileType);
            cs.setString(9, notes);
            cs.setDouble(10, quantity);
            cs.setDouble(11, tips);
            cs.setInt(12, unites);
            cs.setString(13, type);
            cs.setInt(14, FRIDAGE_ID);
            cs.setString(15, tag);
            cs.executeUpdate();
            String checkInsert = cs.getString(1);
            JOptionPane.showMessageDialog(null, checkInsert);
            return 0;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return 0;
    }

    public int getDayOfWeek(Date date) {

        CallableStatement cs = null;
        try {
            cs = con.prepareCall("{?= call GET_WEEK_DAY (?)}");
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setDate(2, date);
            cs.executeQuery();
            return cs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public String getDate() {

        CallableStatement cs = null;
        try {
            cs = con.prepareCall("{?= call GET_DATE ()}");
            cs.registerOutParameter(1, Types.VARCHAR);
            cs.executeQuery();
            return cs.getString(1);
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

//GET_PURSHASES_PROFIT
    public double getPurchasesProfit(java.util.Date date, int seasonID, int fridageID) {

        CallableStatement cs = null;
        java.sql.Date sqlDate = (date == null) ? null : new java.sql.Date(date.getTime());

        String query = "SELECT SUM(COMMISION) as COMMISION FROM customer_order\n"
                + "where  PERIOD_ID=-1\n"
                + "  and  finished=1\n"
                + "AND   buy_price >  0 "
                + " and SEASON_ID = " + seasonID + " \n";
        query += (fridageID == 0) ? "" : " and customer_order.FRIDAGE_ID=" + fridageID;
        try {

            ResultSet rs = null;
            if (date == null) {
                cs = con.prepareCall(query);
                rs = cs.executeQuery();

            } else {
                query += " and to_char(order_date,'MM-YYYY')=to_char(?,'MM-YYYY')";
                cs = con.prepareCall(query);
                cs.setDate(1, sqlDate);
                rs = cs.executeQuery();
            }
            double amount = 0;

            while (rs.next()) {

                amount = rs.getDouble("COMMISION");
            }
            rs.close();
            return amount;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }
//GET_TOTAL_OUTCOME

    public double getTotalOutcome(java.util.Date date, int seasonID) {

        java.sql.Date sqlDate = (date == null) ? null : new java.sql.Date(date.getTime());
        CallableStatement cs = null;
        String query = "SELECT\n"
                + "  SUM(   OUTCOME_DETAIL.AMOUNT )as AMOUNT \n"
                + "FROM\n"
                + "     OUTCOME INNER JOIN OUTCOME_DETAIL ON OUTCOME.OUTCOME_ID = OUTCOME_DETAIL.OUTCOME_ID\n"
                + "WHERE\n"
                + "OUTCOME_DETAIL.OUTCOME_TYPE NOT IN ('ORDER_TIPS','K_L','K_S','K_V','ORDER_PAY','PURCHASES_WITHDRAWALS','IN_LOAN','OUT_LOAN','IN_PAY_LOAN','OUT_PAY_LOAN','NOLOUN')"
                + "and OUTCOME.SEASON_ID= " + seasonID + " \n";

        try {
            ResultSet rs = null;
            if (date != null) {

                query += " and  to_char(OUTCOME_DATE,'MM-YYYY') = to_char(?,'MM-YYYY')\n";
                cs = con.prepareCall(query);
                cs.setDate(1, sqlDate);
            } else {
                cs = con.prepareCall(query);
            }
            double amount = 0;
            rs = cs.executeQuery();
            while (rs.next()) {

                amount = rs.getDouble("AMOUNT");
            }
            rs.close();
            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return 0;
    }

    //GET_COMMISION_RPOFIT
    public double getCommisionProfit(java.util.Date date, int seasonID, int fridageID) {
        java.sql.Date sqlDate = (date == null) ? null : new java.sql.Date(date.getTime());
        CallableStatement cs = null;

        String query = "SELECT SUM(COMMISION) as commission FROM customer_order\n"
                + "where  PERIOD_ID=-1\n"
                + "  and  finished=1\n"
                + "AND   buy_price = 0"
                + "and SEASON_ID= " + seasonID + " \n";
        if (fridageID != 0) {

            query += " and FRIDAGE_ID=" + fridageID + "\n";

        }

        try {
            ResultSet rs = null;
            if (date != null) {

                query += " and  to_char(order_date,'MM-YYYY')=to_char(?,'MM-YYYY')\n";
                cs = con.prepareCall(query);
                cs.setDate(1, sqlDate);
                rs = cs.executeQuery();
            } else {
                cs = con.prepareCall(query);
                rs = cs.executeQuery();
            }
            double commission = 0;
            while (rs.next()) {

                commission = rs.getDouble("commission");

            }
            rs.close();
            return commission;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;

    }

    //GET_SALAMI_PRODUCTS_PROFIT
    public double getSalamiProductsProfit(int id, java.util.Date date, int seasonID, int fridageNumber) {
        java.sql.Date sqlDate = (date == null) ? null : new java.sql.Date(date.getTime());
        CallableStatement cs = null;
        String query = "SELECT\n"
                + "     SUM(SELLER_WEIGHT.amount) as TOTAL_COST\n"
                + "FROM\n"
                + "      SELLER_ORDER INNER JOIN  SELLER_WEIGHT ON SELLER_ORDER.ORDER_ID = SELLER_WEIGHT.SELLER_ORDER_ID\n"
                + "WHERE\n"
                + " SELLER_ORDER.SEASON_ID=" + seasonID + "\n"
                + "AND SELLER_WEIGHT.product_id=" + id + "\n";
        query += (fridageNumber == 0) ? "" : " AND SELLER_ORDER.FRIDAGE_ID = " + fridageNumber + "\n";

        try {
            ResultSet rs = null;

            if (sqlDate == null) {
                cs = con.prepareCall(query);

                rs = cs.executeQuery();
            } else {
                query += "    and  to_char(order_date,'MM-YYYY')=to_char(?,'MM-YYYY')";
                cs = con.prepareCall(query);
                cs.setDate(1, sqlDate);
                rs = cs.executeQuery();

            }
            double amount = 0;
            while (rs.next()) {

                amount = rs.getDouble("TOTAL_COST");

            }
            rs.close();
            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return 0;

    }
//GET_KAREEM_TOTAL_WITHDRAWA

    public double getKTotalOrders(java.util.Date date, int seasonID, int fridageID) {

        String sql1 = "SELECT\n"
                + "    SUM( CUSTOMER_ORDER.NET_PRICE) AS NET_PRICE\n"
                + "FROM\n"
                + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                + "WHERE\n"
                + "     CUSTOMER.CUSTOMER_NAME LIKE 'كريم_%'\n"
                + " AND FINISHED = 1 and to_char( CUSTOMER_ORDER.ORDER_DATE,'MM-YYYY')= to_char(?,'MM-YYYY')"
                + " AND  Season_Id = ? \n";

        String sql2 = "SELECT\n"
                + "    SUM( CUSTOMER_ORDER.NET_PRICE) AS NET_PRICE\n"
                + "FROM\n"
                + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                + "WHERE\n"
                + "     CUSTOMER.CUSTOMER_NAME LIKE 'كريم_%'\n"
                + " AND FINISHED = 1"
                + " AND Season_Id = ?";
        String fridageCond = (fridageID == 0) ? "" : " AND CUSTOMER_ORDER.Fridage_Id= " + fridageID + "\n";
        sql1 += fridageCond;
        sql2 += fridageCond;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            if (date == null) {
                cs = con.prepareCall(sql2);
                cs.setInt(1, seasonID);

            } else {

                cs = con.prepareCall(sql1);
                cs.setDate(1, new java.sql.Date(date.getTime()));
                cs.setInt(2, seasonID);
            }
            double amount = 0;

            rs = cs.executeQuery();
            while (rs.next()) {
                amount = rs.getDouble("NET_PRICE");

            }
            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return 0;
    }

    public double getkaremmTotalWithdrawal(java.util.Date date, int seasonID) {
        java.sql.Date sqlDate = (date == null) ? null : new java.sql.Date(date.getTime());
        String query = "select sum(AMOUNT) AS AMOUNT from SHOP.CONTRACTOR_ACCOUNT_DETAIL \n"
                + " WHERE   \n"
                + "      CONTRACTOR_ACCOUNT_DETAIL.SEASON_ID= " + seasonID
                + "     and paid =1 \n";

        CallableStatement cs = null;
        try {

            ResultSet rs = null;
            if (date == null) {
                cs = con.prepareCall(query);
                rs = cs.executeQuery();

            } else {
                query += "   and  to_char(CONTRACTOR_ACCOUNT_DETAIL.ACCOUNT_DETAIL_DATE,'MM-YYYY') = to_char(?,'MM-YYYY')";
                cs = con.prepareCall(query);
                cs.setDate(1, sqlDate);
                rs = cs.executeQuery();
            }
            double amount = 0;

            while (rs.next()) {
                amount = rs.getDouble("AMOUNT");

            }
            rs.close();
            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return 0;

    }

    public List<java.util.Date> getInventoryDates(int seasonID) {

        List<java.util.Date> dates = new ArrayList<>();
        Statement st = null;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select DISTINCT(to_char(ORDER_DATE,'MM-YYYY'))AS ORDER_DATE FROM CUSTOMER_ORDER"
                    + " where SEASON_ID = " + seasonID + "   order by  ORDER_DATE desc ");
            while (rs.next()) {
                java.util.Date tempDate = StringToDate(rs.getString("ORDER_DATE"), "MM-yyyy");
                dates.add(tempDate);

            }

            rs.close();
            return dates;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;

    }

    public Vector<Object> getPurchasesProfitDet(java.util.Date date, int seasonID, int fridageID) {

        CallableStatement cs = null;
        Vector<Object> data = new Vector<>();
        DateFormat df = new SimpleDateFormat("  dd-MMMMMM hh:mm aaaa ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        try {
            String fridageCond = (fridageID == 0) ? "" : ("   AND FRIDAGE_ID = " + fridageID);
            if (date != null) {
                cs = con.prepareCall("select to_char(ORDER_DATE ,'DD-MM-YYYY HH24:MI AM')AS ORDER_DATE ,"
                        + " order_id,NOLUN,"
                        + " commision, tips,"
                        + " TOTAL_PRICE,NET_PRICE, "
                        + " customer_id,FRIDAGE_ID ,"
                        + " UNITE_PRICE "
                        + " from customer_order"
                        + " where  "
                        + "CUSTOMER_ORDER.PERIOD_ID =-1 and finished=1 and buy_price <>0 "
                        + "and to_char(ORDER_DATE,'MM-YYYY')=to_char(?,'MM-YYYY')"
                        + " and SEASON_ID=?" + fridageCond + "     order by  ORDER_DATE desc");

                cs.setDate(1, new java.sql.Date(date.getTime()));
                cs.setInt(2, seasonID);
            } else {

                cs = con.prepareCall("select to_char(ORDER_DATE ,'DD-MM-YYYY HH24:MI AM')AS ORDER_DATE ,"
                        + "order_id,NOLUN,commision,"
                        + " tips,TOTAL_PRICE,NET_PRICE, "
                        + " customer_id,FRIDAGE_ID , "
                        + " UNITE_PRICE "
                        + "from customer_order"
                        + " where "
                        + "CUSTOMER_ORDER.PERIOD_ID =-1  and finished=1  and buy_price<>0 "
                        + " and SEASON_ID=?  " + fridageCond + "     order by  ORDER_DATE desc");
                cs.setInt(1, seasonID);

            }
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                Vector<Object> temp = new Vector<Object>();
                java.util.Date orderDate = StringToDate(rs.getString("ORDER_DATE"), "dd-MM-yyyy hh:mm ");
                String SorderDate = df.format(orderDate);

                int orderID = rs.getInt("order_id");
                double noloun = rs.getDouble("NOLUN");
                double coomision = rs.getDouble("commision");
                double tips = rs.getDouble("tips");
                double totalPrice = rs.getDouble("TOTAL_PRICE");
                double netPrice = rs.getDouble("NET_PRICE");
                String fridageName = getFridageName(rs.getInt("FRIDAGE_ID"));
                double UNITE_PRICE = rs.getDouble("UNITE_PRICE");
                String customerName = getcustomer_name(rs.getInt("customer_id"));
                temp.add(((String.valueOf(coomision))));
                temp.add((fridageName));
                temp.add(((String.valueOf(UNITE_PRICE))));

                temp.add(((String.valueOf(totalPrice))));
                temp.add(((String.valueOf(tips))));

                temp.add(((String.valueOf(netPrice))));
                temp.add(((String.valueOf(noloun))));
                temp.add(((String.valueOf(customerName + "," + SorderDate))));
                temp.add(((String.valueOf(orderID))));

                data.add(temp);

            }
            rs.close();
            return data;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                if (cs != null) {
                    cs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return null;
    }

    public Vector<Object> getkaremmTotalWithdrawalDet(java.util.Date date, int seasonID) {

        Vector<Object> data = getKareemWithdrawalsContDet(date, seasonID);

        return data;

    }

    public Vector<Object> getSalamiProductsProfitDet(int id, java.util.Date date, int seasonID, int fridageID) {
        Vector<Object> data = new Vector<>();
        CallableStatement cs = null;
        ResultSet rs = null;
        String sqlquery = "";
        DateFormat df = new SimpleDateFormat("  EEEEEE dd-MMMMMM-yyyy ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        String fridageCond = (fridageID == 0) ? "" : " and Fridage_Id=  " + fridageID;
        if (date != null) {

            sqlquery = "SELECT\n"
                    + "     SELLER_WEIGHT.GROSS_QUANTITY AS GROSS_QUANTITY,\n"
                    + "     SELLER_WEIGHT.NET_QUANTITY AS NET_QUANTITY,\n"
                    + "     SELLER_WEIGHT.UNITE_PRICE AS UNITE_PRICE,\n"
                    + "     SELLER_WEIGHT.AMOUNT AS TOTAL_COST,\n"
                    + "     SELLER_ORDER.ORDER_ID AS ORDER_ID,\n"
                    + "     SELLER_ORDER.FRIDAGE_ID AS FRIDAGE_ID ,"
                    + "     to_char(  SELLER_ORDER.ORDER_DATE,'DD-MM-YYYY HH24:MI AM')AS  ORDER_DATE,\n"
                    + "     PRODUCT.PRODUCT_NAME AS PRODUCT_NAME,\n"
                    + "     SELLER_WEIGHT.PACKAGE_NUMBER AS PACKAGE_NUMBER\n"
                    + " FROM\n"
                    + "      SELLER_ORDER INNER JOIN  SELLER_WEIGHT ON SELLER_ORDER.ORDER_ID = SELLER_WEIGHT.SELLER_ORDER_ID\n"
                    + "     INNER JOIN PRODUCT ON SELLER_WEIGHT.PRODUCT_ID = PRODUCT.PRODUCT_ID\n"
                    + "  WHERE PRODUCT.PRODUCT_ID = ? and  to_char(SELLER_ORDER.ORDER_DATE,'MM-YYYY')= to_char(?,'MM-YYYY') "
                    + " and Season_Id=? "
                    + fridageCond + "   order by  ORDER_DATE desc ";

            try {
                cs = con.prepareCall(sqlquery);
                cs.setInt(1, id);
                cs.setDate(2, new java.sql.Date(date.getTime()));
                cs.setInt(3, seasonID);
                rs = cs.executeQuery();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            sqlquery = "SELECT\n"
                    + "     SELLER_WEIGHT.GROSS_QUANTITY AS GROSS_QUANTITY,\n"
                    + "to_char(  SELLER_ORDER.ORDER_DATE,'DD-MM-YYYY HH24:MI AM')AS  ORDER_DATE,"
                    + "     SELLER_WEIGHT.NET_QUANTITY AS NET_QUANTITY,\n"
                    + "     SELLER_WEIGHT.UNITE_PRICE AS UNITE_PRICE,\n"
                    + "     SELLER_WEIGHT.AMOUNT AS TOTAL_COST,\n"
                    + "     SELLER_ORDER.FRIDAGE_ID AS FRIDAGE_ID ,"
                    + "     SELLER_ORDER.ORDER_ID AS ORDER_ID,\n"
                    + "     PRODUCT.PRODUCT_NAME AS PRODUCT_NAME,\n"
                    + "     SELLER_WEIGHT.PACKAGE_NUMBER AS PACKAGE_NUMBER\n"
                    + " FROM\n"
                    + "    SELLER_ORDER INNER JOIN  SELLER_WEIGHT ON SELLER_ORDER.ORDER_ID = SELLER_WEIGHT.SELLER_ORDER_ID\n"
                    + "     INNER JOIN PRODUCT ON SELLER_WEIGHT.PRODUCT_ID = PRODUCT.PRODUCT_ID\n"
                    + "\n"
                    + "\n"
                    + "  WHERE PRODUCT.PRODUCT_ID = ? "
                    + " and Season_Id=? " + fridageCond + " order by  ORDER_DATE desc";

            try {
                cs = con.prepareCall(sqlquery);
                cs.setInt(1, id);
                cs.setInt(2, seasonID);
                rs = cs.executeQuery();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            while (rs.next()) {
                Vector<Object> temp = new Vector<Object>();

                java.util.Date orderDate = StringToDate(rs.getString("ORDER_DATE"), "dd-MM-yyyy hh:mm");
                String sorderDate = df.format(orderDate);

                String orderId = rs.getString("ORDER_ID");
                String fridageName = getFridageName(rs.getInt("FRIDAGE_ID"));
                String productName = rs.getString("PRODUCT_NAME");
                String totalCost = rs.getString("TOTAL_COST");
                String unitPrice = rs.getString("UNITE_PRICE");
                String GrossQuantity = rs.getString("GROSS_QUANTITY");
                String packageNumber = rs.getString("PACKAGE_NUMBER");
                String netQuantity = rs.getString("NET_QUANTITY");

                temp.add((totalCost));
                temp.add((fridageName));
                temp.add((unitPrice));
                temp.add((packageNumber));
                if (id == 4) {
                    temp.add((netQuantity));
                    temp.add((GrossQuantity));
                }

                temp.add(productName);
                temp.add((sorderDate));
                temp.add((orderId));

                data.add(temp);

            }
            rs.close();
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public Vector<Object> getCommisionProfitDet(java.util.Date date, int seasonID, int fridageID) {

        Vector<Object> data = new Vector<>();
        CallableStatement cs = null;
        ResultSet rs = null;
        String sqlquery = "";

        DateFormat df = new SimpleDateFormat("   dd-MMMMMM-yyyy hh:mm aaaa ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        if (date != null) {

            try {
                sqlquery = "SELECT\n"
                        + "     PRODUCT.PRODUCT_NAME AS PRODUCT_NAME,\n"
                        + "     CUSTOMER_ORDER.ORDER_ID AS ORDER_ID,\n"
                        + "    to_char(  CUSTOMER_ORDER.ORDER_DATE,'DD-MM-YYYY HH24:MI AM')AS  ORDER_DATE,\n"
                        + "     CUSTOMER_ORDER.NOLUN AS NOLUN,\n"
                        + "     CUSTOMER_ORDER.GROSS_WEIGHT AS GROSS_WEIGHT,\n"
                        + "     CUSTOMER_ORDER.CUSTOMER_ID AS CUSTOMER_ID,\n"
                        + "     CUSTOMER_ORDER.NET_WEIGHT AS NET_WEIGHT,\n"
                        + "     CUSTOMER_ORDER.TOTAL_PRICE AS TOTAL_PRICE,\n"
                        + "     CUSTOMER_ORDER.NET_PRICE AS NET_PRICE,\n"
                        + "     CUSTOMER_ORDER.TIPS AS TIPS,\n"
                        + "     CUSTOMER_ORDER.COMMISION AS COMMISION,\n"
                        + "     CUSTOMER_ORDER.FRIDAGE_ID AS FRIDAGE_ID,\n"
                        + "     CUSTOMER_ORDER.VECHILE_TYPE AS VECHILE_TYPE\n"
                        + "FROM\n"
                        + "     PRODUCT INNER JOIN  CUSTOMER_ORDER ON PRODUCT.PRODUCT_ID = CUSTOMER_ORDER.PRODUCT_ID\n"
                        + " where   CUSTOMER_ORDER.PERIOD_ID=-1 and CUSTOMER_ORDER.FINISHED=1 "
                        + "and  to_char( CUSTOMER_ORDER.ORDER_DATE,'MM-YYYY')= to_char(?,'MM-YYYY') "
                        + " and buy_price = 0 "
                        + " and Season_Id=? \n";
                String frodageCond = (fridageID == 0) ? "" : "  and Fridage_Id=" + fridageID;
                sqlquery += frodageCond + "  order by  ORDER_DATE desc";
                cs = con.prepareCall(sqlquery);
                cs.setDate(1, new java.sql.Date(date.getTime()));
                cs.setInt(2, seasonID);
                rs = cs.executeQuery();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            try {
                sqlquery = "SELECT\n"
                        + "     PRODUCT.PRODUCT_NAME AS PRODUCT_NAME,\n"
                        + "     CUSTOMER_ORDER.ORDER_ID AS ORDER_ID,\n"
                        + "to_char(  CUSTOMER_ORDER.ORDER_DATE,'DD-MM-YYYY HH24:MI AM')AS  ORDER_DATE,"
                        + "     CUSTOMER_ORDER.NOLUN AS NOLUN,\n"
                        + "     CUSTOMER_ORDER.GROSS_WEIGHT AS GROSS_WEIGHT,\n"
                        + "     CUSTOMER_ORDER.CUSTOMER_ID AS CUSTOMER_ID,\n"
                        + "     CUSTOMER_ORDER.NET_WEIGHT AS NET_WEIGHT,\n"
                        + "     CUSTOMER_ORDER.TOTAL_PRICE AS TOTAL_PRICE,\n"
                        + "     CUSTOMER_ORDER.NET_PRICE AS NET_PRICE,\n"
                        + "     CUSTOMER_ORDER.FRIDAGE_ID AS FRIDAGE_ID,\n"
                        + "     CUSTOMER_ORDER.TIPS AS TIPS,\n"
                        + "     CUSTOMER_ORDER.COMMISION AS COMMISION,\n"
                        + "     CUSTOMER_ORDER.VECHILE_TYPE AS VECHILE_TYPE\n"
                        + "FROM\n"
                        + "     PRODUCT INNER JOIN CUSTOMER_ORDER ON PRODUCT.PRODUCT_ID = CUSTOMER_ORDER.PRODUCT_ID\n"
                        + "\n"
                        + "\n"
                        + "where  CUSTOMER_ORDER.FINISHED=1 and  CUSTOMER_ORDER.PERIOD_ID=-1"
                        + " and buy_price=0"
                        + "  and Season_Id=? ";
                String frodageCond = (fridageID == 0) ? "" : "  and Fridage_Id=" + fridageID;
                sqlquery += frodageCond + "   order by  ORDER_DATE desc";
                cs = con.prepareCall(sqlquery);
                cs.setInt(1, seasonID);
                rs = cs.executeQuery();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            while (rs.next()) {
                Vector<Object> temp = new Vector<>();
                String productName = rs.getString("PRODUCT_NAME");
                String fridageName = getFridageName(rs.getInt("FRIDAGE_ID"));
                String orderID = rs.getString("ORDER_ID");
                String nolun = rs.getString("NOLUN");
                String grossWeight = rs.getString("GROSS_WEIGHT");
                String netWeight = rs.getString("NET_WEIGHT");
                String vechileType = rs.getString("VECHILE_TYPE");
                String commision = rs.getString("COMMISION");
                String totalPrice = rs.getString("TOTAL_PRICE");
                String netPrice = rs.getString("NET_PRICE");
                String tips = rs.getString("TIPS");
                String customerName = getcustomer_name(rs.getInt("CUSTOMER_ID"));
                java.util.Date orderDate = StringToDate(rs.getString("ORDER_DATE"), "dd-MM-yyyy hh:mm");
                String SorderDate = df.format(orderDate);

                temp.add((commision));
                temp.add((fridageName));
                temp.add((netPrice));
                temp.add((totalPrice));
                temp.add((tips));
                temp.add((netWeight));
                temp.add((grossWeight));
                temp.add((productName));
                temp.add((nolun));
                temp.add((vechileType));
                temp.add((customerName + "," + SorderDate));
                temp.add((orderID));
                data.add(temp);
            }
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
                rs.close();;
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;

    }

    public Vector<Object> getTotalOutcomeDet(java.util.Date date, int seasonID, int fridageID) {

        Vector<Object> data = new Vector<>();
        CallableStatement cs = null;
        ResultSet rs = null;
        String sqlquery = "";

        DateFormat df = new SimpleDateFormat(" EEEEE dd-MMMMMM-yyyy ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        if (date != null) {
            sqlquery = "SELECT\n"
                    + " to_char( OUTCOME.OUTCOME_DATE,'DD-MM-YYYY HH24:MI AM')AS  OUTCOME_DATE ,"
                    + "     OUTCOME_DETAIL.OUTCOME_TYPE AS OUTCOME_TYPE,\n"
                    + "     OUTCOME_DETAIL.AMOUNT AS AMOUNT,\n"
                    + "     OUTCOME_DETAIL.SPENDER_NAME AS SPENDER_NAME,\n"
                    + "     OUTCOME_DETAIL.NOTES AS NOTES\n"
                    + "FROM\n"
                    + "     OUTCOME INNER JOIN_DETAIL OUTCOME_DETAIL ON OUTCOME.OUTCOME_ID = OUTCOME_DETAIL.OUTCOME_ID\n"
                    + " WHERE OUTCOME_TYPE NOT IN ('K_L','K_S','K_V','ORDER_PAY','PURCHASES_WITHDRAWALS','IN_LOAN','OUT_LOAN','IN_PAY_LOAN','OUT_PAY_LOAN','NOLOUN') "
                    + "AND  to_char( OUTCOME.OUTCOME_DATE ,'MM-YYYY')= to_char(?,'MM-YYYY')  "
                    + " and OUTCOME.SEASON_ID=?   order by  OUTCOME_DATE desc";
            try {
                cs = con.prepareCall(sqlquery);
                cs.setDate(1, new java.sql.Date(date.getTime()));
                cs.setInt(2, seasonID);
                rs = cs.executeQuery();

            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            sqlquery = "SELECT\n"
                    + " to_char( OUTCOME.OUTCOME_DATE,'DD-MM-YYYY HH24:MI AM')AS  OUTCOME_DATE,"
                    + "     OUTCOME_DETAIL.OUTCOME_TYPE AS OUTCOME_TYPE,\n"
                    + "     OUTCOME_DETAIL.AMOUNT AS AMOUNT,\n"
                    + "     OUTCOME_DETAIL.SPENDER_NAME AS SPENDER_NAME,\n"
                    + "     OUTCOME_DETAIL.NOTES AS NOTES\n"
                    + "FROM\n"
                    + "     OUTCOME INNER JOIN_DETAIL OUTCOME_DETAIL ON OUTCOME.OUTCOME_ID = OUTCOME_DETAIL.OUTCOME_ID\n"
                    + " WHERE OUTCOME_TYPE NOT IN ('K_L','K_S','K_V','ORDER_PAY','PURCHASES_WITHDRAWALS','IN_LOAN','OUT_LOAN','IN_PAY_LOAN','OUT_PAY_LOAN','NOLOUN') "
                    + " and  OUTCOME.SEASON_ID=?    order by  OUTCOME_DATE desc";

            try {
                cs = con.prepareCall(sqlquery);
                cs.setInt(1, seasonID);
                rs = cs.executeQuery();

            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        try {
            while (rs.next()) {
                Vector<Object> temp = new Vector<>();

                java.util.Date outcomeDate = StringToDate(rs.getString("OUTCOME_DATE"), "dd-MM-yyyy hh:mm");
                String SoutcomeDate = df.format(outcomeDate);
                /*العمال
                 صيانة تلاجات
                 نثريات
                 زكاة وصدقات
                 سماحات*/
                String type = rs.getString("OUTCOME_TYPE");
                type = NormalizMap.get(type);
                String amount = rs.getString("AMOUNT");
                String spender = rs.getString("SPENDER_NAME");
                String report = rs.getString("NOTES");

                temp.add(report);
                temp.add(amount);
                temp.add(spender);
                temp.add(type);

                temp.add(SoutcomeDate);
                data.add(temp);

            }
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
                rs.close();;
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    public Vector<Object> getKareemWithdrawalsContDet(java.util.Date date, int seasonID) {
        Vector<Object> data = new Vector<>();
        CallableStatement cs = null;
        ResultSet rs = null;
        String sqlquery = "";

        DateFormat df = new SimpleDateFormat(" EEEEE dd-MMMMMM-yyyy hh:mm aaaa ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        if (date != null) {
            sqlquery = "SELECT\n"
                    + "     to_char(CONTRACTOR_ACCOUNT_DETAIL.ACCOUNT_DETAIL_DATE,'DD-MM-YYYY HH:MI') AS ACCOUNT_DETAIL_DATE,\n"
                    + "    CONTRACTOR_ACCOUNT_DETAIL.AMOUNT AS AMOUNT,\n"
                    + "     CONTRACTOR.CONTRACTOR_NAME AS CONTRACTOR_NAME,\n"
                    + "     CONTRACTOR.CONTRACTOR_TYPE AS CONTRACTOR_TYPE,\n"
                    + "     CONTRACTOR_ACCOUNT_DETAIL.SPENDER_NAME AS SPENDER_NAME,\n"
                    + "     CONTRACTOR_ACCOUNT_DETAIL.REPORT AS REPORT\n"
                    + "FROM\n"
                    + "     SHOP.CONTRACTOR CONTRACTOR INNER JOIN SHOP.CONTRACTOR_ACCOUNT CONTRACTOR_ACCOUNT ON CONTRACTOR.CONTRACTOR_ID = CONTRACTOR_ACCOUNT.CONTRACTOR_ID\n"
                    + "     INNER JOIN SHOP.CONTRACTOR_ACCOUNT_DETAIL CONTRACTOR_ACCOUNT_DETAIL ON CONTRACTOR_ACCOUNT.ACCOUNT_ID = CONTRACTOR_ACCOUNT_DETAIL.CONTRACTOR_ACCOUNT_ID\n"
                    + "WHERE\n"
                    + "     CONTRACTOR_ACCOUNT_DETAIL.PAID = 1\n"
                    + "AND   CONTRACTOR_ACCOUNT_DETAIL.SEASON_ID=?"
                    + "     and to_char(CONTRACTOR_ACCOUNT_DETAIL.ACCOUNT_DETAIL_DATE,'MM-YYYY') =  to_char(?,'MM-YYYY')  order by  ACCOUNT_DETAIL_DATE desc \n ";
            try {
                cs = con.prepareCall(sqlquery);
                cs.setInt(1, seasonID);
                cs.setDate(2, new java.sql.Date(date.getTime()));

                rs = cs.executeQuery();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            sqlquery = "SELECT\n"
                    + "     to_char(CONTRACTOR_ACCOUNT_DETAIL.ACCOUNT_DETAIL_DATE,'DD-MM-YYYY HH:MI') AS ACCOUNT_DETAIL_DATE,\n"
                    + "     CONTRACTOR_ACCOUNT_DETAIL.AMOUNT AS AMOUNT,\n"
                    + "     CONTRACTOR.CONTRACTOR_NAME AS CONTRACTOR_NAME,\n"
                    + "     CONTRACTOR.CONTRACTOR_TYPE AS CONTRACTOR_TYPE,\n"
                    + "     CONTRACTOR_ACCOUNT_DETAIL.SPENDER_NAME AS SPENDER_NAME,\n"
                    + "     CONTRACTOR_ACCOUNT_DETAIL.REPORT AS REPORT\n"
                    + "FROM\n"
                    + "     SHOP.CONTRACTOR CONTRACTOR INNER JOIN SHOP.CONTRACTOR_ACCOUNT CONTRACTOR_ACCOUNT ON CONTRACTOR.CONTRACTOR_ID = CONTRACTOR_ACCOUNT.CONTRACTOR_ID\n"
                    + "     INNER JOIN SHOP.CONTRACTOR_ACCOUNT_DETAIL CONTRACTOR_ACCOUNT_DETAIL ON CONTRACTOR_ACCOUNT.ACCOUNT_ID = CONTRACTOR_ACCOUNT_DETAIL.CONTRACTOR_ACCOUNT_ID\n"
                    + "WHERE\n"
                    + "     CONTRACTOR_ACCOUNT_DETAIL.PAID = 1\n"
                    + " AND   CONTRACTOR_ACCOUNT_DETAIL.SEASON_ID=?  order by  ACCOUNT_DETAIL_DATE desc";

            try {
                cs = con.prepareCall(sqlquery);
                cs.setInt(1, seasonID);
                rs = cs.executeQuery();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            while (rs.next()) {
                Vector<Object> temp = new Vector<>();
                String notes = rs.getString("REPORT");
                String amount = rs.getString("AMOUNT");
                String spender = rs.getString("SPENDER_NAME");
                String type = rs.getString("CONTRACTOR_TYPE");
                type = NormalizMap.get(type);
                String contName = rs.getString("CONTRACTOR_NAME");
                java.util.Date contDate = StringToDate(rs.getString("ACCOUNT_DETAIL_DATE"), "dd-MM-yyyy hh:mm");
                String scontDate = (df.format(contDate));

                temp.add((notes));
                temp.add((amount));
                temp.add((spender));
                temp.add((type));
                temp.add((contName));
                temp.add((scontDate));
                data.add(temp);
            }
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
                rs.close();;
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public double getKTypeWithdrawal(String type, int seasonID, int paid) {

        CallableStatement cs = null;
        try {
            cs = con.prepareCall("SELECT\n"
                    + "       SUM( CONTRACTOR_ACCOUNT_DETAIL.AMOUNT) AS AMOUNT\n"
                    + "FROM\n"
                    + "     SHOP.CONTRACTOR CONTRACTOR INNER JOIN SHOP.CONTRACTOR_ACCOUNT CONTRACTOR_ACCOUNT ON CONTRACTOR.CONTRACTOR_ID = CONTRACTOR_ACCOUNT.CONTRACTOR_ID\n"
                    + "     INNER JOIN SHOP.CONTRACTOR_ACCOUNT_DETAIL CONTRACTOR_ACCOUNT_DETAIL ON CONTRACTOR_ACCOUNT.ACCOUNT_ID = CONTRACTOR_ACCOUNT_DETAIL.CONTRACTOR_ACCOUNT_ID\n"
                    + "WHERE\n"
                    + "     CONTRACTOR.CONTRACTOR_TYPE LIKE ? \n"
                    + "AND  CONTRACTOR_ACCOUNT_DETAIL.PAID=? \n"
                    + "AND  CONTRACTOR_ACCOUNT_DETAIL.SEASON_ID =?");
            cs.setString(1, type);
            cs.setInt(2, paid);
            cs.setInt(3, seasonID);

            ResultSet rs = cs.executeQuery();
            double amount = 0;
            while (rs.next()) {

                amount = rs.getDouble("AMOUNT");

            }
            rs.close();
            return amount;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public Vector<Object> getKareemWithdrawalsOutcometDet(java.util.Date date, int seasonID) {
        Vector<Object> data = new Vector<>();
        CallableStatement cs = null;
        ResultSet rs = null;
        String sqlquery = "";

        DateFormat df = new SimpleDateFormat(" EEEEE dd-MMMMMM-yyyy hh:mm aaaa ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        if (date != null) {

            sqlquery = "SELECT\n"
                    + "     OUTCOME_DETAIL.OUTCOME_TYPE AS OUTCOME_TYPE,\n"
                    + "     OUTCOME_DETAIL.AMOUNT AS AMOUNT,\n"
                    + "     OUTCOME_DETAIL.SPENDER_NAME AS SPENDER_NAME,\n"
                    + "     OUTCOME_DETAIL.NOTES AS NOTES,\n"
                    + "     to_char(  OUTCOME.OUTCOME_DATE,'DD-MM-YYYY HH24:MI AM')AS  OUTCOME_DATE\n"
                    + "FROM\n"
                    + "     OUTCOME INNER JOIN OUTCOME_DETAIL ON OUTCOME.OUTCOME_ID = OUTCOME_DETAIL.OUTCOME_ID\n"
                    + "WHERE\n"
                    + "     OUTCOME_TYPE LIKE 'K_V'"
                    + "  and to_char( OUTCOME.OUTCOME_DATE,'MM-YYYY') = to_char(?,'MM-YYYY')    order by  OUTCOME_DATE desc ";

            try {
                cs = con.prepareCall(sqlquery);
                cs.setDate(1, new java.sql.Date(date.getTime()));
                rs = cs.executeQuery();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            sqlquery = "SELECT\n"
                    + "     OUTCOME_DETAIL.OUTCOME_TYPE AS OUTCOME_TYPE,\n"
                    + "     OUTCOME_DETAIL.AMOUNT AS AMOUNT,\n"
                    + "     OUTCOME_DETAIL.SPENDER_NAME AS SPENDER_NAME,\n"
                    + "     OUTCOME_DETAIL.NOTES AS NOTES,\n"
                    + "     to_char(  OUTCOME.OUTCOME_DATE,'DD-MM-YYYY HH24:MI AM')AS  OUTCOME_DATE\n"
                    + "FROM\n"
                    + "     OUTCOME INNER JOIN OUTCOME_DETAIL ON OUTCOME.OUTCOME_ID = OUTCOME_DETAIL.OUTCOME_ID\n"
                    + "WHERE\n"
                    + "     OUTCOME_TYPE LIKE 'K_V'  order by  OUTCOME_DATE desc";

            try {
                cs = con.prepareCall(sqlquery);
                rs = cs.executeQuery();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            while (rs.next()) {
                Vector<Object> temp = new Vector<>();
                String notes = rs.getString("NOTES");
                String amount = rs.getString("AMOUNT");
                String spender = rs.getString("SPENDER_NAME");
                String type = rs.getString("OUTCOME_TYPE");
                type = NormalizMap.get(type);
                java.util.Date contDate = StringToDate(rs.getString("OUTCOME_DATE"), "dd-MM-yyyy hh:mm");
                String scontDate = (df.format(contDate));

                temp.add((notes));
                temp.add((amount));
                temp.add((spender));
                temp.add((type));
                temp.add(" ");
                temp.add((scontDate));

                data.add(temp);
            }
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
                rs.close();;
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public Vector<Object> searchForContractor(String name, java.sql.Date startDate, java.sql.Date endDate, String type) {

        CallableStatement cs = null;
        Vector<Object> data = new Vector<>();
        DateFormat df = new SimpleDateFormat("EEEEE ,dd-MMMMMM-YYYY hh:mm aaaa ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        try {
            cs = con.prepareCall("SELECT\n"
                    + "     to_char(CONTRACTOR_ACCOUNT_DETAIL.ACCOUNT_DETAIL_DATE,'DD-MM-YYYY HH24:MI AM') AS ACCOUNT_DETAIL_DATE,\n"
                    + "     CONTRACTOR.CONTRACTOR_NAME AS CONTRACTOR_NAME,\n"
                    + "     CONTRACTOR.CONTRACTOR_TYPE AS CONTRACTOR_TYPE,\n"
                    + "     CONTRACTOR_ACCOUNT_DETAIL.REPORT AS REPORT,\n"
                    + "     CONTRACTOR_ACCOUNT_DETAIL.AMOUNT AS AMOUNT,\n"
                    + "     CONTRACTOR_ACCOUNT_DETAIL.PAID AS PAID\n"
                    + "FROM\n"
                    + "     SHOP.CONTRACTOR CONTRACTOR INNER JOIN SHOP.CONTRACTOR_ACCOUNT CONTRACTOR_ACCOUNT ON CONTRACTOR.CONTRACTOR_ID = CONTRACTOR_ACCOUNT.CONTRACTOR_ID\n"
                    + "     INNER JOIN SHOP.CONTRACTOR_ACCOUNT_DETAIL CONTRACTOR_ACCOUNT_DETAIL ON CONTRACTOR_ACCOUNT.ACCOUNT_ID = CONTRACTOR_ACCOUNT_DETAIL.CONTRACTOR_ACCOUNT_ID\n"
                    + "WHERE\n"
                    + "     CONTRACTOR_NAME LIKE ?\n"
                    + "     and CONTRACTOR_TYPE LIKE ?\n"
                    + "     and trunc(ACCOUNT_DETAIL_DATE) < trunc(?)\n"
                    + "     and trunc(ACCOUNT_DETAIL_DATE) > trunc(?)  ?  order by  ACCOUNT_DETAIL_DATE desc");
            cs.setString(1, name);
            cs.setString(2, type);
            cs.setDate(3, endDate);
            cs.setDate(4, startDate);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {

                Vector<Object> temp = new Vector<>();
                java.util.Date date = StringToDate(rs.getString("ACCOUNT_DETAIL_DATE"), "dd-MM-yyyy hh:mm");

                String Sdate = df.format(date);
                String REPORT = rs.getString("REPORT");
                String amount = rs.getString("AMOUNT");
                boolean paid = (rs.getInt("PAID") == 1);

                temp.add(REPORT);
                temp.add(paid);
                temp.add(amount);
                temp.add((Sdate));
                data.add(temp);
            }
            rs.close();
            return data;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public double KContractorAmount(String name, java.sql.Date startDate, java.sql.Date endDate, String type) {

        CallableStatement cs = null;
        double amount = 0;
        DateFormat df = new SimpleDateFormat("EEEEE ,dd-MMMMMM-YYYY hh:mm aaaa ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        try {
            cs = con.prepareCall("SELECT\n"
                    + "    sum( CONTRACTOR_ACCOUNT_DETAIL.AMOUNT )AS AMOUNT \n"
                    + "FROM\n"
                    + "     SHOP.CONTRACTOR CONTRACTOR INNER JOIN SHOP.CONTRACTOR_ACCOUNT CONTRACTOR_ACCOUNT ON CONTRACTOR.CONTRACTOR_ID = CONTRACTOR_ACCOUNT.CONTRACTOR_ID\n"
                    + "     INNER JOIN SHOP.CONTRACTOR_ACCOUNT_DETAIL CONTRACTOR_ACCOUNT_DETAIL ON CONTRACTOR_ACCOUNT.ACCOUNT_ID = CONTRACTOR_ACCOUNT_DETAIL.CONTRACTOR_ACCOUNT_ID\n"
                    + "WHERE\n"
                    + "     CONTRACTOR_NAME LIKE ?\n"
                    + "     and CONTRACTOR_TYPE LIKE ?\n"
                    + "     and  CONTRACTOR_ACCOUNT_DETAIL.PAID = 0 \n"
                    + "     and trunc(CONTRACTOR_ACCOUNT_DETAIL.ACCOUNT_DETAIL_DATE) < trunc(?)\n"
                    + "     and trunc(CONTRACTOR_ACCOUNT_DETAIL.ACCOUNT_DETAIL_DATE) > trunc(?)");
            cs.setString(1, name);
            cs.setString(2, type);
            cs.setDate(3, endDate);
            cs.setDate(4, startDate);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {

                amount = rs.getDouble("AMOUNT");
            }
            rs.close();
            return amount;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double shopContractorAmount(String name, java.sql.Date startDate, java.sql.Date endDate, String type) {

        CallableStatement cs = null;
        double amount = 0;
        DateFormat df = new SimpleDateFormat("EEEEE ,dd-MMMMMM-YYYY hh:mm aaaa ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        try {
            cs = con.prepareCall("SELECT\n"
                    + "    sum( CONTRACTOR_ACCOUNT_DETAIL.AMOUNT )AS AMOUNT\n"
                    + "FROM\n"
                    + "     SHOP.CONTRACTOR CONTRACTOR INNER JOIN SHOP.CONTRACTOR_ACCOUNT CONTRACTOR_ACCOUNT ON CONTRACTOR.CONTRACTOR_ID = CONTRACTOR_ACCOUNT.CONTRACTOR_ID\n"
                    + "     INNER JOIN SHOP.CONTRACTOR_ACCOUNT_DETAIL CONTRACTOR_ACCOUNT_DETAIL ON CONTRACTOR_ACCOUNT.ACCOUNT_ID = CONTRACTOR_ACCOUNT_DETAIL.CONTRACTOR_ACCOUNT_ID\n"
                    + "WHERE\n"
                    + "     CONTRACTOR_NAME LIKE ?\n"
                    + "     and CONTRACTOR_TYPE LIKE ?\n"
                    + "     and  CONTRACTOR_ACCOUNT_DETAIL.PAID= 1 \n"
                    + "     and trunc(CONTRACTOR_ACCOUNT_DETAIL.ACCOUNT_DETAIL_DATE) < trunc(?)\n"
                    + "     and trunc(CONTRACTOR_ACCOUNT_DETAIL.ACCOUNT_DETAIL_DATE) > trunc(?)");
            cs.setString(1, name);
            cs.setString(2, type);
            cs.setDate(3, endDate);
            cs.setDate(4, startDate);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {

                amount = rs.getDouble("AMOUNT");
            }
            rs.close();
            return amount;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public Vector<Object> getkareemOrders(java.util.Date date, int seasonID, int fridageID) {

        Vector<Object> data = new Vector<>();
        CallableStatement cs = null;
        ResultSet rs = null;
        String sqlquery = "";

        DateFormat df = new SimpleDateFormat("EEEEE ,dd-MMMMMM hh:mm aaaa ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        if (date != null) {

            try {
                sqlquery = "SELECT\n"
                        + "     CUSTOMER_ORDER.NET_PRICE AS NET_PRICE,\n"
                        + "     CUSTOMER.CUSTOMER_NAME AS CUSTOMER_CUSTOMER_NAME,\n"
                        + "     CUSTOMER_ORDER.ORDER_ID AS ORDER_ID,\n"
                        + "     CUSTOMER_ORDER.ORDER_DATE AS ORDER_DATE,\n"
                        + "     CUSTOMER_ORDER.NOLUN AS NOLUN,\n"
                        + "     CUSTOMER_ORDER.GROSS_WEIGHT AS GROSS_WEIGHT,\n"
                        + "     CUSTOMER_ORDER.NET_WEIGHT AS NET_WEIGHT,\n"
                        + "     CUSTOMER_ORDER.COMMISION AS COMMISION,\n"
                        + "     CUSTOMER_ORDER.FRIDAGE_ID AS FRIDAGE_ID,\n"
                        + "     CUSTOMER_ORDER.TIPS AS TIPS,\n"
                        + "     CUSTOMER_ORDER.RATIO AS RATIO,\n"
                        + "     CUSTOMER_ORDER.TOTAL_PRICE AS TOTAL_PRICE\n"
                        + "FROM\n"
                        + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                        + "WHERE\n"
                        + "     CUSTOMER.CUSTOMER_NAME LIKE 'كريم_%'\n"
                        + " and  CUSTOMER_ORDER.FINISHED=1 and  to_char( CUSTOMER_ORDER.ORDER_DATE,'MM-YYYY')= to_char(?,'MM-YYYY') "
                        + " and CUSTOMER_ORDER.SEASON_ID=?";
                cs = con.prepareCall(sqlquery);
                cs.setDate(1, new java.sql.Date(date.getTime()));
                cs.setInt(2, seasonID);
                rs = cs.executeQuery();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            try {
                sqlquery = "SELECT\n"
                        + "     CUSTOMER_ORDER.NET_PRICE AS NET_PRICE,\n"
                        + "     CUSTOMER.CUSTOMER_NAME AS CUSTOMER_CUSTOMER_NAME,\n"
                        + "     CUSTOMER_ORDER.ORDER_ID AS ORDER_ID,\n"
                        + "     CUSTOMER_ORDER.ORDER_DATE AS ORDER_DATE,\n"
                        + "     CUSTOMER_ORDER.NOLUN AS NOLUN,\n"
                        + "     CUSTOMER_ORDER.GROSS_WEIGHT AS GROSS_WEIGHT,\n"
                        + "     CUSTOMER_ORDER.NET_WEIGHT AS NET_WEIGHT,\n"
                        + "     CUSTOMER_ORDER.COMMISION AS COMMISION,\n"
                        + "     CUSTOMER_ORDER.FRIDAGE_ID AS FRIDAGE_ID,\n"
                        + "     CUSTOMER_ORDER.TIPS AS TIPS,\n"
                        + "     CUSTOMER_ORDER.RATIO AS RATIO,\n"
                        + "     CUSTOMER_ORDER.TOTAL_PRICE AS TOTAL_PRICE\n"
                        + "FROM\n"
                        + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                        + "WHERE\n"
                        + "     CUSTOMER.CUSTOMER_NAME LIKE 'كريم_%'\n"
                        + "AND FINISHED=1"
                        + "  and CUSTOMER_ORDER.SEASON_ID=?";

                cs = con.prepareCall(sqlquery);
                cs.setInt(1, seasonID);
                rs = cs.executeQuery();

            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            while (rs.next()) {
                Vector<Object> temp = new Vector<>();
                String customer_name = rs.getString("CUSTOMER_CUSTOMER_NAME");
                String orderID = rs.getString("ORDER_ID");
                String fridageName = getFridageName(rs.getInt("FRIDAGE_ID"));
                String nolun = rs.getString("NOLUN");
                String grossWeight = rs.getString("GROSS_WEIGHT");
                String netWeight = rs.getString("NET_WEIGHT");
                String commision = rs.getString("COMMISION");
                String totalPrice = rs.getString("TOTAL_PRICE");
                String netPrice = rs.getString("NET_PRICE");
                String tips = rs.getString("TIPS");

                java.util.Date orderDate = StringToDate(rs.getString("ORDER_DATE"), "dd-MM-yyyy hh:mm");
                String SorderDate = df.format(orderDate);

                temp.add((netPrice));
                temp.add(fridageName);
                temp.add((commision));
                temp.add((totalPrice));
                temp.add((tips));
                temp.add((netWeight));
                temp.add((grossWeight));
                temp.add((nolun));
                temp.add((SorderDate));
                temp.add((customer_name));
                temp.add((orderID));
                data.add(temp);
            }
            return data;
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
                rs.close();;
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;

    }

    public Vector<Object> getSuggestedOrders(int finished, int dued, int seasonID) {

        try {

            Vector<Object> data = new Vector<>();
            CallableStatement cs = null;
            ResultSet rs = null;
            String sqlquery = "";

            DateFormat df = new SimpleDateFormat("EEEEEE , dd-MMMMMM hh:mm aaaa ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

            sqlquery = "SELECT\n"
                    + "     CUSTOMER_ORDER.ORDER_ID AS ORDER_ID,\n"
                    + "     CUSTOMER.CUSTOMER_NAME AS CUSTOMER_NAME,\n"
                    + "     to_char(CUSTOMER_ORDER.ORDER_DATE,'DD-MM HH24:MI AM') AS ORDER_DATE,\n"
                    + " CUSTOMER_ORDER.PRODUCT_ID AS PRODUCT_ID ,"
                    + "     CUSTOMER_ORDER.GROSS_WEIGHT AS GROSS_WEIGHT \n"
                    + " FROM\n"
                    + "      CUSTOMER INNER JOIN  CUSTOMER_ORDER ON CUSTOMER.CUSTOMER_ID = CUSTOMER_ORDER.CUSTOMER_ID\n"
                    + "WHERE\n"
                    + "     CUSTOMER_ORDER.FINISHED = ?\n"
                    + " and CUSTOMER_ORDER.PERIOD_ID=-1"
                    + " and CUSTOMER_ORDER.DUED=?"
                    + " and CUSTOMER_ORDER.SEASON_ID=?    order by CUSTOMER_ORDER.ORDER_DATE desc ";

            //
            cs = con.prepareCall(sqlquery);
            cs.setInt(1, finished);
            cs.setInt(2, dued);
            cs.setInt(3, seasonID);
            rs = cs.executeQuery();

            try {
                while (rs.next()) {
                    Vector<Object> temp = new Vector<>();
                    String customer_name = rs.getString("CUSTOMER_NAME");
                    String orderID = rs.getString("ORDER_ID");
                    int pro_id = rs.getInt("PRODUCT_ID");
                    if (issuggestedOrder(Integer.parseInt(orderID), pro_id)) {

                        String grossWeight = rs.getString("GROSS_WEIGHT");

                        java.util.Date orderDate = StringToDate(rs.getString("ORDER_DATE"), "dd-MM hh:mm");
                        String SorderDate = df.format(orderDate);

                        temp.add((grossWeight));
                        temp.add((customer_name + " ," + SorderDate));
                        temp.add((orderID));
                        data.add(temp);
                    }
                }
                return data;
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            } finally {

                try {
                    cs.close();
                    rs.close();;
                } catch (SQLException ex) {
                    Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            return null;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean issuggestedOrder(int orderID, int pro_id) {

        double grossWeight = getCustomerordergrossweight(orderID);

        double withDrawaled = getCustomerOrderWithdrawaledgrossWeight(orderID, pro_id);
        if ((grossWeight - withDrawaled) < 500) {

            return true;
        }
        return false;

    }

    //forgivness
    public double getTotalForgiveness(java.util.Date date, int seasonID) {

        CallableStatement cs = null;
        ResultSet rs = null;
        Vector<Object> data = new Vector<Object>();
        try {
            if (date == null) {
                cs = con.prepareCall("SELECT\n"
                        + " \n"
                        + "    sum( OUTCOME_DETAIL.AMOUNT )AS OUTCOME_DETAIL_AMOUNT\n"
                        + "  \n"
                        + "FROM\n"
                        + "     OUTCOME INNER JOIN OUTCOME_DETAIL ON OUTCOME.OUTCOME_ID = OUTCOME_DETAIL.OUTCOME_ID\n"
                        + "WHERE\n"
                        + "  OUTCOME_DETAIL.OUTCOME_TYPE like 'forgivness' "
                        + " and OUTCOME.SEASON_ID= " + seasonID);
                ;
            } else {
                cs = con.prepareCall("SELECT\n"
                        + " \n"
                        + "    sum( OUTCOME_DETAIL.AMOUNT )AS OUTCOME_DETAIL_AMOUNT\n"
                        + "  \n"
                        + "FROM\n"
                        + "     OUTCOME INNER JOIN OUTCOME_DETAIL ON OUTCOME.OUTCOME_ID = OUTCOME_DETAIL.OUTCOME_ID\n"
                        + "WHERE\n"
                        + "  OUTCOME_DETAIL.OUTCOME_TYPE like 'forgivness'"
                        + "  and  to_char(  OUTCOME.OUTCOME_DATE,'MM-YYYY')= to_char(?,'MM-YYYY')"
                        + " and OUTCOME.SEASON_ID= " + seasonID);

                cs.setDate(1, new java.sql.Date(date.getTime()));

            }
            rs = cs.executeQuery();
            double amount = 0;
            while (rs.next()) {

                amount = rs.getDouble("OUTCOME_DETAIL_AMOUNT");
            }

            return amount;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                rs.close();
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return 0;

    }

    public Vector<Object> getTotalForgivenessDet(java.util.Date date, int seasonID) {

        CallableStatement cs = null;
        ResultSet rs = null;
        DateFormat df = new SimpleDateFormat(" EEEEEE dd-MMMMMM-yyyy hh:mm aaaa ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        Vector<Object> data = new Vector<Object>();
        try {
            if (date == null) {
                cs = con.prepareCall("SELECT\n"
                        + "     OUTCOME_DETAIL.OUTCOME_DETAIL_ID AS OUTCOME_DETAIL_ID,\n"
                        + "     OUTCOME_DETAIL.OUTCOME_TYPE AS OUTCOME_TYPE,\n"
                        + "     OUTCOME_DETAIL.AMOUNT AS AMOUNT,\n"
                        + "     OUTCOME_DETAIL.SPENDER_NAME AS SPENDER_NAME,\n"
                        + "     OUTCOME_DETAIL.NOTES AS NOTES,\n"
                        + "     OUTCOME_DETAIL.OUTCOME_ID AS OUTCOME_ID,\n"
                        + "     OUTCOME_DETAIL.CUSTOMER_ID AS CUSTOMER_ID,\n"
                        + "     OUTCOME_DETAIL.ORDER_ID AS ORDER_ID,\n"
                        + "    to_char( OUTCOME.OUTCOME_DATE,'DD-MM-YYYY HH24:MI AM') AS OUTCOME_DATE\n"
                        + "FROM\n"
                        + "     OUTCOME INNER JOIN OUTCOME_DETAIL ON OUTCOME.OUTCOME_ID = OUTCOME_DETAIL.OUTCOME_ID\n"
                        + "WHERE\n"
                        + "  OUTCOME_DETAIL.OUTCOME_TYPE like 'forgivness' order by  OUTCOME_DATE desc");
            } else {
                cs = con.prepareCall("SELECT\n"
                        + "     OUTCOME_DETAIL.OUTCOME_DETAIL_ID AS OUTCOME_DETAIL_ID,\n"
                        + "     OUTCOME_DETAIL.OUTCOME_TYPE AS OUTCOME_TYPE,\n"
                        + "     OUTCOME_DETAIL.AMOUNT AS AMOUNT,\n"
                        + "     OUTCOME_DETAIL.SPENDER_NAME AS SPENDER_NAME,\n"
                        + "     OUTCOME_DETAIL.NOTES AS NOTES,\n"
                        + "     OUTCOME_DETAIL.OUTCOME_ID AS OUTCOME_ID,\n"
                        + "     OUTCOME_DETAIL.CUSTOMER_ID AS CUSTOMER_ID,\n"
                        + "     OUTCOME_DETAIL.ORDER_ID AS ORDER_ID,\n"
                        + "    to_char( OUTCOME.OUTCOME_DATE,'DD-MM-YYYY HH24:MI AM') AS OUTCOME_DATE\n"
                        + "FROM\n"
                        + "     OUTCOME INNER JOIN OUTCOME_DETAIL ON OUTCOME.OUTCOME_ID = OUTCOME_DETAIL.OUTCOME_ID\n"
                        + "WHERE\n"
                        + "  OUTCOME_DETAIL.OUTCOME_TYPE like 'forgivness'"
                        + "  and  to_char(  OUTCOME.OUTCOME_DATE,'MM-YYYY')= to_char(?,'MM-YYYY') order by  OUTCOME_DATE desc");

                cs.setDate(1, new java.sql.Date(date.getTime()));

            }
            rs = cs.executeQuery();
            while (rs.next()) {
                Vector<Object> temp = new Vector<Object>();
                String amount = rs.getString("AMOUNT");
                String notes = rs.getString("NOTES");
                java.util.Date d = StringToDate(rs.getString("OUTCOME_DATE"), "dd-MM-yyyy hh:mm");
                String orderDate = df.format(d);

                temp.add(notes);
                temp.add(amount);
                temp.add(orderDate);

                data.add(temp);

            }

            return data;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return null;

    }

    //allah
    public double getTotalOutcomeType(java.util.Date date, String type, int seasonID) {

        CallableStatement cs = null;
        ResultSet rs = null;
        Vector<Object> data = new Vector<Object>();
        try {
            if (date == null) {
                cs = con.prepareCall("SELECT\n"
                        + " \n"
                        + "    sum( OUTCOME_DETAIL.AMOUNT )AS OUTCOME_DETAIL_AMOUNT\n"
                        + "  \n"
                        + "FROM\n"
                        + "     OUTCOME INNER JOIN OUTCOME_DETAIL ON OUTCOME.OUTCOME_ID = OUTCOME_DETAIL.OUTCOME_ID\n"
                        + "WHERE\n"
                        + "  OUTCOME_DETAIL.OUTCOME_TYPE like ?"
                        + " and OUTCOME.SEASON_ID= ?");
                cs.setString(1, type);
                cs.setInt(2, seasonID);
            } else {
                cs = con.prepareCall("SELECT\n"
                        + " \n"
                        + "    sum( OUTCOME_DETAIL.AMOUNT )AS OUTCOME_DETAIL_AMOUNT\n"
                        + "  \n"
                        + "FROM\n"
                        + "     OUTCOME INNER JOIN OUTCOME_DETAIL ON OUTCOME.OUTCOME_ID = OUTCOME_DETAIL.OUTCOME_ID\n"
                        + "WHERE\n"
                        + "  OUTCOME_DETAIL.OUTCOME_TYPE like ?"
                        + " and OUTCOME.SEASON_ID= ?"
                        + "  and  to_char(  OUTCOME.OUTCOME_DATE,'MM-YYYY')= to_char(?,'MM-YYYY')");

                cs.setString(1, type);
                cs.setInt(2, seasonID);
                cs.setDate(3, new java.sql.Date(date.getTime()));

            }
            rs = cs.executeQuery();
            double amount = 0;
            while (rs.next()) {

                amount = rs.getDouble("OUTCOME_DETAIL_AMOUNT");
            }

            return amount;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                rs.close();
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return 0;

    }

    public Vector<Object> getTotaOutcomeTypehDet(java.util.Date date, String type, int seasonID) {

        CallableStatement cs = null;
        ResultSet rs = null;
        DateFormat df = new SimpleDateFormat(" EEEEEE dd-MMMMMM-yyyy hh:mm aaaa ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        Vector<Object> data = new Vector<Object>();
        try {
            if (date == null) {
                cs = con.prepareCall("SELECT\n"
                        + "     OUTCOME_DETAIL.OUTCOME_DETAIL_ID AS OUTCOME_DETAIL_ID,\n"
                        + "     OUTCOME_DETAIL.OUTCOME_TYPE AS OUTCOME_TYPE,\n"
                        + "     OUTCOME_DETAIL.AMOUNT AS AMOUNT,\n"
                        + "     OUTCOME_DETAIL.SPENDER_NAME AS SPENDER_NAME,\n"
                        + "     OUTCOME_DETAIL.NOTES AS NOTES,\n"
                        + "     OUTCOME_DETAIL.OUTCOME_ID AS OUTCOME_ID,\n"
                        + "     OUTCOME_DETAIL.CUSTOMER_ID AS CUSTOMER_ID,\n"
                        + "     OUTCOME_DETAIL.ORDER_ID AS ORDER_ID,\n"
                        + "    to_char( OUTCOME.OUTCOME_DATE,'DD-MM-YYYY HH24:MI AM') AS OUTCOME_DATE\n"
                        + "FROM\n"
                        + "     OUTCOME INNER JOIN OUTCOME_DETAIL ON OUTCOME.OUTCOME_ID = OUTCOME_DETAIL.OUTCOME_ID\n"
                        + "WHERE\n"
                        + "  OUTCOME_DETAIL.OUTCOME_TYPE like ?"
                        + " and OUTCOME.SEASON_ID= ? order by  OUTCOME_DATE desc");
                cs.setString(1, type);
                cs.setInt(2, seasonID);

            } else {
                cs = con.prepareCall("SELECT\n"
                        + "     OUTCOME_DETAIL.OUTCOME_DETAIL_ID AS OUTCOME_DETAIL_ID,\n"
                        + "     OUTCOME_DETAIL.OUTCOME_TYPE AS OUTCOME_TYPE,\n"
                        + "     OUTCOME_DETAIL.AMOUNT AS AMOUNT,\n"
                        + "     OUTCOME_DETAIL.SPENDER_NAME AS SPENDER_NAME,\n"
                        + "     OUTCOME_DETAIL.NOTES AS NOTES,\n"
                        + "     OUTCOME_DETAIL.OUTCOME_ID AS OUTCOME_ID,\n"
                        + "     OUTCOME_DETAIL.CUSTOMER_ID AS CUSTOMER_ID,\n"
                        + "     OUTCOME_DETAIL.ORDER_ID AS ORDER_ID,\n"
                        + "    to_char( OUTCOME.OUTCOME_DATE,'DD-MM-YYYY HH24:MI AM') AS OUTCOME_DATE\n"
                        + "FROM\n"
                        + "     OUTCOME INNER JOIN OUTCOME_DETAIL ON OUTCOME.OUTCOME_ID = OUTCOME_DETAIL.OUTCOME_ID\n"
                        + "WHERE\n"
                        + "  OUTCOME_DETAIL.OUTCOME_TYPE like ?"
                        + " and OUTCOME.SEASON_ID= ?"
                        + "  and  to_char(  OUTCOME.OUTCOME_DATE,'MM-YYYY')= to_char(?,'MM-YYYY') order by  OUTCOME_DATE desc");
                cs.setString(1, type);
                cs.setInt(2, seasonID);
                cs.setDate(3, new java.sql.Date(date.getTime()));

            }
            rs = cs.executeQuery();
            while (rs.next()) {
                Vector<Object> temp = new Vector<Object>();
                String amount = rs.getString("AMOUNT");
                String notes = rs.getString("NOTES");
                java.util.Date d = StringToDate(rs.getString("OUTCOME_DATE"), "dd-MM-yyyy hh:mm");
                String orderDate = df.format(d);

                temp.add(notes);
                temp.add(amount);
                temp.add(orderDate);

                data.add(temp);

            }

            return data;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return null;

    }
//varaity 

    public Vector<Object> getOutcomeTypeDet(java.util.Date date, String type, int seasonID) {

        CallableStatement cs = null;
        ResultSet rs = null;
        DateFormat df = new SimpleDateFormat(" EEEEEE dd-MMMMMM-yyyy hh:mm aaaa ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        Vector<Object> data = new Vector<Object>();
        try {
            if (date == null) {
                cs = con.prepareCall("SELECT    \n"
                        + "     OUTCOME_DETAIL.OUTCOME_DETAIL_ID AS OUTCOME_DETAIL_ID,\n"
                        + "     OUTCOME_DETAIL.OUTCOME_TYPE AS OUTCOME_TYPE,\n"
                        + "     OUTCOME_DETAIL.AMOUNT AS AMOUNT,\n"
                        + "     OUTCOME_DETAIL.SPENDER_NAME AS SPENDER_NAME,\n"
                        + "     OUTCOME_DETAIL.NOTES AS NOTES,\n"
                        + "     OUTCOME_DETAIL.OUTCOME_ID AS OUTCOME_ID,\n"
                        + "     OUTCOME_DETAIL.CUSTOMER_ID AS CUSTOMER_ID,\n"
                        + "     OUTCOME_DETAIL.ORDER_ID AS ORDER_ID,\n"
                        + "    to_char( OUTCOME.OUTCOME_DATE,'DD-MM-YYYY HH24:MI AM') AS OUTCOME_DATE   \n"
                        + " FROM\n"
                        + "     OUTCOME INNER JOIN  OUTCOME_DETAIL ON OUTCOME.OUTCOME_ID = OUTCOME_DETAIL.OUTCOME_ID\n"
                        + " WHERE\n"
                        + "  OUTCOME_DETAIL.OUTCOME_TYPE like ?  and OUTCOME.SEASON_ID=? order by  OUTCOME_DATE desc");
                cs.setString(1, type);
                cs.setInt(2, seasonID);
            } else {
                cs = con.prepareCall("SELECT\n"
                        + "     OUTCOME_DETAIL.OUTCOME_DETAIL_ID AS OUTCOME_DETAIL_ID,\n"
                        + "     OUTCOME_DETAIL.OUTCOME_TYPE AS OUTCOME_TYPE,\n"
                        + "     OUTCOME_DETAIL.AMOUNT AS AMOUNT,\n"
                        + "     OUTCOME_DETAIL.SPENDER_NAME AS SPENDER_NAME,\n"
                        + "     OUTCOME_DETAIL.NOTES AS NOTES,\n"
                        + "     OUTCOME_DETAIL.OUTCOME_ID AS OUTCOME_ID,\n"
                        + "     OUTCOME_DETAIL.CUSTOMER_ID AS CUSTOMER_ID,\n"
                        + "     OUTCOME_DETAIL.ORDER_ID AS ORDER_ID,\n"
                        + "    to_char( OUTCOME.OUTCOME_DATE,'DD-MM-YYYY HH24:MI AM') AS OUTCOME_DATE\n"
                        + "FROM\n"
                        + "     OUTCOME INNER JOIN OUTCOME_DETAIL ON OUTCOME.OUTCOME_ID = OUTCOME_DETAIL.OUTCOME_ID\n"
                        + "WHERE\n"
                        + "  OUTCOME_DETAIL.OUTCOME_TYPE like ? AND OUTCOME.SEASON_ID=?"
                        + "  and  to_char(  OUTCOME.OUTCOME_DATE,'MM-YYYY')= to_char(?,'MM-YYYY') order by  OUTCOME_DATE desc");
                cs.setString(1, type);
                cs.setInt(2, seasonID);
                cs.setDate(3, new java.sql.Date(date.getTime()));

            }
            rs = cs.executeQuery();
            while (rs.next()) {
                Vector<Object> temp = new Vector<Object>();
                String amount = rs.getString("AMOUNT");
                String notes = rs.getString("NOTES");
                java.util.Date d = StringToDate(rs.getString("OUTCOME_DATE"), "dd-MM-yyyy hh:mm");
                String orderDate = df.format(d);

                temp.add((notes));
                temp.add((amount));
                temp.add((orderDate));

                data.add(temp);

            }

            return data;

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return null;

    }

    public void export(String schema, String EXPORT_DIR, String fileName)
            throws SQLException, IOException {
        CallableStatement cs = con.prepareCall("{ call EXPORT_TRANSACTION(?,?,?) } ");
        cs.setString(1, schema.toUpperCase());
        cs.setString(2, EXPORT_DIR);
        cs.setString(3, fileName);
        cs.execute();
    }

    public void settDailyWork(String dir) {

        try {
            CallableStatement cs = con.prepareCall("{ call  CLOSE_DAILY_WORK(?) } ");
            cs.setString(1, dir);
            cs.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Table getTables(String tableName) {

        List<String> ColumnNames = new ArrayList<>();

        CallableStatement cs = null;
        List< List<String>> data = new ArrayList<>();

        try {
            cs = con.prepareCall("select * from  " + tableName + " where changed = 1");

            ResultSet rs = cs.executeQuery();
            ResultSetMetaData rsm = rs.getMetaData();
            for (int i = 1; i <= rsm.getColumnCount(); i++) {
                ColumnNames.add(rsm.getColumnName(i));

            }
            while (rs.next()) {

                List<String> temp = new ArrayList<>();
                for (String columnName : ColumnNames) {
                    temp.add(rs.getString(columnName));

                }
                data.add(temp);

            }

        } catch (SQLException ex) {
            Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new Table(ColumnNames, data, tableName);

    }

    public boolean confirmDataUpload(String[] tables) {

        CallableStatement cs = null;
        for (int i = 0; i < tables.length; i++) {
            try {
                cs = con.prepareCall("update " + tables[i] + " set changed = 1");
                cs.executeQuery();

            } catch (SQLException ex) {
                Logger.getLogger(DataSourc.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }

        }

        return true;

    }
}
