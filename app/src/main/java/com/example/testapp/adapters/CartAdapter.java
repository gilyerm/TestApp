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

import java.util.ArrayList;
import java.util.List;

/// Adapter for the cart recycler view
/// @see RecyclerView
/// @see Cart
/// @see R.layout#item_cart
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface CartClickListener {
        void onCartClick(Cart cart);
    }

    private final List<Cart> cartList;
    private final CartClickListener listener;

    public CartAdapter(CartClickListener listener) {
        this.cartList = new ArrayList<>();
        this.listener = listener;
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
            if (listener != null) {
                listener.onCartClick(cart);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }


    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> carts) {
        this.cartList.clear();
        this.cartList.addAll(carts);
        notifyDataSetChanged();
    }

    public void addCart(Cart cart) {
        this.cartList.add(cart);
        notifyItemInserted(cartList.size() - 1);
    }

    public void removeCart(int position) {
        if (position < 0 || position >= cartList.size()) {
            return;
        }
        cartList.remove(position);
        notifyItemRemoved(position);
    }

    public void clearCarts() {
        this.cartList.clear();
        notifyDataSetChanged();
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