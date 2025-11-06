package com.example.testapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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
import com.example.testapp.utils.SharedPreferencesUtil;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

public class MyCartsActivity extends BaseActivity {

    private static final String TAG = "MyCartsActivity";

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private ProgressBar progressBar;
    private LinearLayout emptyStateLayout;
    private ExtendedFloatingActionButton fabAddCart;

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

        recyclerView = findViewById(R.id.recycler_view_carts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.my_cart_progress_bar);
        emptyStateLayout = findViewById(R.id.empty_state);
        fabAddCart = findViewById(R.id.fab_add_cart);

        // Set up FAB click listener
        fabAddCart.setOnClickListener(v -> {
            Intent intent = new Intent(MyCartsActivity.this, AddCartActivity.class);
            startActivity(intent);
        });

        // Initialize cart list and adapter
        cartAdapter = new CartAdapter(new CartAdapter.CartClickListener() {
            @Override
            public void onCartClick(Cart cart) {
                Intent intent = new Intent(MyCartsActivity.this, CartDetailActivity.class);
                intent.putExtra("cart_id", cart.getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(cartAdapter);

        // Load user carts
        loadCarts();
    }

    private void loadCarts() {
        progressBar.setVisibility(View.VISIBLE);
        String currentUserUid = SharedPreferencesUtil.getUser(this).getUid();
        databaseService.getUserCartList(currentUserUid, new DatabaseService.DatabaseCallback<>() {
            @Override
            public void onCompleted(List<Cart> carts) {
                Log.d(TAG, "onCompleted: " + carts);
                cartAdapter.setCartList(carts);
                progressBar.setVisibility(View.GONE);
                updateEmptyState();
            }

            @Override
            public void onFailed(Exception e) {
                progressBar.setVisibility(View.GONE);
                updateEmptyState();
            }
        });
    }

    private void updateEmptyState() {
        if (cartAdapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);
        }
    }
}