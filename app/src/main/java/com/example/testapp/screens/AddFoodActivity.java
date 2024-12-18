package com.example.testapp.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testapp.R;
import com.example.testapp.models.Food;
import com.example.testapp.services.DatabaseService;

public class AddFoodActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddFoodActivity";
    private EditText foodNameEditText, foodPriceEditText;
    private Button addFoodButton;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseService = DatabaseService.getInstance();

        foodNameEditText = findViewById(R.id.food_name);
        foodPriceEditText = findViewById(R.id.food_price);
        addFoodButton = findViewById(R.id.add_food_button);

        addFoodButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == addFoodButton.getId()) {
            String name = foodNameEditText.getText().toString();
            String priceText = foodPriceEditText.getText().toString();

            if (name.isEmpty() || priceText.isEmpty()) {
                Toast.makeText(AddFoodActivity.this, "Please enter both name and price", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceText);

            String id = databaseService.getNewFoodId();

            Log.d(TAG, "Adding food to database");
            Log.d(TAG, "Name: " + name);
            Log.d(TAG, "Price: " + price);
            Log.d(TAG, "New food ID: " + id);

            Food food = new Food(id, name, price);
            databaseService.createNewFood(food, new DatabaseService.DatabaseCallback<>() {
                @Override
                public void onCompleted(Object object) {
                    Log.d(TAG, "Food added successfully");
                    Toast.makeText(AddFoodActivity.this, "Food added successfully", Toast.LENGTH_SHORT).show();
                    // clear the input fields after adding the food for the next food
                    Log.d(TAG, "Clearing input fields");
                    foodNameEditText.setText("");
                    foodPriceEditText.setText("");
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(AddFoodActivity.this, "Failed to add food", Toast.LENGTH_SHORT).show();
                }
            });


        }
    }
}