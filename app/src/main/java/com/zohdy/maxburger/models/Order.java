package com.zohdy.maxburger.models;

/**
 * Created by peterzohdy on 11/11/2017.
 */

public class Order {

    private String foodId;
    private String foodName;
    private String quantity;
    private String price;

    private int keyId;

    public Order() {

    }

    public Order(String foodId, String FoodName, String quantity, String price) {
        this.foodId = foodId;
        this.foodName = FoodName;
        this.quantity = quantity;
        this.price = price;
    }

    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
