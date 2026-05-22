package com.example.demo.service;

import com.example.demo.dto.ProfileUpdateRequest;
import com.example.demo.dto.WorkExperienceRequest;
import com.example.demo.entity.EmploymentType;
import com.example.demo.entity.User;
import com.example.demo.entity.WorkExperience;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {
    @Autowired
    private UserRepository userRepository;

    //Update basic profile (Personal Info + Preferences)
    public User updateProfile(User loggedInUser, ProfileUpdateRequest request) {

        if (request.getFullName() != null) {
            loggedInUser.setFullName(request.getFullName());
        }
        if (request.getHeadline() != null) {
            loggedInUser.setHeadline(request.getHeadline());
        }
        if (request.getBio() != null) {
            loggedInUser.setBio(request.getBio());
        }
        if (request.getPhoneNumber() != null) {
            loggedInUser.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getPortfolioUrl() != null) {
            loggedInUser.setPortfolioUrl(request.getPortfolioUrl());
        }
        if (request.getCity() != null) {
            loggedInUser.setCity(request.getCity());
        }
        if (request.getCountry() != null) {
            loggedInUser.setCountry(request.getCountry());
        }

        //Preferences
        if (request.getDesiredJobTitle() != null) {
            loggedInUser.setDesiredJobTitle(request.getDesiredJobTitle());
        }
        if (request.getDesiredSalaryRange() != null) {
            loggedInUser.setDesiredSalaryRange(request.getDesiredSalaryRange());
        }
        if (request.getPreferredJobType() != null) {
            loggedInUser.setPreferredJobType(request.getPreferredJobType());
        }
        if (request.getPreferredLocations() != null) {
            loggedInUser.setPreferredLocations(request.getPreferredLocations());
        }
        if (request.getOpenToRelocation() != null) {
            loggedInUser.setOpenToRelocation(request.getOpenToRelocation());
        }

        return userRepository.save(loggedInUser);
    }


    @Autowired
    private WorkExperienceRepository workExperienceRepository;

    // Add work Experience
    public WorkExperience addWorkExperience(User user, WorkExperienceRequest request) {
        WorkExperience exp = new WorkExperience();
        exp.setUser(user);
        exp.setJobTitle(request.getJobTitle());
        exp.setCompanyName(request.getCompanyName());
        exp.setLocation(request.getLocation());
        exp.setEmploymentType(EmploymentType.valueOf(request.getEmploymentType()));
        exp.setStartDate(request.getStartDate());
        exp.setEndDate(request.getEndDate());
        exp.setCurrentlyWorking(request.getCurrentlyWorking() != null ? request.getCurrentlyWorking() : false);
        exp.setDescription(request.getDescription());

        return workExperienceRepository.save(exp);
    }

    // Get All Experiences
    public List<WorkExperience> getUserExperiences(User user) {
        return workExperienceRepository.findByUserIdOrderByStartDateDesc(user.getId());
    }

    // Delete Experience
    public void deleteWorkExperience(Long experienceId, User user) {
        WorkExperience exp = workExperienceRepository.findById(experienceId)
                .orElseThrow(() -> new RuntimeException("Experience not found"));

        if (!exp.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only delete your own experience");
        }

        workExperienceRepository.delete(exp);
    }
}
