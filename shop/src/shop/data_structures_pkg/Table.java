/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shop.data_structures_pkg;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ahmed
 */
public class Table {
    private String name ;
    private List<String> columns;
    private List<List<String>>data;
    public Table(List<String> columns,List<List<String>>data,String name){
    
    this.columns=columns;
    this.data=data;
    this.name=name;
    
    
    
    }
      public Table(){
    
    this.columns=new ArrayList<String>();
    this.data=new ArrayList<>();
    
    
    
    }

    /**
     * @return the columns
     */
    public List<String> getColumns() {
        return columns;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    /**
     * @return the data
     */
    public List<List<String>> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(List<List<String>> data) {
        this.data = data;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
}
