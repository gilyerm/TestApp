package com.example.testapp.screens.fooditems;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testapp.R;
import com.example.testapp.adapters.FoodsAdapter;
import com.example.testapp.core.BaseFragment;
import com.example.testapp.core.ViewModelFactory;

/**
 * FoodItemsFragment displays a list of all available food items.
 */
public class FoodItemsFragment extends BaseFragment {
    private static final String TAG = "FoodItemsFragment";
    private RecyclerView recyclerView;
    private FoodsAdapter foodsAdapter;
    private FoodItemsViewModel viewModel;

    /**
     * Returns the title of the fragment.
     *
     * @return the title string for the Food Items screen.
     */
    @Override
    protected String getTitle() {
        return "Food Items";
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food_items, container, false);
    }

    /**
     * Called immediately after onCreateView() has returned, but before any saved state has been restored in to the view.
     *
     * @param view               The View returned by onCreateView().
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this, new ViewModelFactory()).get(FoodItemsViewModel.class);
        setupRecyclerView(view);
        viewModel.getFoods().observe(getViewLifecycleOwner(), foods -> {
            if (foods != null) {
                foodsAdapter.addFoods(foods);
            }
        });
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.loadFoods();
    }

    /**
     * Sets up the RecyclerView for displaying food items.
     *
     * @param view The root view of the fragment containing the RecyclerView.
     */
    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_foods);
        foodsAdapter = new FoodsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(foodsAdapter);
    }
}
