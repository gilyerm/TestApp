package com.example.testapp.services;

import com.example.testapp.services.impl.firestore.CartFirestoreServiceImpl;
import com.example.testapp.services.impl.firestore.FoodFirestoreServiceImpl;
import com.example.testapp.services.impl.firestore.UserFirestoreServiceImpl;
import com.example.testapp.services.impl.rtdb.CartFirebaseServiceImpl;
import com.example.testapp.services.impl.rtdb.FoodFirebaseServiceImpl;
import com.example.testapp.services.impl.rtdb.UserFirebaseServiceImpl;

/// Singleton implementation of {@link IDatabaseService}.
/// Supports configurable database backend providers (Cloud Firestore or Realtime Database).
/// Defaults to Cloud Firestore.
/// @see IDatabaseService
/// @see DatabaseBackend
public class DatabaseService implements IDatabaseService {

    /// Enum representing available database backend providers.
    public enum DatabaseBackend {
        FIRESTORE,
        REALTIME_DATABASE
    }

    private static DatabaseService instance;
    private static DatabaseBackend currentBackend = DatabaseBackend.FIRESTORE;

    private final IUserService userService;
    private final IFoodService foodService;
    private final ICartService cartService;

    /// Creates a new DatabaseService using the default backend.
    public DatabaseService() {
        this(currentBackend);
    }

    /// Creates a new DatabaseService for the specified backend.
    /// @param backend the desired {@link DatabaseBackend}
    public DatabaseService(DatabaseBackend backend) {
        if (backend == DatabaseBackend.REALTIME_DATABASE) {
            this.userService = new UserFirebaseServiceImpl();
            this.foodService = new FoodFirebaseServiceImpl();
            this.cartService = new CartFirebaseServiceImpl();
        } else {
            this.userService = new UserFirestoreServiceImpl();
            this.foodService = new FoodFirestoreServiceImpl();
            this.cartService = new CartFirestoreServiceImpl();
        }
    }

    /// Returns the shared singleton instance of the database service.
    /// @return the shared {@link DatabaseService} instance
    public static synchronized DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService(currentBackend);
        }
        return instance;
    }

    /// Configures the global database backend provider.
    /// Resets the singleton instance so subsequent calls to {@link #getInstance()} use the new backend.
    /// @param backend the desired {@link DatabaseBackend}
    public static synchronized void setBackend(DatabaseBackend backend) {
        currentBackend = backend;
        instance = null;
    }

    /// Returns the currently active global database backend.
    /// @return the active {@link DatabaseBackend}
    public static DatabaseBackend getBackend() {
        return currentBackend;
    }

    @Override
    public IUserService getUserService() { return userService; }

    @Override
    public IFoodService getFoodService() { return foodService; }

    @Override
    public ICartService getCartService() { return cartService; }
}
