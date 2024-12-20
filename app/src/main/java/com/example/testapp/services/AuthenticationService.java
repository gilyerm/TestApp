package com.example.testapp.services;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;


/// a service to interact with the Firebase Authentication.
/// this class is a singleton, use getInstance() to get an instance of this class
/// @see FirebaseAuth
public class AuthenticationService {

    /// tag for logging
    /// @see Log
    private static final String TAG = "AuthenticationService";

    /// callback interface for authentication operations
    /// @param <T> the type of the object to return
    /// @see AuthCallback#onCompleted(Object)
    /// @see AuthCallback#onFailed(Exception)
    public interface AuthCallback<T> {
        /// called when the operation is completed successfully
        void onCompleted(T object);

        /// called when the operation fails with an exception
        void onFailed(Exception e);
    }

    /// the instance of this class
    /// @see #getInstance()
    private static AuthenticationService instance;

    /// the reference to the authentication
    /// @see FirebaseAuth
    private final FirebaseAuth mAuth;

    /// use getInstance() to get an instance of this class
    private AuthenticationService() {
        mAuth = FirebaseAuth.getInstance();
    }

    /// get an instance of this class
    /// @return an instance of this class
    /// @see AuthenticationService
    public static AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }

    /// sign in a user with email and password
    /// @param email the email of the user
    /// @param password the password of the user
    /// @param callback the callback to call when the operation is completed
    ///              the callback will receive true if the operation is successful
    ///              if the operation fails, the callback will receive an exception
    /// @return void
    /// @see AuthCallback
    public void signIn(@NotNull final String email, @NotNull final String password, @NotNull final AuthCallback<String> callback) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onCompleted(getCurrentUserId());
            } else {
                Log.e(TAG, "Error signing in", task.getException());
                callback.onFailed(task.getException());
            }
        });
    }

    /// sign up a new user with email and password
    /// @param email the email of the user
    /// @param password the password of the user
    /// @param callback the callback to call when the operation is completed
    ///              the callback will receive the FirebaseUser object if the operation is successful
    ///              if the operation fails, the callback will receive an exception
    /// @see AuthCallback
    /// @see FirebaseUser
    public void signUp(@NotNull final String email, @NotNull final String password, @NotNull final AuthCallback<String> callback) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onCompleted(getCurrentUserId());
            } else {
                Log.e(TAG, "Error signing up", task.getException());
                callback.onFailed(task.getException());
            }
        });
    }

    /// sign out the current user
    public void signOut() {
        mAuth.signOut();
    }

    /// get the current user ID
    /// @return the current user ID
    public String getCurrentUserId() {
        if (mAuth.getCurrentUser() == null) {
            return null;
        }
        return mAuth.getCurrentUser().getUid();
    }

    /// check if a user is signed in
    /// @return true if a user is signed in, false otherwise
    public boolean isUserSignedIn() {
        return mAuth.getCurrentUser() != null;
    }
}