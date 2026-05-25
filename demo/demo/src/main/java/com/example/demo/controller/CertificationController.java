package com.example.demo.controller;

import com.example.demo.dto.CertificationRequest;
import com.example.demo.dto.MessageResponse;
import com.example.demo.entity.Certification;
import com.example.demo.entity.User;
import com.example.demo.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile/certifications")
public class CertificationController {

    @Autowired
    private ProfileService profileService;

    // Add Certification
    @PostMapping
    public ResponseEntity<MessageResponse> addCertification(
            @RequestBody CertificationRequest request,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(new MessageResponse("Please login first"));
        }

        try {
            profileService.addCertification(loggedInUser, request);
            return ResponseEntity.ok(new MessageResponse("Certification added successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    // Get All Certifications
    @GetMapping
    public ResponseEntity<List<Certification>> getMyCertifications(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(profileService.getUserCertifications(loggedInUser));
    }

    // Delete Certification
    @DeleteMapping("/{certId}")
    public ResponseEntity<MessageResponse> deleteCertification(
            @PathVariable Long certId,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(new MessageResponse("Please login first"));
        }

        try {
            profileService.deleteCertification(certId, loggedInUser);
            return ResponseEntity.ok(new MessageResponse("Certification deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}