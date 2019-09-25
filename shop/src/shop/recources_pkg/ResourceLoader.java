/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shop.recources_pkg;

import java.io.InputStream;
import javax.swing.JOptionPane;

/**
 *
 * @author ahmed
 */
public class ResourceLoader {

 

    public ResourceLoader() {
       
        
        


    }

    public static void main(String args[]) {

        ResourceLoader rl = new ResourceLoader();
      

    }

    /**THE B
  */
    //Invoices
     public InputStream getStart_image() {
        return getClass().getResourceAsStream("images/StartImage.jpg");
    }
     public InputStream getorders_image() {
        return getClass().getResourceAsStream("images/Invoices.jpg");
    }
     //loginImage
//    public InputStream getLogIn_image() {
//        return getClass().getResourceAsStream("images/LogIn.jpg");
//    }
      public InputStream getLogIn_image() {
        return getClass().getResourceAsStream("images/loginImage.jpg");
    }
    public InputStream getwelcome_image() {
        return getClass().getResourceAsStream("images/w.jpg");
    }
    public InputStream getCustomersBook(){
    
    
    return getClass().getResourceAsStream("images/CustomersBook.jpg");
    
    }
    
    public InputStream getInventoryImage(){
    
    
    return getClass().getResourceAsStream("images/inventoryImage.jpg");
    
    }
    
    
    public InputStream getReportHeaderImage(){
    
    
    return getClass().getResourceAsStream("images/122.jpg");
    
    }
     public InputStream getSettingsImage(){
    
    
    return getClass().getResourceAsStream("images/settings.jpg");
    
    }
    
    
 public InputStream getKareemWithDrawals_image(){
    
    
    return getClass().getResourceAsStream("images/Withdrawals.jpg");
    
    }    
    public InputStream getSellersBookImage(){
    
    
    return getClass().getResourceAsStream("images/DailySales.jpg");
    
    }
    
    public InputStream getExpenesBookImage(){
    
    
    return getClass().getResourceAsStream("images/ExpensesBook.jpg");
    
    }
    
     
}
