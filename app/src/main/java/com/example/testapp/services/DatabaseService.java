package com.example.testapp.services;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testapp.models.Cart;
import com.example.testapp.models.Food;
import com.example.testapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabaseService {

    private static final String TAG = "DatabaseService";

    public interface DatabaseCallback<T> {
        void onCompleted(T object);
        void onFailed(Exception e);
    }

    private static DatabaseService instance;
    private final DatabaseReference databaseReference;

    private DatabaseService() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    private void writeData(String path, Object data, @Nullable DatabaseCallback callback) {
        databaseReference.child(path).setValue(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (callback == null) return;
                callback.onCompleted(null);
            } else {
                if (callback == null) return;
                callback.onFailed(task.getException());
            }
        });
    }

    private DatabaseReference readData(String path) {
        return databaseReference.child(path);
    }

    public void createNewUser(User user, DatabaseCallback<Object> callback) {
        writeData("users/" + user.getUid(), user, callback);
    }

    public void createNewFood(Food food, DatabaseCallback<Object> callback) {
        writeData("foods/" + food.getId(), food, callback);
    }

    public String getNewFoodId() {
        return databaseReference.child("foods").push().getKey();
    }


    public void getFood(String foodId, DatabaseCallback<Food> callback) {
        readData("foods/" + foodId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    if (callback != null) {
                        callback.onFailed(task.getException());
                    }
                    return;
                }
                Food food = task.getResult().getValue(Food.class);
                if (callback != null) {
                    callback.onCompleted(food);
                }
            }
        });
    }

    public void getFoods(DatabaseCallback<List<Food>> callback) {
        readData("foods").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e(TAG, "Error getting data", task.getException());
                    if (callback != null) {
                        callback.onFailed(task.getException());
                    }
                    return;
                }
                List<Food> foods = new ArrayList<>();
                task.getResult().getChildren().forEach(dataSnapshot -> {
                    Food food = dataSnapshot.getValue(Food.class);
                    Log.d(TAG, "Got food: " + food);
                    foods.add(food);
                });

                if (callback != null) {
                    callback.onCompleted(foods);
                }
            }
        });
    }


    public String getNewCartId() {
        return databaseReference.child("carts").push().getKey();
    }


    public void createNewCart(Cart cart, DatabaseCallback<Object> callback) {
        writeData("carts/" + cart.getId(), cart, callback);
    }



}
