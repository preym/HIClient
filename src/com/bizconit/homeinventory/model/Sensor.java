package com.bizconit.homeinventory.model;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 21/9/14
 * Time: 11:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class Sensor {

  private String id;
  private String product_name;
  private String product_type;
  private String smarthub_id;

  public String getSmarthub_id() {
    return smarthub_id;
  }

  public void setSmarthub_id(String smarthub_id) {
    this.smarthub_id = smarthub_id;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getProduct_name() {
    return product_name;
  }

  public void setProduct_name(String product_name) {
    this.product_name = product_name;
  }

  public String getProduct_type() {
    return product_type;
  }

  public void setProduct_type(String product_type) {
    this.product_type = product_type;
  }
}
