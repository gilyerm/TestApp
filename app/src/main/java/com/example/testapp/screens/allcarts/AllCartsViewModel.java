package com.example.testapp.screens.allcarts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.testapp.models.Cart;
import com.example.testapp.services.ICrudService.DatabaseCallback;
import com.example.testapp.services.IDatabaseService;
import java.util.List;

/**
 * ViewModel for viewing all available carts.
 */
public class AllCartsViewModel extends ViewModel {
    private final IDatabaseService databaseService;
    private final MutableLiveData<List<Cart>> _carts = new MutableLiveData<>();

    /**
     * LiveData containing all carts from the database.
     */
    public LiveData<List<Cart>> carts = _carts;
    private final MutableLiveData<String> _error = new MutableLiveData<>();

    /**
     * LiveData containing any error messages.
     */
    public LiveData<String> error = _error;

    /**
     * Constructs an AllCartsViewModel with the given database service.
     *
     * @param databaseService the service used for database operations
     */
    public AllCartsViewModel(IDatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * Loads all carts from the database.
     */
    public void loadAllCarts() {
        databaseService.getCartService().getAll(new DatabaseCallback<List<Cart>>() {
            @Override
            public void onCompleted(List<Cart> carts) {
                _carts.postValue(carts);
            }

            @Override
            public void onFailed(Exception e) {
                _error.postValue("Failed to load carts");
            }
        });
    }
}
