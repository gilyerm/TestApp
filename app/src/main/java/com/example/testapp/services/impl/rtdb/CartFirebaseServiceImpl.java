package com.example.testapp.services.impl.rtdb;

import com.example.testapp.models.Cart;
import com.example.testapp.services.ICartService;
import com.google.firebase.database.DataSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/// Firebase Realtime Database implementation of {@link ICartService}.
/// Inherits all CRUD operations from {@link FirebaseService} for the "carts" table.
/// Adds cart-specific operation: server-side filtering of carts by user ID.
/// @see ICartService
/// @see FirebaseService
public class CartFirebaseServiceImpl extends FirebaseService<Cart> implements ICartService {

    public CartFirebaseServiceImpl() { super("carts", Cart.class); }

    /// {@inheritDoc}
    /// Queries the carts table on the server filtered by user ID.
    @Override
    public void getUserCartList(@NotNull String uid, @NotNull DatabaseCallback<List<Cart>> callback) {
        getTableReference()
                .orderByChild("uid")
                .equalTo(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        callback.onFailed(task.getException());
                        return;
                    }
                    List<Cart> carts = new ArrayList<>();
                    DataSnapshot snapshot = task.getResult();
                    if (snapshot != null && snapshot.exists()) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Cart cart = child.getValue(Cart.class);
                            if (cart != null) {
                                carts.add(cart);
                            }
                        }
                    }
                    callback.onCompleted(carts);
                });
    }
}
