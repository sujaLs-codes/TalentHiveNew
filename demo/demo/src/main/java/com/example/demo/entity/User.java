package com.example.demo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Personal Info
    private String fullName;
    private String headline;
    private String jobTitle;           // Users ke liye
    private String bio;
    private String phoneNumber;
    private String profilePictureUrl;
    private String portfolioUrl;

    // Company Info (Recruiter)
    private String companyName;
    private String companyLogoUrl;
    private String companyWebsite;
    private String companyLinkedInUrl;
    private String companySize;

    // Address
    private String street;
    private String city;
    private String state;
    private String country;

    // Job_Seeker Preferences
    private String desiredJobTitle;
    private String desiredSalaryRange;          // e.g., "5-8 LPA"
    private String preferredJobType;            // Remote, Hybrid, On-site
    private String preferredLocations;
    private Boolean openToRelocation;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum Role {
        JOB_SEEKER, RECRUITER, ADMIN
    }

    // ===================== MANUAL GETTERS & SETTERS =====================

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }


    public String getHeadline() { return headline; }
    public void setHeadline(String headline) { this.headline = headline; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }

    public String getPortfolioUrl() { return portfolioUrl; }
    public void setPortfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getCompanyLogoUrl() { return companyLogoUrl; }
    public void setCompanyLogoUrl(String companyLogoUrl) { this.companyLogoUrl = companyLogoUrl; }

    public String getCompanyWebsite() { return companyWebsite; }
    public void setCompanyWebsite(String companyWebsite) { this.companyWebsite = companyWebsite; }

    public String getCompanyLinkedInUrl() { return companyLinkedInUrl; }
    public void setCompanyLinkedInUrl(String companyLinkedInUrl) { this.companyLinkedInUrl = companyLinkedInUrl; }

    public String getCompanySize() { return companySize; }
    public void setCompanySize(String companySize) { this.companySize = companySize; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }


    // Job Seeker Preferences
    public String getDesiredJobTitle() { return desiredJobTitle; }
    public void setDesiredJobTitle(String desiredJobTitle) { this.desiredJobTitle = desiredJobTitle; }

    public String getDesiredSalaryRange() { return desiredSalaryRange; }
    public void setDesiredSalaryRange(String desiredSalaryRange) { this.desiredSalaryRange = desiredSalaryRange; }

    public String getPreferredJobType() { return preferredJobType; }
    public void setPreferredJobType(String preferredJobType) { this.preferredJobType = preferredJobType; }

    public String getPreferredLocations() { return preferredLocations; }
    public void setPreferredLocations(String preferredLocations) { this.preferredLocations = preferredLocations; }

    public Boolean getOpenToRelocation() { return openToRelocation; }
    public void setOpenToRelocation(Boolean openToRelocation) { this.openToRelocation = openToRelocation; }
}