package com.example.demo.controller;


import com.example.demo.dto.MessageResponse;
import com.example.demo.dto.SkillRequest;
import com.example.demo.entity.Skill;
import com.example.demo.entity.User;
import com.example.demo.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile/skills")
public class SkillController {

    @Autowired
    private ProfileService profileService;

    //Add Skill
    @PostMapping
    public ResponseEntity<MessageResponse> addSkill(
            @RequestBody SkillRequest request,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(new MessageResponse("Please login first"));
        }

        try {
            profileService.addSkill(loggedInUser, request);
            return ResponseEntity.ok(new MessageResponse("Skill added successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    // Get All Skills
    @GetMapping
    public ResponseEntity<List<Skill>> getMySkills(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(profileService.getUserSkills(loggedInUser));
    }

    // Delete Skill
    @DeleteMapping("/{skillId}")
    public ResponseEntity<MessageResponse> deleteSkill(
            @PathVariable Long skillId,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(new MessageResponse("Please login first"));
        }

        try {
            profileService.deleteSkill(skillId, loggedInUser);
            return ResponseEntity.ok(new MessageResponse("Skill deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}
