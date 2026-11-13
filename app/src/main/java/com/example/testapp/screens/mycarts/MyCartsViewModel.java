package com.example.testapp.screens.mycarts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.testapp.models.Cart;
import com.example.testapp.services.ICrudService.DatabaseCallback;
import com.example.testapp.services.IDatabaseService;
import java.util.List;

/**
 * ViewModel for viewing the user's carts.
 */
public class MyCartsViewModel extends ViewModel {
    private final IDatabaseService databaseService;
    private final MutableLiveData<List<Cart>> _carts = new MutableLiveData<>();

    /**
     * LiveData containing the user's list of carts.
     */
    public LiveData<List<Cart>> carts = _carts;
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();

    /**
     * LiveData indicating whether carts are currently loading.
     */
    public LiveData<Boolean> isLoading = _isLoading;
    private final MutableLiveData<String> _error = new MutableLiveData<>();

    /**
     * LiveData containing any error messages.
     */
    public LiveData<String> error = _error;

    /**
     * Constructs a MyCartsViewModel with the given database service.
     *
     * @param databaseService the service used for database operations
     */
    public MyCartsViewModel(IDatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * Loads the carts associated with the specified user.
     *
     * @param userId the ID of the user whose carts should be loaded
     */
    public void loadCarts(String userId) {
        _isLoading.setValue(true);
        databaseService.getCartService().getUserCartList(userId, new DatabaseCallback<List<Cart>>() {
            @Override
            public void onCompleted(List<Cart> carts) {
                _carts.postValue(carts);
                _isLoading.postValue(false);
            }

            @Override
            public void onFailed(Exception e) {
                _error.postValue("Failed to load carts");
                _isLoading.postValue(false);
            }
        });
    }
}
