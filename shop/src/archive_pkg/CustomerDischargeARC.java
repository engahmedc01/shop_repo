/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archive_pkg;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import shop.GUI.CustomerDischarge_panel;
import shop.GUI.Main_Frame;
import shop.data_source_pkg.DataSourc;

/**
 *
 * @author Ahmed
 */
public class CustomerDischargeARC extends javax.swing.JPanel {

    /**
     * Creates new form CustomerDischargeARC
     */
    DataSourc data_source;
    JFrame myFrame;
    boolean fillingMode;
    boolean monthesFillingMode;
    boolean dayFillinMode;
    int seasonID;
    int fridageNumber;
    List<java.util.Date> customerOrderBookDates;

    public CustomerDischargeARC(JFrame myFrame, DataSourc data_source, int seasonID, int fridageNumber) {
        this.data_source = data_source;
        this.myFrame = myFrame;

        initComponents();
        refrishPage(seasonID, fridageNumber);
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

        return monthes;

    }

    public void refrishPage(int seasonID, int fridageNumber) {
        this.fridageNumber = fridageNumber;
        this.seasonID = seasonID;
        fillDates(seasonID, fridageNumber);

    }

    void fillCustomersDailyTransactionTable(java.sql.Date date) {
//int firdage_id =fridage_CB.getSelectedIndex()+1;

        Object[][] data = data_source.getCustomerDailyTransaction(date, this.fridageNumber);
        DefaultTableModel dtm = (DefaultTableModel) customersDailyTransaction_Table.getModel();
        dtm.setDataVector(data, custDailyTrans_columnTitle);

        customersDailyTransaction_Table.setModel(dtm);
        setCustomersDailyTransaction_TableSize();

    }

