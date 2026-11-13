package com.example.testapp.core;

import androidx.fragment.app.Fragment;

/**
 * Interface definition for a navigation host that handles fragment navigation
 * and toolbar configuration.
 */
public interface NavigationHost {
    /**
     * Navigates to a new Fragment.
     *
     * @param fragment       The fragment to navigate to.
     * @param addToBackStack Whether to add this transaction to the back stack.
     */
    void navigateTo(Fragment fragment, boolean addToBackStack);

    /**
     * Pops the current fragment off the back stack, navigating to the previous one.
     */
    void goBack();

    /**
     * Sets the toolbar title.
     *
     * @param title The title to display.
     */
    void setToolbarTitle(String title);

    /**
     * Shows or hides the back button in the toolbar.
     *
     * @param show true to show the back button, false to hide it.
     */
    void showBackButton(boolean show);
}
