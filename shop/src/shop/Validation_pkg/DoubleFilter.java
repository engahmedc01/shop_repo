/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shop.Validation_pkg;

/**
 *
 * @author ahmed
 */
public class DoubleFilter extends NumricFilter {
    
    
     @Override
    boolean test(String text){
        
              try {
         Double.parseDouble(text);
         return true;
      } catch (NumberFormatException e) {
          if(text.equals("."))
              return true;
         return false;
      }
    

    }


}