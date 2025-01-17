package com.example.testapp.models;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/// Model class for the user
/// This class represents a user in the application
/// It contains the user's information
/// @see Serializable
public class User implements Serializable {

    /// unique id of the user
    private String uid;

    private String email, password;
    private String firstName, LastName;
    private String phone;
    private boolean isAdmin;

    public User() {
    }

    public User(String uid, String email, String password, String firstName, String lastName, String phone, boolean isAdmin) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        LastName = lastName;
        this.phone = phone;
        this.isAdmin = isAdmin;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", phone='" + phone + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        User user = (User) object;
        return uid.equals(user.uid);
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }
}
