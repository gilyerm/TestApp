package com.example.testapp.screens;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.adapters.FoodsAdapter;
import com.example.testapp.models.Food;
import com.example.testapp.services.DatabaseService;

import java.util.List;

public class FoodItemsActivity extends BaseActivity {

    private static final String TAG = "FoodItemsActivity";
    private RecyclerView recyclerView;
    private FoodsAdapter foodsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_items);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupRecyclerView();
        loadFoodItems();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view_foods);
        foodsAdapter = new FoodsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(foodsAdapter);
    }

    private void loadFoodItems() {
        databaseService.getFoodList(new DatabaseService.DatabaseCallback<List<Food>>() {
            @Override
            public void onCompleted(List<Food> foods) {
                Log.d(TAG, "Successfully loaded " + foods.size() + " food items");
                foodsAdapter.addFoods(foods);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Failed to load food items", e);
                Toast.makeText(FoodItemsActivity.this, "Failed to load food items", Toast.LENGTH_SHORT).show();
            }
        });
    }
}