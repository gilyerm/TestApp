package com.example.testapp.services;

import com.example.testapp.models.Food;

/// Service interface for food-specific database operations.
/// Inherits all CRUD from {@link ICrudService}; no additional methods needed.
/// @see Food
public interface IFoodService extends ICrudService<Food> {
}
