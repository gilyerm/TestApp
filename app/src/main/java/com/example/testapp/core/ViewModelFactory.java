package com.example.testapp.core;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.testapp.services.DatabaseService;
import com.example.testapp.services.IDatabaseService;

/**
 * Factory class for instantiating ViewModels with required dependencies.
 * It provides instances of {@link IDatabaseService} to the ViewModels.
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    /**
     * The database service instance to be injected into ViewModels.
     */
    private final IDatabaseService databaseService;

    /**
     * Constructs a new ViewModelFactory initializing the database service.
     */
    public ViewModelFactory() {
        this.databaseService = DatabaseService.getInstance();
    }

    /**
     * Creates a new instance of the given {@code Class}.
     *
     * @param modelClass a {@code Class} whose instance is requested
     * @param <T>        The type parameter for the ViewModel.
     * @return a newly created ViewModel
     * @throws RuntimeException if the ViewModel cannot be instantiated.
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        // As we add more ViewModels, we instantiate them here with the databaseService
        try {
            return modelClass.getConstructor(IDatabaseService.class).newInstance(databaseService);
        } catch (Exception e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }
    }
}
