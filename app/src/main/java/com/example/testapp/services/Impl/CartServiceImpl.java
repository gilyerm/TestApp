package com.example.testapp.services.Impl;

import com.example.testapp.models.Cart;
import com.example.testapp.services.ICartService;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/// Firebase implementation of {@link ICartService}.
/// Inherits all CRUD operations from {@link FirebaseService} for the "carts" table.
/// Adds cart-specific operation: filtering carts by user ID.
/// @see ICartService
/// @see FirebaseService
public class CartServiceImpl extends FirebaseService<Cart> implements ICartService {

    public CartServiceImpl() { super("carts", Cart.class); }

    /// {@inheritDoc}
    /// Fetches all carts and filters to only those belonging to the given user.
    @Override
    public void getUserCartList(@NotNull String uid, @NotNull DatabaseCallback<List<Cart>> callback) {
        getAll(new DatabaseCallback<>() {
            @Override
            public void onCompleted(List<Cart> carts) {
                carts.removeIf(cart -> !Objects.equals(cart.getUid(), uid));
                callback.onCompleted(carts);
            }

            @Override
            public void onFailed(Exception e) { callback.onFailed(e); }
        });
    }
}
