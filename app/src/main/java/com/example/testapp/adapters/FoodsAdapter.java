package com.example.testapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.models.Food;
import com.example.testapp.models.Identifiable;
import com.example.testapp.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Locale;

/// Adapter for the foods recycler view.
/// Foods with the same ID are grouped into one row that shows the image, name, quantity, unit price and line total.
/// Takes foods as a flat list (each food repeated by its quantity, the way a `Cart` stores them).
/// @see BaseAdapter
/// @see Food
/// @see R.layout#row_food
public class FoodsAdapter extends BaseAdapter<FoodsAdapter.FoodRow, FoodsAdapter.ViewHolder> {

    /// One row: a food and how many times it appears.
    public static class FoodRow implements Identifiable {
        public final Food food;
        public final int quantity;

        /// @param food the food shown in the row
        /// @param quantity how many times the food appears
        public FoodRow(@NonNull Food food, int quantity) {
            this.food = food;
            this.quantity = quantity;
        }

        /// @return the food's ID, so rows are matched by food
        @Override
        public String getId() {
            return food.getId();
        }
    }

    /// Replace all rows, grouping foods with the same ID.
    /// @param foods flat list of foods; null entries are skipped
    public void setFoods(@NonNull List<Food> foods) {
        List<FoodRow> rows = new ArrayList<>();
        for (Food f : foods) {
            if (f == null) continue;
            int index = -1;
            for (int i = 0; i < rows.size(); i++) {
                if (Objects.equals(rows.get(i).getId(), f.getId())) { index = i; break; }
            }
            if (index == -1) rows.add(new FoodRow(f, 1));
            else rows.set(index, new FoodRow(rows.get(index).food, rows.get(index).quantity + 1));
        }
        setItems(rows);
    }

    /// Inflate a food row.
    /// @param parent the RecyclerView
    /// @param viewType unused, there is one row type
    /// @return a new view holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_food, parent, false);
        return new ViewHolder(view);
    }

    /// Fill a row with the food and quantity at this position.
    /// @param holder the row to fill
    /// @param position the row's position in the list
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodRow row = getItem(position);
        Food food = row.food;

        holder.foodNameTextView.setText(food.getName());
        holder.foodImageView.setImageBitmap(ImageUtil.convertFrom64base(food.getImageBase64()));
        holder.foodQuantityTextView.setText(String.valueOf(row.quantity));
        holder.foodSinglePriceTextView.setText("Single Price: " + String.format(Locale.getDefault(), "$%.2f", food.getPrice()));
        holder.foodPriceTextView.setText(String.format(Locale.getDefault(), "$%.2f", food.getPrice() * row.quantity));
    }

    /// Holds the views of one food row.
    /// @see R.layout#row_food
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView foodNameTextView;
        final ImageView foodImageView;
        final TextView foodQuantityTextView;
        final TextView foodSinglePriceTextView;
        final TextView foodPriceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.food_name_text_view);
            foodImageView = itemView.findViewById(R.id.food_image_view);
            foodQuantityTextView = itemView.findViewById(R.id.food_quantity_text_view);
            foodSinglePriceTextView = itemView.findViewById(R.id.food_single_price_text_view);
            foodPriceTextView = itemView.findViewById(R.id.food_price_text_view);

            foodSinglePriceTextView.setVisibility(View.VISIBLE); // Show the single price
        }
    }
}
