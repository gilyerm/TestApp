package com.example.testapp.services.impl.firestore;

import com.example.testapp.models.Food;
import com.example.testapp.services.IFoodService;

/// Cloud Firestore implementation of {@link IFoodService}.
/// Inherits all CRUD operations from {@link FirestoreService} for the "foods" collection.
/// @see IFoodService
/// @see FirestoreService
public class FoodFirestoreServiceImpl extends FirestoreService<Food> implements IFoodService {

    public FoodFirestoreServiceImpl() {
        super("foods", Food.class);
    }
}
