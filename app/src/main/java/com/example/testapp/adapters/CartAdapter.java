package com.example.testapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.models.Cart;
import com.example.testapp.screens.CartDetailActivity;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<Cart> cartList;
    private Context context;

    public CartAdapter(List<Cart> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        holder.cartName.setText(cart.getTitle());
        holder.cartDescription.setText("Total items: " + cart.getFoods().size());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CartDetailActivity.class);
            intent.putExtra("cart_id", cart.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView cartName;
        TextView cartDescription;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cartName = itemView.findViewById(R.id.tv_item_cart_name);
            cartDescription = itemView.findViewById(R.id.tv_item_cart_description);
        }
    }
}