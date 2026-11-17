package com.example.testapp.screens.userslist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.testapp.core.BaseViewModel;
import com.example.testapp.models.User;
import com.example.testapp.services.ICrudService.DatabaseCallback;
import java.util.List;

/**
 * ViewModel for viewing the list of users.
 */
public class UsersListViewModel extends BaseViewModel {
    private final MutableLiveData<List<User>> usersLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    /**
     * Gets the LiveData containing the list of users.
     *
     * @return the LiveData with the user list
     */
    public LiveData<List<User>> getUsersLiveData() {
        return usersLiveData;
    }

    /**
     * Gets the LiveData containing any error messages.
     *
     * @return the LiveData containing the error message
     */
    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    /**
     * Loads all users from the database.
     */
    public void loadUsers() {
        databaseService.getUserService().getAll(new DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> users) {
                usersLiveData.postValue(users);
            }

            @Override
            public void onFailed(Exception e) {
                errorLiveData.postValue("Failed to get users list");
            }
        });
    }
}
