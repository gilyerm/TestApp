package com.example.testapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;

/// Utility class for image operations
/// Contains methods for converting images to base64 and back.
/// No permissions are needed: the gallery picker (`ACTION_PICK`) and the camera app (`ACTION_IMAGE_CAPTURE`)
/// grant access themselves. Do not declare `CAMERA` in the manifest — then `ACTION_IMAGE_CAPTURE` requires it
/// and throws `SecurityException` if the user denied it.
public class ImageUtil {

    /// Longest side, in pixels, of an image before it is saved.
    /// Keeps a food document well under Firestore's 1 MiB limit and the list fast to scroll.
    private static final int MAX_IMAGE_SIZE = 512;

    /// Convert an ImageView image to a WebP base64 string, scaled down to at most {@link #MAX_IMAGE_SIZE} pixels per side
    /// @param postImage The image to convert
    /// @return The base64 string representation of the image in WebP format, or null if the view has no bitmap
    public static @Nullable String convertTo64Base(@NotNull final ImageView postImage) {
        if (!(postImage.getDrawable() instanceof BitmapDrawable bitmapDrawable)) {
            return null;
        }

        Bitmap bitmap = bitmapDrawable.getBitmap();
        if (bitmap == null) {
            return null;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        scaleDown(bitmap).compress(Bitmap.CompressFormat.WEBP_LOSSY, 80, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    /// Convert a base64 string to an image
    /// @param base64Code The base64 string to convert
    /// @return The image represented by the base64 string, or null if the string is empty or not a valid image
    public static @Nullable Bitmap convertFrom64base(@Nullable final String base64Code) {
        if (base64Code == null || base64Code.isEmpty()) {
            return null;
        }
        try {
            byte[] decodedString = Base64.decode(base64Code, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (IllegalArgumentException e) {
            return null; // corrupt data in the database: show no image instead of crashing
        }
    }

    /// Scale a bitmap so its longest side is at most {@link #MAX_IMAGE_SIZE}, keeping the aspect ratio
    /// @param bitmap The bitmap to scale
    /// @return The same bitmap if it is already small enough, otherwise a scaled copy
    private static @NotNull Bitmap scaleDown(@NotNull Bitmap bitmap) {
        int longest = Math.max(bitmap.getWidth(), bitmap.getHeight());
        if (longest <= MAX_IMAGE_SIZE) {
            return bitmap;
        }
        float scale = (float) MAX_IMAGE_SIZE / longest;
        int width = Math.max(1, Math.round(bitmap.getWidth() * scale));
        int height = Math.max(1, Math.round(bitmap.getHeight() * scale));
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }
}