    Date StringToDate(String s) {
        Date date = null;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(s);
        } catch (ParseException ex) {
            Logger.getLogger(CustomerDischarge_panel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    void fillDates(int seasonID, int fridageNumber) {
        monthesFillingMode = true;

        customerOrderBookDates = data_source.getcustomerDailyDates(fridageNumber, seasonID);
        month_CB.removeAllItems();
        DateFormat df = new SimpleDateFormat("MMMMMM-yyyy ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        List<String> monthes = getMonthesList(customerOrderBookDates);

        for (int i = 0; i < monthes.size(); i++) {
            String formattedDate = (monthes.get(i));

            month_CB.addItem(formattedDate);
        }

        
        if (month_CB.getItemCount() > 0) {
            monthesFillingMode = false;
            month_CB.setSelectedIndex(0);
        } 
        else{
             day_CB.removeAllItems();
            DefaultTableModel dtm=(DefaultTableModel)customersDailyTransaction_Table.getModel();
            dtm.setDataVector(null, custDailyTrans_columnTitle);
            customersDailyTransaction_Table.setModel(dtm);

        }
    }

    void setCustomersDailyTransaction_TableSize() {

        TableColumn column = null;
        for (int i = 0; i < 8; i++) {
            column = customersDailyTransaction_Table.getColumnModel().getColumn(i);
            switch (i) {
                case 0:
                    column.setPreferredWidth(5);
                    break;
                case 1:
                    column.setPreferredWidth(50);
                    break;
                case 2:
                    column.setPreferredWidth(20);
                    break;
                case 3:
                    column.setPreferredWidth(20);
                    break;
                case 4:
                    column.setPreferredWidth(20);
                    break;
                case 5:
                    column.setPreferredWidth(20);
                    break;
                case 6:
                    column.setPreferredWidth(30);
                    break;
                case 7:
                    column.setPreferredWidth(100);
                    break;

            }
        }

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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dataBook_panel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        month_CB = new javax.swing.JComboBox();
        day_CB = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        day_label = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        customersDailyTransaction_Table = new javax.swing.JTable();

        setLayout(new javax.swing.OverlayLayout(this));

        dataBook_panel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        dataBook_panel.setLayout(new javax.swing.OverlayLayout(dataBook_panel));

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 255), 3));
        jPanel2.setForeground(new java.awt.Color(102, 102, 255));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel15.setText("(");
        jLabel15.setPreferredSize(new java.awt.Dimension(8, 30));
        jPanel2.add(jLabel15);

        month_CB.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        month_CB.setPreferredSize(new java.awt.Dimension(150, 30));
        month_CB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                month_CBActionPerformed(evt);
            }
        });
        jPanel2.add(month_CB);

        day_CB.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        day_CB.setPreferredSize(new java.awt.Dimension(75, 30));
        day_CB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                day_CBActionPerformed(evt);
            }
        });
        jPanel2.add(day_CB);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel16.setText(")");
        jLabel16.setPreferredSize(new java.awt.Dimension(8, 30));
        jPanel2.add(jLabel16);

        jLabel8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("الموافق:");
        jLabel8.setPreferredSize(new java.awt.Dimension(60, 22));
        jPanel2.add(jLabel8);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("(");
        jLabel1.setPreferredSize(new java.awt.Dimension(15, 22));
        jPanel2.add(jLabel1);

        day_label.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        day_label.setForeground(new java.awt.Color(255, 0, 51));
        day_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        day_label.setText("الجمعه");
        day_label.setPreferredSize(new java.awt.Dimension(60, 22));
        jPanel2.add(day_label);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setText(")");
        jPanel2.add(jLabel7);
        jPanel2.add(jLabel13);

        jLabel14.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("تحصيل يوم: ");
        jLabel14.setPreferredSize(new java.awt.Dimension(80, 22));
        jPanel2.add(jLabel14);

        customersDailyTransaction_Table.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        customersDailyTransaction_Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "خالص", "مكان التخزين", "وهبه", "عدد السبايط", "الوزن القائم/العدد", "ناولون", "المنتج", "اسم العميل"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        customersDailyTransaction_Table.setCellSelectionEnabled(true);
        customersDailyTransaction_Table.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        customersDailyTransaction_Table.setGridColor(new java.awt.Color(102, 102, 255));
        customersDailyTransaction_Table.setRowHeight(25);
        customersDailyTransaction_Table.setRowMargin(3);
        jScrollPane1.setViewportView(customersDailyTransaction_Table);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                .addContainerGap())
        );

        dataBook_panel.add(jPanel1);

        add(dataBook_panel);
    }// </editor-fold>//GEN-END:initComponents

    private void month_CBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_month_CBActionPerformed
        // TODO add your handling code here:
        DateFormat df_month = new SimpleDateFormat("MMMMMM-yyyy ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        DateFormat df_day = new SimpleDateFormat("dd", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
/**/
        if (!monthesFillingMode&&month_CB.getItemCount()>0) {
            dayFillinMode = true;
            day_CB.removeAllItems();
            List<String> days = getDaysList(customerOrderBookDates, month_CB.getSelectedItem().toString());
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
        DateFormat df_dayName = new SimpleDateFormat("EEEEEEE", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        if (!monthesFillingMode && !dayFillinMode&&day_CB.getItemCount()>0) {

            String month = month_CB.getSelectedItem().toString();
            String day = day_CB.getSelectedItem().toString();
            String selectdDate = day + "-" + month;
            //get selected date
            int i = 0;
            for (i = 0; i < customerOrderBookDates.size(); i++) {
                String s = NormalizeinArabic(df.format(customerOrderBookDates.get(i)));
                if (selectdDate.equals(s)) {

                    break;
                }

            }
            if (i < customerOrderBookDates.size()) {
                Date date = customerOrderBookDates.get(i);

                // String dayname = getWeekDay(date);
                day_label.setText(df_dayName.format(date));
                fillCustomersDailyTransactionTable(new java.sql.Date(date.getTime()));
            }
        }
    }//GEN-LAST:event_day_CBActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable customersDailyTransaction_Table;
    private javax.swing.JPanel dataBook_panel;
    private javax.swing.JComboBox day_CB;
    private javax.swing.JLabel day_label;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox month_CB;
    // End of variables declaration//GEN-END:variables

    String[] custDailyTrans_columnTitle = new String[]{
        "خالص", "مكان التخزين", "وهبه", "عدد السبايط", "الوزن القائم/العدد", "ناولون", "المنتج", "اسم العميل"
    };

}
