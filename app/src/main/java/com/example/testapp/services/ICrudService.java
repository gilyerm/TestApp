package com.example.testapp.services;

import com.example.testapp.models.Identifiable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.UnaryOperator;

/// Generic base interface for CRUD operations on a database table.
/// All domain-specific service interfaces extend this.
/// @param <T> the entity type, must implement {@link Identifiable}
/// @see IUserService
/// @see IFoodService
/// @see ICartService
public interface ICrudService<T extends Identifiable> {

    /// Callback interface for asynchronous database operations.
    /// @param <R> the type of the result returned on success
    interface DatabaseCallback<R> {
        /// Called when the operation completes successfully.
        /// @param object the result of the operation (may be null for void operations)
        void onCompleted(R object);

        /// Called when the operation fails.
        /// @param e the exception that caused the failure
        void onFailed(Exception e);
    }

    /// Generates a new unique ID for an entity.
    /// @return a new unique ID string
    String generateId();

    /// Creates a new entity in the database.
    /// @param item the entity to create
    /// @param callback called on completion or failure (nullable)
    void create(@NotNull T item, @Nullable DatabaseCallback<Void> callback);

    /// Retrieves an entity by its ID.
    /// @param id the entity ID
    /// @param callback called with the entity on success, or with an exception on failure
    void getById(@NotNull String id, @NotNull DatabaseCallback<T> callback);

    /// Retrieves all entities from the table.
    /// @param callback called with the list of entities on success, or with an exception on failure
    void getAll(@NotNull DatabaseCallback<List<T>> callback);

    /// Deletes an entity by its ID.
    /// @param id the entity ID to delete
    /// @param callback called on completion or failure (nullable)
    void delete(@NotNull String id, @Nullable DatabaseCallback<Void> callback);

    /// Updates an entity using a transformation function (transaction-safe).
    /// @param id the entity ID to update
    /// @param function a function that receives the current entity from the db and returns the updated entity
    /// @param callback called on completion or failure (nullable)
    void update(@NotNull String id, @NotNull UnaryOperator<T> function, @Nullable DatabaseCallback<Void> callback);
}
