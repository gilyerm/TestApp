package com.example.testapp.services.impl.firestore;

import android.util.Log;

import com.example.testapp.models.User;
import com.example.testapp.services.IUserService;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/// Cloud Firestore implementation of {@link IUserService}.
/// Inherits generic CRUD operations from {@link FirestoreService} for the "users" collection.
/// Provides optimized server-side queries for user authentication and email verification.
/// @see IUserService
/// @see FirestoreService
public class UserFirestoreServiceImpl extends FirestoreService<User> implements IUserService {

    private static final String TAG = "UserFirestoreService";

    public UserFirestoreServiceImpl() {
        super("users", User.class);
    }

    /// {@inheritDoc}
    /// Executes a server-side indexed query matching email and password.
    @Override
    public void getUserByEmailAndPassword(@NotNull String email, @NotNull String password, @NotNull DatabaseCallback<@Nullable User> callback) {
        getCollectionReference()
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        callback.onCompleted(null);
                    } else {
                        User user = querySnapshot.getDocuments().get(0).toObject(User.class);
                        callback.onCompleted(user);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error finding user by email and password", e);
                    callback.onFailed(e);
                });
    }

    /// {@inheritDoc}
    /// Executes a server-side query to check if any document has the specified email.
    @Override
    public void checkIfEmailExists(@NotNull String email, @NotNull DatabaseCallback<Boolean> callback) {
        getCollectionReference()
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> callback.onCompleted(!querySnapshot.isEmpty()))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error checking if email exists", e);
                    callback.onFailed(e);
                });
    }
}
