package com.example.demo.service;

import com.example.demo.entity.Application;
import com.example.demo.entity.Job;
import com.example.demo.entity.User;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;

    // For apply on jobs
    public Application applyForJob(Long jobId, User applicant, String coverLetter) {

        // First check that user applied for this job or not
        if (applicationRepository.existsByJobIdAndApplicantId(jobId, applicant.getId())) {
            throw new RuntimeException("You have already applied for this job");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job Not Found"));

        Application application = new Application();
        application.setJob(job);
        application.setApplicant(applicant);
        application.setCoverLetter(coverLetter);
        application.setStatus(Application.ApplicationStatus.PENDING);

        return applicationRepository.save(application);
    }

    // Recruiter - Job ki saari applications dekh sake
    public List<Application> getApplicationsForJob(Long jobId, User recruiter) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Check karo ki yeh job isi recruiter ne post ki hai
        if (!job.getPostedBy().equals(recruiter.getUsername())) {
            throw new RuntimeException("You are not authorized to view applications for this job");
        }

        return applicationRepository.findByJobId(jobId);
    }

    // Job Seeker - Apni saari applications dekh sake
    public List<Application> getMyApplications(User applicant) {
        return applicationRepository.findByApplicantId(applicant.getId());
    }

    // Recruiter - Application ka status update kare (Temporary - ownership check hata diya)
    public Application updateApplicationStatus(Long applicationId, Application.ApplicationStatus newStatus, User recruiter) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Ownership check temporarily comment out kiya (testing ke liye)
        // if (!application.getJob().getPostedBy().equals(recruiter.getUsername())) {
        //     throw new RuntimeException("You are not authorized to update this application");
        // }

        application.setStatus(newStatus);
        return applicationRepository.save(application);
    }
}