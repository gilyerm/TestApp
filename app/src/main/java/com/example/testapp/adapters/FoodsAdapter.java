package com.example.testapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.models.Food;
import com.example.testapp.utils.ImageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/// Adapter for the foods recycler view
/// @see RecyclerView
/// @see Food
/// @see R.layout#item_food
public class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.ViewHolder> {

    private static class ItemCount {
        Food food;
        int quantity;
    }

    /// list of foods
    /// @see Food
    private final List<ItemCount> foodItemCountList;

    public FoodsAdapter() {
        foodItemCountList = new ArrayList<>();
    }

    public void addFoods(@NonNull List<Food> foods) {
        for (Food food : foods) {
            addFood(food);
        }
    }

    public void addFood(@Nullable Food f) {
        if (f == null) return;
        for (int i = 0; i < foodItemCountList.size(); i++) {
            ItemCount itemCount = foodItemCountList.get(i);
            if (itemCount.food.getId().equals(f.getId())) {
                itemCount.quantity++;
                notifyItemChanged(i);
                return;
            }
        }
        foodItemCountList.add(new ItemCount() {{
            this.food = new Food(f);
            this.quantity = 1;
        }});
        /// notify the adapter that the data has changed
        /// this specifies that the item at selectedFoods.size() - 1 has been inserted
        /// and the adapter should update the view
        /// @see FoodsAdapter#notifyItemInserted(int)
        notifyItemInserted(foodItemCountList.size() - 1);
    }

    public List<Food> getFoods() {
        List<Food> foods = new ArrayList<>();
        for (ItemCount itemCount : foodItemCountList) {
            for (int i = 0; i < itemCount.quantity; i++) {
                foods.add(itemCount.food);
            }
        }
        return foods;
    }

    /// create a view holder for the adapter
    /// @param parent the parent view group
    /// @param viewType the type of the view
    /// @return the view holder
    /// @see ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /// inflate the item_selected_food layout
        /// @see R.layout.item_selected_food
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new ViewHolder(view);
    }

    /// bind the view holder with the data
    /// @param holder the view holder
    /// @param position the position of the item in the list
    /// @see ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food = foodItemCountList.get(position).food;
        if (food == null) return;

        holder.foodNameTextView.setText(food.getName());
        holder.foodImageView.setImageBitmap(ImageUtil.convertFrom64base(food.getImageBase64()));
        holder.foodQuantityTextView.setText(String.valueOf(foodItemCountList.get(position).quantity));

        holder.foodSinglePriceTextView.setText("Single Price: " + String.format("$%.2f", food.getPrice()));
        holder.foodPriceTextView.setText(String.format("$%.2f", food.getPrice() * foodItemCountList.get(position).quantity));
    }

    /// get the number of items in the list
    /// @return the number of items in the list
    @Override
    public int getItemCount() {
        return foodItemCountList.size();
    }

    /// View holder for the foods adapter
    /// @see RecyclerView.ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView foodNameTextView;
        public final ImageView foodImageView;
        public final TextView foodQuantityTextView;
        public final TextView foodSinglePriceTextView;
        public final TextView foodPriceTextView;

        public ViewHolder(View itemView) {
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