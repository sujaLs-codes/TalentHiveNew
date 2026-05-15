package com.example.demo.dto;

import com.example.demo.entity.Job;
import java.time.LocalDateTime;

public class JobResponse {

    private Long id;
    private String title;
    private String description;
    private String companyName;
    private String location;
    private String employmentType;
    private String workMode;
    private String salaryRange;
    private String skillsRequired;
    private String postedBy;
    private LocalDateTime postedAt;

    public static JobResponse from(Job job) {
        JobResponse response = new JobResponse();
        response.id = job.getId();
        response.title = job.getTitle();
        response.description = job.getDescription();
        response.companyName = job.getCompanyName();
        response.location = job.getLocation();
        response.employmentType = job.getEmploymentType() == null ? null : job.getEmploymentType().name();
        response.workMode = job.getWorkMode() == null ? null : job.getWorkMode().name();
        response.salaryRange = job.getSalaryRange();
        response.skillsRequired = job.getSkillsRequired();
        response.postedBy = job.getPostedBy();
        response.postedAt = job.getPostedAt();
        return response;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getLocation() {
        return location;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public String getWorkMode() {
        return workMode;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public String getSkillsRequired() {
        return skillsRequired;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }
}
