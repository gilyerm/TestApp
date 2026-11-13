package com.example.testapp.screens.admin;

import androidx.lifecycle.ViewModel;
import com.example.testapp.services.IDatabaseService;

/**
 * ViewModel for administrative functionalities.
 */
public class AdminViewModel extends ViewModel {
    private final IDatabaseService databaseService;

    /**
     * Constructs an AdminViewModel with the given database service.
     *
     * @param databaseService the service used for database operations
     */
    public AdminViewModel(IDatabaseService databaseService) {
        this.databaseService = databaseService;
    }
}
