/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_structures_pkg;

import java.util.Date;

/**
 *
 * @author ahmed
 */
public class CustomerInst {

    private int inst_number;
    private double inst_value;
    private Date agreementDate;
    private Date paidDate;
    private String notes;
    private boolean sincere;

    public CustomerInst(int inst_number, double inst_value, Date agreementDate, Date paidDate,String notes,boolean sincere) {
       
        
        this.inst_number=inst_number;
        this.inst_value=inst_value;
        this.agreementDate=agreementDate;
        this.paidDate=paidDate;
        this.notes=notes;
        this.sincere=sincere;
        
    }

    /**
     * @return the inst_number
     */
    public int getInst_number() {
        return inst_number;
    }

    /**
     * @param inst_number the inst_number to set
     */
    public void setInst_number(int inst_number) {
        this.inst_number = inst_number;
    }

    /**
     * @return the inst_value
     */
    public double getInst_value() {
        return inst_value;
    }

    /**
     * @param inst_value the inst_value to set
     */
    public void setInst_value(double inst_value) {
        this.inst_value = inst_value;
    }

    /**
     * @return the agreementDate
     */
    public Date getAgreementDate() {
        return agreementDate;
    }

    /**
     * @param agreementDate the agreementDate to set
     */
    public void setAgreementDate(Date agreementDate) {
        this.agreementDate = agreementDate;
    }

    /**
     * @return the paidDate
     */
    public Date getPaidDate() {
        return paidDate;
    }

    /**
     * @param paidDate the paidDate to set
     */
    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    /**
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return the sincere
     */
    public boolean isSincere() {
        return sincere;
    }

    /**
     * @param sincere the sincere to set
     */
    public void setSincere(boolean sincere) {
        this.sincere = sincere;
    }

}
