package com.example.testapp.services;

import com.example.testapp.models.User;

import org.jetbrains.annotations.NotNull;

/// Service interface for user-specific database operations.
/// Inherits generic CRUD from {@link ICrudService}.
/// @see User
public interface IUserService extends ICrudService<User> {

    /// Retrieves a user by email and password.
    /// @param email the user's email
    /// @param password the user's password
    /// @param callback called with the matching user, or null if not found
    void getUserByEmailAndPassword(@NotNull String email, @NotNull String password, @NotNull ICrudService.DatabaseCallback<User> callback);

    /// Checks whether an email is already registered in the database.
    /// @param email the email to check
    /// @param callback called with true if the email exists, false otherwise
    void checkIfEmailExists(@NotNull String email, @NotNull ICrudService.DatabaseCallback<Boolean> callback);
}
