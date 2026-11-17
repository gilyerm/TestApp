package com.example.testapp.screens.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import com.example.testapp.R;
import com.example.testapp.core.BaseFragment;
import com.example.testapp.screens.allcarts.AllCartsFragment;
import com.example.testapp.screens.fooditems.FoodItemsFragment;
import com.example.testapp.screens.userslist.UsersListFragment;

/**
 * AdminFragment is responsible for displaying the admin dashboard.
 * It provides navigation to users, foods, and carts management.
 */
public class AdminFragment extends BaseFragment<ViewModel> {

    /**
     * Returns the title of the fragment.
     *
     * @return the title string for the Admin Dashboard.
     */
    @NonNull
    @Override
    protected String getTitle() {
        return "Admin Dashboard";
    }

    /**
     * Indicates whether the back button should be shown in the action bar.
     *
     * @return true to show the back button, false otherwise.
     */
    @Override
    protected boolean shouldShowBackButton() {
        return true;
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    /**
     * Called immediately after onCreateView() has returned, but before any saved state has been restored in to the view.
     *
     * @param view               The View returned by onCreateView().
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View cardUsers = view.findViewById(R.id.card_users);
        View cardFoods = view.findViewById(R.id.card_foods);
        View cardCarts = view.findViewById(R.id.card_carts);
        cardUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationHost.navigateTo(new UsersListFragment(), true);
            }
        });
        cardFoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationHost.navigateTo(new FoodItemsFragment(), true);
            }
        });
        cardCarts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationHost.navigateTo(new AllCartsFragment(), true);
            }
        });
    }
}