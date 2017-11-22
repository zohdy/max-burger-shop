package com.zohdy.maxburger.models;

/**
 * Created by peterzohdy on 06/11/2017.
 */

public class Category {

    private String name;
    private String image;
    private String description;

    public Category() {
    }

    public Category(String name, String image, String description) {
        this.name = name;
        this.image = image;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
