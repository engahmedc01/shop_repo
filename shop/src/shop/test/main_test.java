/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shop.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import mondrian.olap.Category;
import shop.data_source_pkg.DataSourc;

/**
 *
 * @author Comu City
 */
public class main_test {
    // "29-فبراير 17:35:27 م"
   public static void main(String[] args) {
           String[] tables = {"SEASON", "SELLER_LOAN_BAG", "MY_SAFE",
            "SELLER_WEIGHT", "SELLER_ORDER", "PURCHASED_CUSTOMRER_INSTS",
            "SELLER", "OUTCOME_DETAIL", "LOAN_PAYING", "OUTCOME", "INC_LOAN",
            "LOANERS", "LOAN_ACCOUNT", "INSTALMENT", "INCOME_DETAIL", "INCOME",
            "CUSTOMER_ORDER", "CUSTOMER", "CONTRACTOR", "CONTRACTOR_ACCOUNT",
            "CONTRACTOR_ACCOUNT_DETAIL", "SELLER_ORDER"};
    
      DataSourc data_source=new DataSourc();
       
   
    data_source.confirmDataUpload(tables);
   
   }
      
}
