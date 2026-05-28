package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.ProfilePdfService;
import com.example.demo.service.ProfileService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
public class ProfilePdfController {

    @Autowired
    private ProfilePdfService profilePdfService;

    @Autowired
    private ProfileService profileService;

    // Download Profile as PDF
    @GetMapping("/download-pdf")
    public void downloadProfileAsPdf(HttpSession session, HttpServletResponse response) throws IOException {

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please login first");
            return;
        }

        // Fetch all data
        List<WorkExperience> experiences = profileService.getUserExperiences(loggedInUser);
        List<Education> educations = profileService.getUserEducations(loggedInUser);
        List<Skill> skills = profileService.getUserSkills(loggedInUser);
        List<Certification> certifications = profileService.getUserCertifications(loggedInUser);
        List<SocialLink> socialLinks = profileService.getUserSocialLinks(loggedInUser);

        // Generate PDF
        byte[] pdfBytes = profilePdfService.generateProfilePdf(
                loggedInUser, experiences, educations, skills, certifications, socialLinks
        );

        // Set response headers for PDF download
        String profileName = Optional.ofNullable(loggedInUser.getFullName())
                .filter(name -> !name.isBlank())
                .orElse(loggedInUser.getUsername())
                .replaceAll("[^a-zA-Z0-9._-]", "_");

        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=Profile_" + profileName + ".pdf");
        response.setContentLength(pdfBytes.length);

        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }
}
