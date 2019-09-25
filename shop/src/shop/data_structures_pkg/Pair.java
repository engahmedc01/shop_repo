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
public class Pair <T,K>{
    private T frist;
    private K second;
  public  Pair(){}
  public  Pair(T frist,K second){
    this.frist=frist;
    this.second=second;
    }

    /**
     * @return the frist
     */
    public T getFrist() {
        return frist;
    }

    /**
     * @param frist the frist to set
     */
    public void setFrist(T frist) {
        this.frist = frist;
    }

    /**
     * @return the second
     */
    public K getSecond() {
        return second;
    }

    /**
     * @param second the second to set
     */
    public void setSecond(K second) {
        this.second = second;
    }
}
