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
public class OrderDetail {

    private String productName;
    private String grossWeight;
    private String netWeight;
    private String packageNumber;
    private String unitePrice;
    private String Cost;
    private String customerName;
   public OrderDetail(){}
    public OrderDetail(String productName,
            String grossWeight,
            String netWeight,
            String packageNumber,
            String unitePrice,
            String Cost,
            String customerName) {
        this.Cost=Cost;
        this.packageNumber=packageNumber;
        this.productName=productName;
        this.grossWeight=grossWeight;
        this.unitePrice=unitePrice;
        this.customerName=customerName;
        this.netWeight=netWeight;
        

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
     * @return the grossWeight
     */
    public String getGrossWeight() {
        return grossWeight;
    }

    /**
     * @param grossWeight the grossWeight to set
     */
    public void setGrossWeight(String grossWeight) {
        this.grossWeight = grossWeight;
    }

    /**
     * @return the netWeight
     */
    public String getNetWeight() {
        return netWeight;
    }

    /**
     * @param netWeight the netWeight to set
     */
    public void setNetWeight(String netWeight) {
        this.netWeight = netWeight;
    }

    /**
     * @return the packageNumber
     */
    public String getPackageNumber() {
        return packageNumber;
    }

    /**
     * @param packageNumber the packageNumber to set
     */
    public void setPackageNumber(String packageNumber) {
        this.packageNumber = packageNumber;
    }

    /**
     * @return the unitePrice
     */
    public String getUnitePrice() {
        return unitePrice;
    }

    /**
     * @param unitePrice the unitePrice to set
     */
    public void setUnitePrice(String unitePrice) {
        this.unitePrice = unitePrice;
    }

    /**
     * @return the Cost
     */
    public String getCost() {
        return Cost;
    }

    /**
     * @param Cost the Cost to set
     */
    public void setCost(String Cost) {
        this.Cost = Cost;
    }

    /**
     * @return the customerOrder_id
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerOrder_id the customerOrder_id to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
