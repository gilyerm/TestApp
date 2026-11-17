package com.example.testapp.screens.addcart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.testapp.core.BaseViewModel;
import com.example.testapp.models.Cart;
import com.example.testapp.models.Food;
import com.example.testapp.services.ICrudService.DatabaseCallback;
import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for adding and managing carts.
 */
public class AddCartViewModel extends BaseViewModel {
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
    private final MutableLiveData<List<Food>> _selectedFoods = new MutableLiveData<>(new ArrayList<>());

    /**
     * LiveData containing the foods picked for the new cart, each repeated by its quantity.
     * Kept here (not in the adapter) so the picks survive configuration changes.
     */
    public LiveData<List<Food>> selectedFoods = _selectedFoods;

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
     * Adds one unit of a food to the new cart.
     *
     * @param food the food to add; ignored if null
     */
    public void addSelectedFood(Food food) {
        if (food == null) return;
        List<Food> foods = new ArrayList<>(_selectedFoods.getValue());
        foods.add(food);
        _selectedFoods.setValue(foods);
    }

    /**
     * Creates a new cart from the selected foods. On success the selection is cleared.
     *
     * @param cartName the name of the cart
     * @param userId   the ID of the user creating the cart
     */
    public void createCart(String cartName, String userId) {
        List<Food> selectedFoods = new ArrayList<>(_selectedFoods.getValue());
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
                _selectedFoods.postValue(new ArrayList<>());
                _cartCreated.postValue(true);
            }

            @Override
            public void onFailed(Exception e) {
                _error.postValue("Failed to create cart");
            }
        });
    }
}
