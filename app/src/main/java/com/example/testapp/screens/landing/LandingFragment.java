package com.example.testapp.screens.landing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.example.testapp.R;
import com.example.testapp.core.BaseFragment;
import com.example.testapp.core.ViewModelFactory;
import com.example.testapp.screens.login.LoginFragment;
import com.example.testapp.screens.register.RegisterFragment;

/**
 * LandingFragment represents the landing screen of the application.
 * It provides users with options to either log in or register.
 */
public class LandingFragment extends BaseFragment implements View.OnClickListener {
    private LandingViewModel viewModel;
    private Button btnLogin, btnRegister;

    /**
     * Returns the title of the fragment.
     *
     * @return the title string for the Landing screen.
     */
    @Override
    protected String getTitle() {
        return "Landing";
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
        return inflater.inflate(R.layout.fragment_landing, container, false);
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
        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(LandingViewModel.class);
        btnLogin = view.findViewById(R.id.btn_landing_login);
        btnRegister = view.findViewById(R.id.btn_landing_register);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == btnLogin.getId()) {
            navigationHost.navigateTo(new LoginFragment(), true);
        } else if (v.getId() == btnRegister.getId()) {
            navigationHost.navigateTo(new RegisterFragment(), true);
        }
    }
}
