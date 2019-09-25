/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archive_pkg;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.eclipse.jdt.core.compiler.IProblem;
import shop.GUI.Main_Frame;
import shop.data_source_pkg.DataSourc;
/**
 *
 * @author Ahmed
 */
public class CustomersTransactionsARC extends javax.swing.JPanel {

    /**
     * Creates new form CustomersTransactionsARC
     */
    DataSourc data_source;
    JFrame myFrame;
    int seasonID;
    int fridageNumber;
    public CustomersTransactionsARC(JFrame myFrame, DataSourc data_source,int seasonID,int fridageNumber) {
        this.data_source = data_source;
        this.myFrame = myFrame;
        this.seasonID=seasonID;
        this.fridageNumber=fridageNumber;
        initComponents();
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
        return  builder.toString();
    }
      void setDetail_table1Size() {
        //customerOrders_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn column = null;
        for (int i = 0; i < detail_table.getColumnModel().getColumnCount(); i++) {
            column = detail_table.getColumnModel().getColumn(i);

            switch (i) {
                case 0:
                    column.setPreferredWidth(80);
                    break;
                case 1:
                    column.setPreferredWidth(60);
                    break;
                case 2:
                    column.setPreferredWidth(60);
                    break;
                case 3:
                    column.setPreferredWidth(80);
                    break;
                case 4:
                    column.setPreferredWidth(60);
                    break;
                case 5:
                    column.setPreferredWidth(80);
                    break;
                case 6:
                    column.setPreferredWidth(80);
                    break;
                case 7:
                    column.setPreferredWidth(100);
                    break;
                case 8:
                    column.setPreferredWidth(220);
                    break;
                     case 9:
                    column.setPreferredWidth(50);
                    break;

            }
        }

    }
    void search(String name){
     DefaultTableModel dtm=null;
      Vector<String>tempheader=detailTable_columnTitles;
    if(name.startsWith("مشتروات_")){
  tempheader=PdetailTable_columnTitles;
    }
     dtm=(DefaultTableModel)detail_table.getModel();
        Vector<Object>data=data_source.getCustomerOrders(name,this.fridageNumber,this.seasonID);
        dtm.setDataVector(data, tempheader);
        detail_table.setModel(dtm);
        setDetail_table1Size();
        
        
        
        
     dtm=(DefaultTableModel)customerSummary_table.getModel();
     
        double tips=data_source.getSumTips(name,this.seasonID);
        double commssion=data_source.getSumCommision(name,this.seasonID);
        int vechil1Count=data_source.getVechile1Count(name,this.seasonID);
        int vechil2Count=data_source.getVechile2Count(name,this.seasonID);
        
        Vector<Object>row=new Vector<Object>(Arrays.asList(tips,commssion,vechil2Count,vechil1Count));
        dtm.setDataVector(new Vector<Object>(Arrays.asList(row)), customerSummary_columnTitles);
        customerSummary_table.setModel(dtm);
    
    setCustomersDailyTransaction_TableSize();
    
    }
    
