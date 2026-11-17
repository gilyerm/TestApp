package com.example.testapp.screens.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.testapp.R;
import com.example.testapp.core.BaseFragment;
import com.example.testapp.screens.addfood.AddFoodFragment;
import com.example.testapp.screens.addcart.AddCartFragment;
import com.example.testapp.screens.mycarts.MyCartsFragment;
import com.example.testapp.screens.userprofile.UserProfileFragment;
import com.example.testapp.screens.admin.AdminFragment;
import com.example.testapp.utils.SharedPreferencesUtil;
import androidx.lifecycle.Observer;
import com.example.testapp.models.User;

/**
 * MainDashboardFragment represents the main landing dashboard for authenticated users.
 * Provides navigation options to add foods, manage carts, edit profile, and an admin panel (if authorized).
 */
public class MainDashboardFragment extends BaseFragment<MainDashboardViewModel> {
    private View adminCard;

    /**
     * Returns the title of the fragment.
     *
     * @return the title string for the Dashboard screen.
     */
    @NonNull
    @Override
    protected String getTitle() {
        return "Dashboard";
    }

    /**
     * Indicates whether the back button should be shown in the action bar.
     *
     * @return true to show the back button, false otherwise.
     */
    @Override
    protected boolean shouldShowBackButton() {
        return false;
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
        return inflater.inflate(R.layout.fragment_main_dashboard, container, false);
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
        viewModel = getViewModel(MainDashboardViewModel.class);
        Button btnAddFood = view.findViewById(R.id.btn_main_add_food);
        Button btnAddCart = view.findViewById(R.id.btn_main_add_cart);
        Button btnMyCarts = view.findViewById(R.id.btn_main_my_carts);
        Button btnEditProfile = view.findViewById(R.id.btn_main_edit_profile);
        Button btnToAdmin = view.findViewById(R.id.btn_main_to_admin);
        adminCard = view.findViewById(R.id.admin_card);
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationHost.navigateTo(new AddFoodFragment(), true);
            }
        });
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationHost.navigateTo(new AddCartFragment(), true);
            }
        });
        btnMyCarts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationHost.navigateTo(new MyCartsFragment(), true);
            }
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationHost.navigateTo(new UserProfileFragment(), true);
            }
        });
        btnToAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationHost.navigateTo(new AdminFragment(), true);
            }
        });
        viewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null && user.isAdmin()) {
                    adminCard.setVisibility(View.VISIBLE);
                } else {
                    adminCard.setVisibility(View.GONE);
                }
            }
        });
        String userId = SharedPreferencesUtil.getUserId(requireContext());
        viewModel.loadUser(userId);
    }
}