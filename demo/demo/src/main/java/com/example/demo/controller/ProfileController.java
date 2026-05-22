package com.example.demo.controller;

import com.example.demo.dto.MessageResponse;
import com.example.demo.dto.ProfileUpdateRequest;
import com.example.demo.entity.User;
import com.example.demo.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    // Get Current User Full Profile
    @GetMapping("/me")
    public ResponseEntity<User> getProfile(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(user);
    }

    // Update Profile
    @PutMapping("/me")
    public ResponseEntity<MessageResponse> updateProfile(@RequestBody ProfileUpdateRequest request, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");

        if(loggedInUser == null) {
            return ResponseEntity.status(401).body(new MessageResponse("Please login first"));
        }

        try {
            profileService.updateProfile(loggedInUser, request);
            return ResponseEntity.ok(new MessageResponse("Profile updated successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error updating profile: " + e.getMessage()));
        }
    }
}
