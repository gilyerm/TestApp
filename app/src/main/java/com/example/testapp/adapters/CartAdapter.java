package com.example.testapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.models.Cart;

/// Adapter for the carts recycler view.
/// Each row shows the cart title and how many items it has.
/// @see BaseAdapter
/// @see Cart
/// @see R.layout#row_cart
public class CartAdapter extends BaseAdapter<Cart, CartAdapter.ViewHolder> {

    /// Callback for clicks on a cart row.
    public interface CartClickListener {
        /// @param cart the clicked cart
        void onCartClick(Cart cart);
    }

    /// receives row clicks; may be null
    @Nullable
    private final CartClickListener listener;

    /// @param listener receives row clicks; may be null
    public CartAdapter(@Nullable CartClickListener listener) {
        this.listener = listener;
    }

    /// Inflate a cart row.
    /// @param parent the RecyclerView
    /// @param viewType unused, there is one row type
    /// @return a new view holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart, parent, false);
        return new ViewHolder(view);
    }

    /// Fill a row with the cart at this position and set its click listener.
    /// @param holder the row to fill
    /// @param position the cart's position in the list
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cart = getItem(position);

        holder.cartName.setText(cart.getTitle());
        holder.cartDescription.setText("Total items: " + cart.getFoods().size());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCartClick(cart);
                }
            }
        });
    }

    /// Holds the views of one cart row.
    /// @see R.layout#row_cart
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView cartName;
        final TextView cartDescription;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartName = itemView.findViewById(R.id.tv_item_cart_name);
            cartDescription = itemView.findViewById(R.id.tv_item_cart_description);
        }
    }
}
