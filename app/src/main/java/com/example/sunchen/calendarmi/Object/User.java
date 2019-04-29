package com.example.sunchen.calendarmi.Object;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class User {
    public static User CURRENT_USER;

    public static User getCurrentUser() {
        return CURRENT_USER;
    }

    public static void setCurrentUser(User user) {
        CURRENT_USER = user;
    }

    private String name;
    private String email;
    private String password;
    private GoogleSignInClient mGoogleSignInClient;


    public User() {
        this.name = "GoogleUser";
        this.email = "GoogleUser";
        this.password = "GoogleUser";
        mGoogleSignInClient = null;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        mGoogleSignInClient = null;
    }

    public GoogleSignInClient getmGoogleSignInClient() {
        return mGoogleSignInClient;
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

    public void setmGoogleSignInClient(GoogleSignInClient mGoogleSignInClient) {
        this.mGoogleSignInClient = mGoogleSignInClient;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
