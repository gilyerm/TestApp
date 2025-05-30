package com.example.testapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testapp.R;
import com.example.testapp.services.AuthenticationService;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final int SPLASH_DISPLAY_TIME = 3000; // 3 seconds

    AuthenticationService authenticationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /// get the instance of the authentication service
        authenticationService = AuthenticationService.getInstance();


        Thread splashThread = new Thread(() -> {
            try {
                Thread.sleep(SPLASH_DISPLAY_TIME); // SPLASH_DISPLAY_TIME delay
            } catch (InterruptedException ignored) {
            } finally {
                // go to the correct activity after the delay
                Intent intent;
                /// Check if user is signed in or not and redirect to LandingActivity if not signed in
                if (authenticationService.isUserSignedIn()) {
                    Log.d(TAG, "User signed in, redirecting to MainActivity");
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    Log.d(TAG, "User not signed in, redirecting to LandingActivity");
                    intent = new Intent(SplashActivity.this, LandingActivity.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        splashThread.start();
    }
}