package com.zohdy.maxburger.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by peterzohdy on 11/11/2017.
 */

public class Order implements Parcelable {

    private String foodId;
    private String foodName;
    private String quantity;
    private String price;
    private int keyId;

    //**************************** Methods related to Parceable ************************* //

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(foodId);
        dest.writeString(foodName);
        dest.writeString(quantity);
        dest.writeString(price);
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    protected Order(Parcel in) {
        foodId = in.readString();
        foodName = in.readString();
        quantity = in.readString();
        price = in.readString();
    }

    //**************************** Parceable methods ending ******************************** //

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
