package com.example.testapp.screens.splash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.testapp.R;
import com.example.testapp.core.BaseFragment;
import com.example.testapp.screens.dashboard.MainDashboardFragment;
import com.example.testapp.screens.landing.LandingFragment;
import com.example.testapp.utils.SharedPreferencesUtil;

/**
 * SplashFragment represents the initial splash screen of the application.
 * It handles the timer and navigation to the landing or main dashboard screen based on user login status.
 */
public class SplashFragment extends BaseFragment<SplashViewModel> {

    /**
     * Returns the title of the fragment.
     *
     * @return the title string for the Splash screen.
     */
    @NonNull
    @Override
    protected String getTitle() {
        return "";
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
        return inflater.inflate(R.layout.fragment_splash, container, false);
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
        viewModel = getViewModel(SplashViewModel.class);
        viewModel.getNavigateToLanding().observe(getViewLifecycleOwner(), navigate -> {
            if (navigate != null && navigate) navigationHost.navigateTo(new LandingFragment(), false);
        });
        viewModel.getNavigateToMain().observe(getViewLifecycleOwner(), navigate -> {
            if (navigate != null && navigate) navigationHost.navigateTo(new MainDashboardFragment(), false);
        });
        viewModel.startSplashTimer(SharedPreferencesUtil.isUserLoggedIn(requireContext()));
    }
}
