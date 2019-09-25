/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archive_pkg;

import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import shop.GUI.Main_Frame;
import shop.GUI.OrderForm_panel;
import shop.data_source_pkg.DataSourc;
import shop.data_structures_pkg.Pair;
import shop.test.AttributiveCellTableModel;
import shop.test.CellSpan;
import shop.test.MultiSpanCellTable;

/**
 *
 * @author Ahmed
 */
public class DailySalesARC extends javax.swing.JPanel {

    /**
     * Creates new form DailySalesARC
     */
    JFrame myFrame;
    DataSourc data_source;
    OrderForm_panel orderForm;
    MultiSpanCellTable fixedTable;
    List<java.util.Date> dailySalesBookDates;
    boolean monthesFillingMode;
    boolean dayFillinMode;

    int seasonID;
    int fridageNumber;

    public DailySalesARC(JFrame myFrame, DataSourc data_source, int seasonID, int fridageNumber) {

        this.myFrame = myFrame;
        this.data_source = data_source;

        this.seasonID = seasonID;
        this.fridageNumber = fridageNumber;
       intiatiateDailysalesBook();
        initComponents();
        fillDailySalesbookDates(seasonID, fridageNumber);

    }

    void refrishPage(int seasonID, int fridageNumber) {

        this.seasonID = seasonID;
        this.fridageNumber = fridageNumber;
        fillDailySalesbookDates(this.seasonID, this.fridageNumber);

    }

    void intiatiateDailysalesBook() {
        Object[][] data = new Object[][]{};

        AttributiveCellTableModel fixedModel = new AttributiveCellTableModel(
                data, dailSalesBook_columntitles) {
                    public boolean CellEditable(int row, int col) {
                        return false;
                    }
                };

        fixedTable = new MultiSpanCellTable(fixedModel);
        fixedTable.setRowHeight(25);
        fixedTable.setFont(new Font("Arail", Font.BOLD, 14));

    }

