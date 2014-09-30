package com.bizconit.homeinventory.model;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 11/9/14
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class Inventory {

  private String id;
  private String smarthub_id;
  private float value;
  private String sensor_id;
  private String productType;

  public Timestamp getInserted_at() {
    return inserted_at;
  }

  public void setInserted_at(Timestamp inserted_at) {
    this.inserted_at = inserted_at;
  }

  private Timestamp inserted_at;

  public String getSensor_id() {
    return sensor_id;
  }

  public void setSensor_id(String sensor_id) {
    this.sensor_id = sensor_id;
  }

  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSmarthub_id() {
    return smarthub_id;
  }

  public void setSmarthub_id(String smarthub_id) {
    this.smarthub_id = smarthub_id;
  }

  public float getValue() {
    return value;
  }

  public void setValue(float value) {
    this.value = value;
  }
}
