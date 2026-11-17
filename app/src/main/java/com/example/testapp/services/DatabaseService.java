package com.example.testapp.services;

import com.example.testapp.services.impl.firestore.CartFirestoreServiceImpl;
import com.example.testapp.services.impl.firestore.FoodFirestoreServiceImpl;
import com.example.testapp.services.impl.firestore.UserFirestoreServiceImpl;

/// Singleton implementation of {@link IDatabaseService} backed by Cloud Firestore.
/// @see IDatabaseService
public class DatabaseService implements IDatabaseService {

    private static DatabaseService instance;

    private final IUserService userService;
    private final IFoodService foodService;
    private final ICartService cartService;

    /// Creates a new DatabaseService using the Cloud Firestore implementations.
    public DatabaseService() {
        this.userService = new UserFirestoreServiceImpl();
        this.foodService = new FoodFirestoreServiceImpl();
        this.cartService = new CartFirestoreServiceImpl();
    }

    /// Returns the shared singleton instance of the database service.
    /// @return the shared {@link DatabaseService} instance
    public static synchronized DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    @Override
    public IUserService getUserService() { return userService; }

    @Override
    public IFoodService getFoodService() { return foodService; }

    @Override
    public ICartService getCartService() { return cartService; }
}
