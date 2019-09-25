/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_structures_pkg;

/**
 *
 * @author ahmed
 */
public class SupplierOrder {
    
    
    
    
    private String category_name;
    private String ProductName;
    private double unitePrice;
    private double quantity;
    private String date;
    private double total;
    private double orderID;
    public SupplierOrder(){}
    public SupplierOrder( String category_name,String ProductName,double unitePrice,double quantity, String date,double total, double orderID){
    
    this.orderID=orderID;
    this.quantity=quantity;
    this.unitePrice=unitePrice;
    this.date=date;
    this.total=total;
    this.category_name=category_name;
    this.ProductName=ProductName;
    
    }

    /**
     * @return the category_name
     */
    public String getCategory_name() {
        return category_name;
    }

    /**
     * @param category_name the category_name to set
     */
    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    /**
     * @return the ProductName
     */
    public String getProductName() {
        return ProductName;
    }

    /**
     * @param ProductName the ProductName to set
     */
    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    /**
     * @return the unitePrice
     */
    public double getUnitePrice() {
        return unitePrice;
    }

    /**
     * @param unitePrice the unitePrice to set
     */
    public void setUnitePrice(double unitePrice) {
        this.unitePrice = unitePrice;
    }

    /**
     * @return the quantity
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
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
     * @return the orderID
     */
    public double getOrderID() {
        return orderID;
    }

    /**
     * @param orderID the orderID to set
     */
    public void setOrderID(double orderID) {
        this.orderID = orderID;
    }
    
    
}
