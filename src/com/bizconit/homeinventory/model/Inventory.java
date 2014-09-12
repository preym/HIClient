package com.bizconit.homeinventory.model;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 11/9/14
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class Inventory {

    private String id;
    private String productName;
    private String smarthubId;
    private float value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSmarthubId() {
        return smarthubId;
    }

    public void setSmarthubId(String smarthubId) {
        this.smarthubId = smarthubId;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
