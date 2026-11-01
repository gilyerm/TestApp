package com.example.testapp.services.impl.rtdb;

import com.example.testapp.models.Food;
import com.example.testapp.services.IFoodService;

/// Firebase Realtime Database implementation of {@link IFoodService}.
/// Inherits all CRUD operations from {@link FirebaseService} for the "foods" table.
/// No additional methods needed.
/// @see IFoodService
/// @see FirebaseService
public class FoodFirebaseServiceImpl extends FirebaseService<Food> implements IFoodService {
    public FoodFirebaseServiceImpl() { super("foods", Food.class); }
}
