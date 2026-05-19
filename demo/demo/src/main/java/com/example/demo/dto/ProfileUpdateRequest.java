package com.example.demo.dto;

import lombok.Data;

@Data
public class ProfileUpdateRequest {
    // Personal Info
    private String fillName;
    private String headline;
    private String bio;
    private String phoneNumber;
    private String portfolioUrl;
    private String city;
    private String country;

    // Job Seeker Preferences
    private String desiredJobTitle;
    private String desiredSalaryRange;
    private String preferredJobType;
    private String preferredLocations;
    private Boolean openToRelocation;
}
