package com.example.testapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.adapters.CartAdapter;
import com.example.testapp.models.Cart;
import com.example.testapp.services.DatabaseService;

import java.util.List;

public class AllCartsActivity extends BaseActivity {

    private static final String TAG = "AllCartsActivity";
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_carts);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupRecyclerView();
        loadAllCarts();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_carts);
        cartAdapter = new CartAdapter(cart -> {
            Intent intent = new Intent(AllCartsActivity.this, CartDetailActivity.class);
            intent.putExtra("cart_id", cart.getId());
            startActivity(intent);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);
    }

    private void loadAllCarts() {
        databaseService.getCartList(new DatabaseService.DatabaseCallback<List<Cart>>() {
            @Override
            public void onCompleted(List<Cart> carts) {
                Log.d(TAG, "Successfully loaded " + carts.size() + " carts");
                cartAdapter.setCartList(carts);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Failed to load carts", e);
                Toast.makeText(AllCartsActivity.this, "Failed to load carts", Toast.LENGTH_SHORT).show();
            }
        });
    }
}