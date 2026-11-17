package com.example.testapp.screens.fooditems;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.testapp.core.BaseViewModel;
import com.example.testapp.models.Food;
import com.example.testapp.services.ICrudService.DatabaseCallback;
import java.util.List;

/**
 * ViewModel for managing and viewing food items.
 */
public class FoodItemsViewModel extends BaseViewModel {
    private final MutableLiveData<List<Food>> foods = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    /**
     * Gets the LiveData containing the list of food items.
     *
     * @return the LiveData with food items
     */
    public LiveData<List<Food>> getFoods() {
        return foods;
    }

    /**
     * Gets the LiveData containing any error messages.
     *
     * @return the LiveData containing the error message
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Loads all food items from the database.
     */
    public void loadFoods() {
        databaseService.getFoodService().getAll(new DatabaseCallback<List<Food>>() {
            @Override
            public void onCompleted(List<Food> result) {
                foods.postValue(result);
            }

            @Override
            public void onFailed(Exception e) {
                errorMessage.postValue(e.getMessage() != null ? e.getMessage() : "Failed to load foods");
            }
        });
    }
}
