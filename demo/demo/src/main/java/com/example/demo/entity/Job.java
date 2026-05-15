package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    @JsonProperty("location")
    @JsonAlias("Location")
    private String location;

    @Enumerated(EnumType.STRING) // \
    @Column(nullable = false) // \
    @JsonProperty("employmentType")
    @JsonAlias("employementType")
    private JobType employmentType; // \
                                     // --> JPA saves enum as text/string.
    @Enumerated(EnumType.STRING) //
    @Column(nullable = false) //
    private WorkMode workMode; //

    @Column(nullable = false)
    private String salaryRange; // e.g., "5-8 LPA", "₹50,000 - ₹80,000"

    private String skillsRequired;

    @Column(nullable = false)
    private String postedBy; // Usernames of anyone

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime postedAt;

    public enum JobType {
        FULL_TIME, PART_TIME, INTERNSHIP, CONTRACT
    }

    public enum WorkMode {
        ON_SITE, REMOTE, HYBRID
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public JobType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(JobType employmentType) {
        this.employmentType = employmentType;
    }

    // Backward-compatible typo aliases used elsewhere in the project.
    public JobType getEmployementType() {
        return employmentType;
    }

    public void setEmployementType(JobType employementType) {
        this.employmentType = employementType;
    }

    public WorkMode getWorkMode() {
        return workMode;
    }

    public void setWorkMode(WorkMode workMode) {
        this.workMode = workMode;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }

    public String getSkillsRequired() {
        return skillsRequired;
    }

    public void setSkillsRequired(String skillsRequired) {
        this.skillsRequired = skillsRequired;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }
}
