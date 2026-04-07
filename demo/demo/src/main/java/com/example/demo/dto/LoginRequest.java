package com.example.demo.dto;

public class LoginRequest {

    private String usernameOrEmail;
    private String password;

    // Manual Getters
    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    // Manual Setters (optional, lekin safe rahega)
    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}