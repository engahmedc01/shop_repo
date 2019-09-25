/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shop.data_structures_pkg;

/**
 *
 * @author ahmed
 */
public class CustomerDailyTransactions {

    private double noloun;
    private String customerName;
    private double tips;
    private String store_place;
    private double weight;
    private String notes;

    public CustomerDailyTransactions(
            double noloun,
            String customerName,
            double tips,
            String store_place,
            double weight, String notes) {
        
        
    }

    /**
     * @return the noloun
     */
    public double getNoloun() {
        return noloun;
    }

    /**
     * @param noloun the noloun to set
     */
    public void setNoloun(double noloun) {
        this.noloun = noloun;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the tips
     */
    public double getTips() {
        return tips;
    }

    /**
     * @param tips the tips to set
     */
    public void setTips(double tips) {
        this.tips = tips;
    }

    /**
     * @return the store_place
     */
    public String getStore_place() {
        return store_place;
    }

    /**
     * @param store_place the store_place to set
     */
    public void setStore_place(String store_place) {
        this.store_place = store_place;
    }

    /**
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(double weight) {
        this.weight = weight;
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
}
