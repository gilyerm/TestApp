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
import com.example.testapp.models.ImageSourceOption;

import java.util.List;

/// Adapter for the image source dialog
public class ImageSourceAdapter extends ArrayAdapter<ImageSourceOption> {


    public interface OnImageSourceSelectedListener {
        void onImageSourceSelected(ImageSourceOption option);
    }

    private final LayoutInflater inflater;
    private final List<ImageSourceOption> objects;
    private OnImageSourceSelectedListener listener;

    public ImageSourceAdapter(@NonNull Context context, @NonNull List<ImageSourceOption> objects,
                              @NonNull OnImageSourceSelectedListener listener) {
        super(context, R.layout.item_image_source, objects);
        this.inflater = LayoutInflater.from(context);
        this.objects = objects;
        this.listener = listener;
    }


    @Override
    public int getCount() {
        /// return the number of items in the list
        return objects.size();
    }

    @Nullable
    @Override
    public ImageSourceOption getItem(int position) {
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
        TextView title = convertView.findViewById(R.id.text_dialog_item);
        TextView description = convertView.findViewById(R.id.text_dialog_item_description);

        /// get the item at the position
        ImageSourceOption item = getItem(position);

        if (item != null) {
            /// set the text and icon
            title.setText(item.getTitle());
            description.setText(item.getDescription());
            icon.setImageResource(item.getIconResource());
        }

        /// set the click listener for the item
        convertView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onImageSourceSelected(item);
            }
        });

        return convertView;
    }
}