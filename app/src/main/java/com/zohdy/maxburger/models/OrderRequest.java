package com.zohdy.maxburger.models;

import java.util.List;

/**
 * Created by peterzohdy on 13/11/2017.
 */

public class OrderRequest {
    private String phoneNumber;
    private String totalAmount;
    private String specialInstructions;
    private List<Order> foodItems;

    // TODO make enum
    private String status;

    public OrderRequest() {
    }

    public OrderRequest(String phoneNumber, String totalAmount, String specialInstructions, List<Order> foodItems) {
        this.phoneNumber = phoneNumber;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
