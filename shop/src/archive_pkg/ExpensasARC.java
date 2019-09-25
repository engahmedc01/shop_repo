/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archive_pkg;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import shop.GUI.Main_Frame;
import shop.data_source_pkg.DataSourc;
import shop.data_structures_pkg.Pair;

/**
 *
 * @author ahmed
 */
public class ExpensasARC extends javax.swing.JPanel {

    /**
     * Creates new form InputOutput
     */
    DataSourc data_source;
    JFrame myFrame;
    List<Date> inDates;
    List<Date> outDates;
    boolean fillingMode;
    boolean outMonthesFillingMode;
    boolean outDayFillinMode;
    boolean inMonthesFillingMode;
    boolean inDayFillinMode;

    public ExpensasARC(JFrame myFrame, DataSourc data_source, int seasonID) {
        this.data_source = data_source;
        this.myFrame = myFrame;
        initComponents();
        intiateInPage(seasonID);
        intialteOutPage(seasonID);
    }

    void intiateInPage(int seasonID) {

        fillIndates(seasonID);

    }

    void refrishPage(int seasonID, int fridageNumber) {

        fillIndates(seasonID);
        fillOutdates(seasonID);

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
        String currentMonth = NormalizeinArabic(df.format(new Date()));
        if (!monthes.contains(currentMonth)) {
            monthes.add(currentMonth);

        }
        return monthes;

    }

    public void fillIndates(int seasonID) {
        inMonthesFillingMode = true;

        inDates = data_source.getIncomeDates(seasonID);
        inMonth_CB.removeAllItems();
        DateFormat df = new SimpleDateFormat("MMMMMM-yyyy ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        List<String> monthes = getMonthesList(inDates);

        for (int i = 0; i < monthes.size(); i++) {
            String formattedDate = (monthes.get(i));
            inMonth_CB.addItem(formattedDate);
        }

        inMonthesFillingMode = false;
        String currentMonth = NormalizeinArabic(df.format(new Date()));
        inMonth_CB.setSelectedItem(currentMonth);

    }

    public void fillOutdates(int seasonID) {
        outMonthesFillingMode = true;

        outDates = data_source.getOutcomeDates(seasonID);
        outMonth_CB.removeAllItems();
        DateFormat df = new SimpleDateFormat("MMMMMM-yyyy ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        List<String> monthes = getMonthesList(outDates);

        for (int i = 0; i < monthes.size(); i++) {
            String formattedDate = (monthes.get(i));
            outMonth_CB.addItem(formattedDate);
        }
        outMonthesFillingMode = false;
        String currentMonth = NormalizeinArabic(df.format(new Date()));
        outMonth_CB.setSelectedItem(currentMonth);

    }

    void intialteOutPage(int seasonID) {

        fillIndates(seasonID);
        fillOutdates(seasonID);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        outcomeKinds_RB = new javax.swing.ButtonGroup();
        outDates_CB = new javax.swing.JComboBox();
        inDates_CB = new javax.swing.JComboBox();
        jScrollPane5 = new javax.swing.JScrollPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        outcome_panel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        outDay_label = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        outTotalAmount_label = new javax.swing.JLabel();
        outDay_CB = new javax.swing.JComboBox();
        outMonth_CB = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        outcome_table = new javax.swing.JTable();
        income_panel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        income_table = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        inTotalAmount_label = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        inMonth_CB = new javax.swing.JComboBox();
        inDay_CB = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        inDay_label = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        outDates_CB.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        outDates_CB.setForeground(new java.awt.Color(255, 0, 51));
        outDates_CB.setPreferredSize(new java.awt.Dimension(150, 30));
        outDates_CB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outDates_CBActionPerformed(evt);
            }
        });

