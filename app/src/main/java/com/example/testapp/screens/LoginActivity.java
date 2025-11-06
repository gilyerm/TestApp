package com.example.testapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testapp.R;
import com.example.testapp.models.User;
import com.example.testapp.services.DatabaseService;
import com.example.testapp.utils.SharedPreferencesUtil;
import com.example.testapp.utils.Validator;

/// Activity for logging in the user
/// This activity is used to log in the user
/// It contains fields for the user to enter their email and password
/// It also contains a button to log in the user
/// When the user is logged in, they are redirected to the main activity
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        /// set the layout for the activity
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /// get the views
        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login_login);
        tvRegister = findViewById(R.id.tv_login_register);

        /// set the click listener
        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnLogin.getId()) {
            Log.d(TAG, "onClick: Login button clicked");

            /// get the email and password entered by the user
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            /// log the email and password
            Log.d(TAG, "onClick: Email: " + email);
            Log.d(TAG, "onClick: Password: " + password);

            Log.d(TAG, "onClick: Validating input...");
            /// Validate input
            if (!checkInput(email, password)) {
                /// stop if input is invalid
                return;
            }

            Log.d(TAG, "onClick: Logging in user...");

            /// Login user
            loginUser(email, password);
        } else if (v.getId() == tvRegister.getId()) {
            /// Navigate to Register Activity
            Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(registerIntent);
        }
    }

    /// Method to check if the input is valid
    /// It checks if the email and password are valid
    /// @see Validator#isEmailValid(String)
    /// @see Validator#isPasswordValid(String)
    private boolean checkInput(String email, String password) {
        if (!Validator.isEmailValid(email)) {
            Log.e(TAG, "checkInput: Invalid email address");
            /// show error message to user
            etEmail.setError("Invalid email address");
            /// set focus to email field
            etEmail.requestFocus();
            return false;
        }

        if (!Validator.isPasswordValid(password)) {
            Log.e(TAG, "checkInput: Invalid password");
            /// show error message to user
            etPassword.setError("Password must be at least 6 characters long");
            /// set focus to password field
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void loginUser(String email, String password) {
        databaseService.getUserByEmailAndPassword(email, password, new DatabaseService.DatabaseCallback<User>() {
            /// Callback method called when the operation is completed
            /// @param user the user object that is logged in
            @Override
            public void onCompleted(User user) {
                Log.d(TAG, "onCompleted: User logged in: " + user.toString());
                /// save the user data to shared preferences
                SharedPreferencesUtil.saveUser(LoginActivity.this, user);
                /// Redirect to main activity and clear back stack to prevent user from going back to login screen
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                /// Clear the back stack (clear history) and start the MainActivity
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to retrieve user data", e);
                /// Show error message to user
                etPassword.setError("Invalid email or password");
                etPassword.requestFocus();
                /// Sign out the user if failed to retrieve user data
                /// This is to prevent the user from being logged in again
                SharedPreferencesUtil.signOutUser(LoginActivity.this);
            }
        });
    }
}