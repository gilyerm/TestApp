package com.example.testapp.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.adapters.FoodSpinnerAdapter;
import com.example.testapp.adapters.SelectedFoodsAdapter;
import com.example.testapp.models.Cart;
import com.example.testapp.models.Food;
import com.example.testapp.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class AddCartActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddCartActivity";

    private Spinner foodSpinner;
    private Button addButton;
    private RecyclerView selectedFoodsRecyclerView;
    private Button createCartButton;
    private FoodSpinnerAdapter foodSpinnerAdapter;
    private SelectedFoodsAdapter selectedFoodsAdapter;
    private List<Food> selectedFoods;

    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseService = DatabaseService.getInstance();

        foodSpinner = findViewById(R.id.spinner_food_addcart);
        addButton = findViewById(R.id.btn_add_cart);
        createCartButton = findViewById(R.id.btn_create_add_cart);
        selectedFoodsRecyclerView = findViewById(R.id.recycler_view_selected_foods);

        selectedFoods = new ArrayList<>();
        selectedFoodsAdapter = new SelectedFoodsAdapter(selectedFoods);
        selectedFoodsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedFoodsRecyclerView.setAdapter(selectedFoodsAdapter);

        List<Food> allFoods = new ArrayList<>();
        foodSpinnerAdapter = new FoodSpinnerAdapter(AddCartActivity.this, android.R.layout.simple_spinner_item, R.layout.item_selected_food, allFoods);
        foodSpinner.setAdapter(foodSpinnerAdapter);
        databaseService.getFoods(new DatabaseService.DatabaseCallback<>() {
            @Override
            public void onCompleted(List<Food> object) {
                Log.d(TAG, "onCompleted: " + object);
                allFoods.clear();
                allFoods.addAll(object);
                foodSpinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: ", e);
            }
        });


        addButton.setOnClickListener(this);
        createCartButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == addButton.getId()) {
            Food selectedFood = (Food) foodSpinner.getSelectedItem();
            if (selectedFood == null) {
                return;
            }
            selectedFoods.add(selectedFood);
            selectedFoodsAdapter.notifyDataSetChanged();
            return;
        }
        if (v.getId() == createCartButton.getId()) {
            // create a new cart
            Cart cart = new Cart();
            // add all the selected foods to the cart
            cart.addFoods(selectedFoods);
            // save the cart to the database
            String cartId = databaseService.getNewCartId();
            cart.setId(cartId);
            databaseService.createNewCart(cart, new DatabaseService.DatabaseCallback<>() {
                @Override
                public void onCompleted(Object object) {
                    Log.d(TAG, "Cart created successfully");
                    // clear the selected foods list
                    selectedFoods.clear();
                    selectedFoodsAdapter.notifyDataSetChanged();
                    Toast.makeText(AddCartActivity.this, "Cart created successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailed(Exception e) {
                    Log.e(TAG, "Failed to create cart", e);
                    Toast.makeText(AddCartActivity.this, "Failed to create cart", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }
}