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

    private String email;
    private String password;
    private String fName;
    private String lName;
    private String phone;

    public User() {
    }

    public User(String uid, String email, String password, String fName, String lName, String phone) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.fName = fName;
        this.lName = lName;
        this.phone = phone;
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

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @NotNull
    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
