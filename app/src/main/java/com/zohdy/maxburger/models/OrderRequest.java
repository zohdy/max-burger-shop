package com.zohdy.maxburger.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peterzohdy on 13/11/2017.
 */

public class OrderRequest implements Parcelable {

    private String phoneNumber;
    private String totalAmount;
    private String specialInstructions;
    private List<Order> foodItems;
    private String status;


    //**************************** Methods related to Parceable ************************* //
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(phoneNumber);
        dest.writeString(totalAmount);
        dest.writeString(specialInstructions);
        dest.writeTypedList(foodItems);
        dest.writeString(status);
    }

    public static final Creator<OrderRequest> CREATOR = new Creator<OrderRequest>() {
        @Override
        public OrderRequest createFromParcel(Parcel in) {
            return new OrderRequest(in);
        }

        @Override
        public OrderRequest[] newArray(int size) {
            return new OrderRequest[size];
        }
    };

    protected OrderRequest(Parcel in) {
        this.phoneNumber = in.readString();
        this.totalAmount = in.readString();
        this.specialInstructions = in.readString();
        this.foodItems = new ArrayList<Order>();
        in.readTypedList(foodItems, Order.CREATOR);
        this.status = in.readString();
    }
    //**************************** Parceable methods ending ******************************** //


    public OrderRequest() {
    }

    public OrderRequest(String phoneNumber, String totalAmount, String specialInstructions, List<Order> foodItems) {
        this.phoneNumber = phoneNumber;
        this.totalAmount = totalAmount;
        this.specialInstructions = specialInstructions;
        this.foodItems = foodItems;
        this.status = "0";
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
