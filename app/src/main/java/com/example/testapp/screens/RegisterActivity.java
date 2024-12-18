package com.example.testapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testapp.R;
import com.example.testapp.models.User;
import com.example.testapp.services.AuthenticationService;
import com.example.testapp.services.DatabaseService;
import com.example.testapp.utils.SharedPreferencesUtil;
import com.example.testapp.utils.Validator;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";

    private EditText etUsername, etEmail, etPassword;
    private Button btnRegister;

    private AuthenticationService authenticationService;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();

        etUsername = findViewById(R.id.et_register_username);
        etEmail = findViewById(R.id.et_register_email);
        etPassword = findViewById(R.id.et_register_password);
        btnRegister = findViewById(R.id.btn_register_register);

        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnRegister.getId()) {
            Log.d(TAG, "onClick: Register button clicked");

            String username = etUsername.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            Log.d(TAG, "onClick: Username: " + username);
            Log.d(TAG, "onClick: Email: " + email);
            Log.d(TAG, "onClick: Password: " + password);

            // Validate input
            Log.d(TAG, "onClick: Validating input...");
            if (!checkInput(username, email, password)) {
                return;
            }

            Log.d(TAG, "onClick: Registering user...");

            // Register user
            registerUser(username, email, password);
            return;
        }
    }

    private boolean checkInput(String username, String email, String password) {
        if (!Validator.isUsernameValid(username)) {
            Log.e(TAG, "checkInput: Username must be at least 3 characters long");
            etUsername.setError("Username must be at least 3 characters long");
            etUsername.requestFocus();
            return false;
        }

        if (!Validator.isEmailValid(email)) {
            Log.e(TAG, "checkInput: Invalid email address");
            etEmail.setError("Invalid email address");
            etEmail.requestFocus();
            return false;
        }

        if (!Validator.isPasswordValid(password)) {
            Log.e(TAG, "checkInput: Password must be at least 8 characters long");
            etPassword.setError("Password must be at least 8 characters long");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void registerUser(String username, String email, String password) {
        // Register user
        Log.d(TAG, "registerUser: Registering user...");

        authenticationService.signUp(email, password, new AuthenticationService.AuthCallback() {

            @Override
            public void onCompleted(Object object) {
                Log.d(TAG, "onCompleted: User registered successfully");
                String uid = authenticationService.getCurrentUser().getUid();
                User user = new User(uid, username, email);
                databaseService.createNewUser(user, new DatabaseService.DatabaseCallback<>() {
                    @Override
                    public void onCompleted(Object object) {
                        Log.d(TAG, "onCompleted: User registered successfully");
                        SharedPreferencesUtil.saveUser(RegisterActivity.this, user);
                        // Redirect to MainActivity and clear back stack to prevent user from going back to register screen
                        Log.d(TAG, "onCompleted: Redirecting to MainActivity");
                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finish();
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.e(TAG, "onFailed: Failed to register user", e);
                    }
                });
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to register user", e);

            }
        });


    }
}