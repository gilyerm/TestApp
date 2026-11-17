package com.example.testapp.screens.splash;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.testapp.services.IDatabaseService;

/**
 * ViewModel for the splash screen.
 */
public class SplashViewModel extends ViewModel {
    /// runs the splash timer on the main thread
    private final Handler handler = new Handler(Looper.getMainLooper());
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
        // drop a timer still pending from an earlier call, so navigation fires only once
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isUserLoggedIn) {
                    _navigateToMain.setValue(true);
                } else {
                    _navigateToLanding.setValue(true);
                }
            }
        }, 3000);
    }

    /**
     * Cancels the pending timer when the ViewModel is destroyed.
     */
    @Override
    protected void onCleared() {
        handler.removeCallbacksAndMessages(null);
    }
}
