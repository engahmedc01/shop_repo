/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shop.GUI;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.PlainDocument;
import shop.GUI_attachement_pkg.PersonName_STF;
import shop.Validation_pkg.DoubleInputValidator;
import shop.Validation_pkg.IntegerInputValidator;
import shop.data_source_pkg.DataSourc;
import shop.data_structures_pkg.Pair;
import shop.data_structures_pkg.Person;

/**
 *
 * @author Comu City
 */
public class EditeSellerOrder extends javax.swing.JPanel {

    /**
     * Creates new form EditeSellerOrder
     *
     */
    int fridageNumber;

    Main_Frame myFrame;
    DataSourc data_source;
    Map<String, String> orderDataMap;
    Map<String, Double> orderWithDrawalQuantity;
    List< Pair<java.util.Date, Integer>> customersOrdersDates;

    public EditeSellerOrder(Main_Frame myFrame, DataSourc data_source) {
        this.myFrame = myFrame;
        this.data_source = data_source;
        initComponents();

        weight_TF.getDocument().addDocumentListener(new WeightValueTracker());
        UnitePrice_TF.getDocument().addDocumentListener(new CostValueTracker());
//         packageNumber_TF.getDocument().addDocumentListener(new PackageNumberTracker());
    }

    public class WeightValueTracker implements DocumentListener {

        public WeightValueTracker() {

        }

        void track() {
            if (!weight_TF.getText().isEmpty()) {
                double weight = Double.parseDouble(weight_TF.getText());

                if (weight != 0) {
                    int index = product_cB.getSelectedIndex();
                    double commission = 0.0;
                    switch (index) {
                        case 0:
                            commission = .935;
                            netWeight_TF.setText(String.valueOf(Math.rint(weight * commission)));
                            break;
                        case 1:
                            commission = 0;
                            netWeight_TF.setText(String.valueOf(Math.rint(weight * 1)));
                            break;
                        case 2:
                            netWeight_TF.setText(String.valueOf(Math.rint(weight * 1)));
                            break;

                    }

                }
            }

        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            track();

        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            track();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            track();
        }

    }

    public class CostValueTracker implements DocumentListener {

        public CostValueTracker() {

        }

        void track() {
            int index = product_cB.getSelectedIndex();
            switch (index) {

                case 0:
                    if (!netWeight_TF.getText().isEmpty() && !UnitePrice_TF.getText().isEmpty()) {
                        double netWeight = Double.parseDouble(netWeight_TF.getText());
                        double unitePrice = Double.parseDouble(UnitePrice_TF.getText());

                        if (netWeight != 0 && unitePrice != 0) {

                            double x = netWeight * unitePrice;
                            x = Math.rint(x);
                            totalCost_TF.setText(String.valueOf(x));

                        }
                    }

                    break;
                case 1:
                    break;
                case 2:

                case 3:
                    if (!netWeight_TF.getText().isEmpty() && !UnitePrice_TF.getText().isEmpty()) {
                        double netWeight = Double.parseDouble(netWeight_TF.getText());
                        double unitePrice = Double.parseDouble(UnitePrice_TF.getText());

                        if (netWeight != 0 && unitePrice != 0) {

                            double x = netWeight * unitePrice;
                            totalCost_TF.setText(String.valueOf(x));

                        }
                    }

                    break;

            }

        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            track();

        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            track();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            track();
        }

    }

    public class PackageNumberTracker implements DocumentListener {

        public PackageNumberTracker() {

        }

