package com.example.testapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.testapp.R;
import com.example.testapp.models.Food;

import java.util.List;

public class FoodSpinnerAdapter extends ArrayAdapter<Food> {

    private final LayoutInflater inflater;
    private final int resource;
    private final int dropDownResource;

    public FoodSpinnerAdapter(Context context, int resource, int dropDownResource, List<Food> objects) {
        super(context, resource, objects);
        this.inflater = LayoutInflater.from(context);
        this.resource = resource;
        this.dropDownResource = dropDownResource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
        }
        TextView textView = convertView.findViewById(android.R.id.text1);
        Food food = getItem(position);
        if (food != null) {
            textView.setText(food.getName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(dropDownResource, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.food_name_text_view);
        Food food = getItem(position);
        if (food != null) {
            textView.setText(food.getName());
        }
        return convertView;
    }
}