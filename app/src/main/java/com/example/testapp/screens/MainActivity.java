package com.example.testapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testapp.R;
import com.example.testapp.services.AuthenticationService;
import com.example.testapp.utils.SharedPreferencesUtil;

/// Main activity for the app
/// This activity is the main activity that is shown when the user is signed in
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private AuthenticationService authenticationService;
    private Button btnLogout, btnAddFood, btnAddCart;

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

        /// get the instance of the authentication service
        authenticationService = AuthenticationService.getInstance();

        /// Check if user is signed in or not and redirect to LandingActivity if not signed in
        if (!authenticationService.isUserSignedIn()) {
            Log.d(TAG, "User not signed in, redirecting to LandingActivity");
            Intent landingIntent = new Intent(MainActivity.this, LandingActivity.class);
            startActivity(landingIntent);
            finish();
        }

        /// get the views
        btnLogout = findViewById(R.id.btn_main_logout);
        btnAddFood = findViewById(R.id.btn_main_add_food);
        btnAddCart = findViewById(R.id.btn_main_add_cart);

        /// set the click listeners
        btnLogout.setOnClickListener(this);
        btnAddFood.setOnClickListener(this);
        btnAddCart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnLogout.getId()) {
            Log.d(TAG, "Sign out button clicked");
            /// Sign out the user using the authentication service
            authenticationService.signOut();
            /// Clear the user data from shared preferences
            SharedPreferencesUtil.signOutUser(MainActivity.this);

            Log.d(TAG, "User signed out, redirecting to LandingActivity");
            Intent landingIntent = new Intent(MainActivity.this, LandingActivity.class);
            /// Clear the back stack (clear history) and start the LandingActivity
            landingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(landingIntent);
            return;
        }
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
    }
}