package com.mobilelive.model;

public class User {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final int mobileNumber;
    private final String userType;

    public User() {
        this.name = "";
        this.email = "";
        this.id = null;
        this.password = "";
        this.mobileNumber = 0;
        this.userType = "";
    }

    public User(Long id, String name, String email, String password, int mobileNumber, String userType) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.userType = userType;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getMobileNumber() {
        return mobileNumber;
    }

    public String getUserType() {
        return userType;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", mobileNumber=" + mobileNumber +
                ", userType='" + userType + '\'' +
                '}';
    }
}