     void setCustomersDailyTransaction_TableSize() {
//detail_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn column = null;
        for (int i = 0; i < detail_table.getColumnModel().getColumnCount(); i++) {
            column = detail_table.getColumnModel().getColumn(i);
            
            switch (i) {
            case 2:
                     column.setPreferredWidth(40);
                    break;
                case 8:
                     column.setPreferredWidth(130);
                    break;
                    case 9:
                     column.setPreferredWidth(60);
                    break;

            }
        }

    }
    void refrishPage(int seasonID,int fridageNumber){
    
    
    this.fridageNumber=fridageNumber;
    this.seasonID=seasonID;
    
    
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jSplitPane1 = new javax.swing.JSplitPane();
        results_panel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        summary_table = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        customerSummary_table = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        detail_table = new javax.swing.JTable();
        search_panel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        customers_table = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        KCustomers_RB = new javax.swing.JRadioButton();
        PCustomers_RB = new javax.swing.JRadioButton();
        Ocustomers_RB = new javax.swing.JRadioButton();

        setLayout(new javax.swing.OverlayLayout(this));

        jSplitPane1.setDividerLocation(900);

        results_panel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 153, 255), 3, true));

        jPanel5.setLayout(new javax.swing.OverlayLayout(jPanel5));

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 255), 2, true), "إجمالي العملاء", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 18))); // NOI18N
        jScrollPane3.setPreferredSize(new java.awt.Dimension(452, 100));

        summary_table.setFont(new java.awt.Font("B Fantezy", 0, 22)); // NOI18N
        summary_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "دخاخين", "العمولة", "الجامبو", "الدبابة"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        summary_table.setRowHeight(35);
        jScrollPane3.setViewportView(summary_table);

        jPanel5.add(jScrollPane3);

        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 51), 1, true));

        jScrollPane4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 255), 2, true), "إجمالي", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 18))); // NOI18N
        jScrollPane4.setPreferredSize(new java.awt.Dimension(452, 100));

        customerSummary_table.setFont(new java.awt.Font("B Fantezy", 0, 22)); // NOI18N
        customerSummary_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "دخاخين", "العمولة", "الجامبو", "الدبابة"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        customerSummary_table.setRowHeight(35);
        jScrollPane4.setViewportView(customerSummary_table);

        detail_table.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        detail_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "صافي المبلغ", "العمولة", "الوهبة", "إجمالي المبلغ", "الناولون", "الصافي", "القائم", "المنتج", "تاريخ الفاتورة", "رقم الفاتورة"
            }
        ));
        detail_table.setRowHeight(25);
        jScrollPane6.setViewportView(detail_table);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout results_panelLayout = new javax.swing.GroupLayout(results_panel);
        results_panel.setLayout(results_panelLayout);
        results_panelLayout.setHorizontalGroup(
            results_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, results_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(results_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        results_panelLayout.setVerticalGroup(
            results_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(results_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane1.setLeftComponent(results_panel);

        search_panel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(42, 139, 9), 3, true));

        customers_table.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        customers_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "اسم العميل"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        customers_table.setRowHeight(25);
        customers_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customers_tableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(customers_table);

        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 255), 1, true));

        buttonGroup1.add(KCustomers_RB);
        KCustomers_RB.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        KCustomers_RB.setText("كريم");
        KCustomers_RB.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        KCustomers_RB.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        KCustomers_RB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KCustomers_RBActionPerformed(evt);
            }
        });
        jPanel4.add(KCustomers_RB);

        buttonGroup1.add(PCustomers_RB);
        PCustomers_RB.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        PCustomers_RB.setText("مشتروات");
        PCustomers_RB.setToolTipText("");
        PCustomers_RB.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        PCustomers_RB.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        PCustomers_RB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PCustomers_RBActionPerformed(evt);
            }
        });
        jPanel4.add(PCustomers_RB);

        buttonGroup1.add(Ocustomers_RB);
        Ocustomers_RB.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        Ocustomers_RB.setText("عميل");
        Ocustomers_RB.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Ocustomers_RB.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        Ocustomers_RB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ocustomers_RBActionPerformed(evt);
            }
        });
        jPanel4.add(Ocustomers_RB);

        javax.swing.GroupLayout search_panelLayout = new javax.swing.GroupLayout(search_panel);
        search_panel.setLayout(search_panelLayout);
        search_panelLayout.setHorizontalGroup(
            search_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, search_panelLayout.createSequentialGroup()
                .addGroup(search_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
                .addContainerGap())
        );
        search_panelLayout.setVerticalGroup(
            search_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(search_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(search_panel);

        add(jSplitPane1);
    }// </editor-fold>//GEN-END:initComponents

    private void customers_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customers_tableMouseClicked
        // TODO add your handling code here:

        String name ="";
        Vector<Object> row=(Vector<Object>)((DefaultTableModel)customers_table.getModel()).getDataVector().get(customers_table.getSelectedRow());
        name=row.get(0).toString();
        search(name);

    }//GEN-LAST:event_customers_tableMouseClicked

    private void KCustomers_RBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KCustomers_RBActionPerformed
        // TODO add your handling code here:
        if(KCustomers_RB.isSelected()){
            DefaultTableModel dtm=(DefaultTableModel)customers_table.getModel();
            Vector<Object>data=data_source.getAllKareemNames(this.fridageNumber,this.seasonID);
            dtm.setDataVector(data, customerTable_ColumnTitle);
            customers_table.setModel(dtm);

            //        int firdage_id=fridage_CB.getSelectedIndex()+1;
            dtm=(DefaultTableModel)summary_table.getModel();
            double tips=data_source.getKareemTipsAmount(this.seasonID);
            double commssion=data_source.getKareemOrdersCommssionAmount(this.seasonID);
            int vechil1Count=data_source.getkareemVechile1Count(this.seasonID);
            int vechil2Count=data_source.getkareemVechile2Count(this.seasonID);
            Vector<Object>row=new Vector<Object>(Arrays.asList(tips,commssion,vechil2Count,vechil1Count));
            dtm.setDataVector(new Vector<Object>(Arrays.asList(row)), customerSummary_columnTitles);
            summary_table.setModel(dtm);

        }

    }//GEN-LAST:event_KCustomers_RBActionPerformed

    private void PCustomers_RBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PCustomers_RBActionPerformed
        // TODO add your handling code here:

        if(PCustomers_RB.isSelected()){
            DefaultTableModel dtm=(DefaultTableModel)customers_table.getModel();
            Vector<Object>data=data_source.getAllPurchaseCustomers(this.fridageNumber,this.seasonID);
            dtm.setDataVector(data, customerTable_ColumnTitle);
            customers_table.setModel(dtm);

            //        int firdage_id=fridage_CB.getSelectedIndex()+1;
            dtm=(DefaultTableModel)summary_table.getModel();
            double tips=data_source.getPurchasedTipsAmount(this.seasonID);
            double commssion=data_source.getPurchasesOrdersCommssionAmount(this.seasonID);
            int vechil1Count=data_source.getPurchasedVechile1Count(this.seasonID);
            int vechil2Count=data_source.getPurchasedVechile2Count(this.seasonID);
            Vector<Object>row=new Vector<Object>(Arrays.asList(tips,commssion,vechil2Count,vechil1Count));
            dtm.setDataVector(new Vector<Object>(Arrays.asList(row)), customerSummary_columnTitles);
            summary_table.setModel(dtm);

        }

    }//GEN-LAST:event_PCustomers_RBActionPerformed

    private void Ocustomers_RBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ocustomers_RBActionPerformed
        // TODO add your handling code here:
        if(Ocustomers_RB.isSelected()){
            DefaultTableModel dtm=(DefaultTableModel)customers_table.getModel();
            Vector<Object>data=data_source.getAllOCustomersNames(this.fridageNumber,this.seasonID);
            dtm.setDataVector(data, customerTable_ColumnTitle);
            customers_table.setModel(dtm);

            //        int firdage_id=fridage_CB.getSelectedIndex()+1;
            dtm=(DefaultTableModel)summary_table.getModel();
            double tips=data_source.getCustomersTipsAmount(this.seasonID);
            double commssion=data_source.getCustomersOrdersCommssionAmount(this.seasonID);
            int vechil1Count=data_source.getCustomerVechile1Count(this.seasonID);
            int vechil2Count=data_source.getCustomerVechile2Count(this.seasonID);
            Vector<Object>row=new Vector<Object>(Arrays.asList(tips,commssion,vechil2Count,vechil1Count));
            dtm.setDataVector(new Vector<Object>(Arrays.asList(row)), customerSummary_columnTitles);
            summary_table.setModel(dtm);

        }

    }//GEN-LAST:event_Ocustomers_RBActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton KCustomers_RB;
    private javax.swing.JRadioButton Ocustomers_RB;
    private javax.swing.JRadioButton PCustomers_RB;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTable customerSummary_table;
    private javax.swing.JTable customers_table;
    private javax.swing.JTable detail_table;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel results_panel;
    private javax.swing.JPanel search_panel;
    private javax.swing.JTable summary_table;
    // End of variables declaration//GEN-END:variables

Vector<String>detailTable_columnTitles=new Vector<String>
        (Arrays.asList(       
      "صافي المبلغ", "العمولة", "الوهبة", "إجمالي المبلغ", "الناولون", "الصافي", "القائم", "البيان", " تاريخ الفاتورة", "رقم الفاتورة"
));
Vector<String> customerTable_ColumnTitle=new Vector<String>(Arrays.asList( "اسم العميل"));

Vector<String>PdetailTable_columnTitles=new Vector<String>
        (Arrays.asList(       
      " الثمن", "العمولة", "الوهبة", "إجمالي المبلغ", "الناولون", "الصافي", "القائم", "البيان", " تاريخ الفاتورة", "رقم الفاتورة"
));

Vector<String>customerSummary_columnTitles=new Vector<>(Arrays.asList(        "دخاخين", "العمولة", "الجامبو", "الدبابة"
));

 
}
