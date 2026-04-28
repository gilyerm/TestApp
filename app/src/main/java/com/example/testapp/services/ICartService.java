package com.example.testapp.services;

import com.example.testapp.models.Cart;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/// Service interface for cart-specific database operations.
/// Inherits generic CRUD from {@link ICrudService}.
/// @see Cart
public interface ICartService extends ICrudService<Cart> {

    /// Retrieves all carts belonging to a specific user.
    /// @param uid the user ID to filter carts by
    /// @param callback called with the filtered list of carts on success
    void getUserCartList(@NotNull String uid, @NotNull ICrudService.DatabaseCallback<List<Cart>> callback);
}
