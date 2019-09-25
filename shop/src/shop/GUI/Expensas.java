/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shop.GUI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.PlainDocument;
import shop.GUI.Main_Frame;
import shop.GUI_attachement_pkg.PersonName_STF;
import shop.Validation_pkg.DoubleInputValidator;
import shop.data_source_pkg.DataSourc;
import shop.data_structures_pkg.Pair;

/**
 *
 * @author Ahmed
 */
public class Expensas extends javax.swing.JPanel {

    /**
     * Creates new form ExpensesARC
     */
    DataSourc data_source;
    Main_Frame myFrame;
    List<Date> inDates;
    List<Date> outDates;
    boolean fillingMode;
    boolean outMonthesFillingMode;
    boolean outDayFillinMode;
    boolean inMonthesFillingMode;
    boolean inDayFillinMode;

    public Expensas(Main_Frame myFrame,DataSourc data_source ) {
        this.data_source = data_source;
        this.myFrame = myFrame;
        initComponents();
        intiateInPage();
        intialteOutPage();
    }

    void intiateInPage() {
        this.inAmount_TF.setText("");
        this.inName_TF.setText("");
        this.inNotes_TA.setText("");
        fillIndates();
        refreshBalance();

    }


    List<String> getDaysList(List<java.util.Date> dates, String selectedMonth) {
        DateFormat df = new SimpleDateFormat("MMMMMM-yyyy ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        DateFormat df_day = new SimpleDateFormat("dd", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));

        List<String> days = new ArrayList<>();
        boolean isCurrentMonthe = false;
        for (int i = 0; i < dates.size(); i++) {
            String month = (df.format(dates.get(i)));
            if (month.equals(selectedMonth)) {
                days.add((df_day.format(dates.get(i))));

            }

        }
        String currentMonthe = (df.format(new Date()));
        String currentDay = (df_day.format(new Date()));

        if (!days.contains(currentDay) && selectedMonth.equals(currentMonthe)) {
            days.add(currentDay);

        }
        return days;

    }

    List<String> getMonthesList(List<java.util.Date> dates) {
        DateFormat df = new SimpleDateFormat("MMMMMM-yyyy ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        List<String> monthes = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            String date = (df.format(dates.get(i)));
            if (!monthes.contains(date)) {
                monthes.add((date));

            }

        }
        String currentMonth = (df.format(new Date()));
        if (!monthes.contains(currentMonth)) {
            monthes.add(currentMonth);

        }
        return monthes;

    }

    public void fillIndates() {
        refreshBalance();
        inMonthesFillingMode = true;
        int seasonID = data_source.getCurrentSeason();

        inDates = data_source.getIncomeDates(seasonID);
        inMonth_CB.removeAllItems();
        DateFormat df = new SimpleDateFormat("MMMMMM-yyyy ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        List<String> monthes = getMonthesList(inDates);

        for (int i = 0; i < monthes.size(); i++) {
            String formattedDate = (monthes.get(i));
            inMonth_CB.addItem(formattedDate);
        }

        inMonthesFillingMode = false;
        String currentMonth = (df.format(new Date()));
        inMonth_CB.setSelectedItem(currentMonth);

    }

    public void fillOutdates() {
        refreshBalance();
        outMonthesFillingMode = true;
        int seasonID = data_source.getCurrentSeason();

        outDates = data_source.getOutcomeDates(seasonID);
        outMonth_CB.removeAllItems();
        DateFormat df = new SimpleDateFormat("MMMMMM-yyyy ", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        List<String> monthes = getMonthesList(outDates);

        for (int i = 0; i < monthes.size(); i++) {
            String formattedDate = (monthes.get(i));
            outMonth_CB.addItem(formattedDate);
        }
        outMonthesFillingMode = false;
        String currentMonth = (df.format(new Date()));
        outMonth_CB.setSelectedItem(currentMonth);

    }

    void intialteOutPage() {

        this.outamount_TF.setText("");
        this.outNotes_TA.setText("");
        fillOutdates();
        refreshBalance();

    }

    void refreshBalance() {

        double balance = data_source.getSafeBalance();

        this.Balance_TF.setText((String.valueOf(balance)));
    }

    void fillOutcomeDailyTransaction(java.util.Date date) {

        Double totalAmount = new Double(0);
        double x = data_source.getOutcomeAmount(date);
        Object[][] data = data_source.getOutcomeDailyTranscation(date, totalAmount);
        if (data != null) {
            DefaultTableModel dtm = (DefaultTableModel) outcome_table.getModel();
            dtm.setDataVector(data, outcomeTable_columnTitles);

            outcome_table.setModel(dtm);

            this.outTotalAmount_label.setText((String.valueOf(x)));

        }

    }

    void fillIncomeDailyTransaction(java.util.Date date) {

        Double totalAmount = null;
        double x = data_source.getIncomeAmount(date);
        Object[][] data = data_source.getIncomeDailyTranscation(date, totalAmount);
        DefaultTableModel dtm = (DefaultTableModel) income_table.getModel();
        dtm.setDataVector(data, incomeTable_columnTitles);
        income_table.setModel(dtm);
        this.inTotalAmount_label.setText((String.valueOf(x)));

    }

    boolean validateOutForm() {
        String amount = outamount_TF.getText();

        String name = outName_TF.getText();
        int index = outcomeKinds_CB.getSelectedIndex();

        if (amount.isEmpty()) {
            outWraning_label.setText("يجب إدخال المبلغ");
            return false;

        } else if (!data_source.vallidateSafeWithdrawal(Double.parseDouble(amount))) {
            return false;
        } else if (index == 5) {
            if (name.isEmpty()) {

                this.outWraning_label.setText("يجب ادخال الأسم");
                return false;

            }

            Pair< Double, String> loanerAmount_type = data_source.getLoanerAmount_type(name);
            if (loanerAmount_type == null) {

                this.outWraning_label.setText("هذا العميل غير موجود");
                return false;

            }
            double d_amount = Double.parseDouble(outamount_TF.getText());
            if (loanerAmount_type.getFrist() > 0 && loanerAmount_type.getSecond().equals("OUT_LOAN")) {

                this.outWraning_label.setText("يجب تحصيل مبلغ " + loanerAmount_type.getFrist() + " من هذا العميل اولا");
                return false;

            }
            if (d_amount > loanerAmount_type.getFrist()) {

                this.outWraning_label.setText("القيمة المدفوعة اكبر من القيمة المستحقة " + loanerAmount_type.getFrist());
                return false;
            }
        } else {
            outWraning_label.setText("");
        }
        return true;

    }

    String getWeekDay(java.util.Date date) {

        DateFormat df = new SimpleDateFormat("EEEEEEE", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
        String day = df.format(date);
        return day;

    }

    boolean validateInForm() {
        String name = inName_TF.getText();
        String inAmount = inAmount_TF.getText();

        if (name.isEmpty()) {

            this.inWraning_label.setText("يجب ادخال الأسم");
            return false;
        } else if (inAmount.isEmpty()) {
            this.inWraning_label.setText("يجب ادخال المبلغ");
            return false;
        }

        Pair< Double, String> loanerAmount_type = data_source.getLoanerAmount_type(name);
        if (loanerAmount_type == null) {

            this.inWraning_label.setText("هذا العميل غير موجود");
            return false;

        }
        double d_amount = Double.parseDouble(inAmount_TF.getText());

        if (d_amount > 0 && loanerAmount_type.getSecond().equals("IN_LOAN")) {

            this.inWraning_label.setText("يجب تسديد مبلغ " + loanerAmount_type.getFrist() + " لصالح هذا العميل اولا");
            return false;

        }
        if (Double.parseDouble(inAmount) > loanerAmount_type.getFrist()) {

            this.inWraning_label.setText("القيمة المدفوعة اكبر من القيمة المستحقة " + loanerAmount_type.getFrist());
            return false;
        } else {
            this.inWraning_label.setText("");
            return true;

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
        jPanel2 = new javax.swing.JPanel();
        Balance_TF = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel5 = new javax.swing.JPanel();
        outcomeDatainput_panel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        outcomeKinds_CB = new javax.swing.JComboBox();
        paid_label1 = new javax.swing.JLabel();
        outamount_TF = new javax.swing.JTextField();
        report_panel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        outNotes_TA = new javax.swing.JTextArea();
        outSave_btn = new javax.swing.JButton();
        outWraning_label = new javax.swing.JLabel();
        paid_label4 = new javax.swing.JLabel();
        outName_TF = new PersonName_STF(myFrame, "IN_LOAN", data_source);
        ;
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
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        inAmount_TF = new javax.swing.JTextField();
        paid_label2 = new javax.swing.JLabel();
        report_panel3 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        inNotes_TA = new javax.swing.JTextArea();
        paid_label3 = new javax.swing.JLabel();
        inName_TF = new PersonName_STF(myFrame, "OUT_LOAN", data_source)
        ;
        insave_btn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        inWraning_label = new javax.swing.JLabel();
        income_panel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        income_table = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        inTotalAmount_label = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        inMonth_CB = new javax.swing.JComboBox();
        inDay_CB = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        inDay_label = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setLayout(new javax.swing.OverlayLayout(this));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(163, 137, 137), 3));
        jPanel2.setForeground(new java.awt.Color(255, 0, 51));

        Balance_TF.setEditable(false);
        Balance_TF.setFont(new java.awt.Font("Arial", 0, 22)); // NOI18N
        Balance_TF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Balance_TF.setPreferredSize(new java.awt.Dimension(250, 30));
        jPanel2.add(Balance_TF);

        jLabel11.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 51, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("رصيد  اليوم:");
        jPanel2.add(jLabel11);

        outcomeDatainput_panel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 0), 3, true), "بيانات الخارج", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("نوع الخارج:");

        outcomeKinds_CB.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        outcomeKinds_CB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "العمال", "صيانة تلاجات", "نثريات", "زكاة وصدقات", "سماحات", "تسديد مديونية", "وهبة الفواتير" }));
        outcomeKinds_CB.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                outcomeKinds_CBItemStateChanged(evt);
            }
        });
        outcomeKinds_CB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outcomeKinds_CBActionPerformed(evt);
            }
        });

        paid_label1.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        paid_label1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        paid_label1.setText("المبلغ:");

        outamount_TF.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        outamount_TF.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        report_panel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ملاحظات", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        report_panel2.setLayout(new javax.swing.OverlayLayout(report_panel2));

        jScrollPane4.setWheelScrollingEnabled(false);

        outNotes_TA.setColumns(20);
        outNotes_TA.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        outNotes_TA.setLineWrap(true);
        outNotes_TA.setRows(5);
        outNotes_TA.setToolTipText("ملاحظات");
        outNotes_TA.setWrapStyleWord(true);
        outNotes_TA.setBorder(null);
        jScrollPane4.setViewportView(outNotes_TA);

        report_panel2.add(jScrollPane4);

        outSave_btn.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        outSave_btn.setText("حفظ");
        outSave_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outSave_btnActionPerformed(evt);
            }
        });

        outWraning_label.setBackground(new java.awt.Color(204, 204, 255));
        outWraning_label.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        outWraning_label.setForeground(new java.awt.Color(255, 0, 51));
        outWraning_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        paid_label4.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        paid_label4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        paid_label4.setText("الأسم:");

        outName_TF.setEditable(false);
        outName_TF.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        outName_TF.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout outcomeDatainput_panelLayout = new javax.swing.GroupLayout(outcomeDatainput_panel);
        outcomeDatainput_panel.setLayout(outcomeDatainput_panelLayout);
        outcomeDatainput_panelLayout.setHorizontalGroup(
            outcomeDatainput_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outcomeDatainput_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(outcomeDatainput_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(report_panel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, outcomeDatainput_panelLayout.createSequentialGroup()
                        .addComponent(outWraning_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(outSave_btn))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, outcomeDatainput_panelLayout.createSequentialGroup()
                        .addGroup(outcomeDatainput_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(outName_TF, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(outamount_TF)
                            .addComponent(outcomeKinds_CB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(outcomeDatainput_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, outcomeDatainput_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(paid_label1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                            .addComponent(paid_label4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        outcomeDatainput_panelLayout.setVerticalGroup(
            outcomeDatainput_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, outcomeDatainput_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(outcomeDatainput_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(outcomeKinds_CB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(outcomeDatainput_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paid_label1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(outamount_TF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(outcomeDatainput_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(outName_TF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paid_label4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(report_panel2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(outcomeDatainput_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(outWraning_label, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(outSave_btn))
                .addContainerGap())
        );

        PlainDocument outamount_TF_doc = (PlainDocument)outamount_TF.getDocument();
        outamount_TF_doc.setDocumentFilter(new DoubleInputValidator(this.outamount_TF , this.outWraning_label));

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
            .addComponent(jScrollPane2)
            .addGroup(outcome_panelLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        outcome_panelLayout.setVerticalGroup(
            outcome_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outcome_panelLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(outcome_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(outcomeDatainput_panel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(outcomeDatainput_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(outcome_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane1.setLeftComponent(jPanel5);

        jPanel3.setPreferredSize(new java.awt.Dimension(350, 643));

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(16, 145, 2), 3, true), "بيانات الداخل", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N

        inAmount_TF.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        inAmount_TF.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        paid_label2.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        paid_label2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        paid_label2.setText("المبلغ:");

        report_panel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ملاحظات", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        report_panel3.setLayout(new javax.swing.OverlayLayout(report_panel3));

        jScrollPane6.setWheelScrollingEnabled(false);

        inNotes_TA.setColumns(20);
        inNotes_TA.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        inNotes_TA.setLineWrap(true);
        inNotes_TA.setRows(5);
        inNotes_TA.setToolTipText("ملاحظات");
        inNotes_TA.setWrapStyleWord(true);
        inNotes_TA.setBorder(null);
        jScrollPane6.setViewportView(inNotes_TA);

        report_panel3.add(jScrollPane6);

        paid_label3.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        paid_label3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        paid_label3.setText("الأسم:");

        inName_TF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inName_TFActionPerformed(evt);
            }
        });

        insave_btn.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        insave_btn.setText("حفظ");
        insave_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insave_btnActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("نوع الداخل : تحصيل فلوس السلف");

        inWraning_label.setBackground(new java.awt.Color(204, 204, 255));
        inWraning_label.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        inWraning_label.setForeground(new java.awt.Color(255, 0, 51));
        inWraning_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(report_panel3, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jLabel1))
                                    .addComponent(inName_TF)
                                    .addComponent(inAmount_TF))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(paid_label2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(paid_label3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(inWraning_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(insave_btn)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paid_label2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inAmount_TF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paid_label3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inName_TF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(report_panel3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(insave_btn)
                        .addGap(0, 6, Short.MAX_VALUE))
                    .addComponent(inWraning_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        PlainDocument inamount_TF_doc = (PlainDocument)inAmount_TF.getDocument();
        inamount_TF_doc.setDocumentFilter(new DoubleInputValidator(this.inAmount_TF, this.inWraning_label));

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

        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 255), 3));

        inTotalAmount_label.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        inTotalAmount_label.setForeground(new java.awt.Color(255, 0, 51));
        inTotalAmount_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        inTotalAmount_label.setText("0.0");
        inTotalAmount_label.setPreferredSize(new java.awt.Dimension(100, 22));
        jPanel7.add(inTotalAmount_label);

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("إجمالي:");
        jLabel4.setPreferredSize(new java.awt.Dimension(80, 22));
        jPanel7.add(jLabel4);

        inMonth_CB.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        inMonth_CB.setPreferredSize(new java.awt.Dimension(150, 30));
        inMonth_CB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inMonth_CBActionPerformed(evt);
            }
        });
        jPanel7.add(inMonth_CB);

        inDay_CB.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        inDay_CB.setPreferredSize(new java.awt.Dimension(75, 30));
        inDay_CB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inDay_CBActionPerformed(evt);
            }
        });
        jPanel7.add(inDay_CB);

        jLabel12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("الموافق:");
        jLabel12.setPreferredSize(new java.awt.Dimension(60, 22));
        jPanel7.add(jLabel12);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel15.setPreferredSize(new java.awt.Dimension(8, 30));
        jPanel7.add(jLabel15);

        inDay_label.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        inDay_label.setForeground(new java.awt.Color(255, 0, 51));
        inDay_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        inDay_label.setText("الجمعه");
        inDay_label.setPreferredSize(new java.awt.Dimension(60, 22));
        jPanel7.add(inDay_label);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setPreferredSize(new java.awt.Dimension(5, 22));
        jPanel7.add(jLabel10);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("داخل يوم:");
        jLabel2.setPreferredSize(new java.awt.Dimension(80, 22));
        jPanel7.add(jLabel2);

        javax.swing.GroupLayout income_panelLayout = new javax.swing.GroupLayout(income_panel);
        income_panel.setLayout(income_panelLayout);
        income_panelLayout.setHorizontalGroup(
            income_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
        );
        income_panelLayout.setVerticalGroup(
            income_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(income_panelLayout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(income_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(income_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane1.setRightComponent(jPanel3);

        jScrollPane5.setViewportView(jSplitPane1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1067, Short.MAX_VALUE))
            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE))
        );

        add(jPanel1);
    }// </editor-fold>//GEN-END:initComponents

    private void outcomeKinds_CBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_outcomeKinds_CBItemStateChanged
        // TODO add your handling code here:

        JComboBox cb = (JComboBox) evt.getSource();
        Object item = evt.getItem();

        if (evt.getStateChange() == evt.SELECTED) {
            int index = outcomeKinds_CB.getSelectedIndex();
            if (index == 5) {
                this.outName_TF.setEditable(true);
            } else {
                this.outName_TF.setEditable(false);
            }

        } else if (evt.getStateChange() == evt.DESELECTED) {
            // Item is no longer selected
        }
    }//GEN-LAST:event_outcomeKinds_CBItemStateChanged

    private void outcomeKinds_CBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outcomeKinds_CBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_outcomeKinds_CBActionPerformed

    private void outSave_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outSave_btnActionPerformed
        if (validateOutForm()) {   // TODO add your handling code here:
            int selectedIndex = outcomeKinds_CB.getSelectedIndex();
            String outcomeKind = "";
            switch (selectedIndex) {

                case 0:
                    outcomeKind = "labours";
                    break;
                case 1:
                    outcomeKind = "maintaince";
                    break;
                case 2:   //labours maintaince varaity allah forgivness TIPS
                    outcomeKind = "varaity";
                    break;
                case 3:
                    outcomeKind = "allah";
                    break;
                case 4:
                    outcomeKind = "forgivness";
                    break;
                case 5:
                    outcomeKind = "OUT_PAY_LOAN";
                    break;
                case 6:
                    outcomeKind = "ORDER_TIPS";
                    break;

            }
            double amount = Double.parseDouble(outamount_TF.getText());

            String notes = outNotes_TA.getText();
            if (selectedIndex == 5) {
                data_source.loanPayTransaction(amount, outName_TF.getText(), notes, outcomeKind, myFrame.firdage_id);
            } else {
                //                int firdage_id = fridage_CB.getSelectedIndex() + 1;
                data_source.outcomeTransaction(amount, notes, outcomeKind, -1, -1, myFrame.firdage_id);

            }
            intialteOutPage();
        }
    }//GEN-LAST:event_outSave_btnActionPerformed

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
                String s = (df.format(outDates.get(i)));
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
            if ((df_month.format(new Date())).equals(outMonth_CB.getSelectedItem().toString())) {

                String currentday = df_day.format(new Date());

                outDay_CB.setSelectedItem(((currentday)));

            } else {
                outDay_CB.setSelectedIndex(0);
            }

        }
    }//GEN-LAST:event_outMonth_CBActionPerformed

    private void inName_TFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inName_TFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inName_TFActionPerformed

    private void insave_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insave_btnActionPerformed
        // TODO add your handling code here:
        if (validateInForm()) {

            String type = "IN_PAY_LOAN";
            String amount = inAmount_TF.getText();
            String notes = inNotes_TA.getText();
            String name = inName_TF.getText();
            data_source.loanPayTransaction(Double.parseDouble(amount), name, notes, type, myFrame.firdage_id);
                
            intiateInPage();
        }
    }//GEN-LAST:event_insave_btnActionPerformed

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
            if ((df_month.format(new Date())).equals(inMonth_CB.getSelectedItem().toString())) {

                String currentday = df_day.format(new Date());

                inDay_CB.setSelectedItem(((currentday)));

            } else {
                inDay_CB.setSelectedIndex(0);
            }

        }
    }//GEN-LAST:event_inMonth_CBActionPerformed

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
                String s = (df.format(inDates.get(i)));
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Balance_TF;
    private javax.swing.JTextField inAmount_TF;
    private javax.swing.JComboBox inDay_CB;
    private javax.swing.JLabel inDay_label;
    private javax.swing.JComboBox inMonth_CB;
    private javax.swing.JTextField inName_TF;
    private javax.swing.JTextArea inNotes_TA;
    private javax.swing.JLabel inTotalAmount_label;
    private javax.swing.JLabel inWraning_label;
    private javax.swing.JPanel income_panel;
    private javax.swing.JTable income_table;
    private javax.swing.JButton insave_btn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JComboBox outDay_CB;
    private javax.swing.JLabel outDay_label;
    private javax.swing.JComboBox outMonth_CB;
    private javax.swing.JTextField outName_TF;
    private javax.swing.JTextArea outNotes_TA;
    private javax.swing.JButton outSave_btn;
    private javax.swing.JLabel outTotalAmount_label;
    private javax.swing.JLabel outWraning_label;
    private javax.swing.JTextField outamount_TF;
    private javax.swing.JPanel outcomeDatainput_panel;
    private javax.swing.JComboBox outcomeKinds_CB;
    private javax.swing.JPanel outcome_panel;
    private javax.swing.JTable outcome_table;
    private javax.swing.JLabel paid_label1;
    private javax.swing.JLabel paid_label2;
    private javax.swing.JLabel paid_label3;
    private javax.swing.JLabel paid_label4;
    private javax.swing.JPanel report_panel2;
    private javax.swing.JPanel report_panel3;
    // End of variables declaration//GEN-END:variables

    String[] incomeTable_columnTitles = new String[]{
        "الملاحظات", "الاسم", "نوع الدخل", "المبلغ"
    };
    String[] outcomeTable_columnTitles = new String[]{
        "البيان", "ثلاجة رقم", "رقم الفاتورة", "الاسم", "نوع الصرف", "المبلغ"
    };

}
