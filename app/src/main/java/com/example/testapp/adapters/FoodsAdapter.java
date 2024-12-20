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
import com.example.testapp.utils.ImageUtil;

import java.util.List;


/// Adapter for the foods recycler view
/// @see RecyclerView
/// @see Food
/// @see R.layout#item_selected_food
public class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.ViewHolder> {

    /// list of foods
    /// @see Food
    private final List<Food> foodList;

    public FoodsAdapter(List<Food> foodList) {
        this.foodList = foodList;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_food, parent, false);
        return new ViewHolder(view);
    }

    /// bind the view holder with the data
    /// @param holder the view holder
    /// @param position the position of the item in the list
    /// @see ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food = foodList.get(position);
        if (food == null) return;

        holder.foodNameTextView.setText(food.getName());
        holder.foodImageView.setImageBitmap(ImageUtil.convertFrom64base(food.getImageBase64()));
    }

    /// get the number of items in the list
    /// @return the number of items in the list
    @Override
    public int getItemCount() {
        return foodList.size();
    }

    /// View holder for the foods adapter
    /// @see RecyclerView.ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView foodNameTextView;
        public final ImageView foodImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.food_name_text_view);
            foodImageView = itemView.findViewById(R.id.food_image_view);
        }
    }
}