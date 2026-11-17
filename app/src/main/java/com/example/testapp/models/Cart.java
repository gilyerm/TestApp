package com.example.testapp.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/// A shopping cart owned by one user.
/// Holds a flat list of foods: a food bought twice appears twice in the list.
/// Two carts are equal if they have the same ID.
/// @see com.example.testapp.services.ICartService
public class Cart implements Serializable, Identifiable {

    /// unique ID of the cart
    private String id;

    /// name of the cart, chosen by the user
    private String title;

    /// foods in the cart, each repeated by its quantity; never null
    private final List<Food> foods;

    /// the user ID of the cart owner
    private String uid;

    /// Empty constructor, required by Firebase to convert documents to objects.
    public Cart() {
        foods = new ArrayList<>();
    }

    /// @param id    unique ID of the cart
    /// @param title name of the cart
    /// @param foods foods in the cart; null means empty
    /// @param uid   user ID of the cart owner
    public Cart(String id, String title, List<Food> foods, String uid) {
        this.id = id;
        this.title = title;
        this.foods = foods != null ? foods : new ArrayList<>();
        this.uid = uid;
    }

    /// @return unique ID of the cart
    public String getId() {
        return id;
    }

    /// @param id unique ID of the cart
    public void setId(String id) {
        this.id = id;
    }

    /// @return name of the cart
    public String getTitle() {
        return title;
    }

    /// @param title name of the cart
    public void setTitle(String title) {
        this.title = title;
    }

    /// Add one unit of a food.
    /// @param food the food to add
    public void addFood(Food food) {
        foods.add(food);
    }

    /// Add several foods.
    /// @param foods the foods to add
    public void addFoods(List<Food> foods) {
        this.foods.addAll(foods);
    }

    /// Remove one unit of a food (the first with the same ID).
    /// @param food the food to remove
    /// @return true if the food was in the cart
    public boolean removeFood(Food food) {
        return foods.remove(food);
    }

    /// Remove the food at a position.
    /// @param index position in the list
    /// @return the removed food, or null if the index is out of range
    public Food removeFood(int index) {
        if (index < 0 || index >= foods.size()) {
            return null;
        }
        return foods.remove(index);
    }

    /// @param index position in the list
    /// @return the food at that position, or null if the index is out of range
    public Food getFood(int index) {
        if (index < 0 || index >= foods.size()) {
            return null;
        }
        return foods.get(index);
    }

    /// @return the foods in the cart (the live list, not a copy)
    public List<Food> getFoods() {
        return foods;
    }

    /// Replace all foods.
    /// @param foods the new foods; null empties the cart
    public void setFoods(List<Food> foods) {
        this.foods.clear();
        if (foods != null) {
            this.foods.addAll(foods);
        }
    }

    /// Sum of the prices of all foods.
    /// Firebase also saves it as a `totalPrice` field, because it looks like a getter.
    /// @return the total price
    public double getTotalPrice() {
        double totalPrice = 0;
        for (Food food : foods) {
            totalPrice += food.getPrice();
        }
        return totalPrice;
    }

    /// Remove all foods.
    public void clear() {
        foods.clear();
    }

    /// @return user ID of the cart owner
    public String getUid() {
        return uid;
    }

    /// @param uid user ID of the cart owner
    public void setUid(String uid) {
        this.uid = uid;
    }

    /// @return the cart's fields, for logging
    @NonNull
    @Override
    public String toString() {
        return "Cart{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", foods=" + foods +
                ", uid='" + uid + '\'' +
                '}';
    }

    /// Two carts are equal if they have the same ID.
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Cart cart = (Cart) object;
        return Objects.equals(id, cart.id);
    }

    /// @return hash of the ID, matching {@link #equals(Object)}
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
