package com.bizconit.homeinventory.model;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 11/9/14
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class Inventory {

    private int id;
    private String productName;
    private int smarthubId;
    private float value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getSmarthubId() {
        return smarthubId;
    }

    public void setSmarthubId(int smarthubId) {
        this.smarthubId = smarthubId;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
