package com.example.testapp.services.impl.rtdb;

import com.example.testapp.models.User;
import com.example.testapp.services.IUserService;
import com.google.firebase.database.DataSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/// Firebase Realtime Database implementation of {@link IUserService}.
/// Inherits all CRUD operations from {@link FirebaseService} for the "users" table.
/// Adds user-specific operations: login lookup and email existence check using server-side queries.
/// @see IUserService
/// @see FirebaseService
public class UserFirebaseServiceImpl extends FirebaseService<User> implements IUserService {

    public UserFirebaseServiceImpl() { super("users", User.class); }

    /// {@inheritDoc}
    /// Queries the user table by email on the server and checks the password.
    @Override
    public void getUserByEmailAndPassword(@NotNull String email, @NotNull String password, @NotNull DatabaseCallback<User> callback) {
        getTableReference()
                .orderByChild("email")
                .equalTo(email)
                .limitToFirst(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        callback.onFailed(task.getException());
                        return;
                    }
                    DataSnapshot snapshot = task.getResult();
                    if (snapshot != null && snapshot.exists()) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            User user = child.getValue(User.class);
                            if (user != null && Objects.equals(user.getPassword(), password)) {
                                callback.onCompleted(user);
                                return;
                            }
                        }
                    }
                    callback.onCompleted(null);
                });
    }

    /// {@inheritDoc}
    /// Queries the user table on the server to check if any user has the given email.
    @Override
    public void checkIfEmailExists(@NotNull String email, @NotNull DatabaseCallback<Boolean> callback) {
        getTableReference()
                .orderByChild("email")
                .equalTo(email)
                .limitToFirst(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        callback.onFailed(task.getException());
                        return;
                    }
                    DataSnapshot snapshot = task.getResult();
                    callback.onCompleted(snapshot != null && snapshot.exists() && snapshot.hasChildren());
                });
    }
}
