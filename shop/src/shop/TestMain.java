/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shop;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.util.IOUtils;
import shop.data_source_pkg.DataSourc;

/**
 *
 * @author ahmed
 */
public class TestMain {

    public static final String EXPORT_DIR = "F:\\shop_workshop\\EXPORT";

 
    public static void main(String[] args) throws IOException {
long start=System.currentTimeMillis();
        try {
            new DataSourc().export("TEST", EXPORT_DIR,"export1" );//.getProductID();   
        } catch (SQLException ex) {
            Logger.getLogger(TestMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        long end=System.currentTimeMillis();
     System.out.println(end-start);
    }

}
