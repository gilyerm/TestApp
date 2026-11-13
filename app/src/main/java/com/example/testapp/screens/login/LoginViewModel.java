package com.example.testapp.screens.login;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.testapp.models.User;
import com.example.testapp.services.IDatabaseService;
import com.example.testapp.services.ICrudService.DatabaseCallback;

/**
 * ViewModel for handling user login.
 */
public class LoginViewModel extends ViewModel {
    private final IDatabaseService databaseService;
    private final MutableLiveData<User> _loginSuccess = new MutableLiveData<>();

    /**
     * Gets the LiveData for successful login events.
     *
     * @return the LiveData containing the logged-in user
     */
    public LiveData<User> getLoginSuccess() {
        return _loginSuccess;
    }

    private final MutableLiveData<String> _loginError = new MutableLiveData<>();

    /**
     * Gets the LiveData for login error events.
     *
     * @return the LiveData containing the error message
     */
    public LiveData<String> getLoginError() {
        return _loginError;
    }

    /**
     * Constructs a LoginViewModel with the given database service.
     *
     * @param databaseService the service used for database operations
     */
    public LoginViewModel(IDatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * Attempts to log in a user with the provided email and password.
     *
     * @param email    the user's email address
     * @param password the user's password
     */
    public void loginUser(String email, String password) {
        databaseService.getUserService().getUserByEmailAndPassword(email, password, new DatabaseCallback<User>() {
            @Override
            public void onCompleted(@Nullable User user) {
                if (user == null) {
                    _loginError.postValue("Invalid email or password");
                    return;
                }
                _loginSuccess.postValue(user);
            }

            @Override
            public void onFailed(Exception e) {
                _loginError.postValue("Invalid email or password");
            }
        });
    }
}
