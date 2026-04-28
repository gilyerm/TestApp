package com.example.testapp.services.Impl;

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
/// @see UserServiceImpl
/// @see FoodServiceImpl
/// @see CartServiceImpl
public class FirebaseService<T extends Identifiable> implements ICrudService<T> {

    private static final String TAG = "FirebaseService";
    private final DatabaseReference databaseReference;
    private final String tableName;
    private final Class<T> clazz;

    /// Creates a new FirebaseService for the given table.
    /// @param tableName the Firebase path/table name
    /// @param clazz the entity class, used for Firebase deserialization
    protected FirebaseService(@NotNull String tableName, @NotNull Class<T> clazz) {
        this.tableName = tableName;
        this.clazz = clazz;
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    // region ICrudService<T> implementation

    /// {@inheritDoc}
    @Override
    public final String generateId() {
        return databaseReference.child(tableName).push().getKey();
    }

    /// {@inheritDoc}
    @Override
    public final void create(@NotNull T item, @Nullable ICrudService.DatabaseCallback<Void> callback) {
        writeData(tableName + "/" + item.getId(), item, callback);
    }

    /// {@inheritDoc}
    @Override
    public final void getById(@NotNull String id, @NotNull ICrudService.DatabaseCallback<T> callback) {
        databaseReference.child(tableName).child(id).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            callback.onCompleted(task.getResult().getValue(clazz));
        });
    }

    /// {@inheritDoc}
    @Override
    public final void getAll(@NotNull ICrudService.DatabaseCallback<List<T>> callback) {
        databaseReference.child(tableName).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<T> list = new ArrayList<>();
            task.getResult().getChildren().forEach(snap -> list.add(snap.getValue(clazz)));
            callback.onCompleted(list);
        });
    }

    /// {@inheritDoc}
    @Override
    public final void delete(@NotNull String id, @Nullable DatabaseCallback<Void> callback) {
        databaseReference.child(tableName).child(id).removeValue((error, ref) -> {
            if (callback == null) return;
            if (error != null) callback.onFailed(error.toException());
            else callback.onCompleted(null);
        });
    }

    /// {@inheritDoc}
    @Override
    public void update(@NotNull String id, @NotNull UnaryOperator<T> function, @Nullable DatabaseCallback<Void> callback) {
        runTransaction(tableName + "/" + id, function, new DatabaseCallback<T>() {
            @Override
            public void onCompleted(T object) { if (callback != null) callback.onCompleted(null); }

            @Override
            public void onFailed(Exception e) { if (callback != null) callback.onFailed(e); }
        });
    }

    // endregion

    // region Private helpers

    /// Writes data to a specific path in the database.
    /// @param path the full path to write to
    /// @param data the data object to write
    /// @param callback called on completion or failure (nullable)
    private void writeData(@NotNull String path, @NotNull Object data, @Nullable DatabaseCallback<Void> callback) {
        databaseReference.child(path).setValue(data, (error, ref) -> {
            if (callback == null) return;
            if (error != null) callback.onFailed(error.toException());
            else callback.onCompleted(null);
        });
    }

    /// Runs a Firebase transaction on the given path.
    /// Reads the current value, applies the function, and writes the result atomically.
    /// @param path the full path to run the transaction on
    /// @param function the transformation to apply to the current value
    /// @param callback called with the result on success, or with an exception on failure
    private void runTransaction(@NotNull String path, @NotNull UnaryOperator<T> function, @NotNull DatabaseCallback<T> callback) {
        databaseReference.child(path).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                T currentValue = currentData.getValue(clazz);
                if (currentValue != null) currentValue = function.apply(currentValue);
                currentData.setValue(currentValue);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if (error != null) {
                    Log.e(TAG, "Transaction failed", error.toException());
                    callback.onFailed(error.toException());
                    return;
                }
                callback.onCompleted(currentData != null ? currentData.getValue(clazz) : null);
            }
        });
    }

    /// Returns the table name this service operates on.
    /// @return the Firebase path/table name
    protected String getTableName() { return tableName; }

    // endregion
}
