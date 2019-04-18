package com.example.sunchen.calendarmi.Object;

public class User {
    public static User CURRENT_USER;

    public User getCurrentUser() {
        return CURRENT_USER;
    }

    public void setCurrentUser(User user) {
        CURRENT_USER = user;
    }

    private String name;
    private String email;
    private String password;

    public User() {
        this.name = "GoogleUser";
        this.email = "GoogleUser";
        this.password = "GoogleUser";
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
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
