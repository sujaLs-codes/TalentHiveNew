package com.example.demo.controller;

import com.example.demo.dto.EducationRequest;
import com.example.demo.dto.MessageResponse;
import com.example.demo.entity.Education;
import com.example.demo.entity.User;
import com.example.demo.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile/education")
public class EducationController {

    @Autowired
    private ProfileService profileService;

    @PostMapping
    public ResponseEntity<MessageResponse> addEducation(@RequestBody EducationRequest request, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(new MessageResponse("Please login first"));
        }

        if (!"JOB_SEEKER".equals(loggedInUser.getRole().name())) {
            return ResponseEntity.status(403).body(new MessageResponse("Only Job Seekers can add education"));
        }

        try {
            profileService.addEducation(loggedInUser, request);
            return ResponseEntity.ok(new MessageResponse("Education added successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Education>> getMyEducations(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(profileService.getUserEducations(loggedInUser));
    }

    @DeleteMapping("/{educationId}")
    public ResponseEntity<MessageResponse> deleteEducation(
            @PathVariable Long educationId,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(new MessageResponse("Please login first"));
        }

        try {
            profileService.deleteEducation(educationId, loggedInUser);
            return ResponseEntity.ok(new MessageResponse("Education deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}
