package com.example.demo.controller;


import com.example.demo.dto.MessageResponse;
import com.example.demo.dto.WorkExperienceRequest;
import com.example.demo.entity.WorkExperience;
import com.example.demo.service.ProfileService;
import com.example.demo.entity.User;
import jakarta.servlet.http.HttpSession;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile/experience")
public class WorkExperienceController {

    @Autowired
    private ProfileService profileService;

    //Add new work experience
    @PostMapping
    public ResponseEntity<MessageResponse> addExperience(@RequestBody WorkExperienceRequest request, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");

        if(loggedInUser == null) {
            return ResponseEntity.status(401).body(new MessageResponse("Please login first"));
        }

        if(!"JOB_SEEKER".equals(loggedInUser.getRole().name())) {
            return ResponseEntity.status(403).body(new MessageResponse("Only Job Seeker can add work experience"));
        }

        try {
            profileService.addWorkExperience(loggedInUser, request);
            return ResponseEntity.ok(new MessageResponse("Work experience added successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error : " + e.getMessage()));
        }
    }

    // Get all my experiences
    @GetMapping
    public ResponseEntity<List<WorkExperience>> getMyExperiences(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");

        if(loggedInUser == null) {
            return ResponseEntity.status(401).build();
        }

        List<WorkExperience> experiences = profileService.getUserExperiences(loggedInUser);
        return ResponseEntity.ok(experiences);
    }

    //Delete Experience
    @DeleteMapping("/{experienceId}")
    public ResponseEntity<MessageResponse> deleteExperience(
            @PathVariable Long experienceId,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401)
                    .body(new MessageResponse("Please login first"));
        }

        try {
            profileService.deleteWorkExperience(experienceId, loggedInUser);
            return ResponseEntity.ok(new MessageResponse("Experience deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}
