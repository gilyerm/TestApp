package com.example.testapp.services.impl.firestore;

import android.util.Log;

import com.example.testapp.models.Cart;
import com.example.testapp.services.ICartService;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/// Cloud Firestore implementation of {@link ICartService}.
/// Inherits all CRUD operations from {@link FirestoreService} for the "carts" collection.
/// Uses server-side querying to retrieve only carts belonging to a specific user.
/// @see ICartService
/// @see FirestoreService
public class CartFirestoreServiceImpl extends FirestoreService<Cart> implements ICartService {

    private static final String TAG = "CartFirestoreService";

    public CartFirestoreServiceImpl() {
        super("carts", Cart.class);
    }

    /// {@inheritDoc}
    /// Retrieves carts filtered server-side by the user's ID (uid).
    @Override
    public void getUserCartList(@NotNull String uid, @NotNull DatabaseCallback<List<Cart>> callback) {
        getCollectionReference()
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Cart> carts = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Cart cart = doc.toObject(Cart.class);
                        if (cart != null) {
                            carts.add(cart);
                        }
                    }
                    callback.onCompleted(carts);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching carts for user: " + uid, e);
                    callback.onFailed(e);
                });
    }
}
