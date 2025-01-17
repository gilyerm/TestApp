package com.example.testapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testapp.R;

import java.util.List;
import java.util.Map;

/// Adapter for the image source dialog
public class ImageSourceAdapter extends ArrayAdapter<Map.Entry<String, Integer>> {

    private final LayoutInflater inflater;
    private final List<Map.Entry<String, Integer>> objects;

    public ImageSourceAdapter(@NonNull Context context, @NonNull List<Map.Entry<String, Integer>> objects) {
        super(context, R.layout.item_image_source, objects);
        this.inflater = LayoutInflater.from(context);
        this.objects = objects;
    }


    @Override
    public int getCount() {
        /// return the number of items in the list
        return objects.size();
    }

    @Nullable
    @Override
    public Map.Entry<String, Integer> getItem(int position) {
        /// return the item at the position
        return objects.get(position);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.item_image_source, parent, false);
        }

        /// get the views from the layout
        ImageView icon = convertView.findViewById(R.id.icon_dialog_item);
        TextView text = convertView.findViewById(R.id.text_dialog_item);

        /// get the item at the position
        Map.Entry<String, Integer> item = getItem(position);

        if (item != null) {
            /// set the text and icon
            text.setText(item.getKey());
            icon.setImageResource(item.getValue());
        }

        return convertView;
    }
}