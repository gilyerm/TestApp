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
| `requestPermission(Activity)` | Asks for camera and storage permissions |
| `convertTo64Base(ImageView)` | Compresses the image in an `ImageView` to WEBP (quality 80) and returns it as a base64 string |
| `convertFrom64base(String)` | Turns a base64 string back into a `Bitmap` (returns `null` for `null` input) |

Images are stored as base64 strings inside `Food` documents instead of in Firebase Storage.

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
