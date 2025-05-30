package com.example.testapp.screens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testapp.R;
import com.example.testapp.adapters.ImageSourceAdapter;
import com.example.testapp.models.Food;
import com.example.testapp.models.ImageSourceOption;
import com.example.testapp.services.DatabaseService;
import com.example.testapp.utils.ImageUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class AddFoodActivity extends BaseActivity implements View.OnClickListener {

    /// tag for logging
    private static final String TAG = "AddFoodActivity";

    private EditText foodNameEditText, foodPriceEditText;
    private Button addFoodButton;
    private ImageView foodImageView;

    /// Activity result launcher for selecting image from gallery
    private ActivityResultLauncher<Intent> selectImageLauncher;
    /// Activity result launcher for capturing image from camera
    private ActivityResultLauncher<Intent> captureImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        /// set the layout for the activity
        setContentView(R.layout.activity_add_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /// request permission for the camera and storage
        ImageUtil.requestPermission(this);

        /// get the views
        foodNameEditText = findViewById(R.id.food_name);
        foodPriceEditText = findViewById(R.id.food_price);
        addFoodButton = findViewById(R.id.add_food_button);
        foodImageView = findViewById(R.id.food_image);

        /// set the tag for the image view
        /// to check if the image was changed from app:srcCompat="@drawable/image"
        foodImageView.setTag(R.drawable.image);

        /// set the on click listeners
        foodImageView.setOnClickListener(this);
        addFoodButton.setOnClickListener(this);

        /// register the activity result launcher for selecting image from gallery
        selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        foodImageView.setImageURI(selectedImage);
                        /// set the tag for the image view to null
                        foodImageView.setTag(null);
                    }
                });

        /// register the activity result launcher for capturing image from camera
        captureImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                        foodImageView.setImageBitmap(bitmap);
                        /// set the tag for the image view to null
                        foodImageView.setTag(null);
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == addFoodButton.getId()) {
            Log.d(TAG, "Add food button clicked");
            addFoodToDatabase();
            return;
        }
        if (v.getId() == foodImageView.getId()) {
            Log.d(TAG, "Select image button clicked");
            showImageSourceDialog();
            return;
        }
    }

    /// show the image source dialog
    /// this dialog will show the options to select image from gallery or capture image from camera
    /// @see ImageSourceOption
    /// @see ImageSourceAdapter
    /// @see BottomSheetDialog
    private void showImageSourceDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_image_source, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        final ArrayList<ImageSourceOption> options = new ArrayList<>();
        options.add(new ImageSourceOption(getString(R.string.gallery_title), getString(R.string.gallery_description), R.drawable.gallery_thumbnail));
        options.add(new ImageSourceOption(getString(R.string.camera_title), getString(R.string.camera_description), R.drawable.photo_camera));

        ListView listView = bottomSheetView.findViewById(R.id.list_view_image_sources);
        ImageSourceAdapter adapter = new ImageSourceAdapter(this, options, option -> {
            bottomSheetDialog.dismiss();
            if (option.getTitle().equals(getString(R.string.gallery_title))) {
                selectImageFromGallery();
            } else if (option.getTitle().equals(getString(R.string.camera_title))) {
                captureImageFromCamera();
            }
        });
        listView.setAdapter(adapter);

        bottomSheetDialog.show();
    }

    /// add the food to the database
    /// @see Food
    private void addFoodToDatabase() {
        /// get the values from the input fields
        String name = foodNameEditText.getText().toString();
        String priceText = foodPriceEditText.getText().toString();


        /// validate the input
        /// stop if the input is not valid
        if (!isValid(name, priceText, foodImageView)) return;

        String imageBase64 = ImageUtil.convertTo64Base(foodImageView);
        /// convert the price to double
        double price = Double.parseDouble(priceText);

        /// generate a new id for the food
        String id = databaseService.generateFoodId();

        Log.d(TAG, "Adding food to database");
        Log.d(TAG, "ID: " + id);
        Log.d(TAG, "Name: " + name);
        Log.d(TAG, "Price: " + price);
//        Log.d(TAG, "Image: " + imageBase64);

        /// create a new food object
        Food food = new Food(id, name, price, imageBase64);

        /// save the food to the database and get the result in the callback
        databaseService.createNewFood(food, new DatabaseService.DatabaseCallback<>() {
            @Override
            public void onCompleted(Void object) {
                Log.d(TAG, "Food added successfully");
                Toast.makeText(AddFoodActivity.this, "Food added successfully", Toast.LENGTH_SHORT).show();
                /// clear the input fields after adding the food for the next food
                Log.d(TAG, "Clearing input fields");
                foodNameEditText.setText("");
                foodPriceEditText.setText("");
                foodImageView.setImageBitmap(null);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Failed to add food", e);
                Toast.makeText(AddFoodActivity.this, "Failed to add food", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /// select image from gallery
    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectImageLauncher.launch(intent);
    }

    /// capture image from camera
    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImageLauncher.launch(takePictureIntent);
    }


    /// validate the input
    private boolean isValid(String name, String priceText, ImageView foodImageView) {
        if (name.isEmpty()) {
            Log.e(TAG, "Name is empty");
            foodNameEditText.setError("Name is required");
            foodNameEditText.requestFocus();
            return false;
        }

        if (priceText.isEmpty()) {
            Log.e(TAG, "Price is empty");
            foodPriceEditText.setError("Price is required");
            foodPriceEditText.requestFocus();
            return false;
        }

        // check if foodImageView was changed from app:srcCompat="@drawable/image"
        if (foodImageView.getTag() != null) {
            Log.e(TAG, "Image is required");
            Toast.makeText(this, "Image is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}