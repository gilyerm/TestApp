package com.example.testapp.screens.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.testapp.core.BaseViewModel;
import com.example.testapp.models.User;
import com.example.testapp.services.ICrudService.DatabaseCallback;

/**
 * ViewModel for the main dashboard screen.
 */
public class MainDashboardViewModel extends BaseViewModel {
    private final MutableLiveData<User> _user = new MutableLiveData<>();

    /**
     * Gets the LiveData containing the currently logged-in user.
     *
     * @return the LiveData with the user
     */
    public LiveData<User> getUser() {
        return _user;
    }

    /**
     * Loads the details for the given user ID.
     *
     * @param userId the ID of the user to load
     */
    public void loadUser(String userId) {
        if (userId == null) return;
        databaseService.getUserService().getById(userId, new DatabaseCallback<User>() {
            @Override
            public void onCompleted(User user) {
                _user.postValue(user);
            }

            @Override
            public void onFailed(Exception e) {
                // Ignore for now or handle appropriately
            }
        });
    }
}
