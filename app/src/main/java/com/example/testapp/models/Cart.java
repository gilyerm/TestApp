package com.example.testapp.models;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    /// unique id of the cart
    private String id;

    private final ArrayList<Food> foods;

    public Cart() {
        foods = new ArrayList<>();
    }

    public Cart(String id) {
        this.id = id;
        foods = new ArrayList<>();
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

    public ArrayList<Food> getFoods() {
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

    @NonNull
    @Override
    public String toString() {
        return "Cart{" +
                "id='" + id + '\'' +
                ", foods=" + foods +
                '}';
    }

}
