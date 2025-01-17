package com.example.testapp.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

/// single Food
/// for example, a single item in a menu
/// or a single item in a shopping cart
/// tomato, apple, etc.
public class Food implements Serializable {

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

    public Food(Food other) {
        this.id = other.id;
        this.name = other.name;
        this.price = other.price;
        this.imageBase64 = other.imageBase64;
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

    @NonNull
    @Override
    public String toString() {
        return "Food{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price + '\'' +
//                ", imageBase64='" + imageBase64 + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Food food = (Food) object;
        return Objects.equals(id, food.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
