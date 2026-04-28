package com.example.testapp.services.Impl;

import com.example.testapp.models.User;
import com.example.testapp.services.IUserService;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/// Firebase implementation of {@link IUserService}.
/// Inherits all CRUD operations from {@link FirebaseService} for the "users" table.
/// Adds user-specific operations: login lookup and email existence check.
/// @see IUserService
/// @see FirebaseService
public class UserServiceImpl extends FirebaseService<User> implements IUserService {

    public UserServiceImpl() { super("users", User.class); }

    /// {@inheritDoc}
    /// Fetches all users and finds the one matching the given email and password.
    @Override
    public void getUserByEmailAndPassword(@NotNull String email, @NotNull String password, @NotNull DatabaseCallback<User> callback) {
        getAll(new DatabaseCallback<>() {
            @Override
            public void onCompleted(List<User> users) {
                for (User user : users) {
                    if (Objects.equals(user.getEmail(), email) && Objects.equals(user.getPassword(), password)) {
                        callback.onCompleted(user);
                        return;
                    }
                }
                callback.onCompleted(null);
            }

            @Override
            public void onFailed(Exception e) { callback.onFailed(e); }
        });
    }

    /// {@inheritDoc}
    /// Fetches all users and checks if any has the given email.
    @Override
    public void checkIfEmailExists(@NotNull String email, @NotNull DatabaseCallback<Boolean> callback) {
        getAll(new DatabaseCallback<>() {
            @Override
            public void onCompleted(List<User> users) {
                for (User user : users) {
                    if (Objects.equals(user.getEmail(), email)) {
                        callback.onCompleted(true);
                        return;
                    }
                }
                callback.onCompleted(false);
            }

            @Override
            public void onFailed(Exception e) { callback.onFailed(e); }
        });
    }
}
