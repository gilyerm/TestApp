package com.example.testapp.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cart {

    /// unique id of the cart
    private String id;

    private final List<Food> foods;

    /// the user ID of the cart owner
    private String uid;

    public Cart() {
        foods = new ArrayList<>();
    }

    public Cart(String id, List<Food> foods, String uid) {
        this.foods = foods;
        this.id = id;
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addFood(Food food) {
        foods.add(food);
    }

    public void addFoods(List<Food> foods) {
        this.foods.addAll(foods);
    }

    public boolean removeFood(Food food) {
        return foods.remove(food);
    }

    public Food removeFood(int index) {
        if (index < 0 || index >= foods.size()) {
            return null;
        }
        return foods.remove(index);
    }

    public Food getFood(int index) {
        if (index < 0 || index >= foods.size()) {
            return null;
        }
        return foods.get(index);
    }

    public List<Food> getFoods() {
        return foods;
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (Food food : foods) {
            totalPrice += food.getPrice();
        }
        return totalPrice;
    }

    public void clear() {
        foods.clear();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id='" + id + '\'' +
                ", foods=" + foods +
                ", uid='" + uid + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Cart cart = (Cart) object;
        return Objects.equals(id, cart.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
