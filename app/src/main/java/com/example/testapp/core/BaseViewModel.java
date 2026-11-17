package com.example.testapp.core;

import androidx.lifecycle.ViewModel;

import com.example.testapp.services.DatabaseService;
import com.example.testapp.services.IDatabaseService;

/**
 * Base class for every screen ViewModel. Gives subclasses the shared {@link IDatabaseService}.
 */
public abstract class BaseViewModel extends ViewModel {

    /**
     * The shared database service used for database operations.
     */
    protected final IDatabaseService databaseService = DatabaseService.getInstance();
}
