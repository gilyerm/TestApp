package com.example.testapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.testapp.core.NavigationHost;
import com.example.testapp.screens.splash.SplashFragment;
import androidx.core.view.OnApplyWindowInsetsListener;
import android.view.View;

/**
 * Main activity that serves as the entry point for the application.
 * It implements {@link NavigationHost} to handle fragment navigation.
 */
public class MainActivity extends AppCompatActivity implements NavigationHost {

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Check if savedInstanceState is null.
        // If it is null, this is the very first time the Activity is being launched,
        // so we load the initial fragment (SplashFragment).
        // If it is not null, Android is recreating the Activity (e.g., after a screen rotation),
        // and the system will automatically restore the previously active fragment.
        if (savedInstanceState == null) {
            navigateTo(new SplashFragment(), false);
        }
    }

    /**
     * Navigates to a specific fragment.
     *
     * @param fragment       The fragment to navigate to.
     * @param addToBackStack Whether this transaction should be added to the back stack.
     */
    @Override
    public void navigateTo(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    /**
     * Navigates back to the previous fragment on the back stack, or finishes the activity
     * if the back stack is empty.
     */
    @Override
    public void goBack() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    /**
     * Sets the title of the toolbar.
     *
     * @param title The title to be displayed.
     */
    @Override
    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    /**
     * Shows or hides the back button in the toolbar.
     *
     * @param show True to show the back button, false to hide it.
     */
    @Override
    public void showBackButton(boolean show) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(show);
        actionBar.setDisplayShowHomeEnabled(show);
    }

    /**
     * Called when an item in the options menu is selected.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            goBack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
