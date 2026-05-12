package com.example.demo.controller;

import com.example.demo.entity.Job;
import com.example.demo.entity.User;
import com.example.demo.service.JobService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    // Sab jobs dekhne ke liye (Public)
    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyPostedJobs(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Please login first.");
        }

        if (!"RECRUITER".equals(loggedInUser.getRole().name())) {
            return ResponseEntity.status(403).body("Only recruiters can view their posted jobs.");
        }

        List<Job> jobs = jobService.getJobsPostedBy(loggedInUser.getUsername());
        return ResponseEntity.ok(jobs);
    }

    // Ek job ki details
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        Job job = jobService.getJobById(id);
        if (job == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(job);
    }

    // Recruiter - Nayi job post kare
    @PostMapping
    public ResponseEntity<?> postJob(@RequestBody Job job, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body("Please login first.");
        }

        if (!"RECRUITER".equals(loggedInUser.getRole().name())) {
            return ResponseEntity.status(403)
                    .body("Only recruiters can post jobs. Current role: " + loggedInUser.getRole().name());
        }

        try {
            Job savedJob = jobService.postJob(job, loggedInUser);
            return ResponseEntity.ok(savedJob);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Search & Filter Jobs
    @GetMapping("/search")
    public ResponseEntity<List<Job>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String employmentType,
            @RequestParam(required = false) String workMode) {

        List<Job> jobs = jobService.searchJobs(keyword, location, employmentType, workMode);
        return ResponseEntity.ok(jobs);
    }
}
