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
import com.example.testapp.models.User;
import com.example.testapp.services.DatabaseService;
import com.example.testapp.utils.SharedPreferencesUtil;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "UserProfileActivity";

    private EditText etUserFirstName, etUserLastName, etUserEmail, etUserPhone;
    private Button btnUpdateProfile;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseService = DatabaseService.getInstance();

        // Initialize the EditText fields
        etUserFirstName = findViewById(R.id.et_user_first_name);
        etUserLastName = findViewById(R.id.et_user_last_name);
        etUserEmail = findViewById(R.id.et_user_email);
        etUserPhone = findViewById(R.id.et_user_phone);
        btnUpdateProfile = findViewById(R.id.btn_edit_profile);

        btnUpdateProfile.setOnClickListener(this);

        showUserProfile();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_edit_profile) {
            updateUserProfile();
            return;
        }
    }

    void showUserProfile() {
        // Get the user data from shared preferences
        User user = SharedPreferencesUtil.getUser(this);

        // Set the user data to the EditText fields
        etUserFirstName.setText(user.getFirstName());
        etUserLastName.setText(user.getLastName());
        etUserEmail.setText(user.getEmail());
        etUserPhone.setText(user.getPhone());
    }

    void updateUserProfile() {
        // Get the updated user data from the EditText fields
        String firstName = etUserFirstName.getText().toString();
        String lastName = etUserLastName.getText().toString();
        String phone = etUserPhone.getText().toString();

        // Update the user object
        User user = SharedPreferencesUtil.getUser(this);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);

        // Update the user data in the database
        databaseService.createNewUser(user, new DatabaseService.DatabaseCallback<>() {
            @Override
            public void onCompleted(Void object) {
                Log.d(TAG, "Profile updated successfully");
                // Save the updated user data to shared preferences
                SharedPreferencesUtil.saveUser(getApplicationContext(), user);
                Toast.makeText(UserProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Error updating profile", e);
                Toast.makeText(UserProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });


    }
}