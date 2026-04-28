package com.example.testapp.services;

import com.example.testapp.services.Impl.CartServiceImpl;
import com.example.testapp.services.Impl.FoodServiceImpl;
import com.example.testapp.services.Impl.UserServiceImpl;

/// Singleton implementation of {@link IDatabaseService}.
/// Wires each domain service to its concrete Firebase implementation.
/// To swap the database backend, change the Impl classes instantiated here.
/// @see IDatabaseService
/// @see UserServiceImpl
/// @see FoodServiceImpl
/// @see CartServiceImpl
public class DatabaseService implements IDatabaseService {

    private static DatabaseService instance;

    private final IUserService userService;
    private final IFoodService foodService;
    private final ICartService cartService;

    private DatabaseService() {
        userService = new UserServiceImpl();
        foodService = new FoodServiceImpl();
        cartService = new CartServiceImpl();
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
