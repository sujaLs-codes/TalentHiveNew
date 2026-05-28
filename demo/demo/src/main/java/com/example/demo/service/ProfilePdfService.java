package com.example.demo.service;

import com.example.demo.entity.*;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ProfilePdfService {

    private static final DateTimeFormatter YEAR_FORMATTER = DateTimeFormatter.ofPattern("yyyy");

    public byte[] generateProfilePdf(User user,
                                     List<WorkExperience> experiences,
                                     List<Education> educations,
                                     List<Skill> skills,
                                     List<Certification> certifications,
                                     List<SocialLink> socialLinks) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Title
            document.add(new Paragraph("Professional Profile")
                    .setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Generated on: " + LocalDate.now())
                    .setTextAlignment(TextAlignment.CENTER));

            // Personal Information
            document.add(new Paragraph("\nPersonal Information").setBold().setFontSize(14));
            document.add(new Paragraph("Name: " + (user.getFullName() != null ? user.getFullName() : "N/A")));
            document.add(new Paragraph("Headline: " + (user.getHeadline() != null ? user.getHeadline() : "N/A")));
            document.add(new Paragraph("Email: " + user.getEmail()));
            document.add(new Paragraph("Phone: " + (user.getPhoneNumber() != null ? user.getPhoneNumber() : "N/A")));
            document.add(new Paragraph("Location: " + (user.getCity() != null ? user.getCity() : "") +
                    ", " + (user.getCountry() != null ? user.getCountry() : "")));

            if (user.getBio() != null && !user.getBio().isEmpty()) {
                document.add(new Paragraph("\nAbout:\n" + user.getBio()));
            }

            document.add(new Paragraph("\nJob Preferences").setBold().setFontSize(14));
            document.add(new Paragraph("Desired Job Title: " + valueOrDefault(user.getDesiredJobTitle())));
            document.add(new Paragraph("Desired Salary Range: " + valueOrDefault(user.getDesiredSalaryRange())));
            document.add(new Paragraph("Preferred Job Type: " + valueOrDefault(user.getPreferredJobType())));
            document.add(new Paragraph("Preferred Locations: " + valueOrDefault(user.getPreferredLocations())));
            document.add(new Paragraph("Open To Relocation: " + yesNoOrDefault(user.getOpenToRelocation())));

            // Social Links
            if (socialLinks != null && !socialLinks.isEmpty()) {
                document.add(new Paragraph("\nSocial Links").setBold().setFontSize(14));
                for (SocialLink link : socialLinks) {
                    document.add(new Paragraph(link.getPlatform() + ": " + link.getUrl()));
                }
            }

            // Work Experience
            if (experiences != null && !experiences.isEmpty()) {
                document.add(new Paragraph("\nWork Experience").setBold().setFontSize(14));
                for (WorkExperience exp : experiences) {
                    document.add(new Paragraph(exp.getJobTitle() + " at " + exp.getCompanyName()).setBold());
                    document.add(new Paragraph(buildDateRange(exp.getStartDate(), exp.getEndDate(), exp.getCurrentlyWorking())));
                    if (exp.getEmploymentType() != null || exp.getLocation() != null) {
                        document.add(new Paragraph(valueOrDefault(exp.getEmploymentType()) + " | " + valueOrDefault(exp.getLocation())));
                    }
                    if (exp.getDescription() != null) {
                        document.add(new Paragraph(exp.getDescription()));
                    }
                    document.add(new Paragraph(" "));
                }
            }

            // Education
            if (educations != null && !educations.isEmpty()) {
                document.add(new Paragraph("\nEducation").setBold().setFontSize(14));
                for (Education edu : educations) {
                    document.add(new Paragraph(edu.getDegree() + " in " + edu.getFieldOfStudy()).setBold());
                    document.add(new Paragraph(edu.getInstitutionName()));
                    document.add(new Paragraph(buildDateRange(edu.getStartDate(), edu.getEndDate(), false)));
                    if (edu.getGrade() != null) {
                        document.add(new Paragraph("Grade: " + edu.getGrade()));
                    }
                    document.add(new Paragraph(" "));
                }
            }

            // Skills
            if (skills != null && !skills.isEmpty()) {
                document.add(new Paragraph("\nSkills").setBold().setFontSize(14));
                for (Skill skill : skills) {
                    document.add(new Paragraph(skill.getSkillName() + " - " + valueOrDefault(skill.getProficiencyLevel())));
                }
            }

            // Certifications
            if (certifications != null && !certifications.isEmpty()) {
                document.add(new Paragraph("\nCertifications").setBold().setFontSize(14));
                for (Certification cert : certifications) {
                    document.add(new Paragraph(cert.getCertificationName()).setBold());
                    document.add(new Paragraph("Issued by: " + valueOrDefault(cert.getIssuingOrganization())));
                    document.add(new Paragraph(buildDateRange(cert.getIssueDate(), cert.getExpiryDate(), false)));
                    if (cert.getCredentialUrl() != null && !cert.getCredentialUrl().isBlank()) {
                        document.add(new Paragraph("Credential: " + cert.getCredentialUrl()));
                    }
                    document.add(new Paragraph(" "));
                }
            }

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage());
        }
    }

    private String buildDateRange(LocalDate startDate, LocalDate endDate, Boolean currentlyActive) {
        String start = formatYear(startDate);
        String end = Boolean.TRUE.equals(currentlyActive) ? "Present" : formatYear(endDate);

        if ("N/A".equals(start) && "N/A".equals(end)) {
            return "Duration: N/A";
        }

        return "Duration: " + start + " - " + end;
    }

    private String formatYear(LocalDate date) {
        return date != null ? date.format(YEAR_FORMATTER) : "N/A";
    }

    private String valueOrDefault(Object value) {
        return value != null && !value.toString().isBlank() ? value.toString() : "N/A";
    }

    private String yesNoOrDefault(Boolean value) {
        if (value == null) {
            return "N/A";
        }
        return value ? "Yes" : "No";
    }
}
