package com.example.testapp.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.adapters.CartAdapter;
import com.example.testapp.models.Cart;
import com.example.testapp.services.AuthenticationService;
import com.example.testapp.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class MyCartsActivity extends AppCompatActivity {

    private static final String TAG = "MyCartsActivity";

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<Cart> cartList;
    private ProgressBar progressBar;


    AuthenticationService authenticationService;
    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_carts);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();

        recyclerView = findViewById(R.id.recycler_view_carts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.my_cart_progress_bar);

        // Initialize cart list and adapter
        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(cartList, this);
        recyclerView.setAdapter(cartAdapter);

        // Load user carts
        loadCarts();
    }

    private void loadCarts() {
        progressBar.setVisibility(View.VISIBLE);
        databaseService.getUserCartList(authenticationService.getCurrentUserId(), new DatabaseService.DatabaseCallback<List<Cart>>() {
            @Override
            public void onCompleted(List<Cart> carts) {
                Log.d(TAG, "onCompleted: " + carts);
                cartList.clear();
                cartList.addAll(carts);
                cartAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }
}