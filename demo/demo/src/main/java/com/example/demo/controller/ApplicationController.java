package com.example.demo.controller;

import com.example.demo.dto.ApplicationResponse;
import com.example.demo.dto.CoverLetterRequest;
import com.example.demo.dto.MessageResponse;
import com.example.demo.dto.PagedResponse;
import com.example.demo.dto.UpdateApplicationStatusRequest;
import com.example.demo.entity.Application;
import com.example.demo.entity.User;
import com.example.demo.service.ApplicationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    // Apply for Job with Resume Upload
    @PostMapping("/apply/{jobId}")
    public ResponseEntity<MessageResponse> applyForJob(
            @PathVariable Long jobId,
            @RequestParam(value = "coverLetter", required = false) String coverLetter,
            @RequestParam("resume") MultipartFile resumeFile,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401)
                    .body(new MessageResponse("Please login first"));
        }

        if (!"JOB_SEEKER".equals(loggedInUser.getRole().name())) {
            return ResponseEntity.status(403)
                    .body(new MessageResponse("Only Job Seekers can apply for jobs"));
        }

        try {
            // File validation
            if (resumeFile.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Resume file is required"));
            }

            // Save resume file
            String fileName = System.currentTimeMillis() + "_" + resumeFile.getOriginalFilename();
            String uploadDir = "uploads/resumes/";
            File uploadPath = new File(uploadDir);

            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            Path filePath = Paths.get(uploadDir + fileName);
            resumeFile.transferTo(filePath);

            String resumeUrl = "/uploads/resumes/" + fileName;

            // Service call with resume
            applicationService.applyForJob(jobId, loggedInUser, coverLetter, resumeUrl);

            return ResponseEntity.ok(new MessageResponse("Application submitted successfully with resume for Job ID: " + jobId));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error uploading resume: " + e.getMessage()));
        }
    }

    // My Applications (Job Seeker)
    @GetMapping("/my")
    public ResponseEntity<PagedResponse<ApplicationResponse>> getMyApplications(
            HttpSession session,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).build();
        }

        if (!"JOB_SEEKER".equals(loggedInUser.getRole().name())) {
            return ResponseEntity.status(403).build();
        }

        var applications = applicationService.getMyApplications(loggedInUser, page, size).map(ApplicationResponse::from);
        return ResponseEntity.ok(PagedResponse.from(applications));
    }

    // Job Seeker - Apni application withdraw kare
    @DeleteMapping("/{applicationId}")
    public ResponseEntity<MessageResponse> withdrawApplication(
            @PathVariable Long applicationId,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(new MessageResponse("Please login first"));
        }

        if (!"JOB_SEEKER".equals(loggedInUser.getRole().name())) {
            return ResponseEntity.status(403).body(new MessageResponse("Only Job Seekers can withdraw applications"));
        }

        try {
            applicationService.withdrawApplication(applicationId, loggedInUser);
            return ResponseEntity.ok(new MessageResponse("Application withdrawn successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Recruiter - Job ki saari applications dekh sake
    @GetMapping("/job/{jobId}")
    public ResponseEntity<?> getApplicationsForJob(
            @PathVariable Long jobId,
            HttpSession session,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).build();
        }

        if (!"RECRUITER".equals(loggedInUser.getRole().name())) {
            return ResponseEntity.status(403).build();
        }

        try {
            var applications = applicationService.getApplicationsForJob(jobId, loggedInUser, page, size).map(ApplicationResponse::from);
            return ResponseEntity.ok(PagedResponse.from(applications));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Recruiter - Application ka status update kare
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<MessageResponse> updateApplicationStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody UpdateApplicationStatusRequest request,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(new MessageResponse("Please login first"));
        }

        if (!"RECRUITER".equals(loggedInUser.getRole().name())) {
            return ResponseEntity.status(403).body(new MessageResponse("Only Recruiters can update application status"));
        }

        try {
            String cleanStatus = request.getStatus().trim().toUpperCase();
            Application.ApplicationStatus status = Application.ApplicationStatus.valueOf(cleanStatus);

            applicationService.updateApplicationStatus(applicationId, status, loggedInUser);
            return ResponseEntity.ok(new MessageResponse("Application status updated to: " + status));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid status. Allowed: PENDING, REVIEWING, SHORTLISTED, REJECTED, ACCEPTED"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}