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
public class IntegerFilter extends NumricFilter {
    @Override
    boolean test(String text){
        
              try {
         Integer.parseInt(text);
         return true;
      } catch (NumberFormatException e) {
         return false;
      }
      
    
    
    }
    
}
