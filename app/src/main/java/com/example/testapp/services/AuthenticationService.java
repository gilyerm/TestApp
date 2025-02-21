package com.example.testapp.services;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.testapp.R;
import com.example.testapp.models.User;
import com.google.admin_firebase.auth.UserRecord;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;


/// a service to interact with the Firebase Authentication.
/// this class is a singleton, use getInstance() to get an instance of this class
/// @see FirebaseAuth
public class AuthenticationService {

    /// tag for logging
    /// @see Log
    private static final String TAG = "AuthenticationService";

    /// callback interface for authentication operations
    /// @see AuthCallback#onCompleted(String uid)
    /// @see AuthCallback#onFailed(Exception)
    public interface AuthCallback {
        /// called when the operation is completed successfully
        /// @param uid the user ID
        public void onCompleted(String uid);

        /// called when the operation fails with an exception
        public void onFailed(Exception e);
    }

    public static class Admin {
        com.google.admin_firebase.auth.FirebaseAuth adminAuth;
        private static Admin instance;
        private Admin(Context context) {
            try {
                /// for creating the firebase_admin_sdk.json file, follow the instructions here:
                /// 1. Go to the Firebase Console
                /// 2. Go to Project Settings
                /// 3. Go to Service Accounts
                /// 4. Click on Generate New Private Key
                /// 5. Save the file as firebase_admin_sdk.json in the app/src/main/res/raw folder
                    /// DON'T FORGET TO ADD THE FILE TO .gitignore file to avoid committing it to the repository
                    /// add the following line to the .gitignore file:
                    /// app/src/main/res/raw/firebase_admin_sdk.json
                /// 6. Add the code to the your build.gradle file to create the library dependency for the Firebase Admin SDK
                    /// NOTE: you need to have the Firebase Admin SDK in your project to use this code
                    /// see `shadow` task in the build.gradle file to see how to include the Firebase Admin SDK in the build
                InputStream serviceAccount =
                    context.getResources().openRawResource(R.raw.firebase_admin_sdk);
                com.google.admin_firebase.FirebaseOptions options = null;
                options = new com.google.admin_firebase.FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("https://gilyerm-test-app-default-rtdb.firebaseio.com")
                        .build();
                com.google.admin_firebase.FirebaseApp.initializeApp(options);
                adminAuth = com.google.admin_firebase.auth.FirebaseAuth.getInstance();
                Log.d(TAG, "Admin initialized");
            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "Error getting resource", e);
                throw new RuntimeException(e);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public static Admin getInstance(Context context) {
            if (instance == null) {
                instance = new Admin(context);
            }
            return instance;
        }

        public void createUser(@NotNull final String email, @NotNull final String password, @NotNull final AuthCallback callback) {
            UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest().setEmail(email).setPassword(password);
            ApiFuture<UserRecord> userAsync = adminAuth.createUserAsync(createRequest);
            userAsync.addListener(() -> {
                try {
                    UserRecord userRecord = userAsync.get();
                    callback.onCompleted(userRecord.getUid());
                } catch (Exception e) {
                    callback.onFailed(e);
                }
            }, Runnable::run);
        }

        public void updateUser(@NotNull final String uid, @NotNull final String email, @NotNull final String password, @NotNull final AuthCallback callback) {
            UserRecord.UpdateRequest updateRequest = new UserRecord.UpdateRequest(uid).setEmail(email).setPassword(password);
            ApiFuture<UserRecord> userAsync = adminAuth.updateUserAsync(updateRequest);
            userAsync.addListener(() -> {
                try {
                    UserRecord userRecord = userAsync.get();
                    callback.onCompleted(userRecord.getUid());
                } catch (Exception e) {
                    callback.onFailed(e);
                }
            }, Runnable::run);
        }

        public void updateUser(@NotNull final User user, @NotNull final AuthCallback callback) {
            updateUser(user.getUid(), user.getEmail(), user.getPassword(), callback);
        }

        public void deleteUser(@NotNull final String uid, @NotNull final AuthCallback callback) {
            ApiFuture<Void> userAsync = adminAuth.deleteUserAsync(uid);
            userAsync.addListener(() -> {
                try {
                    userAsync.get();
                    callback.onCompleted(uid);
                } catch (Exception e) {
                    callback.onFailed(e);
                }
            }, Runnable::run);
        }
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
    /// @see AuthCallback
    public void signIn(@NotNull final String email, @NotNull final String password, @NotNull final AuthCallback callback) {
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
    public void signUp(@NotNull final String email, @NotNull final String password, @NotNull final AuthCallback callback) {
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