package com.zohdy.maxburger.models;

import java.util.List;

/**
 * Created by peterzohdy on 13/11/2017.
 */

public class Request {
    private String name;
    private String phone;
    private String email;
    private String totalAmount;
    private String specialInstructions;
    private List<Order> foodItems;
    private String status;

    public Request() {
    }

    public Request(String name, String phone, String email, String totalAmount, String specialInstructions, List<Order> foodItems) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.totalAmount = totalAmount;
        this.foodItems = foodItems;
        this.specialInstructions = specialInstructions;
        this.status = "0";
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<Order> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<Order> foodItems) {
        this.foodItems = foodItems;
    }
}
