package com.example.testapp.screens.splash;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.testapp.services.IDatabaseService;

/**
 * ViewModel for the splash screen.
 */
public class SplashViewModel extends ViewModel {
    private final MutableLiveData<Boolean> _navigateToLanding = new MutableLiveData<>();

    /**
     * Gets the LiveData for navigating to the landing screen.
     *
     * @return the LiveData indicating when to navigate
     */
    public LiveData<Boolean> getNavigateToLanding() {
        return _navigateToLanding;
    }

    private final MutableLiveData<Boolean> _navigateToMain = new MutableLiveData<>();

    /**
     * Gets the LiveData for navigating to the main screen.
     *
     * @return the LiveData indicating when to navigate
     */
    public LiveData<Boolean> getNavigateToMain() {
        return _navigateToMain;
    }

    /**
     * Constructs a SplashViewModel with the given database service.
     *
     * @param databaseService the service used for database operations
     */
    public SplashViewModel(IDatabaseService databaseService) {
    }

    /**
     * Starts the timer for the splash screen duration and triggers navigation.
     *
     * @param isUserLoggedIn true if the user is already logged in, false otherwise
     */
    public void startSplashTimer(boolean isUserLoggedIn) {
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
            } finally {
                if (isUserLoggedIn) {
                    _navigateToMain.postValue(true);
                } else {
                    _navigateToLanding.postValue(true);
                }
            }
        }).start();
    }
}
