package com.example.demo.controller;

import com.example.demo.dto.MessageResponse;
import com.example.demo.dto.SocialLinkRequest;
import com.example.demo.entity.SocialLink;
import com.example.demo.entity.User;
import com.example.demo.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile/social")
public class SocialLinkController {

    @Autowired
    private ProfileService profileService;

    // Add Social Link (LinkedIn, GitHub, etc.)
    @PostMapping
    public ResponseEntity<MessageResponse> addSocialLink(
            @RequestBody SocialLinkRequest request,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(new MessageResponse("Please login first"));
        }

        try {
            profileService.addSocialLink(loggedInUser, request);
            return ResponseEntity.ok(new MessageResponse("Social link added successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    // Get All Social Links
    @GetMapping
    public ResponseEntity<List<SocialLink>> getSocialLinks(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(profileService.getUserSocialLinks(loggedInUser));
    }

    // Delete Social Link
    @DeleteMapping("/{linkId}")
    public ResponseEntity<MessageResponse> deleteSocialLink(
            @PathVariable Long linkId,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(new MessageResponse("Please login first"));
        }

        try {
            profileService.deleteSocialLink(linkId, loggedInUser);
            return ResponseEntity.ok(new MessageResponse("Social link deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}