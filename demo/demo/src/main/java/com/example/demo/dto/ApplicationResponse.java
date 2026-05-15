package com.example.demo.dto;

import com.example.demo.entity.Application;
import java.time.LocalDateTime;

public class ApplicationResponse {

    private Long id;
    private String status;
    private String coverLetter;
    private LocalDateTime appliedAt;
    private UserResponse applicant;
    private JobResponse job;

    public static ApplicationResponse from(Application application) {
        ApplicationResponse response = new ApplicationResponse();
        response.id = application.getId();
        response.status = application.getStatus() == null ? null : application.getStatus().name();
        response.coverLetter = application.getCoverLetter();
        response.appliedAt = application.getAppliedAt();
        response.applicant = UserResponse.from(application.getApplicant());
        response.job = JobResponse.from(application.getJob());
        return response;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public UserResponse getApplicant() {
        return applicant;
    }

    public JobResponse getJob() {
        return job;
    }
}
