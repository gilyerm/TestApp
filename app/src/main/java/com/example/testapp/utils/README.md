# 🛠️ utils

> Static helper classes used across the app.

## `Validator`

| Method | Rule |
|:-------|:-----|
| `isEmailValid(String)` | Matches `Patterns.EMAIL_ADDRESS` |
| `isPasswordValid(String)` | At least 6 characters |
| `isPhoneValid(String)` | At least 10 characters and matches `Patterns.PHONE` |
| `isNameValid(String)` | At least 3 characters |

All methods return `false` for `null`.

## `ImageUtil`

| Method | Description |
|:-------|:------------|
| `convertTo64Base(ImageView)` | Scales the image in an `ImageView` down to at most 512 px per side, compresses it to WEBP (quality 80) and returns it as a base64 string |
| `convertFrom64base(String)` | Turns a base64 string back into a `Bitmap` (returns `null` for empty or corrupt input) |

Images are stored as base64 strings inside `Food` documents instead of in Firebase Storage. The 512 px limit keeps each document well under Firestore's 1 MiB limit.

**No permissions needed.** The gallery picker (`ACTION_PICK`) and the camera app (`ACTION_IMAGE_CAPTURE`) grant access themselves. Don't add `CAMERA` to the manifest: once declared, `ACTION_IMAGE_CAPTURE` requires it and crashes if the user denied it.

## `ImagePicker`

Bottom sheet to pick an image from the gallery or take one with the camera; shows the result in an `ImageView`. Used by Add Food and User Profile.

| Method | Description |
|:-------|:------------|
| `ImagePicker(Fragment, Supplier<ImageView>, Runnable onPicked)` | Create in the fragment's `onCreate` (result launchers must be registered before it starts). The ImageView is looked up when the result arrives. |
| `show()` | Opens the gallery / camera bottom sheet |

```java
imagePicker = new ImagePicker(this, new Supplier<ImageView>() {
    @Override public ImageView get() { return imageView; }
}, new Runnable() {
    @Override public void run() { imageChanged = true; }
});
imageView.setOnClickListener(new View.OnClickListener() {
    @Override public void onClick(View v) { imagePicker.show(); }
});
```

## `SharedPreferencesUtil`

Keeps the logged-in user on the device (saved as JSON with Gson).

| Method | Description |
|:-------|:------------|
| `saveUser(Context, User)` | Saves the logged-in user |
| `getUser(Context)` | Returns the saved user, or `null` |
| `getUserId(Context)` | Returns the saved user's ID |
| `isUserLoggedIn(Context)` | `true` if a user is saved |
| `signOutUser(Context)` | Removes the saved user |
| `clear(Context)` | Deletes all saved preferences |
