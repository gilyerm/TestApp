package com.example.testapp.screens.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.testapp.models.User;
import com.example.testapp.services.IDatabaseService;
import com.example.testapp.services.ICrudService.DatabaseCallback;

/**
 * ViewModel for handling user registration.
 */
public class RegisterViewModel extends ViewModel {
    private final IDatabaseService databaseService;
    private final MutableLiveData<User> _registerSuccess = new MutableLiveData<>();

    /**
     * Gets the LiveData for successful registration events.
     *
     * @return the LiveData containing the registered user
     */
    public LiveData<User> getRegisterSuccess() {
        return _registerSuccess;
    }

    private final MutableLiveData<String> _registerError = new MutableLiveData<>();

    /**
     * Gets the LiveData for registration error events.
     *
     * @return the LiveData containing the error message
     */
    public LiveData<String> getRegisterError() {
        return _registerError;
    }

    /**
     * Constructs a RegisterViewModel with the given database service.
     *
     * @param databaseService the service used for database operations
     */
    public RegisterViewModel(IDatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * Registers a new user with the provided details.
     *
     * @param email    the user's email address
     * @param password the user's password
     * @param fName    the user's first name
     * @param lName    the user's last name
     * @param phone    the user's phone number
     */
    public void registerUser(String email, String password, String fName, String lName, String phone) {
        String uid = databaseService.getUserService().generateId();
        User user = new User(uid, email, password, fName, lName, phone, false);
        databaseService.getUserService().checkIfEmailExists(email, new DatabaseCallback<Boolean>() {
            @Override
            public void onCompleted(Boolean exists) {
                if (exists != null && exists) {
                    _registerError.postValue("Email already exists");
                } else {
                    createUserInDatabase(user);
                }
            }

            @Override
            public void onFailed(Exception e) {
                _registerError.postValue("Failed to check if email exists");
            }
        });
    }

    /**
     * Creates the user record in the database.
     *
     * @param user the user object to create
     */
    private void createUserInDatabase(User user) {
        databaseService.getUserService().create(user, new DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                _registerSuccess.postValue(user);
            }

            @Override
            public void onFailed(Exception e) {
                _registerError.postValue("Failed to register user");
            }
        });
    }
}
