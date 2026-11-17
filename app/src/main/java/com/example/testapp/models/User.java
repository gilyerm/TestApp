package com.example.testapp.models;

import java.io.Serializable;

/// A user of the app.
/// Stored in the database; the logged-in user is also saved on the device.
/// Two users are equal if they have the same ID.
/// @see com.example.testapp.services.IUserService
/// @see com.example.testapp.utils.SharedPreferencesUtil
public class User implements Serializable, Identifiable {

    /// unique ID of the user
    private String id;

    /// email, used to log in
    private String email;

    /// password, used to log in (stored as plain text)
    private String password;

    /// first and last name
    private String firstName, lastName;

    /// phone number
    private String phone;

    /// true if the user can see the Admin screens
    private boolean isAdmin;

    /// profile picture as a WebP base64 string, or null if the user has none
    /// @see com.example.testapp.utils.ImageUtil
    private String imageBase64;

    /// Empty constructor, required by Firebase to convert documents to objects.
    public User() {
    }

    /// Creates a user without a profile picture.
    /// @param id        unique ID of the user
    /// @param email     email, used to log in
    /// @param password  password, used to log in
    /// @param firstName first name
    /// @param lastName  last name
    /// @param phone     phone number
    /// @param isAdmin   true if the user is an admin
    public User(String id, String email, String password, String firstName, String lastName, String phone, boolean isAdmin) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.isAdmin = isAdmin;
    }

    /// @return unique ID of the user
    public String getId() {
        return id;
    }

    /// @param id unique ID of the user
    public void setId(String id) {
        this.id = id;
    }

    /// @return email, used to log in
    public String getEmail() {
        return email;
    }

    /// @param email email, used to log in
    public void setEmail(String email) {
        this.email = email;
    }

    /// @return password, used to log in
    public String getPassword() {
        return password;
    }

    /// @param password password, used to log in
    public void setPassword(String password) {
        this.password = password;
    }

    /// @return first name
    public String getFirstName() {
        return firstName;
    }

    /// @param firstName first name
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /// @return last name
    public String getLastName() {
        return lastName;
    }

    /// @param lastName last name
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /// @return phone number
    public String getPhone() {
        return phone;
    }

    /// @param phone phone number
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /// @return true if the user is an admin
    public boolean isAdmin() {
        return isAdmin;
    }

    /// @param admin true to make the user an admin
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    /// @return profile picture as a WebP base64 string, or null if the user has none
    public String getImageBase64() {
        return imageBase64;
    }

    /// @param imageBase64 profile picture as a WebP base64 string, or null for none
    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    /// @return the user's fields, without the picture (too long to log)
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }

    /// Two users are equal if they have the same ID.
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        User user = (User) object;
        return id.equals(user.id);
    }

    /// @return hash of the ID, matching {@link #equals(Object)}
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /// First and last name joined with a space.
    /// Firebase also saves it as a `fullName` field, because it looks like a getter.
    /// @return the full name
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
