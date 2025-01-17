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
import com.example.testapp.adapters.FoodsAdapter;
import com.example.testapp.models.Cart;
import com.example.testapp.models.Food;
import com.example.testapp.services.AuthenticationService;
import com.example.testapp.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class AddCartActivity extends AppCompatActivity implements View.OnClickListener {

    /// tag for logging
    private static final String TAG = "AddCartActivity";

    private Spinner foodSpinner;
    private Button addButton, createCartButton;
    private FoodSpinnerAdapter foodSpinnerAdapter;
    private FoodsAdapter foodsAdapter;
    private List<Food> selectedFoods;

    private DatabaseService databaseService;
    private AuthenticationService authenticationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        /// set the layout for the activity
        setContentView(R.layout.activity_add_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /// get the instance of the database service
        databaseService = DatabaseService.getInstance();
        authenticationService = AuthenticationService.getInstance();

        /// get the views
        addButton = findViewById(R.id.btn_add_cart);
        createCartButton = findViewById(R.id.btn_create_add_cart);

        /// Adapter for the food recycler view
        /// @see ArrayAdapter
        /// @see Food
        RecyclerView selectedFoodsRecyclerView = findViewById(R.id.recycler_view_selected_foods);
        selectedFoods = new ArrayList<>();
        foodsAdapter = new FoodsAdapter(selectedFoods);
        selectedFoodsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedFoodsRecyclerView.setAdapter(foodsAdapter);

        /// Adapter for the food spinner
        /// @see FoodSpinnerAdapter
        /// @see Food
        foodSpinner = findViewById(R.id.spinner_food_add_cart);
        List<Food> allFoods = new ArrayList<>();
        foodSpinnerAdapter = new FoodSpinnerAdapter(AddCartActivity.this, android.R.layout.simple_spinner_item, allFoods);
        foodSpinner.setAdapter(foodSpinnerAdapter);

        /// get all the foods from the database
        databaseService.getFoodList(new DatabaseService.DatabaseCallback<>() {
            @Override
            public void onCompleted(List<Food> object) {
                Log.d(TAG, "onCompleted: " + object);
                allFoods.clear();
                allFoods.addAll(object);
                /// notify the adapter that the data has changed
                /// this specifies that the data has changed
                /// and the adapter should update the view
                /// @see FoodSpinnerAdapter#notifyDataSetChanged()
                foodSpinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: ", e);
            }
        });

        /// set the click listeners
        addButton.setOnClickListener(this);
        createCartButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == addButton.getId()) {
            /// get the selected food from the spinner
            Food selectedFood = (Food) foodSpinner.getSelectedItem();
            if (selectedFood == null) return;
            /// add the selected food to the list of selected foods
            selectedFoods.add(selectedFood);
            /// notify the adapter that the data has changed
            /// this specifies that the item at selectedFoods.size() - 1 has been inserted
            /// and the adapter should update the view
            /// @see FoodsAdapter#notifyItemInserted(int)
            foodsAdapter.notifyItemInserted(selectedFoods.size() - 1);
            return;
        }
        if (v.getId() == createCartButton.getId()) {
            /// generate a new id for the new cart
            String cartId = databaseService.generateCartId();
            String userId = authenticationService.getCurrentUserId();
            /// create a new cart
            Cart cart = new Cart(cartId, selectedFoods, userId);
            /// save the cart to the database and get the result in the callback
            databaseService.createNewCart(cart, new DatabaseService.DatabaseCallback<>() {
                @Override
                public void onCompleted(Void object) {
                    Log.d(TAG, "Cart created successfully");
                    /// clear the selected foods
                    int itemCount = selectedFoods.size();
                    selectedFoods.clear();
                    /// notify the adapter that the data has changed
                    /// this specifies that the items from 0 to itemCount have been removed
                    /// and the adapter should update the view
                    /// @see FoodsAdapter#notifyItemRangeRemoved(int, int)
                    /// @see itemCount
                    foodsAdapter.notifyItemRangeRemoved(0, itemCount);
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