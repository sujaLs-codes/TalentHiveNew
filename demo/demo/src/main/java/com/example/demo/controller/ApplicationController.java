package com.example.demo.controller;

import com.example.demo.dto.CoverLetterRequest;
import com.example.demo.dto.UpdateApplicationStatusRequest;
import com.example.demo.entity.Application;
import com.example.demo.entity.User;
import com.example.demo.service.ApplicationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    // Apply for Job
    @PostMapping("/apply/{jobId}")
    public ResponseEntity<String> applyForJob(
            @PathVariable Long jobId,
            @RequestBody CoverLetterRequest request,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Please login first");
        }

        if (!"JOB_SEEKER".equals(loggedInUser.getRole().name())) {
            return ResponseEntity.status(403).body("Only Job Seekers can apply for jobs");
        }

        try {
            String coverLetter = request == null ? null : request.getCoverLetter();
            Application application = applicationService.applyForJob(jobId, loggedInUser, coverLetter);
            return ResponseEntity.ok("Application submitted successfully for job ID: " + jobId);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // My Applications (Job Seeker)
    @GetMapping("/my")
    public ResponseEntity<List<Application>> getMyApplications(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).build();
        }

        if (!"JOB_SEEKER".equals(loggedInUser.getRole().name())) {
            return ResponseEntity.status(403).build();
        }

        List<Application> applications = applicationService.getMyApplications(loggedInUser);
        return ResponseEntity.ok(applications);
    }

    // Job Seeker - Apni application withdraw kare
    @DeleteMapping("/{applicationId}")
    public ResponseEntity<String> withdrawApplication(
            @PathVariable Long applicationId,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Please login first");
        }

        if (!"JOB_SEEKER".equals(loggedInUser.getRole().name())) {
            return ResponseEntity.status(403).body("Only Job Seekers can withdraw applications");
        }

        try {
            applicationService.withdrawApplication(applicationId, loggedInUser);
            return ResponseEntity.ok("Application withdrawn successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Recruiter - Job ki saari applications dekh sake
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Application>> getApplicationsForJob(
            @PathVariable Long jobId,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).build();
        }

        if (!"RECRUITER".equals(loggedInUser.getRole().name())) {
            return ResponseEntity.status(403).build();
        }

        try {
            List<Application> applications = applicationService.getApplicationsForJob(jobId, loggedInUser);
            return ResponseEntity.ok(applications);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Recruiter - Application ka status update kare
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<String> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestBody UpdateApplicationStatusRequest request,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Please login first");
        }

        if (!"RECRUITER".equals(loggedInUser.getRole().name())) {
            return ResponseEntity.status(403).body("Only Recruiters can update application status");
        }

        try {
            String newStatus = request == null ? null : request.getStatus();
            if (newStatus == null || newStatus.isBlank()) {
                return ResponseEntity.badRequest().body("Status is required");
            }

            String cleanStatus = newStatus.trim().toUpperCase();
            Application.ApplicationStatus status = Application.ApplicationStatus.valueOf(cleanStatus);

            Application updated = applicationService.updateApplicationStatus(applicationId, status, loggedInUser);
            return ResponseEntity.ok("Application status updated to: " + status);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status. Allowed: PENDING, REVIEWING, SHORTLISTED, REJECTED, ACCEPTED");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
