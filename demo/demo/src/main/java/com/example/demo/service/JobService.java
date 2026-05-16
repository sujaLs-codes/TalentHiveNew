package com.example.demo.service;

import com.example.demo.entity.Job;
import com.example.demo.entity.User;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<Job> getAllJobs(int page, int size) {
        Pageable pageable = PageRequest.of(normalizePage(page), normalizeSize(size), Sort.by(Sort.Direction.DESC, "postedAt"));
        return jobRepository.findAll(pageable);
    }

    public Page<Job> getJobsPostedBy(String username, int page, int size) {
        Pageable pageable = PageRequest.of(normalizePage(page), normalizeSize(size), Sort.by(Sort.Direction.DESC, "postedAt"));
        return jobRepository.findByPostedBy(username, pageable);
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
    public Page<Job> searchJobs(String keyword, String location, String employmentType, String workMode, int page, int size) {
        int normalizedPage = normalizePage(page);
        int normalizedSize = normalizeSize(size);
        List<Job> filteredJobs = jobRepository.findAll(Sort.by(Sort.Direction.DESC, "postedAt")).stream()
                .filter(job -> matchesKeyword(job, keyword))
                .filter(job -> matchesLocation(job, location))
                .filter(job -> matchesEmploymentType(job, employmentType))
                .filter(job -> matchesWorkMode(job, workMode))
                .collect(Collectors.toList());

        int start = Math.min(normalizedPage * normalizedSize, filteredJobs.size());
        int end = Math.min(start + normalizedSize, filteredJobs.size());
        List<Job> pageContent = filteredJobs.subList(start, end);

        return new PageImpl<>(pageContent, PageRequest.of(normalizedPage, normalizedSize), filteredJobs.size());
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

    private int normalizePage(int page) {
        return Math.max(page, 0);
    }

    private int normalizeSize(int size) {
        if (size <= 0) {
            return 10;
        }
        return Math.min(size, 100);
    }
}
