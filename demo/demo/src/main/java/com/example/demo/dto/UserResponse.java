package com.example.demo.dto;

import com.example.demo.entity.User;

public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String role;
    private String fullName;
    private String profilePictureUrl;

    public static UserResponse from(User user) {
        UserResponse response = new UserResponse();
        response.id = user.getId();
        response.username = user.getUsername();
        response.email = user.getEmail();
        response.role = user.getRole().name();
        response.fullName = user.getFullName();
        response.profilePictureUrl = user.getProfilePictureUrl();
        return response;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getFullName() {
        return fullName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
}
