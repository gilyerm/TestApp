package com.example.testapp.services;

import com.example.testapp.services.impl.firestore.CartFirestoreServiceImpl;
import com.example.testapp.services.impl.firestore.FoodFirestoreServiceImpl;
import com.example.testapp.services.impl.firestore.UserFirestoreServiceImpl;

/// Singleton implementation of {@link IDatabaseService}.
/// Wires each domain service to its concrete Cloud Firestore implementation.
/// To swap the database backend, change the impl classes instantiated here.
/// @see IDatabaseService
/// @see UserFirestoreServiceImpl
/// @see FoodFirestoreServiceImpl
/// @see CartFirestoreServiceImpl
public class DatabaseService implements IDatabaseService {

    private static DatabaseService instance;

    private final IUserService userService;
    private final IFoodService foodService;
    private final ICartService cartService;

    private DatabaseService() {
        userService = new UserFirestoreServiceImpl();
        foodService = new FoodFirestoreServiceImpl();
        cartService = new CartFirestoreServiceImpl();
    }

    /// Returns the singleton instance of the database service.
    /// @return the shared {@link DatabaseService} instance
    public static DatabaseService getInstance() {
        if (instance == null) instance = new DatabaseService();
        return instance;
    }

    @Override
    public IUserService getUserService() { return userService; }

    @Override
    public IFoodService getFoodService() { return foodService; }

    @Override
    public ICartService getCartService() { return cartService; }
}
