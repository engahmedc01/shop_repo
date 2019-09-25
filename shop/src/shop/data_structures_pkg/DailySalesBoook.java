/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shop.data_structures_pkg;

import java.util.List;

/**
 *
 * @author ahmed
 */
public class DailySalesBoook {
    
    
    private String sellername;
    private String totalCost;
    private java.util.Date orderDate;
    private List<OrderDetail>orderDetails;
    public DailySalesBoook(){}
    public DailySalesBoook(String sellerName,String totalCost,java.util.Date orderDate,List<OrderDetail>orderDetails){
        this.sellername=sellerName;
        this.totalCost=totalCost;
        this.orderDate=orderDate;
        this.orderDetails=orderDetails;
    }

    /**
     * @return the sellername
     */
    public String getSellername() {
        return sellername;
    }

    /**
     * @param sellername the sellername to set
     */
    public void setSellername(String sellername) {
        this.sellername = sellername;
    }

    /**
     * @return the totalCost
     */
    public String getTotalCost() {
        return totalCost;
    }

    /**
     * @param totalCost the totalCost to set
     */
    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * @return the orderDate
     */
    public java.util.Date getOrderDate() {
        return orderDate;
    }

    /**
     * @param orderDate the orderDate to set
     */
    public void setOrderDate(java.util.Date orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * @return the orderDetails
     */
    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    /**
     * @param orderDetails the orderDetails to set
     */
    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
