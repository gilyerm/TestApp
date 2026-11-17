package com.example.testapp.core;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Base fragment class providing common functionality for fragments in the application.
 * It manages interaction with the {@link NavigationHost} and holds the screen's ViewModel.
 *
 * @param <T> the ViewModel type of the screen.
 */
public abstract class BaseFragment<T extends ViewModel> extends Fragment {

    /**
     * The navigation host for managing fragment navigation.
     */
    protected NavigationHost navigationHost;

    /**
     * The ViewModel of this screen. Assign it with {@link #getViewModel(Class)}.
     */
    protected T viewModel;

    /**
     * Constructs a new BaseFragment.
     */
    public BaseFragment() {
        super();
    }

    /**
     * Called when a fragment is first attached to its context.
     *
     * @param context The context to attach to.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (!(context instanceof NavigationHost)) {
            throw new RuntimeException(context.toString() + " must implement NavigationHost");
        }
        navigationHost = (NavigationHost) context;
    }

    /**
     * Called when the fragment is no longer attached to its activity.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        navigationHost = null;
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     */
    @Override
    public void onResume() {
        super.onResume();
        assert navigationHost != null;
        navigationHost.setToolbarTitle(getTitle());
        navigationHost.showBackButton(shouldShowBackButton());
    }

    /**
     * Returns the ViewModel of the given class for this fragment, created with {@link ViewModelFactory}.
     *
     * @param modelClass the ViewModel class, e.g. {@code LoginViewModel.class}
     * @return the ViewModel instance (the same one is returned again after a configuration change)
     */
    @NonNull
    protected T getViewModel(@NonNull Class<T> modelClass) {
        return new ViewModelProvider(this, new ViewModelFactory()).get(modelClass);
    }

    /**
     * Get the title for the toolbar when this fragment is active.
     *
     * @return The title string for the toolbar.
     */
    @NonNull
    protected abstract String getTitle();

    /**
     * Determine if the back button should be shown in the toolbar.
     * Default is true. Override to return false for root fragments (like Splash, Landing, MainDashboard).
     *
     * @return true if the back button should be shown, false otherwise.
     */
    protected boolean shouldShowBackButton() {
        return true;
    }
}
