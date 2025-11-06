package com.example.testapp.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import android.widget.ArrayAdapter;
import com.example.testapp.adapters.FoodsAdapter;
import com.example.testapp.models.Cart;
import com.example.testapp.models.Food;
import com.example.testapp.services.DatabaseService;
import com.example.testapp.utils.SharedPreferencesUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AddCartActivity extends BaseActivity implements View.OnClickListener {

    /// tag for logging
    private static final String TAG = "AddCartActivity";

    private AutoCompleteTextView foodSelector;
    private Button createCartButton;
    private FloatingActionButton addButton;
    private EditText etCartName;
    private ArrayAdapter<Food> foodDropdownAdapter;
    private FoodsAdapter foodsAdapter;
    private Food _selectedFood;
    private List<Food> allFoods;

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


        /// get the views
        addButton = findViewById(R.id.btn_add_cart);
        createCartButton = findViewById(R.id.btn_create_add_cart);
        etCartName = findViewById(R.id.edit_text_cart_name);
        foodSelector = findViewById(R.id.autocomplete_food_selector);

        /// Adapter for the food recycler view
        RecyclerView selectedFoodsRecyclerView = findViewById(R.id.recycler_view_selected_foods);
        foodsAdapter = new FoodsAdapter();
        selectedFoodsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedFoodsRecyclerView.setAdapter(foodsAdapter);

        /// Initialize food list and adapter
        allFoods = new ArrayList<>();
        foodDropdownAdapter = new ArrayAdapter<Food>(this, android.R.layout.simple_dropdown_item_1line, allFoods) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Food food = getItem(position);
                if (food != null && view instanceof TextView) {
                    ((TextView) view).setText(food.getName() + " - $" + String.format("%.2f", food.getPrice()));
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                Food food = getItem(position);
                if (food != null && view instanceof TextView) {
                    ((TextView) view).setText(food.getName() + " - $" + String.format("%.2f", food.getPrice()));
                }
                return view;
            }
        };
        foodSelector.setAdapter(foodDropdownAdapter);
        
        /// Set up food selection listener
        foodSelector.setOnItemClickListener((parent, view, position, id) -> {
            Food selectedFood = (Food) parent.getItemAtPosition(position);
            if (selectedFood != null) {
                Log.d(TAG, "Selected food: " + selectedFood.getName());
                _selectedFood = selectedFood;
                foodSelector.setText(selectedFood.getName(), false);
            }
        });

        /// Load foods from database
        loadFoodsFromDatabase();

        /// set the click listeners
        addButton.setOnClickListener(this);
        createCartButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == addButton.getId()) {
            addFoodToCart();
            return;
        }
        if (v.getId() == createCartButton.getId()) {
            addCartToDatabase();
            return;
        }
    }

    private void addFoodToCart() {
        /// get the selected food from the spinner
        if (_selectedFood == null) return;
        /// add the selected food to the list of selected foods
        foodsAdapter.addFood(_selectedFood);
    }

    private void addCartToDatabase() {
        String cartName = etCartName.getText().toString().trim();
        List<Food> selectedFoods = foodsAdapter.getFoods();
        if (!isValid(cartName, selectedFoods)) {
            return;
        }

        /// generate a new id for the new cart
        String cartId = databaseService.generateCartId();
        String userId = SharedPreferencesUtil.getUserId(AddCartActivity.this);
        /// create a new cart
        Cart cart = new Cart(cartId, cartName,  selectedFoods, userId);
        /// save the cart to the database and get the result in the callback
        databaseService.createNewCart(cart, new DatabaseService.DatabaseCallback<>() {
            @Override
            public void onCompleted(Void object) {
                Log.d(TAG, "Cart created successfully");
                /// clear the selected foods
                int itemCount = selectedFoods.size();
                selectedFoods.clear();
                etCartName.setText("");
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


    private boolean isValid(String cartName, List<Food> selectedFoods) {
        if (cartName.isEmpty()) {
            etCartName.setError("Cart name cannot be empty");
            etCartName.requestFocus();
            return false;
        }
        if (selectedFoods.isEmpty()) {
            Toast.makeText(this, "Please add some food to the cart", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void loadFoodsFromDatabase() {
        databaseService.getFoodList(new DatabaseService.DatabaseCallback<List<Food>>() {
            @Override
            public void onCompleted(List<Food> foods) {
                Log.d(TAG, "Successfully loaded " + foods.size() + " foods");
                allFoods.clear();
                allFoods.addAll(foods);
                foodDropdownAdapter.notifyDataSetChanged();
                
                // Set first food as selected if available
                if (!foods.isEmpty()) {
                    _selectedFood = foods.get(0);
                    foodSelector.setText(foods.get(0).getName(), false);
                    Log.d(TAG, "Default food selected: " + _selectedFood.getName());
                }
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Failed to load foods", e);
                Toast.makeText(AddCartActivity.this, "Failed to load food items", Toast.LENGTH_SHORT).show();
            }
        });
    }
}