    String NormalizeinArabic(String str) {

        char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
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

    List<String> getDaysList(List<java.util.Date> dates, String selectedMonth) {
        DateFormat df = new SimpleDateFormat("MMMMMM-yyyy ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        DateFormat df_day = new SimpleDateFormat("dd", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        List<String> days = new ArrayList<>();
        boolean isCurrentMonthe = false;
        for (int i = 0; i < dates.size(); i++) {
            String month = NormalizeinArabic(df.format(dates.get(i)));
            if (month.equals(selectedMonth)) {
                days.add(NormalizeinArabic(df_day.format(dates.get(i))));

            }

        }
        String currentMonthe = NormalizeinArabic(df.format(new Date()));
        String currentDay = NormalizeinArabic(df_day.format(new Date()));

        if (!days.contains(currentDay) && selectedMonth.equals(currentMonthe)) {
            days.add(currentDay);

        }
        return days;

    }

    List<String> getMonthesList(List<java.util.Date> dates) {
        DateFormat df = new SimpleDateFormat("MMMMMM-yyyy ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        List<String> monthes = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            String date = NormalizeinArabic(df.format(dates.get(i)));
            if (!monthes.contains(date)) {
                monthes.add((date));

            }

        }
//        String currentMonth = NormalizeinArabic(df.format(new Date()));
//        if (!monthes.contains(currentMonth)) {
//            monthes.add(currentMonth);
//
//        }
        return monthes;

    }

    void fillDailySalesbookDates(int seasonID, int fridageNumber) {

        monthesFillingMode = true;
        dailySalesBookDates = data_source.getDailySalesBookDates(fridageNumber, seasonID);
        DateFormat df = new SimpleDateFormat("MMMMMM-yyyy ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        List<String> monthes = getMonthesList(dailySalesBookDates);
        month_CB.removeAllItems();

        for (int i = 0; i < monthes.size(); i++) {
            String formattedDate = (monthes.get(i));
            month_CB.addItem(formattedDate);
        }
        
       if(month_CB.getItemCount()==0){
       day_CB.removeAllItems();
       
  Object[][] data = new Object[][]{};

        AttributiveCellTableModel fixedModel = new AttributiveCellTableModel(
                data, dailSalesBook_columntitles) {
                    public boolean CellEditable(int row, int col) {
                        return false;
                    }
                };       
       fixedTable.setModel(fixedModel);
       }else{
       monthesFillingMode=false;
       month_CB.setSelectedIndex(0);
       }

//        String currentDate = df.format(new Date());
//        for (int i = 0; i < dailySalesBookDates.size(); i++) {
//            String sDate = df.format(dailySalesBookDates.get(i));
//            if (sDate.equals(currentDate)) {
//                month_CB.setSelectedIndex(i);
//                return;
//            }
//        }
//        dailySalesBookDates.add(new Date());
//
//        dates_CB.addItem(NormalizeinArabic(currentDate));
//        dates_CB.setSelectedItem(NormalizeinArabic(currentDate));
    }

    String getSellerTypeInenglish(String name) {
        switch (name) {

            case "نقدي":
                return "CASH";
            case "مؤقت":
                return "TEMPERORY";
            case "دائم":
                return "PERMENANT";

        }
        return null;

    }

    void fillDailySalesBook_Table(java.sql.Date date) {
        List<Pair<int[], int[]>> spanRowsList = new ArrayList<>();
        List<Integer> GapRows = new ArrayList<>();
        Object[][] data = data_source.getDailyBookTransaction(date, spanRowsList, GapRows, this.fridageNumber);

        AttributiveCellTableModel fixedModel = new AttributiveCellTableModel(
                data, dailSalesBook_columntitles) {
                    @Override
                    public boolean isCellEditable(int row, int col) {
                        return false;
                    }
                };
        CellSpan cellAtt = (CellSpan) fixedModel.getCellAttribute();
        for (int i = 0; i < spanRowsList.size(); i++) {
            cellAtt.combine(spanRowsList.get(i).getFrist(), spanRowsList.get(i).getSecond());

        }

        for (int i = 0; i < GapRows.size(); i++) {
            cellAtt.combine(new int[]{GapRows.get(i) - 1}, new int[]{1, 2, 3, 4, 5, 6, 7});

        }

        fixedTable.setModel(fixedModel);

        fixedTable.setDefaultRenderer(Object.class, new DailySalesARC.MyCellRenderer(GapRows));
        setFixedTableSize();
    }

    void setFixedTableSize() {
        fixedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn column = null;
        for (int i = 0; i < fixedTable.getColumnModel().getColumnCount(); i++) {
            column = fixedTable.getColumnModel().getColumn(i);

            switch (i) {
                case 0:
                    column.setPreferredWidth(250);
                    break;
                case 1:
                    column.setPreferredWidth(110);
                    break;

                case 2:
                    column.setPreferredWidth(110);
                    break;

                case 3:
                    column.setPreferredWidth(110);
                    break;

                case 4:
                    column.setPreferredWidth(110);
                    break;

                case 5:
                    column.setPreferredWidth(110);
                    break;

                case 6:
                    column.setPreferredWidth(110);
                    break;

                case 7:
                    column.setPreferredWidth(200);
                    break;

                case 8:
                    column.setPreferredWidth(200);
                    break;
            }
        }

    }

    public class MyCellRenderer extends javax.swing.table.DefaultTableCellRenderer {

        List<Integer> rows = new ArrayList<>();

        public MyCellRenderer(List<Integer> rows) {
            this.rows = rows;

        }

        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, java.lang.Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            final java.awt.Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (rows.contains(row)) {

                cellComponent.setBackground(Color.lightGray);

            } else {
                cellComponent.setBackground(Color.white);

            }
            cellComponent.setFocusable(false);

            return cellComponent;

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        month_CB = new javax.swing.JComboBox();
        day_CB = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        day_label = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane(fixedTable);

        setLayout(new javax.swing.OverlayLayout(this));

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 255), 3));
        jPanel3.setForeground(new java.awt.Color(102, 102, 255));

        month_CB.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        month_CB.setPreferredSize(new java.awt.Dimension(150, 30));
        month_CB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                month_CBActionPerformed(evt);
            }
        });
        jPanel3.add(month_CB);

        day_CB.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        day_CB.setPreferredSize(new java.awt.Dimension(75, 30));
        day_CB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                day_CBActionPerformed(evt);
            }
        });
        jPanel3.add(day_CB);

        jLabel9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("الموافق:");
        jLabel9.setPreferredSize(new java.awt.Dimension(60, 22));
        jPanel3.add(jLabel9);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("(");
        jLabel1.setPreferredSize(new java.awt.Dimension(15, 22));
        jPanel3.add(jLabel1);

        day_label.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        day_label.setForeground(new java.awt.Color(255, 0, 51));
        day_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        day_label.setText("الجمعه");
        day_label.setPreferredSize(new java.awt.Dimension(60, 22));
        jPanel3.add(day_label);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setText(")");
        jPanel3.add(jLabel10);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText(" مبيع يوم:");
        jLabel2.setPreferredSize(new java.awt.Dimension(80, 22));
        jPanel3.add(jLabel2);
        jPanel3.add(jLabel13);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jScrollPane4)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
        );

        add(jPanel1);
    }// </editor-fold>//GEN-END:initComponents

    private void month_CBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_month_CBActionPerformed
        // TODO add your handling code here:
        DateFormat df_month = new SimpleDateFormat("MMMMMM-yyyy ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        DateFormat df_day = new SimpleDateFormat("dd", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        if (!monthesFillingMode) {
            dayFillinMode = true;
            day_CB.removeAllItems();
            List<String> days = getDaysList(dailySalesBookDates, month_CB.getSelectedItem().toString());
            for (int i = 0; i < days.size(); i++) {
                day_CB.addItem(days.get(i));

            }
            dayFillinMode = false;
            //if selected month is cusrrent month
            if (NormalizeinArabic(df_month.format(new Date())).equals(month_CB.getSelectedItem().toString())) {

                String currentday = df_day.format(new Date());

                day_CB.setSelectedItem((NormalizeinArabic(currentday)));

            } else {
                day_CB.setSelectedIndex(0);
            }

        }

    }//GEN-LAST:event_month_CBActionPerformed

    private void day_CBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_day_CBActionPerformed
        // TODO add your handling code here:

        DateFormat df = new SimpleDateFormat("dd-MMMMMM-yyyy ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        DateFormat df_DAY_NAME = new SimpleDateFormat("EEEEEEE", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        if (!monthesFillingMode && !dayFillinMode) {

            String month = month_CB.getSelectedItem().toString();
            String day = day_CB.getSelectedItem().toString();
            String selectdDate = day + "-" + month;
            //get selected date
            int i = 0;
            for (i = 0; i < dailySalesBookDates.size(); i++) {
                String s = NormalizeinArabic(df.format(dailySalesBookDates.get(i)));
                if (selectdDate.equals(s)) {

                    break;
                }

            }
            if (i < dailySalesBookDates.size()) {
                Date date = dailySalesBookDates.get(i);

                String dayname = df_DAY_NAME.format(date);
                day_label.setText(dayname);
                fillDailySalesBook_Table(new java.sql.Date(date.getTime()));

            }
        }
    }//GEN-LAST:event_day_CBActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox day_CB;
    private javax.swing.JLabel day_label;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JComboBox month_CB;
    // End of variables declaration//GEN-END:variables

    String orderDetails_columnsNames[] = new String[]{
        "اسم العميل", "المبلغ", "الفئة", "عدد", "الصافي", "القائم", "المنتج"
    };

    String dailSalesBook_columntitles[] = new String[]{
        "اسم العميل", "المبلغ", "الفئه", "الصافي", "القائم", "عدد", "المنتج", "اسم البياع"
    };

}
