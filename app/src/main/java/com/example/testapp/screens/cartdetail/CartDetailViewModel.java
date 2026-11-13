package com.example.testapp.screens.cartdetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.testapp.models.Cart;
import com.example.testapp.services.ICrudService.DatabaseCallback;
import com.example.testapp.services.IDatabaseService;

/**
 * ViewModel for viewing the details of a specific cart.
 */
public class CartDetailViewModel extends ViewModel {
    private final IDatabaseService databaseService;
    private final MutableLiveData<Cart> _cart = new MutableLiveData<>();

    /**
     * LiveData containing the details of the cart.
     */
    public LiveData<Cart> cart = _cart;
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();

    /**
     * LiveData indicating whether the cart details are currently loading.
     */
    public LiveData<Boolean> isLoading = _isLoading;
    private final MutableLiveData<String> _error = new MutableLiveData<>();

    /**
     * LiveData containing any error messages.
     */
    public LiveData<String> error = _error;

    /**
     * Constructs a CartDetailViewModel with the given database service.
     *
     * @param databaseService the service used for database operations
     */
    public CartDetailViewModel(IDatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * Loads the details of a cart using its ID.
     *
     * @param cartId the ID of the cart to load
     */
    public void loadCart(String cartId) {
        _isLoading.setValue(true);
        databaseService.getCartService().getById(cartId, new DatabaseCallback<Cart>() {
            @Override
            public void onCompleted(Cart cart) {
                if (cart == null) {
                    _error.postValue("Cart not found");
                } else {
                    _cart.postValue(cart);
                }
                _isLoading.postValue(false);
            }

            @Override
            public void onFailed(Exception e) {
                _error.postValue("Failed to load cart");
                _isLoading.postValue(false);
            }
        });
    }
}
