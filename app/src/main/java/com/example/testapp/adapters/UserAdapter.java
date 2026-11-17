package com.example.testapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.models.User;
import com.google.android.material.chip.Chip;

/// Adapter for the users recycler view.
/// Each row shows the user's initials, full name, email, phone, and an "Admin" chip for admins.
/// @see BaseAdapter
/// @see User
/// @see R.layout#row_user
public class UserAdapter extends BaseAdapter<User, UserAdapter.ViewHolder> {

    /// Callback for clicks on a user row.
    public interface OnUserClickListener {
        /// @param user the clicked user
        void onUserClick(User user);

        /// @param user the long-pressed user
        void onLongUserClick(User user);
    }

    /// receives row clicks and long presses; may be null
    @Nullable
    private final OnUserClickListener onUserClickListener;

    /// @param onUserClickListener receives row clicks and long presses; may be null
    public UserAdapter(@Nullable OnUserClickListener onUserClickListener) {
        this.onUserClickListener = onUserClickListener;
    }

    /// Inflate a user row.
    /// @param parent the RecyclerView
    /// @param viewType unused, there is one row type
    /// @return a new view holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
        return new ViewHolder(view);
    }

    /// Fill a row with the user at this position and set its click listeners.
    /// @param holder the row to fill
    /// @param position the user's position in the list
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = getItem(position);

        holder.tvName.setText(user.getFullName());
        holder.tvEmail.setText(user.getEmail());
        holder.tvPhone.setText(user.getPhone());

        // Set initials
        String initials = "";
        if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
            initials += user.getFirstName().charAt(0);
        }
        if (user.getLastName() != null && !user.getLastName().isEmpty()) {
            initials += user.getLastName().charAt(0);
        }
        holder.tvInitials.setText(initials.toUpperCase());

        // Show admin chip if user is admin
        if (user.isAdmin()) {
            holder.chipRole.setVisibility(View.VISIBLE);
            holder.chipRole.setText("Admin");
        } else {
            holder.chipRole.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (onUserClickListener != null) {
                onUserClickListener.onUserClick(user);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (onUserClickListener != null) {
                onUserClickListener.onLongUserClick(user);
            }
            return true;
        });
    }

    /// Holds the views of one user row.
    /// @see R.layout#row_user
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvName;
        final TextView tvEmail;
        final TextView tvPhone;
        final TextView tvInitials;
        final Chip chipRole;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_item_user_name);
            tvEmail = itemView.findViewById(R.id.tv_item_user_email);
            tvPhone = itemView.findViewById(R.id.tv_item_user_phone);
            tvInitials = itemView.findViewById(R.id.tv_user_initials);
            chipRole = itemView.findViewById(R.id.chip_user_role);
        }
    }
}
