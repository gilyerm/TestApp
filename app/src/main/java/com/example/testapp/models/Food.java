package com.example.testapp.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

/// A single food item, for example an item on a menu or in a shopping cart (tomato, apple, etc.).
/// Stored in the database; a {@link Cart} holds copies of foods.
/// Two foods are equal if they have the same ID.
/// @see com.example.testapp.services.IFoodService
public class Food implements Serializable, Identifiable {

    /// unique ID of the food
    private String id;

    /// name of the food
    private String name;

    /// price of one unit
    private double price;

    /// image as a WebP base64 string
    /// @see com.example.testapp.utils.ImageUtil
    private String imageBase64;

    /// Empty constructor, required by Firebase to convert documents to objects.
    public Food() {
    }

    /// Copy constructor.
    /// @param other the food to copy
    public Food(Food other) {
        this.id = other.id;
        this.name = other.name;
        this.price = other.price;
        this.imageBase64 = other.imageBase64;
    }

    /// @param id          unique ID of the food
    /// @param name        name of the food
    /// @param price       price of one unit
    /// @param imageBase64 image as a WebP base64 string
    public Food(String id, String name, double price, String imageBase64) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageBase64 = imageBase64;
    }

    /// @return unique ID of the food
    public String getId() {
        return id;
    }

    /// @return name of the food
    public String getName() {
        return name;
    }

    /// @return price of one unit
    public double getPrice() {
        return price;
    }

    /// @param id unique ID of the food
    public void setId(String id) {
        this.id = id;
    }

    /// @param name name of the food
    public void setName(String name) {
        this.name = name;
    }

    /// @param price price of one unit
    public void setPrice(double price) {
        this.price = price;
    }

    /// @return image as a WebP base64 string
    public String getImageBase64() {
        return imageBase64;
    }

    /// @param imageBase64 image as a WebP base64 string
    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    /// @return the food's fields, without the image (too long to log)
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

    /// Two foods are equal if they have the same ID.
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Food food = (Food) object;
        return Objects.equals(id, food.id);
    }

    /// @return hash of the ID, matching {@link #equals(Object)}
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
