package com.jahanbabu.AndroidAppSkeleton.model;

/**
 * Created by JK on 11/26/16.
 */
public class Product {
    String name,type,description,image,date,place,remarks;

    public Product(String name, String type, String description, String image, String date, String place, String remarks) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.image = image;
        this.date = date;
        this.place = place;
        this.remarks = remarks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
