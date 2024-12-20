package com.example.testapp.models;

import androidx.annotation.NonNull;

/// single Food
/// for example, a single item in a menu
/// or a single item in a shopping cart
/// tomato, apple, etc.
public class Food {

    /// unique id of the food
    private String id;

    // name of the food
    private String name;

    // price of the food
    private double price;

    private String imageBase64;

    // constructor
    public Food() {
    }
    public Food(String id, String name, double price, String imageBase64) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageBase64 = imageBase64;
    }

    public String getId() {
        return id;
    }

    // getter for name
    public String getName() {
        return name;
    }

    // getter for price
    public double getPrice() {
        return price;
    }

    public void setId(String id) {
        this.id = id;
    }

    // setter for name
    public void setName(String name) {
        this.name = name;
    }

    // setter for price
    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }



    // toString method
    @NonNull
    @Override
    public String toString() {
        return "Food{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price + '\'' +
                ", imageBase64='" + imageBase64 + '\'' +
                '}';
    }

}
