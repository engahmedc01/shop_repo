/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_structures_pkg;

import static java.util.Collections.list;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ahmed
 */
public class OrderHeader {

    private double id;
    private Date orderDate;
    private double total;
    private double customerID;
    private String notes;
    private double paid;
    private double rest;
    private String order_type;
    private int sincere;
    private double inst_valus;
    private int inst_paid;
    private int inst_due;
    private String inst_period;
    private List<data_structures_pkg.CustomerInst> order_inst;

    private OrderHeader() {
    }

    public OrderHeader(double id, Date orderDate, double total, double customerID, String notes, double paid,
            double rest, String order_type, int sincere,
            double inst_valus, int inst_paid, int inst_due, String inst_period, List<data_structures_pkg.CustomerInst> order_inst) {

        this.id = id;
        this.orderDate = orderDate;
        this.notes = notes;
        this.total = total;
        this.customerID = customerID;
        this.paid = paid;
        this.rest = rest;
        this.orderDate = orderDate;
        this.sincere = sincere;
        this.order_type = order_type;
        this.inst_valus = inst_valus;
        this.inst_paid = inst_paid;
        this.inst_period = inst_period;
        this.inst_due = inst_due;
        this.order_inst=order_inst;
    }

    /**
     * @return the id
     */
    public double getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(double id) {
        this.id = id;
    }

    /**
     * @return the orderDate
     */
    public Date getOrderDate() {
        return orderDate;
    }

    /**
     * @param orderDate the orderDate to set
     */
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * @return the total
     */
    public double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * @return the customerID
     */
    public double getCustomerID() {
        return customerID;
    }

    /**
     * @param customerID the customerID to set
     */
    public void setCustomerID(double customerID) {
        this.customerID = customerID;
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
     * @return the paid
     */
    public double getPaid() {
        return paid;
    }

    /**
     * @param paid the paid to set
     */
    public void setPaid(double paid) {
        this.paid = paid;
    }

    /**
     * @return the rest
     */
    public double getRest() {
        return rest;
    }

    /**
     * @param rest the rest to set
     */
    public void setRest(double rest) {
        this.rest = rest;
    }

    /**
     * @return the order_type
     */
    public String getOrder_type() {
        return order_type;
    }

    /**
     * @param order_type the order_type to set
     */
    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    /**
     * @return the sincere
     */
    public int getSincere() {
        return sincere;
    }

    /**
     * @param sincere the sincere to set
     */
    public void setSincere(int sincere) {
        this.sincere = sincere;
    }

    /**
     * @return the inst_valus
     */
    public double getInst_valus() {
        return inst_valus;
    }

    /**
     * @param inst_valus the inst_valus to set
     */
    public void setInst_valus(double inst_valus) {
        this.inst_valus = inst_valus;
    }

    /**
     * @return the inst_paid
     */
    public int getInst_paid() {
        return inst_paid;
    }

    /**
     * @param inst_paid the inst_paid to set
     */
    public void setInst_paid(int inst_paid) {
        this.inst_paid = inst_paid;
    }

    /**
     * @return the inst_due
     */
    public int getInst_due() {
        return inst_due;
    }

    /**
     * @param inst_due the inst_due to set
     */
    public void setInst_due(int inst_due) {
        this.inst_due = inst_due;
    }

    /**
     * @return the inst_period
     */
    public String getInst_period() {
        return inst_period;
    }

    /**
     * @param inst_period the inst_period to set
     */
    public void setInst_period(String inst_period) {
        this.inst_period = inst_period;
    }

    /**
     * @return the order_inst
     */
    public List<data_structures_pkg.CustomerInst> getOrder_inst() {
        return order_inst;
    }

    /**
     * @param order_inst the order_inst to set
     */
    public void setOrder_inst(List<data_structures_pkg.CustomerInst> order_inst) {
        this.order_inst = order_inst;
    }

 

}
