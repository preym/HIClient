package com.bizconit.homeinventory.model;

/**
 * Created with IntelliJ IDEA.
 * User: ehc
 * Date: 11/9/14
 * Time: 11:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class User {

    private String id;
    private String name;
    private int pin;
    private String familyMembers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public String getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers(String familyMembers) {
        this.familyMembers = familyMembers;
    }


    public String toString() {
        return this.getId() + " " + getName() + " " + getPin() + " " + getFamilyMembers();
    }

}
