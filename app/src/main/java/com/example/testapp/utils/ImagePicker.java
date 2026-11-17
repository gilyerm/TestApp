package com.example.testapp.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.testapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.function.Supplier;

/// Lets the user pick an image from the gallery or take one with the camera, and shows it in an ImageView.
/// Create it in the fragment's `onCreate` (activity result launchers must be registered before the fragment starts),
/// then call {@link #show()} when the user taps the image.
/// No permissions are needed, see {@link ImageUtil}.
///
/// ```java
/// imagePicker = new ImagePicker(this, () -> imageView, () -> imageChanged = true);
/// imageView.setOnClickListener(v -> imagePicker.show());
/// ```
public class ImagePicker {

    private final Fragment fragment;
    private final ActivityResultLauncher<Intent> galleryLauncher;
    private final ActivityResultLauncher<Intent> cameraLauncher;

    /// @param fragment the fragment that shows the picker; call from its `onCreate`
    /// @param target   returns the ImageView to show the picked image in (looked up when the result arrives,
    ///                 because the view does not exist yet in `onCreate`)
    /// @param onPicked runs after the picked image is shown in the target
    public ImagePicker(@NonNull Fragment fragment, @NonNull Supplier<ImageView> target, @NonNull Runnable onPicked) {
        this.fragment = fragment;
        galleryLauncher = fragment.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                target.get().setImageURI(result.getData().getData());
                onPicked.run();
            }
        });
        cameraLauncher = fragment.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                target.get().setImageBitmap(bitmap);
                onPicked.run();
            }
        });
    }

    /// Show a bottom sheet to choose between gallery and camera.
    public void show() {
        BottomSheetDialog dialog = new BottomSheetDialog(fragment.requireContext());
        View sheet = fragment.getLayoutInflater().inflate(R.layout.bottom_sheet_image_source, null);
        dialog.setContentView(sheet);
        setupRow(sheet.findViewById(R.id.option_gallery), dialog, R.string.gallery_title, R.string.gallery_description,
                R.drawable.gallery_thumbnail,
                () -> galleryLauncher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)));
        setupRow(sheet.findViewById(R.id.option_camera), dialog, R.string.camera_title, R.string.camera_description,
                R.drawable.photo_camera,
                () -> cameraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)));
        dialog.show();
    }

    /// Fill one image-source row (icon, title, description) and run the action when it is tapped.
    private void setupRow(View row, BottomSheetDialog dialog, int titleRes, int descriptionRes, int iconRes, Runnable action) {
        ((ImageView) row.findViewById(R.id.icon_dialog_item)).setImageResource(iconRes);
        ((TextView) row.findViewById(R.id.text_dialog_item)).setText(titleRes);
        ((TextView) row.findViewById(R.id.text_dialog_item_description)).setText(descriptionRes);
        row.setOnClickListener(v -> {
            dialog.dismiss();
            action.run();
        });
    }
}
