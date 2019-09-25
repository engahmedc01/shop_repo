/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shop.GUI_attachement_pkg;

import java.awt.Frame;
import java.util.Vector;
import shop.data_source_pkg.DataSourc;

/**
 *
 * @author ahmed
 */
public class PersonAddress_STF extends JSuggestField{
String PersonType;
 DataSourc data_source;
    public PersonAddress_STF( DataSourc data_source,Frame owner, String PersonType) {
        super(owner);
        this.PersonType=PersonType;
        this.data_source=data_source;

    }

  @Override
       Vector<String>getSuggestedData(){
           if(PersonType.equals("buyer"))
           {
           return data_source.inExactMatchSearchBuyerAddress(getText());
           }
             if(PersonType.equals("customer"))
           {
           return data_source.inExactMatchSearchCustomersAddress(getText());
           }
            
            if(PersonType.equals("customer"))
           {
           return data_source.inExactMatchSearchCustomersAddress(getText());
           }
            

        return null;
        }
    
    
}
