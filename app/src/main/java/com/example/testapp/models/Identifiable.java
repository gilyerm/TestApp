package com.example.testapp.models;

/// An object with a unique ID.
/// Every database entity implements it, so the generic services and adapters can find items by ID.
/// @see com.example.testapp.services.ICrudService
/// @see com.example.testapp.adapters.BaseAdapter
public interface Identifiable {
    /// @return the unique ID of this object
    String getId();
}
