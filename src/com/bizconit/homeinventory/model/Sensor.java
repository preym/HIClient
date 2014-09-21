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
  private String productName;
  private String productType;
  private String smartHubId;

  public String getSmartHubId() {
    return smartHubId;
  }

  public void setSmartHubId(String smartHubId) {
    this.smartHubId = smartHubId;
  }

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

  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }
}
