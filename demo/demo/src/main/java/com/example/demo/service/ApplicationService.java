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

    // Job Seeker - Job pe apply kare
    public Application applyForJob(Long jobId, User applicant, String coverLetter) {
        if (applicationRepository.existsByJobIdAndApplicantId(jobId, applicant.getId())) {
            throw new RuntimeException("You have already applied for this job");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Application application = new Application();
        application.setJob(job);
        application.setApplicant(applicant);
        application.setCoverLetter(coverLetter);
        application.setStatus(Application.ApplicationStatus.PENDING);

        return applicationRepository.save(application);
    }

    // Job Seeker - Apni saari applications dekh sake
    public List<Application> getMyApplications(User applicant) {
        return applicationRepository.findByApplicantId(applicant.getId());
    }

    // Job Seeker - Apni application withdraw kar sake
    public void withdrawApplication(Long applicationId, User applicant) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getApplicant().getId().equals(applicant.getId())) {
            throw new RuntimeException("You are not authorized to withdraw this application");
        }

        applicationRepository.delete(application);
    }

    // Recruiter - Ek specific job ki saari applications dekh sake
    public List<Application> getApplicationsForJob(Long jobId, User recruiter) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Ownership check - Sirf job poster hi dekh sake
        if (!job.getPostedBy().equals(recruiter.getUsername())) {
            throw new RuntimeException("You are not authorized to view applications for this job");
        }

        return applicationRepository.findByJobId(jobId);
    }

    // Recruiter - Application ka status update kare
    public Application updateApplicationStatus(Long applicationId, Application.ApplicationStatus newStatus, User recruiter) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Ownership check - Sirf job poster hi status update kar sake
        if (!application.getJob().getPostedBy().equals(recruiter.getUsername())) {
            throw new RuntimeException("You are not authorized to update this application");
        }

        application.setStatus(newStatus);
        return applicationRepository.save(application);
    }
}
