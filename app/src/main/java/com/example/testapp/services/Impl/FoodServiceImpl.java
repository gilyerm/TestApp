package com.example.testapp.services.Impl;

import com.example.testapp.models.Food;
import com.example.testapp.services.IFoodService;

/// Firebase implementation of {@link IFoodService}.
/// Inherits all CRUD operations from {@link FirebaseService} for the "foods" table.
/// No additional methods needed.
/// @see IFoodService
/// @see FirebaseService
public class FoodServiceImpl extends FirebaseService<Food> implements IFoodService {
    public FoodServiceImpl() { super("foods", Food.class); }
}
