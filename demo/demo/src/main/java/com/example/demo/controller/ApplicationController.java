package com.example.demo.controller;

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

    // Endpoint of apply on job
    @PostMapping("/apply/{jobId}")
    public ResponseEntity<String> applyForJob(@PathVariable Long jobId, @RequestBody String coverLetter, HttpSession session)
    {
        User loggedInUser = (User) session.getAttribute("user");

        if(loggedInUser == null)
        {
            return ResponseEntity.status(401).body("Please login first");
        }

        if(!"JOB_SEEKER".equals(loggedInUser.getRole().name()))
        {
            return ResponseEntity.status(403).body("Only Job Seekers can apply for jobs");
        }

        try {
            Application application = applicationService.applyForJob(jobId, loggedInUser, coverLetter);
            return ResponseEntity.ok("Application submitted successfully for job ID: " + jobId);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Looking for my all applications
    @GetMapping("/my")
    public ResponseEntity<List<Application>> getMyApplications(HttpSession session)
    {
        User loggedInUser = (User) session.getAttribute("user");

        if(loggedInUser == null){
            return ResponseEntity.status(401).build();
        }

        //Only jobseeker can see it
        if (!"JOB_SEEKER".equals(loggedInUser.getRole().name())) {
            return ResponseEntity.status(403).build();
        }

        List<Application> applications = applicationService.getMyApplications(loggedInUser);
        return ResponseEntity.ok(applications);
    }

    //Recruiter :- Can update application status
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<String> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestBody String newStatus,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Please login first");
        }

        if (!"RECRUITER".equals(loggedInUser.getRole().name())) {
            return ResponseEntity.status(403).body("Only Recruiters can update application status");
        }

        try {
            // Extra spaces aur case sensitivity handle kar rahe hain
            String statusStr = newStatus.trim().toUpperCase();
            Application.ApplicationStatus status = Application.ApplicationStatus.valueOf(statusStr);

            Application updatedApplication = applicationService.updateApplicationStatus(applicationId, status, loggedInUser);
            return ResponseEntity.ok("Application status updated to: " + status);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status. Allowed: PENDING, REVIEWING, SHORTLISTED, REJECTED, ACCEPTED");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Recruiter - He can see all the applications of his job
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Application>> getApplicationsForJob(@PathVariable Long jobId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).build();
        }

        if(!"RECRUITER".equals(loggedInUser.getRole().name()))
        {
            return ResponseEntity.status(403).build();
        }

        try {
            List<Application> applications = applicationService.getApplicationsForJob(jobId, loggedInUser);
            return ResponseEntity.ok(applications);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
