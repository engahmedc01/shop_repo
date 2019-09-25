/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_structures_pkg;

import java.util.List;

/**
 *
 * @author ahmed
 */
public class CustomerOrder {
    private List<CustomerOrderDetail> order_detail;
    private OrderHeader order_header;
    public CustomerOrder(){}
    public CustomerOrder(List<CustomerOrderDetail> order_detail,OrderHeader order_header)
    {
    this.order_detail=order_detail;
    this.order_header=order_header;
    }

 

    /**
     * @return the order_header
     */
    public OrderHeader getOrder_header() {
        return order_header;
    }

    /**
     * @param order_header the order_header to set
     */
    public void setOrder_header(OrderHeader order_header) {
        this.order_header = order_header;
    }

    /**
     * @return the order_detail
     */
    public List<CustomerOrderDetail> getOrder_detail() {
        return order_detail;
    }

    /**
     * @param order_detail the order_detail to set
     */
    public void setOrder_detail(List<CustomerOrderDetail> order_detail) {
        this.order_detail = order_detail;
    }

   
    
}
