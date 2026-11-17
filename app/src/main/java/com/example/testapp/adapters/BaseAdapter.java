package com.example.testapp.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.models.Identifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/// Base class for every RecyclerView adapter in the app.
/// Holds the list of items shown by the RecyclerView and gives set / add / update / remove methods
/// that notify it. Items are matched by {@link Identifiable#getId()}, so an updated copy replaces the old one.
/// The ViewModel owns the data: call these from the Fragment's LiveData observer, not to keep screen state.
/// Not to be confused with {@link android.widget.BaseAdapter} (for ListView / Spinner).
/// @param <T>  the item type, must implement {@link Identifiable}
/// @param <VH> the view holder type
public abstract class BaseAdapter<T extends Identifiable, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    /// the items shown, in display order
    protected final List<T> items = new ArrayList<>();

    /// Replace all items.
    /// @param newItems the new items; null clears the list
    public void setItems(@Nullable List<T> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }

    /// Add an item to the end of the list.
    /// @param item the item to add; ignored if null
    public void addItem(@Nullable T item) {
        if (item == null) return;
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    /// Replace the item with the same ID.
    /// @param item the updated item
    /// @return true if an item with that ID was found
    public boolean updateItem(@NonNull T item) {
        int index = indexOf(item.getId());
        if (index == -1) return false;
        items.set(index, item);
        notifyItemChanged(index);
        return true;
    }

    /// Remove the item with the same ID.
    /// @param item the item to remove
    /// @return true if an item with that ID was found
    public boolean removeItem(@NonNull T item) {
        int index = indexOf(item.getId());
        if (index == -1) return false;
        items.remove(index);
        notifyItemRemoved(index);
        return true;
    }

    /// @param position the adapter position
    /// @return the item at that position
    @NonNull
    public T getItem(int position) {
        return items.get(position);
    }

    /// @param id the item ID
    /// @return the position of the item with that ID, or -1 if not found
    public int indexOf(@Nullable String id) {
        for (int i = 0; i < items.size(); i++) {
            if (Objects.equals(items.get(i).getId(), id)) return i;
        }
        return -1;
    }

    /// @return the number of items shown
    @Override
    public int getItemCount() {
        return items.size();
    }
}
