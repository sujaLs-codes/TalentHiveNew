package com.example.demo.service;

import com.example.demo.entity.Job;
import com.example.demo.entity.User;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    // Sab jobs list karne ke liye
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public List<Job> getJobsPostedBy(String username) {
        return jobRepository.findByPostedBy(username);
    }

    // Ek specific job dikhane ke liye (ID se)
    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    //For insert dummy data of jobs
    public void addDummyJobs() {
        if(jobRepository.count() == 0) {  //If there were no jobs then add
            Job j1 = new Job();
            j1.setTitle("Java Backend Developer");
            j1.setDescription("We are looking for experienced Java Spring Boot developer");
            j1.setCompanyName("TechNova Solutions");
            j1.setLocation("Nagpur, Maharashtra");
            j1.setEmployementType(Job.JobType.FULL_TIME);
            j1.setWorkMode(Job.WorkMode.HYBRID);
            j1.setSalaryRange("8-12 LPA");
            j1.setSkillsRequired("Spring Boot, JPA, MySQL, REST API");
            j1.setPostedBy("recruter1");

            Job j2 = new Job();
            j2.setTitle("Frontend Developer (React)");
            j2.setDescription("Looking for React.js developer with good UI/UX skills");
            j2.setCompanyName("PixelCraft Studio");
            j2.setLocation("Remote");
            j2.setEmployementType(Job.JobType.FULL_TIME);
            j2.setWorkMode(Job.WorkMode.REMOTE);
            j2.setSalaryRange("6-10 LPA");
            j2.setSkillsRequired("React, JavaScript, Tailwind CSS");
            j2.setPostedBy("recruiter1");

            jobRepository.save(j1);
            jobRepository.save(j2);

            System.out.println("jobs added successfully!");
        }
    }

    // Search & Filter Jobs
    public List<Job> searchJobs(String keyword, String location, String employmentType, String workMode) {
        return jobRepository.findAll().stream()
                .filter(job -> matchesKeyword(job, keyword))
                .filter(job -> matchesLocation(job, location))
                .filter(job -> matchesEmploymentType(job, employmentType))
                .filter(job -> matchesWorkMode(job, workMode))
                .collect(Collectors.toList());
    }

    //Recruiter - For job posting
    public Job postJob(Job job, User recruiter) {
        //Only recruiter can post job
        if(!"RECRUITER".equals(recruiter.getRole().name())) {
            throw new RuntimeException("Only recruiter can post jobs");
        }
        job.setPostedBy(recruiter.getUsername());
        return jobRepository.save(job);
    }

    public Job updateJob(Long jobId, Job updatedJob, User recruiter) {
        if (!"RECRUITER".equals(recruiter.getRole().name())) {
            throw new RuntimeException("Only recruiter can update jobs");
        }

        Job existingJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!existingJob.getPostedBy().equals(recruiter.getUsername())) {
            throw new RuntimeException("You are not authorized to update this job");
        }

        existingJob.setTitle(updatedJob.getTitle());
        existingJob.setDescription(updatedJob.getDescription());
        existingJob.setCompanyName(updatedJob.getCompanyName());
        existingJob.setLocation(updatedJob.getLocation());
        existingJob.setEmploymentType(updatedJob.getEmploymentType());
        existingJob.setWorkMode(updatedJob.getWorkMode());
        existingJob.setSalaryRange(updatedJob.getSalaryRange());
        existingJob.setSkillsRequired(updatedJob.getSkillsRequired());

        return jobRepository.save(existingJob);
    }

    public void deleteJob(Long jobId, User recruiter) {
        if (!"RECRUITER".equals(recruiter.getRole().name())) {
            throw new RuntimeException("Only recruiter can delete jobs");
        }

        Job existingJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!existingJob.getPostedBy().equals(recruiter.getUsername())) {
            throw new RuntimeException("You are not authorized to delete this job");
        }

        applicationRepository.deleteByJobId(jobId);
        jobRepository.delete(existingJob);
    }

    private boolean matchesKeyword(Job job, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return true;
        }

        String normalizedKeyword = keyword.trim().toLowerCase(Locale.ROOT);
        return containsIgnoreCase(job.getTitle(), normalizedKeyword)
                || containsIgnoreCase(job.getCompanyName(), normalizedKeyword)
                || containsIgnoreCase(job.getDescription(), normalizedKeyword)
                || containsIgnoreCase(job.getSkillsRequired(), normalizedKeyword);
    }

    private boolean matchesLocation(Job job, String location) {
        if (location == null || location.trim().isEmpty()) {
            return true;
        }
        return containsIgnoreCase(job.getLocation(), location.trim().toLowerCase(Locale.ROOT));
    }

    private boolean matchesEmploymentType(Job job, String employmentType) {
        if (employmentType == null || employmentType.trim().isEmpty()) {
            return true;
        }

        try {
            Job.JobType requestedType = Job.JobType.valueOf(employmentType.trim().toUpperCase(Locale.ROOT));
            return requestedType == job.getEmploymentType();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean matchesWorkMode(Job job, String workMode) {
        if (workMode == null || workMode.trim().isEmpty()) {
            return true;
        }

        try {
            Job.WorkMode requestedMode = Job.WorkMode.valueOf(workMode.trim().toUpperCase(Locale.ROOT));
            return requestedMode == job.getWorkMode();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean containsIgnoreCase(String source, String searchText) {
        return source != null && source.toLowerCase(Locale.ROOT).contains(searchText);
    }
}
