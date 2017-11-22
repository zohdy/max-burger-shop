package com.zohdy.maxburger.models;

/**
 * Created by peterzohdy on 07/11/2017.
 */

public class Food {

    private String name;
    private String image;
    private String price;
    private String menuId;
    private String description;
    private int quantity;

    public Food() {
    }

    public Food(String name, String image, String price, String menuId, String description) {
        this.description = description;
        this.name = name;
        this.image = image;
        this.price = price;
        this.menuId = menuId;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
