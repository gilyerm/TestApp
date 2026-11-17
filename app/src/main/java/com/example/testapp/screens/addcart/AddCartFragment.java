package com.example.testapp.screens.addcart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testapp.R;
import com.example.testapp.adapters.FoodsAdapter;
import com.example.testapp.core.BaseFragment;
import com.example.testapp.models.Food;
import com.example.testapp.utils.SharedPreferencesUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

/**
 * AddCartFragment provides a user interface for creating a new cart.
 * Users can select foods, add them to the cart, and name the cart.
 */
public class AddCartFragment extends BaseFragment<AddCartViewModel> implements View.OnClickListener {
    private static final String TAG = "AddCartFragment";
    private AutoCompleteTextView foodSelector;
    private Button createCartButton;
    private FloatingActionButton addButton;
    private EditText etCartName;
    private ArrayAdapter<Food> foodDropdownAdapter;
    private FoodsAdapter foodsAdapter;
    private Food _selectedFood;
    private List<Food> allFoods;

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
        View view = inflater.inflate(R.layout.fragment_add_cart, container, false);
        addButton = view.findViewById(R.id.btn_add_cart);
        createCartButton = view.findViewById(R.id.btn_create_add_cart);
        etCartName = view.findViewById(R.id.edit_text_cart_name);
        foodSelector = view.findViewById(R.id.autocomplete_food_selector);
        RecyclerView selectedFoodsRecyclerView = view.findViewById(R.id.recycler_view_selected_foods);
        foodsAdapter = new FoodsAdapter();
        selectedFoodsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        selectedFoodsRecyclerView.setAdapter(foodsAdapter);
        allFoods = new ArrayList<>();
        foodDropdownAdapter = new ArrayAdapter<Food>(requireContext(), android.R.layout.simple_dropdown_item_1line, allFoods) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Food food = getItem(position);
                if (food != null && view instanceof TextView) {
                    ((TextView) view).setText(food.getName() + " - $" + String.format("%.2f", food.getPrice()));
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                Food food = getItem(position);
                if (food != null && view instanceof TextView) {
                    ((TextView) view).setText(food.getName() + " - $" + String.format("%.2f", food.getPrice()));
                }
                return view;
            }
        };
        foodSelector.setAdapter(foodDropdownAdapter);
        foodSelector.setOnItemClickListener((parent, v, position, id) -> {
            Food selectedFood = (Food) parent.getItemAtPosition(position);
            if (selectedFood != null) {
                Log.d(TAG, "Selected food: " + selectedFood.getName());
                _selectedFood = selectedFood;
                foodSelector.setText(selectedFood.getName(), false);
            }
        });
        addButton.setOnClickListener(this);
        createCartButton.setOnClickListener(this);
        return view;
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
        viewModel = getViewModel(AddCartViewModel.class);
        viewModel.foods.observe(getViewLifecycleOwner(), foods -> {
            allFoods.clear();
            allFoods.addAll(foods);
            foodDropdownAdapter.notifyDataSetChanged();
            if (!foods.isEmpty()) {
                _selectedFood = foods.get(0);
                foodSelector.setText(foods.get(0).getName(), false);
            }
        });
        viewModel.cartCreated.observe(getViewLifecycleOwner(), success -> {
            if (success) {
                int itemCount = foodsAdapter.getFoods().size();
                foodsAdapter.getFoods().clear();
                etCartName.setText("");
                foodsAdapter.notifyItemRangeRemoved(0, itemCount);
                Toast.makeText(requireContext(), "Cart created successfully", Toast.LENGTH_SHORT).show();
                navigationHost.goBack();
            }
        });
        viewModel.error.observe(getViewLifecycleOwner(), errorMsg -> {
            Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show();
        });
        viewModel.loadFoods();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == addButton.getId()) {
            if (_selectedFood != null) {
                foodsAdapter.addFood(_selectedFood);
            }
        } else if (v.getId() == createCartButton.getId()) {
            String cartName = etCartName.getText().toString().trim();
            List<Food> selectedFoods = new ArrayList<>(foodsAdapter.getFoods());
            String userId = SharedPreferencesUtil.getUserId(requireContext());
            viewModel.createCart(cartName, selectedFoods, userId);
        }
    }

    /**
     * Returns the title of the fragment.
     *
     * @return the title string for the Add Cart screen.
     */
    @NonNull
    @Override
    protected String getTitle() {
        return "Add Cart";
    }
}
