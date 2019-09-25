/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shop;

import shop.GUI.Login;
import shop.recources_pkg.ResourceLoader;
import shop.data_source_pkg.DataSourc;
/**
 *
 * @author ahmed
 */
public class Shop {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        {   // TODO code application logic here
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } }
        
        
        
        
        
        ResourceLoader RL=new ResourceLoader();
        DataSourc data_source;
        data_source = new DataSourc();
                
                
                
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        Login dialog = new Login(new javax.swing.JFrame(), true,RL.getLogIn_image(),data_source);
                        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                            @Override
                            public void windowClosing(java.awt.event.WindowEvent e) {
                                System.exit(0);
                            }
                        });
                        dialog.setVisible(true);
                    }
                });
        
        
        System.out.print("here");
       
    
}
}