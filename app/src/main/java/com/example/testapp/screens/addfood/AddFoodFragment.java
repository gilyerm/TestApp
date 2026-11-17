package com.example.testapp.screens.addfood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.testapp.R;
import com.example.testapp.core.BaseFragment;
import com.example.testapp.utils.ImagePicker;
import com.example.testapp.utils.ImageUtil;
import androidx.lifecycle.Observer;
import java.util.function.Supplier;

/**
 * AddFoodFragment provides the user interface for adding new food items.
 * It handles image selection (gallery or camera) and food detail inputs.
 */
public class AddFoodFragment extends BaseFragment<AddFoodViewModel> {
    private static final String TAG = "AddFoodFragment";
    private EditText foodNameEditText, foodPriceEditText;
    private Button addFoodButton;
    private ImageView foodImageView;
    private ImagePicker imagePicker;

    /**
     * Returns the title of the fragment.
     *
     * @return the title string for the Add Food screen.
     */
    @NonNull
    @Override
    protected String getTitle() {
        return "Add Food";
    }

    /**
     * Called to do initial creation of a fragment.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePicker = new ImagePicker(this, new Supplier<ImageView>() {
            @Override
            public ImageView get() {
                return foodImageView;
            }
        }, new Runnable() {
            @Override
            public void run() {
                foodImageView.setTag(null);
            }
        });
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
        return inflater.inflate(R.layout.fragment_add_food, container, false);
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
        viewModel = getViewModel(AddFoodViewModel.class);
        foodNameEditText = view.findViewById(R.id.food_name);
        foodPriceEditText = view.findViewById(R.id.food_price);
        addFoodButton = view.findViewById(R.id.add_food_button);
        foodImageView = view.findViewById(R.id.food_image);
        foodImageView.setTag(R.drawable.image);
        foodImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.show();
            }
        });
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFoodToDatabase();
            }
        });
        viewModel.getFoodAddedSuccessfully().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success != null && success) {
                    Toast.makeText(getContext(), "Food added successfully", Toast.LENGTH_SHORT).show();
                    foodNameEditText.setText("");
                    foodPriceEditText.setText("");
                    foodImageView.setImageBitmap(null);
                    foodImageView.setTag(R.drawable.image);
                }
            }
        });
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Validates input fields and adds the food item to the database using the ViewModel.
     */
    private void addFoodToDatabase() {
        String name = foodNameEditText.getText().toString();
        String priceText = foodPriceEditText.getText().toString();
        if (!isValid(name, priceText, foodImageView)) return;
        String imageBase64 = ImageUtil.convertTo64Base(foodImageView);
        double price = Double.parseDouble(priceText);
        viewModel.addFood(name, price, imageBase64);
    }

    /**
     * Validates the inputs for the new food item.
     *
     * @param name          The name of the food item.
     * @param priceText     The price of the food item as a string.
     * @param foodImageView The ImageView containing the selected food image.
     * @return true if all inputs are valid, false otherwise.
     */
    private boolean isValid(String name, String priceText, ImageView foodImageView) {
        if (name.isEmpty()) {
            foodNameEditText.setError("Name is required");
            foodNameEditText.requestFocus();
            return false;
        }
        if (priceText.isEmpty()) {
            foodPriceEditText.setError("Price is required");
            foodPriceEditText.requestFocus();
            return false;
        }
        if (foodImageView.getTag() != null) {
            Toast.makeText(getContext(), "Image is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
