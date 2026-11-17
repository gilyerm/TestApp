package com.example.testapp.screens.addfood;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.testapp.R;
import com.example.testapp.core.BaseFragment;
import com.example.testapp.utils.ImageUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

/**
 * AddFoodFragment provides the user interface for adding new food items.
 * It handles image selection (gallery or camera) and food detail inputs.
 */
public class AddFoodFragment extends BaseFragment<AddFoodViewModel> implements View.OnClickListener {
    private static final String TAG = "AddFoodFragment";
    private EditText foodNameEditText, foodPriceEditText;
    private Button addFoodButton;
    private ImageView foodImageView;
    private ActivityResultLauncher<Intent> selectImageLauncher;
    private ActivityResultLauncher<Intent> captureImageLauncher;

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
        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri selectedImage = result.getData().getData();
                foodImageView.setImageURI(selectedImage);
                foodImageView.setTag(null);
            }
        });
        captureImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                foodImageView.setImageBitmap(bitmap);
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
        ImageUtil.requestPermission(requireActivity());
        foodNameEditText = view.findViewById(R.id.food_name);
        foodPriceEditText = view.findViewById(R.id.food_price);
        addFoodButton = view.findViewById(R.id.add_food_button);
        foodImageView = view.findViewById(R.id.food_image);
        foodImageView.setTag(R.drawable.image);
        foodImageView.setOnClickListener(this);
        addFoodButton.setOnClickListener(this);
        viewModel.getFoodAddedSuccessfully().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                Toast.makeText(getContext(), "Food added successfully", Toast.LENGTH_SHORT).show();
                foodNameEditText.setText("");
                foodPriceEditText.setText("");
                foodImageView.setImageBitmap(null);
                foodImageView.setTag(R.drawable.image);
            }
        });
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == addFoodButton.getId()) {
            addFoodToDatabase();
        } else if (v.getId() == foodImageView.getId()) {
            showImageSourceDialog();
        }
    }

    /**
     * Displays a dialog allowing the user to select an image source (gallery or camera).
     */
    private void showImageSourceDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_image_source, null);
        bottomSheetDialog.setContentView(bottomSheetView);
        setupImageSourceRow(bottomSheetView.findViewById(R.id.option_gallery), bottomSheetDialog,
                R.string.gallery_title, R.string.gallery_description, R.drawable.gallery_thumbnail, this::selectImageFromGallery);
        setupImageSourceRow(bottomSheetView.findViewById(R.id.option_camera), bottomSheetDialog,
                R.string.camera_title, R.string.camera_description, R.drawable.photo_camera, this::captureImageFromCamera);
        bottomSheetDialog.show();
    }

    /**
     * Fills one image-source row (icon, title, description) and runs the action when it is tapped.
     */
    private void setupImageSourceRow(View row, BottomSheetDialog dialog, int titleRes, int descriptionRes, int iconRes, Runnable action) {
        ((ImageView) row.findViewById(R.id.icon_dialog_item)).setImageResource(iconRes);
        ((TextView) row.findViewById(R.id.text_dialog_item)).setText(titleRes);
        ((TextView) row.findViewById(R.id.text_dialog_item_description)).setText(descriptionRes);
        row.setOnClickListener(v -> {
            dialog.dismiss();
            action.run();
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
     * Launches the intent to select an image from the device's gallery.
     */
    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectImageLauncher.launch(intent);
    }

    /**
     * Launches the intent to capture a new image using the device's camera.
     */
    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImageLauncher.launch(takePictureIntent);
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
