package com.example.testapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.models.Food;

import java.util.List;

public class SelectedFoodsAdapter extends RecyclerView.Adapter<SelectedFoodsAdapter.ViewHolder> {

    private final List<Food> selectedFoods;

    public SelectedFoodsAdapter(List<Food> selectedFoods) {
        this.selectedFoods = selectedFoods;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food = selectedFoods.get(position);
        holder.foodNameTextView.setText(food.getName());
    }

    @Override
    public int getItemCount() {
        return selectedFoods.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView foodNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.food_name_text_view);
        }
    }
}