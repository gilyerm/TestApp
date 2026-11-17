package com.example.testapp.screens.userprofile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.testapp.core.BaseViewModel;
import com.example.testapp.models.User;
import com.example.testapp.services.ICrudService.DatabaseCallback;
import androidx.annotation.Nullable;
import java.util.function.UnaryOperator;

/**
 * ViewModel for managing and viewing the user's profile.
 */
public class UserProfileViewModel extends BaseViewModel {
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateSuccessLiveData = new MutableLiveData<>();

    /**
     * Gets the LiveData containing the user's profile details.
     *
     * @return the LiveData with the user profile
     */
    public LiveData<User> getUserLiveData() { return userLiveData; }

    /**
     * Gets the LiveData containing any error messages.
     *
     * @return the LiveData containing the error message
     */
    public LiveData<String> getErrorLiveData() { return errorLiveData; }

    /**
     * Gets the LiveData indicating if the user profile was successfully updated.
     *
     * @return the LiveData with the update success status
     */
    public LiveData<Boolean> getUpdateSuccessLiveData() { return updateSuccessLiveData; }

    /**
     * Loads the user profile details for the specified user ID.
     *
     * @param uid the ID of the user to load
     */
    public void loadUser(String uid) {
        databaseService.getUserService().getById(uid, new DatabaseCallback<User>() {
            @Override
            public void onCompleted(@Nullable User user) {
                if (user != null) {
                    userLiveData.postValue(user);
                } else {
                    errorLiveData.postValue("User profile not found");
                }
            }

            @Override
            public void onFailed(Exception e) {
                errorLiveData.postValue("Error getting user profile");
            }
        });
    }

    /**
     * Updates the user's profile details in the database.
     *
     * @param user the updated user object
     */
    public void updateUser(User user) {
        databaseService.getUserService().update(user.getId(), new UnaryOperator<User>() {
            @Override
            public User apply(User dbUser) {
                return user;
            }
        }, new DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void result) {
                updateSuccessLiveData.postValue(true);
            }

            @Override
            public void onFailed(Exception e) {
                errorLiveData.postValue("Failed to update profile");
            }
        });
    }
}
