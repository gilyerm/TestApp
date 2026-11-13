package com.example.testapp.screens.addfood;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.testapp.models.Food;
import com.example.testapp.services.ICrudService.DatabaseCallback;
import com.example.testapp.services.IDatabaseService;

/**
 * ViewModel for adding new food items.
 */
public class AddFoodViewModel extends ViewModel {
    private final IDatabaseService databaseService;
    private final MutableLiveData<Boolean> foodAddedSuccessfully = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    /**
     * Constructs an AddFoodViewModel with the given database service.
     *
     * @param databaseService the service used for database operations
     */
    public AddFoodViewModel(IDatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * Gets the LiveData indicating if the food was added successfully.
     *
     * @return the LiveData with the success status
     */
    public LiveData<Boolean> getFoodAddedSuccessfully() {
        return foodAddedSuccessfully;
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
     * Adds a new food item to the database.
     *
     * @param name        the name of the food item
     * @param price       the price of the food item
     * @param imageBase64 the base64 encoded image string of the food item
     */
    public void addFood(String name, double price, String imageBase64) {
        String id = databaseService.getFoodService().generateId();
        Food food = new Food(id, name, price, imageBase64);
        databaseService.getFoodService().create(food, new DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                foodAddedSuccessfully.postValue(true);
            }

            @Override
            public void onFailed(Exception e) {
                errorMessage.postValue(e.getMessage() != null ? e.getMessage() : "Failed to add food");
            }
        });
    }
}
