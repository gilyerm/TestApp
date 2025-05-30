package com.example.testapp.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.adapters.FoodsAdapter;
import com.example.testapp.models.Cart;
import com.example.testapp.services.DatabaseService;

public class CartDetailActivity extends BaseActivity {

    private TextView tvCartTitle;
    private TextView tvCartTotalAmount, tvCartTotalPrice;
    private RecyclerView rvCartItems;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvCartTitle = findViewById(R.id.tv_cart_title);
        tvCartTotalAmount = findViewById(R.id.tv_cart_total_items);
        tvCartTotalPrice = findViewById(R.id.tv_cart_total_price);
        rvCartItems = findViewById(R.id.rv_cart_items);
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.cart_detail_progress_bar);

        String cart_id = getIntent().getStringExtra("cart_id");
        if (cart_id == null) {
            Toast.makeText(this, "Cart ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        rvCartItems.setVisibility(View.GONE);
        databaseService.getCart(cart_id, new DatabaseService.DatabaseCallback<>() {
            @Override
            public void onCompleted(Cart cart) {
                progressBar.setVisibility(View.GONE);
                rvCartItems.setVisibility(View.VISIBLE);
                if (cart == null) {
                    Toast.makeText(CartDetailActivity.this, "Cart not found", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                setCartView(cart);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }

    private void setCartView(Cart cart) {
        tvCartTitle.setText(cart.getTitle());
        tvCartTotalAmount.setText(cart.getFoods().size() + "");
        tvCartTotalPrice.setText(String.format("₪%.2f", cart.getTotalPrice()));
        FoodsAdapter foodsAdapter = new FoodsAdapter();
        foodsAdapter.addFoods(cart.getFoods());
        rvCartItems.setAdapter(foodsAdapter);
    }
}