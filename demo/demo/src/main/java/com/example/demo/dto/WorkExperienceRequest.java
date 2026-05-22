package com.example.demo.dto;

import java.time.LocalDate;

public class WorkExperienceRequest {

    private String jobTitle;
    private String companyName;
    private String location;
    private String employmentType;   // FULL_TIME, PART_TIME, etc.
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean currentlyWorking;
    private String description;

    // Getters & Setters
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Boolean getCurrentlyWorking() { return currentlyWorking; }
    public void setCurrentlyWorking(Boolean currentlyWorking) { this.currentlyWorking = currentlyWorking; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}