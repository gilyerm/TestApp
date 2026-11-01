package com.example.testapp.services.impl.rtdb;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testapp.models.Identifiable;
import com.example.testapp.services.ICrudService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

/// Generic Firebase Realtime Database implementation of {@link ICrudService}.
/// Provides all CRUD operations for a given table and entity type.
/// Subclasses only need to call {@code super(tableName, clazz)} and add domain-specific methods.
/// Shared CRUD methods are {@code final} to prevent accidental overrides.
/// @param <T> the entity type, must implement {@link Identifiable}
/// @see UserFirebaseServiceImpl
/// @see FoodFirebaseServiceImpl
/// @see CartFirebaseServiceImpl
public class FirebaseService<T extends Identifiable> implements ICrudService<T> {

    private static final String TAG = "FirebaseService";
    private final DatabaseReference tableReference;
    private final String tableName;
    private final Class<T> clazz;

    /// Creates a new FirebaseService for the given table.
    /// @param tableName the Firebase path/table name
    /// @param clazz the entity class, used for Firebase deserialization
    protected FirebaseService(@NotNull String tableName, @NotNull Class<T> clazz) {
        this.tableName = tableName;
        this.clazz = clazz;
        this.tableReference = FirebaseDatabase.getInstance().getReference(tableName);
    }

    // region ICrudService<T> implementation

    /// {@inheritDoc}
    @Override
    public final String generateId() {
        return tableReference.push().getKey();
    }

    /// {@inheritDoc}
    @Override
    public final void create(@NotNull T item, @Nullable ICrudService.DatabaseCallback<Void> callback) {
        tableReference.child(item.getId()).setValue(item, (error, ref) -> {
            if (callback == null) return;
            if (error != null) callback.onFailed(error.toException());
            else callback.onCompleted(null);
        });
    }

    /// {@inheritDoc}
    @Override
    public final void getById(@NotNull String id, @NotNull ICrudService.DatabaseCallback<T> callback) {
        tableReference.child(id).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            DataSnapshot snapshot = task.getResult();
            callback.onCompleted(snapshot != null ? snapshot.getValue(clazz) : null);
        });
    }

    /// {@inheritDoc}
    @Override
    public final void getAll(@NotNull ICrudService.DatabaseCallback<List<T>> callback) {
        tableReference.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<T> list = new ArrayList<>();
            DataSnapshot snapshot = task.getResult();
            if (snapshot != null) {
                snapshot.getChildren().forEach(snap -> {
                    T item = snap.getValue(clazz);
                    if (item != null) {
                        list.add(item);
                    }
                });
            }
            callback.onCompleted(list);
        });
    }

    /// {@inheritDoc}
    @Override
    public final void delete(@NotNull String id, @Nullable DatabaseCallback<Void> callback) {
        tableReference.child(id).removeValue((error, ref) -> {
            if (callback == null) return;
            if (error != null) callback.onFailed(error.toException());
            else callback.onCompleted(null);
        });
    }

    /// {@inheritDoc}
    @Override
    public void update(@NotNull String id, @NotNull UnaryOperator<T> function, @Nullable DatabaseCallback<Void> callback) {
        tableReference.child(id).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                T currentValue = currentData.getValue(clazz);
                if (currentValue != null) {
                    currentValue = function.apply(currentValue);
                }
                currentData.setValue(currentValue);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if (error != null) {
                    Log.e(TAG, "Transaction failed", error.toException());
                    if (callback != null) callback.onFailed(error.toException());
                    return;
                }
                if (callback != null) callback.onCompleted(null);
            }
        });
    }

    // endregion

    /// Returns the database reference for this table.
    /// @return the {@link DatabaseReference} pointing to this table
    protected DatabaseReference getTableReference() {
        return tableReference;
    }

    /// Returns the table name this service operates on.
    /// @return the Firebase path/table name
    protected String getTableName() {
        return tableName;
    }
}
