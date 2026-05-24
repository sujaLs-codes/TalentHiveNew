package com.example.demo.dto;

public class ProfileUpdateRequest {

    // Personal Info
    private String fullName;
    private String headline;
    private String bio;
    private String phoneNumber;
    private String portfolioUrl;
    private String city;
    private String country;

    private String linkedinUrl;

    // Job Seeker Preferences
    private String desiredJobTitle;
    private String desiredSalaryRange;
    private String preferredJobType;
    private String preferredLocations;
    private Boolean openToRelocation;

    // ===================== MANUAL GETTERS & SETTERS =====================

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getHeadline() { return headline; }
    public void setHeadline(String headline) { this.headline = headline; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPortfolioUrl() { return portfolioUrl; }
    public void setPortfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getLinkedinUrl() { return linkedinUrl; }
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }

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