        inDates_CB.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        inDates_CB.setForeground(new java.awt.Color(255, 0, 51));
        inDates_CB.setPreferredSize(new java.awt.Dimension(150, 30));
        inDates_CB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inDates_CBActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 255), 3));

        jLabel13.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("الموافق:");
        jLabel13.setPreferredSize(new java.awt.Dimension(60, 22));

        outDay_label.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        outDay_label.setForeground(new java.awt.Color(255, 0, 51));
        outDay_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        outDay_label.setText("الجمعه");
        outDay_label.setPreferredSize(new java.awt.Dimension(60, 22));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("خارج يوم:");
        jLabel3.setPreferredSize(new java.awt.Dimension(80, 22));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("إجمالي:");
        jLabel5.setPreferredSize(new java.awt.Dimension(80, 22));

        outTotalAmount_label.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        outTotalAmount_label.setForeground(new java.awt.Color(255, 0, 51));
        outTotalAmount_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        outTotalAmount_label.setText("0.0");
        outTotalAmount_label.setPreferredSize(new java.awt.Dimension(60, 22));

        outDay_CB.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        outDay_CB.setPreferredSize(new java.awt.Dimension(75, 30));
        outDay_CB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outDay_CBActionPerformed(evt);
            }
        });

        outMonth_CB.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        outMonth_CB.setPreferredSize(new java.awt.Dimension(150, 30));
        outMonth_CB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outMonth_CBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(outTotalAmount_label, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(outMonth_CB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(outDay_CB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(outDay_label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(outMonth_CB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(outDay_CB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(outDay_label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(outTotalAmount_label, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        outcome_table.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        outcome_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "البيان", "ثلاجة رقم", "رقم الفاتورة", "الاسم", "نوع الصرف", "المبلغ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        outcome_table.setRowHeight(25);
        jScrollPane2.setViewportView(outcome_table);

        javax.swing.GroupLayout outcome_panelLayout = new javax.swing.GroupLayout(outcome_panel);
        outcome_panel.setLayout(outcome_panelLayout);
        outcome_panelLayout.setHorizontalGroup(
            outcome_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outcome_panelLayout.createSequentialGroup()
                .addGroup(outcome_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        outcome_panelLayout.setVerticalGroup(
            outcome_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outcome_panelLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(outcome_panel);

        income_table.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        income_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "الملاحظات", "الاسم", "نوع الداخل", "المبلغ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        income_table.setRowHeight(25);
        jScrollPane1.setViewportView(income_table);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 255), 3));

        inTotalAmount_label.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        inTotalAmount_label.setForeground(new java.awt.Color(255, 0, 51));
        inTotalAmount_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        inTotalAmount_label.setText("0.0");
        inTotalAmount_label.setPreferredSize(new java.awt.Dimension(60, 22));
        jPanel3.add(inTotalAmount_label);

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("إجمالي:");
        jLabel4.setPreferredSize(new java.awt.Dimension(80, 22));
        jPanel3.add(jLabel4);

        inMonth_CB.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        inMonth_CB.setPreferredSize(new java.awt.Dimension(150, 30));
        inMonth_CB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inMonth_CBActionPerformed(evt);
            }
        });
        jPanel3.add(inMonth_CB);

        inDay_CB.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        inDay_CB.setPreferredSize(new java.awt.Dimension(75, 30));
        inDay_CB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inDay_CBActionPerformed(evt);
            }
        });
        jPanel3.add(inDay_CB);

        jLabel12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("الموافق:");
        jLabel12.setPreferredSize(new java.awt.Dimension(60, 22));
        jPanel3.add(jLabel12);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel15.setPreferredSize(new java.awt.Dimension(8, 30));
        jPanel3.add(jLabel15);

        inDay_label.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        inDay_label.setForeground(new java.awt.Color(255, 0, 51));
        inDay_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        inDay_label.setText("الجمعه");
        inDay_label.setPreferredSize(new java.awt.Dimension(60, 22));
        jPanel3.add(inDay_label);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setPreferredSize(new java.awt.Dimension(5, 22));
        jPanel3.add(jLabel10);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("داخل يوم:");
        jLabel2.setPreferredSize(new java.awt.Dimension(80, 22));
        jPanel3.add(jLabel2);

        javax.swing.GroupLayout income_panelLayout = new javax.swing.GroupLayout(income_panel);
        income_panel.setLayout(income_panelLayout);
        income_panelLayout.setHorizontalGroup(
            income_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(income_panelLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        income_panelLayout.setVerticalGroup(
            income_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(income_panelLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(income_panel);

        jScrollPane5.setViewportView(jSplitPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1194, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5))
        );
    }// </editor-fold>//GEN-END:initComponents

    void fillOutcomeDailyTransaction(java.util.Date date) {

        Double totalAmount = new Double(0);
        double x = data_source.getOutcomeAmount(date);
        Object[][] data = data_source.getOutcomeDailyTranscation(date, totalAmount);
        if (data != null) {
            DefaultTableModel dtm = (DefaultTableModel) outcome_table.getModel();
            dtm.setDataVector(data, outcomeTable_columnTitles);

            outcome_table.setModel(dtm);

            this.outTotalAmount_label.setText(NormalizeinArabic(String.valueOf(x)));

        }

    }

    void fillIncomeDailyTransaction(java.util.Date date) {

        Double totalAmount = null;
        double x = data_source.getIncomeAmount(date);
        Object[][] data = data_source.getIncomeDailyTranscation(date, totalAmount);
        DefaultTableModel dtm = (DefaultTableModel) income_table.getModel();
        dtm.setDataVector(data, incomeTable_columnTitles);
        income_table.setModel(dtm);
        this.inTotalAmount_label.setText(NormalizeinArabic(String.valueOf(x)));

    }

    String getWeekDay(java.util.Date date) {

        DateFormat df = new SimpleDateFormat("EEEEEEE", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        String day = df.format(date);
        return day;

    }
    private void inDates_CBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inDates_CBActionPerformed

        if (!fillingMode) {
            int index = inDates_CB.getSelectedIndex();
            Date date = inDates.get(index);
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            String day = getWeekDay(date);
            inDay_label.setText(day);
            fillIncomeDailyTransaction(sqlDate);
        }
    }//GEN-LAST:event_inDates_CBActionPerformed

    private void outDates_CBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outDates_CBActionPerformed
        // TODO add your handling code here:

        if (!fillingMode) {
            String selected = outDates_CB.getSelectedItem().toString();
            int index = outDates_CB.getSelectedIndex();
            Date date = outDates.get(index);
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            String day = getWeekDay(date);
            outDay_label.setText(day);
            fillOutcomeDailyTransaction(sqlDate);
        }
    }//GEN-LAST:event_outDates_CBActionPerformed

    private void outDay_CBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outDay_CBActionPerformed
        // TODO add your handling code here:

        DateFormat df = new SimpleDateFormat("dd-MMMMMM-yyyy ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        DateFormat df_dayName = new SimpleDateFormat("EEEEEEE", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        if (!outMonthesFillingMode && !outDayFillinMode) {

            String month = outMonth_CB.getSelectedItem().toString();
            String day = outDay_CB.getSelectedItem().toString();
            String selectdDate = day + "-" + month;
            //get selected date
            int i = 0;
            for (i = 0; i < outDates.size(); i++) {
                String s = NormalizeinArabic(df.format(outDates.get(i)));
                if (selectdDate.equals(s)) {

                    break;
                }

            }
            if (i < outDates.size()) {
                Date date = outDates.get(i);

                // String dayname = getWeekDay(date);
                outDay_label.setText(df_dayName.format(date));
                fillOutcomeDailyTransaction(new java.sql.Date(date.getTime()));
            }
        }
    }//GEN-LAST:event_outDay_CBActionPerformed

    private void outMonth_CBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outMonth_CBActionPerformed
        // TODO add your handling code here:
        DateFormat df_month = new SimpleDateFormat("MMMMMM-yyyy ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        DateFormat df_day = new SimpleDateFormat("dd", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        if (!outMonthesFillingMode) {
            outDayFillinMode = true;
            outDay_CB.removeAllItems();
            List<String> days = getDaysList(outDates, outMonth_CB.getSelectedItem().toString());

            for (int i = 0; i < days.size(); i++) {
                outDay_CB.addItem(days.get(i));

            }
            outDayFillinMode = false;
            //if selected month is cusrrent month
            if (NormalizeinArabic(df_month.format(new Date())).equals(outMonth_CB.getSelectedItem().toString())) {

                String currentday = df_day.format(new Date());

                outDay_CB.setSelectedItem((NormalizeinArabic(currentday)));

            } else {
                outDay_CB.setSelectedIndex(0);
            }

        }
    }//GEN-LAST:event_outMonth_CBActionPerformed

    private void inDay_CBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inDay_CBActionPerformed
        // TODO add your handling code here:

        DateFormat df = new SimpleDateFormat("dd-MMMMMM-yyyy ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        DateFormat df_dayName = new SimpleDateFormat("EEEEEEE", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        if (!inMonthesFillingMode && !inDayFillinMode) {

            String month = inMonth_CB.getSelectedItem().toString();
            String day = inDay_CB.getSelectedItem().toString();
            String selectdDate = day + "-" + month;
            //get selected date
            int i = 0;
            for (i = 0; i < inDates.size(); i++) {
                String s = NormalizeinArabic(df.format(inDates.get(i)));
                if (selectdDate.equals(s)) {

                    break;
                }

            }
            if (i < inDates.size()) {
                Date date = inDates.get(i);

                // String dayname = getWeekDay(date);
                inDay_label.setText(df_dayName.format(date));
                fillIncomeDailyTransaction(new java.sql.Date(date.getTime()));
            }
        }
    }//GEN-LAST:event_inDay_CBActionPerformed

    private void inMonth_CBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inMonth_CBActionPerformed
        // TODO add your handling code here:
        DateFormat df_month = new SimpleDateFormat("MMMMMM-yyyy ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        DateFormat df_day = new SimpleDateFormat("dd", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        if (!inMonthesFillingMode) {
            inDayFillinMode = true;
            inDay_CB.removeAllItems();
            List<String> days = getDaysList(inDates, inMonth_CB.getSelectedItem().toString());
            for (int i = 0; i < days.size(); i++) {
                inDay_CB.addItem(days.get(i));

            }
            inDayFillinMode = false;
            //if selected month is cusrrent month
            if (NormalizeinArabic(df_month.format(new Date())).equals(inMonth_CB.getSelectedItem().toString())) {

                String currentday = df_day.format(new Date());

                inDay_CB.setSelectedItem((NormalizeinArabic(currentday)));

            } else {
                inDay_CB.setSelectedIndex(0);
            }

        }
    }//GEN-LAST:event_inMonth_CBActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox inDates_CB;
    private javax.swing.JComboBox inDay_CB;
    private javax.swing.JLabel inDay_label;
    private javax.swing.JComboBox inMonth_CB;
    private javax.swing.JLabel inTotalAmount_label;
    private javax.swing.JPanel income_panel;
    private javax.swing.JTable income_table;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JComboBox outDates_CB;
    private javax.swing.JComboBox outDay_CB;
    private javax.swing.JLabel outDay_label;
    private javax.swing.JComboBox outMonth_CB;
    private javax.swing.JLabel outTotalAmount_label;
    private javax.swing.ButtonGroup outcomeKinds_RB;
    private javax.swing.JPanel outcome_panel;
    private javax.swing.JTable outcome_table;
    // End of variables declaration//GEN-END:variables
String[] incomeTable_columnTitles = new String[]{
        "الملاحظات", "الاسم", "نوع الدخل", "المبلغ"
    };
    String[] outcomeTable_columnTitles = new String[]{
        "البيان", "ثلاجة رقم", "رقم الفاتورة", "الاسم", "نوع الصرف", "المبلغ"
    };
}
