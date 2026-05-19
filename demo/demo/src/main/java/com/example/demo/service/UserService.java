package com.example.demo.service;

import com.example.demo.dto.RecruiterProfileRequest;
import com.example.demo.dto.SignupRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.valueOf(request.getRole().toUpperCase()));
        user.setFullName(request.getUsername());  // baad mein update kar sakte hain

        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User updateProfilePicture(Long userId, String profilePictureUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setProfilePictureUrl(profilePictureUrl);
        return userRepository.save(user);
    }

    // Recruiter Profile Update
    public User updateRecruiterProfile(User recruiter, RecruiterProfileRequest request) {
        if (request.getFullName() != null) recruiter.setFullName(request.getFullName());
        if (request.getJobTitle() != null) recruiter.setJobTitle(request.getJobTitle()); // agar field add kiya ho
        if (request.getBio() != null) recruiter.setBio(request.getBio());
        if (request.getPhoneNumber() != null) recruiter.setPhoneNumber(request.getPhoneNumber());

        // Company fields (agar alag column hai toh)
        if (request.getCompanyName() != null) recruiter.setCompanyName(request.getCompanyName());
        if (request.getCompanyLogoUrl() != null) recruiter.setCompanyLogoUrl(request.getCompanyLogoUrl());
        if (request.getCompanyWebsite() != null) recruiter.setCompanyWebsite(request.getCompanyWebsite());
        if (request.getCompanyLinkedInUrl() != null) recruiter.setCompanyLinkedInUrl(request.getCompanyLinkedInUrl());
        if (request.getCompanySize() != null) recruiter.setCompanySize(request.getCompanySize());
        if (request.getStreet() != null) recruiter.setStreet(request.getStreet());
        if (request.getCity() != null) recruiter.setCity(request.getCity());
        if (request.getState() != null) recruiter.setState(request.getState());
        if (request.getCountry() != null) recruiter.setCountry(request.getCountry());

        return userRepository.save(recruiter);
    }
}
