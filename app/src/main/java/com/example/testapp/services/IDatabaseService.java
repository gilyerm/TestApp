package com.example.testapp.services;

/// Root interface for the database service layer.
/// Provides access to domain-specific services.
/// @see DatabaseService
/// @see IUserService
/// @see IFoodService
/// @see ICartService
public interface IDatabaseService {

    /// Returns the user service for user-related database operations.
    /// @return the user service instance
    IUserService getUserService();

    /// Returns the food service for food-related database operations.
    /// @return the food service instance
    IFoodService getFoodService();

    /// Returns the cart service for cart-related database operations.
    /// @return the cart service instance
    ICartService getCartService();
}
