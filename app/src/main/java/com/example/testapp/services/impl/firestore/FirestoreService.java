package com.example.testapp.services.impl.firestore;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.testapp.models.Identifiable;
import com.example.testapp.services.ICrudService;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

/// Generic Cloud Firestore implementation of {@link ICrudService}.
/// Provides all CRUD operations for a given collection and entity type.
/// Subclasses only need to call {@code super(collectionName, clazz)} and add domain-specific queries.
/// Shared CRUD methods are {@code final} to ensure uniform data access.
/// @param <T> the entity type, must implement {@link Identifiable}
/// @see UserFirestoreServiceImpl
/// @see FoodFirestoreServiceImpl
/// @see CartFirestoreServiceImpl
public class FirestoreService<T extends Identifiable> implements ICrudService<T> {

    private static final String TAG = "FirestoreService";
    private final FirebaseFirestore db;
    private final CollectionReference collectionReference;
    private final String collectionName;
    private final Class<T> clazz;

    /// Creates a new FirestoreService for the given collection.
    /// @param collectionName the Firestore collection name
    /// @param clazz the entity class, used for Firestore deserialization
    protected FirestoreService(@NotNull String collectionName, @NotNull Class<T> clazz) {
        this.collectionName = collectionName;
        this.clazz = clazz;
        this.db = FirebaseFirestore.getInstance();
        this.collectionReference = db.collection(collectionName);
    }

    // region ICrudService<T> implementation

    /// {@inheritDoc}
    @Override
    public final String generateId() {
        return collectionReference.document().getId();
    }

    /// {@inheritDoc}
    @Override
    public final void create(@NotNull T item, @Nullable DatabaseCallback<Void> callback) {
        collectionReference.document(item.getId())
                .set(item)
                .addOnSuccessListener(aVoid -> {
                    if (callback != null) callback.onCompleted(null);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error creating document in " + collectionName, e);
                    if (callback != null) callback.onFailed(e);
                });
    }

    /// {@inheritDoc}
    @Override
    public final void getById(@NotNull String id, @NotNull DatabaseCallback<T> callback) {
        collectionReference.document(id)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        callback.onCompleted(snapshot.toObject(clazz));
                    } else {
                        callback.onCompleted(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting document from " + collectionName, e);
                    callback.onFailed(e);
                });
    }

    /// {@inheritDoc}
    @Override
    public final void getAll(@NotNull DatabaseCallback<List<T>> callback) {
        collectionReference.get()
                .addOnSuccessListener(querySnapshot -> {
                    List<T> list = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        T item = doc.toObject(clazz);
                        if (item != null) {
                            list.add(item);
                        }
                    }
                    callback.onCompleted(list);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting all documents from " + collectionName, e);
                    callback.onFailed(e);
                });
    }

    /// {@inheritDoc}
    @Override
    public final void delete(@NotNull String id, @Nullable DatabaseCallback<Void> callback) {
        collectionReference.document(id)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    if (callback != null) callback.onCompleted(null);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error deleting document from " + collectionName, e);
                    if (callback != null) callback.onFailed(e);
                });
    }

    /// {@inheritDoc}
    @Override
    public void update(@NotNull String id, @NotNull UnaryOperator<T> function, @Nullable DatabaseCallback<Void> callback) {
        DocumentReference docRef = collectionReference.document(id);
        db.runTransaction(transaction -> {
            DocumentSnapshot snapshot = transaction.get(docRef);
            T current = snapshot.toObject(clazz);
            if (current != null) {
                T updated = function.apply(current);
                transaction.set(docRef, updated);
                return updated;
            }
            return null;
        }).addOnSuccessListener(result -> {
            if (callback != null) callback.onCompleted(null);
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Transaction failed in " + collectionName, e);
            if (callback != null) callback.onFailed(e);
        });
    }

    // endregion

    /// Returns the Firestore collection reference for domain-specific queries.
    /// @return the {@link CollectionReference}
    protected CollectionReference getCollectionReference() {
        return collectionReference;
    }

    /// Returns the collection name this service operates on.
    /// @return the Firestore collection name
    protected String getCollectionName() {
        return collectionName;
    }
}
