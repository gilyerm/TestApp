package com.example.testapp.screens.addcart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.testapp.models.Cart;
import com.example.testapp.models.Food;
import com.example.testapp.services.ICrudService.DatabaseCallback;
import com.example.testapp.services.IDatabaseService;
import java.util.List;

/**
 * ViewModel for adding and managing carts.
 */
public class AddCartViewModel extends ViewModel {
    private final IDatabaseService databaseService;
    private final MutableLiveData<List<Food>> _foods = new MutableLiveData<>();

    /**
     * LiveData containing the list of available food items.
     */
    public LiveData<List<Food>> foods = _foods;
    private final MutableLiveData<Boolean> _cartCreated = new MutableLiveData<>();

    /**
     * LiveData indicating whether the cart was successfully created.
     */
    public LiveData<Boolean> cartCreated = _cartCreated;
    private final MutableLiveData<String> _error = new MutableLiveData<>();

    /**
     * LiveData containing any error messages.
     */
    public LiveData<String> error = _error;

    /**
     * Constructs an AddCartViewModel with the given database service.
     *
     * @param databaseService the service used for database operations
     */
    public AddCartViewModel(IDatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * Loads all available food items from the database.
     */
    public void loadFoods() {
        databaseService.getFoodService().getAll(new DatabaseCallback<List<Food>>() {
            @Override
            public void onCompleted(List<Food> foods) {
                _foods.postValue(foods);
            }

            @Override
            public void onFailed(Exception e) {
                _error.postValue("Failed to load food items");
            }
        });
    }

    /**
     * Creates a new cart with the specified details.
     *
     * @param cartName      the name of the cart
     * @param selectedFoods the list of foods to add to the cart
     * @param userId        the ID of the user creating the cart
     */
    public void createCart(String cartName, List<Food> selectedFoods, String userId) {
        if (cartName.isEmpty()) {
            _error.postValue("Cart name cannot be empty");
            return;
        }
        if (selectedFoods.isEmpty()) {
            _error.postValue("Please add some food to the cart");
            return;
        }
        String cartId = databaseService.getCartService().generateId();
        Cart cart = new Cart(cartId, cartName, selectedFoods, userId);
        databaseService.getCartService().create(cart, new DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                _cartCreated.postValue(true);
            }

            @Override
            public void onFailed(Exception e) {
                _error.postValue("Failed to create cart");
            }
        });
    }
}
