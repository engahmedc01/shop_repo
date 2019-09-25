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
public class CustomerOrderDetail {

    private String catName;
    private String productName;
    private double quanity;
    private double price;

    public CustomerOrderDetail() {
    }

    public CustomerOrderDetail(String productName, double quanity, double price, String catName) {

        this.productName = productName;
        this.quanity = quanity;
        this.price = price;
        this.catName = catName;

    }

    /**
     * @return the quanity
     */
    public double getQuanity() {
        return quanity;
    }

    /**
     * @param quanity the quanity to set
     */
    public void setQuanity(double quanity) {
        this.quanity = quanity;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the catName
     */
    public String getCatName() {
        return catName;
    }

    /**
     * @param catName the catName to set
     */
    public void setCatName(String catName) {
        this.catName = catName;
    }

}
