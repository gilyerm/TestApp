package com.example.testapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testapp.R;
import com.example.testapp.models.User;
import com.example.testapp.utils.SharedPreferencesUtil;

/// Main activity for the app
/// This activity is the main activity that is shown when the user is signed in
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private Button btnAddFood, btnAddCart, btnToAdmin, btnUserProfile, btnMyCarts;

    /// the current user instance
    /// NOTE:
    /// THIS IS THE INSTANCE WHEN THE USER LOGS IN
    /// THIS IS NOT THE REALTIME USER INSTANCE
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        /// set the layout for the activity
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /// get the user data from shared preferences
        user = SharedPreferencesUtil.getUser(MainActivity.this);
        Log.d(TAG, "User: " + user);

        /// get the views
        btnAddFood = findViewById(R.id.btn_main_add_food);
        btnAddCart = findViewById(R.id.btn_main_add_cart);
        btnMyCarts = findViewById(R.id.btn_main_my_carts);
        btnToAdmin = findViewById(R.id.btn_main_to_admin);
        btnUserProfile = findViewById(R.id.btn_main_edit_profile);


        /// set the click listeners
        btnAddFood.setOnClickListener(this);
        btnAddCart.setOnClickListener(this);
        btnMyCarts.setOnClickListener(this);
        btnToAdmin.setOnClickListener(this);
        btnUserProfile.setOnClickListener(this);

        if (user != null && user.isAdmin()) {
            btnToAdmin.setVisibility(View.VISIBLE);
            findViewById(R.id.admin_card).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected boolean shouldShowBackButton() {
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnAddFood.getId()) {
            Log.d(TAG, "Add food button clicked");
            Intent addFoodIntent = new Intent(MainActivity.this, AddFoodActivity.class);
            startActivity(addFoodIntent);
            return;
        }
        if (v.getId() == btnAddCart.getId()) {
            Log.d(TAG, "Add cart button clicked");
            Intent addCartIntent = new Intent(MainActivity.this, AddCartActivity.class);
            startActivity(addCartIntent);
            return;
        }
        if (v.getId() == btnMyCarts.getId()) {
            Log.d(TAG, "My carts button clicked");
            Intent myCartsIntent = new Intent(MainActivity.this, MyCartsActivity.class);
            startActivity(myCartsIntent);
            return;
        }
        if (v.getId() == btnToAdmin.getId()) {
            Log.d(TAG, "To admin button clicked");
            Intent adminIntent = new Intent(MainActivity.this, AdminActivity.class);
            startActivity(adminIntent);
            return;
        }
        if (v.getId() == btnUserProfile.getId()) {
            Log.d(TAG, "Edit profile button clicked");
            Intent userProfileIntent = new Intent(MainActivity.this, UserProfileActivity.class);
            startActivity(userProfileIntent);
        }
    }
}