package com.example.demo.controller;

import com.example.demo.dto.JobRequest;
import com.example.demo.dto.JobResponse;
import com.example.demo.dto.MessageResponse;
import com.example.demo.entity.Job;
import com.example.demo.entity.User;
import com.example.demo.service.JobService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    // Sab jobs dekhne ke liye (Public)
    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        List<Job> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(toJobResponses(jobs));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyPostedJobs(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(new MessageResponse("Please login first."));
        }

        if (!"RECRUITER".equals(loggedInUser.getRole().name())) {
            return ResponseEntity.status(403).body(new MessageResponse("Only recruiters can view their posted jobs."));
        }

        List<Job> jobs = jobService.getJobsPostedBy(loggedInUser.getUsername());
        return ResponseEntity.ok(toJobResponses(jobs));
    }

    // Ek job ki details
    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        Job job = jobService.getJobById(id);
        if (job == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(JobResponse.from(job));
    }

    // Recruiter - Nayi job post kare
    @PostMapping
    public ResponseEntity<?> postJob(@Valid @RequestBody JobRequest request, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(new MessageResponse("Please login first."));
        }

        if (!"RECRUITER".equals(loggedInUser.getRole().name())) {
            return ResponseEntity.status(403)
                    .body(new MessageResponse("Only recruiters can post jobs. Current role: " + loggedInUser.getRole().name()));
        }

        try {
            Job job = toJobEntity(request);
            Job savedJob = jobService.postJob(job, loggedInUser);
            return ResponseEntity.ok(JobResponse.from(savedJob));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid employmentType or workMode value."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<?> updateJob(
            @PathVariable Long jobId,
            @Valid @RequestBody JobRequest request,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(new MessageResponse("Please login first."));
        }

        if (!"RECRUITER".equals(loggedInUser.getRole().name())) {
            return ResponseEntity.status(403).body(new MessageResponse("Only recruiters can update jobs."));
        }

        try {
            Job job = toJobEntity(request);
            Job updatedJob = jobService.updateJob(jobId, job, loggedInUser);
            return ResponseEntity.ok(JobResponse.from(updatedJob));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid employmentType or workMode value."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<MessageResponse> deleteJob(
            @PathVariable Long jobId,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return ResponseEntity.status(401).body(new MessageResponse("Please login first."));
        }

        if (!"RECRUITER".equals(loggedInUser.getRole().name())) {
            return ResponseEntity.status(403).body(new MessageResponse("Only recruiters can delete jobs."));
        }

        try {
            jobService.deleteJob(jobId, loggedInUser);
            return ResponseEntity.ok(new MessageResponse("Job deleted successfully."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Search & Filter Jobs
    @GetMapping("/search")
    public ResponseEntity<List<JobResponse>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String employmentType,
            @RequestParam(required = false) String workMode) {

        List<Job> jobs = jobService.searchJobs(keyword, location, employmentType, workMode);
        return ResponseEntity.ok(toJobResponses(jobs));
    }

    private List<JobResponse> toJobResponses(List<Job> jobs) {
        return jobs.stream().map(JobResponse::from).collect(Collectors.toList());
    }

    private Job toJobEntity(JobRequest request) {
        Job job = new Job();
        job.setTitle(request.getTitle().trim());
        job.setDescription(request.getDescription().trim());
        job.setCompanyName(request.getCompanyName().trim());
        job.setLocation(request.getLocation().trim());
        job.setEmploymentType(Job.JobType.valueOf(request.getEmploymentType().trim().toUpperCase(Locale.ROOT)));
        job.setWorkMode(Job.WorkMode.valueOf(request.getWorkMode().trim().toUpperCase(Locale.ROOT)));
        job.setSalaryRange(request.getSalaryRange().trim());
        job.setSkillsRequired(request.getSkillsRequired().trim());
        return job;
    }
}
