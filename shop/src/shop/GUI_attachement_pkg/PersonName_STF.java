/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shop.GUI_attachement_pkg;

import java.awt.Frame;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JTextField;
import shop.data_source_pkg.DataSourc;
import shop.data_structures_pkg.Person;
/**
 *
 * @author ahmed
 */
public class PersonName_STF extends JSuggestField  {

    
    
    String PersonType;
    JTextField address_TF;
    JTextField phone_TF;
    DataSourc data_source;
    boolean nameOnly;
    public PersonName_STF(Frame owner, String PersonType,DataSourc data_source, JTextField address, JTextField phone) {
        super(owner);
        this.PersonType=PersonType;
        this.address_TF=address;
        this.phone_TF=phone;
        this.data_source=data_source;
        nameOnly=false;
    }

       public PersonName_STF(Frame owner, String PersonType,DataSourc data_source) {
        super(owner);
        this.PersonType=PersonType;
        this.data_source=data_source;
        nameOnly=true;
    }
    @Override
       Vector<String>getSuggestedData(){
           if(PersonType.equals("seller_temperory"))
           {
           return data_source.inExactMatchSearchBuyerName(getText(),"TEMPERORY");
           }
            if(PersonType.equals("seller_permenant"))
           {
           return data_source.inExactMatchSearchBuyerName(getText(),"PERMENANT");
           }
               if(PersonType.equals("seller"))
           {
           return data_source.inExactMatchSearchBuyerName(getText());
           }
             if(PersonType.equals("customer"))
           {
           return data_source.inExactMatchSearchCustomersName(getText());
           }
                if(PersonType.equals("Pcustomer"))
           {
                 Vector<String>data=new Vector<>();
               Vector<String>temp=data_source.inExactMatchSearchCustomersName("مشتروات_"+getText());
               if(temp!=null)
               for(int i=0;i<temp.size();i++){
               String []str=temp.get(i).split("_");
               data.add(str[1]);
               
               }
               
           return data;
           }   if(PersonType.equals("Kcustomer"))
           {
                Vector<String>data=new Vector<>();
               Vector<String>temp=data_source.inExactMatchSearchCustomersName("كريم_"+getText());
               if(temp!=null)
               for(int i=0;i<temp.size();i++){
               String []str=temp.get(i).split("_");
               data.add(str[1]);
               
               }
               
           return data;
           
           }
             
             
               if(PersonType.equals("K_L")||PersonType.equals("K_S")||PersonType.equals("K_V"))
           {
           return data_source.inExactMatchSearchContractorName(getText(),PersonType);
           }
            
             if(PersonType.equals("IN_LOAN")||PersonType.equals("OUT_LOAN"))
           {
           return data_source.inExactMatchSearchloanerName(getText(),PersonType);
           }
          

        return null;
        }
    
      
    @Override
        void autoComplete(String name){
            if(address_TF!=null&&phone_TF!=null){
             Person p=null;
            if(PersonType.equals("customer")){
           p=data_source.customerSelectAll(name);
           
            }
            else if(PersonType.equals("buyer")){
            
             p=data_source.customerSelectAll(name);
            
            }
            if(nameOnly)
            {address_TF.setText(p.getAddress());
            phone_TF.setText(p.getPhone());
        }}
}}