        void track() {

        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            track();

        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            track();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            track();
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

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        permenant_TF = new PersonName_STF(myFrame, "seller", data_source) ;
        unit_label2 = new javax.swing.JLabel();
        date_DP = new org.jdesktop.swingx.JXDatePicker();
        jButton1 = new javax.swing.JButton();
        wraning_label = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        sellers_table = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        delete_bnt = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        orderDetaile_table = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        net_label = new javax.swing.JLabel();
        netWeight_TF = new javax.swing.JTextField();
        totalCost_TF = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        customeName_label = new javax.swing.JLabel();
        customerOrderTag_CB = new javax.swing.JComboBox();
        customerOrderDate_DP = new org.jdesktop.swingx.JXDatePicker();
        jLabel12 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        weight_TF = new javax.swing.JTextField();
        storeID_CB = new javax.swing.JComboBox();
        product_cB = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        storeID_label = new javax.swing.JLabel();
        weight_label = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        UnitePrice_TF = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        ok_btn = new javax.swing.JButton();
        warning_label = new javax.swing.JLabel();

        setLayout(new javax.swing.OverlayLayout(this));

        jSplitPane1.setDividerLocation(900);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(18, 159, 3), 2, true), "إدخال البيانات", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 16))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("اسم البياع:");

        permenant_TF.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        permenant_TF.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        unit_label2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        unit_label2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        unit_label2.setText("التاريخ");

        jButton1.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jButton1.setText("بحث");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        wraning_label.setBackground(new java.awt.Color(204, 204, 255));
        wraning_label.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        wraning_label.setForeground(new java.awt.Color(255, 0, 51));
        wraning_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(date_DP, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(permenant_TF, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(unit_label2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(wraning_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(permenant_TF, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(date_DP, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unit_label2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(wraning_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        sellers_table.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        sellers_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "إجمالي", "المحل", "التاريخ", "رقم الفاتورة"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        sellers_table.setRowHeight(30);
        sellers_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sellers_tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(sellers_table);

        delete_bnt.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        delete_bnt.setText("مسح");
        delete_bnt.setEnabled(false);
        delete_bnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_bntActionPerformed(evt);
            }
        });
        jPanel9.add(delete_bnt);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(70, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel1);

        jPanel4.setLayout(new javax.swing.OverlayLayout(jPanel4));

        orderDetaile_table.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        orderDetaile_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "اسم العميل", "المبلغ", "الفئة", "عدد", "الصافي", "القائم", "المنتج"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        orderDetaile_table.setRowHeight(25);
        orderDetaile_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                orderDetaile_tableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(orderDetaile_table);

        jPanel4.add(jScrollPane2);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 51, 51), 1, true), "تعديل البيانات"));

        net_label.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        net_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        net_label.setText("الصافي:");

        netWeight_TF.setEditable(false);
        netWeight_TF.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        netWeight_TF.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        totalCost_TF.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        totalCost_TF.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel11.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("المبلغ:");

        customeName_label.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        customeName_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        customeName_label.setText("اسم العميل:");

        customerOrderTag_CB.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        customerOrderDate_DP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerOrderDate_DPActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("التاريخ:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(netWeight_TF)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(net_label, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(customerOrderDate_DP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalCost_TF, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customerOrderTag_CB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(4, 4, 4)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customeName_label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(netWeight_TF)
                    .addComponent(net_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(totalCost_TF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(customerOrderDate_DP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customeName_label, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(customerOrderTag_CB, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        PlainDocument netWeight_TF_doc = (PlainDocument)netWeight_TF .getDocument();
        netWeight_TF_doc.setDocumentFilter(new DoubleInputValidator(this.netWeight_TF, this.warning_label));
        PlainDocument totalCost_TF_doc = (PlainDocument)totalCost_TF.getDocument();
        totalCost_TF_doc.setDocumentFilter(new DoubleInputValidator(this.totalCost_TF , this.warning_label));

        weight_TF.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        weight_TF.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        weight_TF.setText("55");
        weight_TF.setMinimumSize(new java.awt.Dimension(150, 30));
        weight_TF.setPreferredSize(new java.awt.Dimension(6, 27));

        storeID_CB.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        storeID_CB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ثلاجة رقم 1", "ثلاجة رقم 2", "ثلاجة رقم 3", "ثلاجة رقم 4", "ثلاجة رقم 5" }));
        storeID_CB.setMinimumSize(new java.awt.Dimension(150, 28));
        storeID_CB.setPreferredSize(new java.awt.Dimension(150, 28));
        storeID_CB.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                storeID_CBItemStateChanged(evt);
            }
        });
        storeID_CB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                storeID_CBActionPerformed(evt);
            }
        });

        product_cB.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        product_cB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "موز بلدي", "موز مستورد" }));
        product_cB.setMinimumSize(new java.awt.Dimension(150, 28));
        product_cB.setPreferredSize(new java.awt.Dimension(150, 28));
        product_cB.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                product_cBItemStateChanged(evt);
            }
        });
        product_cB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                product_cBActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("المنتج:");

        storeID_label.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        storeID_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        storeID_label.setText("رقم الثلاجة:");

        weight_label.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        weight_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        weight_label.setText("الوزن القائم:");

        jLabel9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("الفئه:");

        UnitePrice_TF.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        UnitePrice_TF.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(weight_TF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(storeID_CB, javax.swing.GroupLayout.Alignment.TRAILING, 0, 289, Short.MAX_VALUE)
                            .addComponent(product_cB, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGap(4, 4, 4)
                                        .addComponent(weight_label)))
                                .addGap(2, 2, 2))
                            .addComponent(storeID_label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(UnitePrice_TF)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(product_cB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(storeID_label, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(storeID_CB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(weight_TF, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(weight_label, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(UnitePrice_TF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PlainDocument weight_TF_doc = (PlainDocument)weight_TF .getDocument();
        weight_TF_doc.setDocumentFilter(new DoubleInputValidator(this.weight_TF , this.warning_label));
        PlainDocument UnitePrice_TF_doc = (PlainDocument)UnitePrice_TF.getDocument();
        UnitePrice_TF_doc.setDocumentFilter(new DoubleInputValidator(this.UnitePrice_TF, this.warning_label));

        ok_btn.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        ok_btn.setText("تم");
        ok_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ok_btnActionPerformed(evt);
            }
        });

        warning_label.setBackground(new java.awt.Color(204, 204, 255));
        warning_label.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        warning_label.setForeground(new java.awt.Color(255, 0, 51));
        warning_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        warning_label.setPreferredSize(new java.awt.Dimension(0, 30));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(ok_btn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(warning_label, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(279, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ok_btn)
            .addComponent(warning_label, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane1.setLeftComponent(jPanel2);

        add(jSplitPane1);
    }// </editor-fold>//GEN-END:initComponents
   void zeroMode() {

        this.weight_TF.setVisible(true);
        this.weight_label.setVisible(true);
        this.net_label.setVisible(true);
        this.netWeight_TF.setVisible(true);
        this.customerOrderTag_CB.setVisible(true);
        this.customeName_label.setVisible(true);

        this.storeID_CB.setVisible(true);
        this.storeID_label.setVisible(true);
        customerOrderTag_CB.removeAllItems();
    }

    void fillOrderTags_CB() {
        customerOrderTag_CB.removeAllItems();
        java.util.Date date = customerOrderDate_DP.getDate();
        if (date == null) {
            return;
        }
        int productID = product_cB.getSelectedIndex() + 1;

        int storeNumber = storeID_CB.getSelectedIndex() + 1;
        int seasonID = data_source.getCurrentSeason();

        Vector<String> customerNames = data_source.getCustomersNamesByOrdersDate(new java.sql.Date(date.getTime()), storeNumber, seasonID, productID, myFrame.firdage_id);
        JOptionPane.showMessageDialog(null, customerNames.size());
        for (int i = 0; i < customerNames.size(); i++) {
            customerOrderTag_CB.addItem(customerNames.get(i));

        }

//        int storeNumber = storeID_CB.getSelectedIndex() + 1;
//        customersOrdersDates = data_source.getAllCustomersOrdersNames(id, storeNumber,myFrame.firdage_id);
//        DateFormat df = new SimpleDateFormat("dd-MMMMMM HH:mm:ss aaaa", new Locale("ar", "AE", "Arabic"));// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
//
//        for (int i = 0; i < customersOrdersDates.size(); i++) {
//            String formattedDate = df.format(customersOrdersDates.get(i).getFrist());
//            String customerName = data_source.getcustomer_name(customersOrdersDates.get(i).getSecond());
//            customerNames_CB.addItem(customerName + ", " + formattedDate);
//
//        }
    }

    void oneMode() {

        this.weight_TF.setVisible(false);
        this.weight_label.setVisible(false);
        this.net_label.setVisible(false);
        this.netWeight_TF.setVisible(false);

        this.customerOrderTag_CB.setVisible(true);
        this.customeName_label.setVisible(true);
        this.storeID_CB.setVisible(true);
        this.storeID_label.setVisible(true);
        fillOrderTags_CB();
    }

    void twoMode() {

        this.weight_TF.setVisible(false);
        this.weight_label.setVisible(false);
        this.net_label.setVisible(false);
        this.netWeight_TF.setVisible(false);
        this.customerOrderTag_CB.setVisible(false);
        this.customeName_label.setVisible(false);

        this.storeID_CB.setVisible(false);
        this.storeID_label.setVisible(false);

    }

    void threeMode() {

        this.weight_TF.setVisible(true);
        this.weight_label.setVisible(true);
        this.net_label.setVisible(true);
        this.netWeight_TF.setVisible(true);
        this.customerOrderTag_CB.setVisible(false);
        this.customeName_label.setVisible(false);
        this.storeID_CB.setVisible(false);
        this.storeID_label.setVisible(false);

    }

    private void product_cBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_product_cBItemStateChanged
        // TODO add your handling code here:

        JComboBox cb = (JComboBox) evt.getSource();
        Object item = evt.getItem();

        if (evt.getStateChange() == evt.SELECTED) {
            int index = product_cB.getSelectedIndex();
            switch (index) {

                case 0:
                    zeroMode();
                    break;
                case 1:
                    oneMode();
                    break;
                case 2:
                    twoMode();
                    break;
                case 3:
                    threeMode();
                    break;

            }

        } else if (evt.getStateChange() == evt.DESELECTED) {
            // Item is no longer selected
        }
    }//GEN-LAST:event_product_cBItemStateChanged

    private void product_cBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_product_cBActionPerformed
        // TODO add your handling code here:
        int index = product_cB.getSelectedIndex();
        switch (index) {

            case 0:
                zeroMode();
                break;
            case 1:
                oneMode();
                break;
            case 2:
                twoMode();
                break;
            case 3:
                threeMode();
                break;
        }
    }//GEN-LAST:event_product_cBActionPerformed

    private void storeID_CBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_storeID_CBItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_storeID_CBItemStateChanged

    private void storeID_CBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_storeID_CBActionPerformed
        // TODO add your handling code here:

        fillOrderTags_CB();


    }//GEN-LAST:event_storeID_CBActionPerformed
    boolean validateForm() {

        String weight = weight_TF.getText();
        String UnitePrice = UnitePrice_TF.getText();

        int index = product_cB.getSelectedIndex();

        switch (index) {
            case 0:

                if (weight.isEmpty()) {

                    this.warning_label.setText("يجب إدخال الوزن");
                    return false;
                }
                if (UnitePrice.isEmpty()) {

                    this.warning_label.setText("يجب إدخال الفئة");
                    return false;

                }

                DateFormat df = new SimpleDateFormat("dd-MM HH:mm:ss", Locale.ENGLISH);// DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.FULL, new Locale("ar","AE","Arabic"));
                int storeNumber = storeID_CB.getSelectedIndex() + 1;
                int orderid = getCustomerOrderID(customerOrderTag_CB.getSelectedItem().toString());
                double avaliable = data_source.getProductavaliableAmount(orderid, 1);
                double d_weight = Double.parseDouble(weight);

                d_weight += getOrderWithDrawalQuantity(customerOrderTag_CB.getSelectedItem().toString());
                if (d_weight > avaliable && avaliable != -10000000) {
                    avaliable -= getOrderWithDrawalQuantity(customerOrderTag_CB.getSelectedItem().toString());
                    this.warning_label.setText("الوزن اكبر من المتاح" + avaliable);

                    return false;
                } else {

                    this.warning_label.setText("");
                    return true;

                }

            case 2:

                if (UnitePrice.isEmpty()) {

                    this.warning_label.setText("يجب إدخال الفئة");
                    return false;

                } else {

                    this.warning_label.setText("");
                    return true;
                }

            case 3:

                if (weight.isEmpty()) {

                    this.warning_label.setText("يجب إدخال الوزن");
                    return false;
                }

                if (UnitePrice.isEmpty()) {

                    this.warning_label.setText("يجب إدخال الفئة");
                    return false;

                } else {

                    this.warning_label.setText("");
                    return true;

                }

        }

        return true;

    }


    private void ok_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ok_btnActionPerformed
        // TODO add your handling code here:
        int index = orderDetaile_table.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) orderDetaile_table.getModel();
        Vector<Object> row = (Vector<Object>) dtm.getDataVector().get(index);

        int weightid = Integer.parseInt(row.get(8).toString());
        int storeNumber = storeID_CB.getSelectedIndex() + 1;
        int newid = getCustomerOrderID(customerOrderTag_CB.getSelectedItem().toString());
        data_source.changeSllerCustomerOrderID(weightid, newid);

    }//GEN-LAST:event_ok_btnActionPerformed
    int getCustomerOrderID(String orderTag) {
        String [] orderTagParts=orderTag.replace(" ", "").split("_");
       int OrderID=Integer.parseInt(orderTagParts[orderTagParts.length-1]);
        return OrderID;

    }
    private void orderDetaile_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_orderDetaile_tableMouseClicked
        // TODO add your handling code here:
        int index = orderDetaile_table.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) orderDetaile_table.getModel();
        Vector<Object> row = (Vector<Object>) dtm.getDataVector().get(index);

        String productName = row.get(6).toString();
        String store = row.get(7).toString();
        String grossWeight = row.get(5).toString();
        String netWeight = row.get(4).toString();
        String unitePrice = row.get(2).toString();
        String totalCost = row.get(1).toString();
        String customerName = row.get(0).toString();

        this.netWeight_TF.setText(netWeight);
        this.UnitePrice_TF.setText(unitePrice);
        this.totalCost_TF.setText(totalCost);
        this.weight_TF.setText(grossWeight);

        for (int i = 0; i < product_cB.getItemCount(); i++) {

            if (product_cB.getItemAt(i).toString().equals(productName)) {
                product_cB.setSelectedIndex(i);
                break;

            }

        }
        for (int i = 0; i < storeID_CB.getItemCount(); i++) {
            String item = storeID_CB.getItemAt(i).toString();
            if (item.equals(store)) {
                storeID_CB.setSelectedIndex(i);
                break;

            }

        }

        for (int i = 0; i < customerOrderTag_CB.getItemCount(); i++) {

            if (customerOrderTag_CB.getItemAt(i).toString().equals(customerName)) {
                customerOrderTag_CB.setSelectedIndex(i);

            }

        }


    }//GEN-LAST:event_orderDetaile_tableMouseClicked

    boolean validatesearchForm() {
        String name = permenant_TF.getText();
        if (permenant_TF.getText().isEmpty()) {
            wraning_label.setText("يجب إدخال اسم البياع");
            return false;

        }
        if (date_DP.getDate() == null) {

            wraning_label.setText("يجب إدخال التاريخ");
            return false;

        }
        if (data_source.sellerSelectAll(name) == null) {

            wraning_label.setText("الأسم غير مسجل");
            return false;

        }

        return true;
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (validatesearchForm()) {
            String name = permenant_TF.getText();
            java.util.Date date = date_DP.getDate();
            Vector<Object> data = data_source.getSellersOrders(name, new java.sql.Date(date.getTime()), myFrame.firdage_id);
            DefaultTableModel dtm = (DefaultTableModel) sellers_table.getModel();
            dtm.setDataVector(data, sellers_columnTitles);
            sellers_table.setModel(dtm);
            clearorderDetailable();
            clearEditeFields();

        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void sellers_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sellers_tableMouseClicked
        // TODO add your handling code here:
        int index = sellers_table.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) sellers_table.getModel();
        Vector<Object> row = (Vector<Object>) dtm.getDataVector().get(index);
        int order_id = Integer.parseInt(row.get(3).toString());

        Vector<Object> data = data_source.getSellerOrderDetail(order_id);
        if (data != null) {

            dtm = (DefaultTableModel) orderDetaile_table.getModel();
            dtm.setDataVector(data, dailSalesBook_columntitles);
            orderDetaile_table.setModel(dtm);
            setOrderDetail_tableSize();
        }


    }//GEN-LAST:event_sellers_tableMouseClicked

    private void delete_bntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_bntActionPerformed
        // TODO add your handling code here:
        int index = sellers_table.getSelectedRow();
        DefaultTableModel dtm = (DefaultTableModel) sellers_table.getModel();
        Vector<Object> row = (Vector<Object>) dtm.getDataVector().get(index);
        int order_id = Integer.parseInt(row.get(3).toString());

        int seasonID = data_source.getCurrentSeason();
        data_source.deleteSllerCustomerOrderID(order_id, seasonID);
    }//GEN-LAST:event_delete_bntActionPerformed

    private void customerOrderDate_DPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerOrderDate_DPActionPerformed
        // TODO add your handling code here:
        fillOrderTags_CB();

    }//GEN-LAST:event_customerOrderDate_DPActionPerformed

    void setOrderDetail_tableSize() {
//detail_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn column = null;
        for (int i = 0; i < orderDetaile_table.getColumnModel().getColumnCount(); i++) {
            column = orderDetaile_table.getColumnModel().getColumn(i);

            switch (i) {
                case 0:
                    column.setPreferredWidth(400);
                    break;
                case 1:
                    column.setPreferredWidth(100);
                    break;
                case 2:
                    column.setPreferredWidth(80);
                    break;
                case 3:
                    column.setPreferredWidth(80);
                    break;
                case 4:
                    column.setPreferredWidth(100);
                    break;
                case 5:
                    column.setPreferredWidth(100);
                    break;
                case 6:
                    column.setPreferredWidth(150);
                    break;
            }
        }

    }

    void clearEditeFields() {

        this.weight_TF.setText("");
        this.netWeight_TF.setText("");
        this.UnitePrice_TF.setText("");
        this.totalCost_TF.setText("");

        this.product_cB.setSelectedIndex(0);

    }

    void clearorderDetailable() {
        DefaultTableModel dtm = (DefaultTableModel) orderDetaile_table.getModel();
        dtm.setDataVector(new Vector<Object>(), sellers_columnTitles);
        orderDetaile_table.setModel(dtm);

    }

    void addOrderWithDrawalQuantity(String name, Double quantity) {

        if (orderWithDrawalQuantity.containsKey(name)) {

            Double tempQuantity = orderWithDrawalQuantity.get(name);
            tempQuantity += quantity;
            orderWithDrawalQuantity.put(name, tempQuantity);

        } else {
            orderWithDrawalQuantity.put(name, quantity);

        }

    }

    public void removeOrderWithDrawalQuantity(String name, Double quantity) {

        if (orderWithDrawalQuantity.containsKey(name)) {

            Double tempQuantity = orderWithDrawalQuantity.get(name);
            tempQuantity -= quantity;
            orderWithDrawalQuantity.put(name, tempQuantity);

        }

    }

    double getOrderWithDrawalQuantity(String name) {

        if (orderWithDrawalQuantity.containsKey(name)) {

            Double tempQuantity = orderWithDrawalQuantity.get(name);

            return tempQuantity;

        } else {
            return 0;
        }

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField UnitePrice_TF;
    private javax.swing.JLabel customeName_label;
    private org.jdesktop.swingx.JXDatePicker customerOrderDate_DP;
    private javax.swing.JComboBox customerOrderTag_CB;
    private org.jdesktop.swingx.JXDatePicker date_DP;
    private javax.swing.JButton delete_bnt;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextField netWeight_TF;
    private javax.swing.JLabel net_label;
    private javax.swing.JButton ok_btn;
    private javax.swing.JTable orderDetaile_table;
    private javax.swing.JTextField permenant_TF;
    private javax.swing.JComboBox product_cB;
    private javax.swing.JTable sellers_table;
    private javax.swing.JComboBox storeID_CB;
    private javax.swing.JLabel storeID_label;
    private javax.swing.JTextField totalCost_TF;
    private javax.swing.JLabel unit_label2;
    private javax.swing.JLabel warning_label;
    private javax.swing.JTextField weight_TF;
    private javax.swing.JLabel weight_label;
    private javax.swing.JLabel wraning_label;
    // End of variables declaration//GEN-END:variables

//        "إجمالي", "المحل", "التاريخ", "رقم الفاتورة"
    Vector<String> sellers_columnTitles = new Vector<>(Arrays.asList("إجمالي", "المحل", "التاريخ", "رقم الفاتورة"));
    Vector<String> dailSalesBook_columntitles = new Vector<>(Arrays.asList("اسم العميل", "المبلغ", "الفئه", "عدد", "الصافي", "القائم", "المنتج", "رقم الثلاجة", "رقم الوزنة"));

}
