package com.example.demo.service;

import com.example.demo.entity.*;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ProfilePdfService {

    private static final DateTimeFormatter YEAR_FORMATTER = DateTimeFormatter.ofPattern("yyyy");
    private static final DeviceRgb PRIMARY_COLOR = new DeviceRgb(32, 74, 135);
    private static final DeviceRgb TEXT_COLOR = new DeviceRgb(35, 35, 35);
    private static final DeviceRgb MUTED_COLOR = new DeviceRgb(90, 90, 90);

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
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(42, 48, 42, 48);

            // Title
            document.add(new Paragraph("Professional Profile")
                    .setBold()
                    .setFontSize(22)
                    .setFontColor(PRIMARY_COLOR)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(4));

            document.add(new Paragraph("Generated on: " + LocalDate.now())
                    .setFontSize(9)
                    .setFontColor(MUTED_COLOR)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(26));

            // Personal Information
            addSectionHeading(document, "Personal Information");
            document.add(detail("Name: ", valueOrDefault(user.getFullName())));
            document.add(detail("Headline: ", valueOrDefault(user.getHeadline())));
            document.add(detail("Email: ", valueOrDefault(user.getEmail())));
            document.add(detail("Phone: ", valueOrDefault(user.getPhoneNumber())));
            document.add(detail("Location: ", buildLocation(user)));

            if (user.getBio() != null && !user.getBio().isEmpty()) {
                document.add(detail("About: ", user.getBio()).setMarginTop(8));
            }

            addSectionHeading(document, "Job Preferences");
            document.add(detail("Desired Job Title: ", valueOrDefault(user.getDesiredJobTitle())));
            document.add(detail("Desired Salary Range: ", valueOrDefault(user.getDesiredSalaryRange())));
            document.add(detail("Preferred Job Type: ", valueOrDefault(user.getPreferredJobType())));
            document.add(detail("Preferred Locations: ", valueOrDefault(user.getPreferredLocations())));
            document.add(detail("Open To Relocation: ", yesNoOrDefault(user.getOpenToRelocation())));

            // Social Links
            if (socialLinks != null && !socialLinks.isEmpty()) {
                addSectionHeading(document, "Social Links");
                for (SocialLink link : socialLinks) {
                    document.add(detail(valueOrDefault(link.getPlatform()) + ": ", valueOrDefault(link.getUrl())));
                }
            }

            // Work Experience
            if (experiences != null && !experiences.isEmpty()) {
                addSectionHeading(document, "Work Experience");
                for (WorkExperience exp : experiences) {
                    document.add(itemTitle(valueOrDefault(exp.getJobTitle()) + " at " + valueOrDefault(exp.getCompanyName())));
                    document.add(meta(buildDateRange(exp.getStartDate(), exp.getEndDate(), exp.getCurrentlyWorking())));
                    if (exp.getEmploymentType() != null || exp.getLocation() != null) {
                        document.add(meta(valueOrDefault(exp.getEmploymentType()) + " | " + valueOrDefault(exp.getLocation())));
                    }
                    if (exp.getDescription() != null) {
                        document.add(bodyText(exp.getDescription()));
                    }
                    addItemGap(document);
                }
            }

            // Education
            if (educations != null && !educations.isEmpty()) {
                addSectionHeading(document, "Education");
                for (Education edu : educations) {
                    document.add(itemTitle(valueOrDefault(edu.getDegree()) + " in " + valueOrDefault(edu.getFieldOfStudy())));
                    document.add(meta(valueOrDefault(edu.getInstitutionName())));
                    document.add(meta(buildDateRange(edu.getStartDate(), edu.getEndDate(), false)));
                    if (edu.getGrade() != null) {
                        document.add(detail("Grade: ", edu.getGrade()));
                    }
                    addItemGap(document);
                }
            }

            // Skills
            if (skills != null && !skills.isEmpty()) {
                addSectionHeading(document, "Skills");
                for (Skill skill : skills) {
                    document.add(detail(valueOrDefault(skill.getSkillName()) + ": ", valueOrDefault(skill.getProficiencyLevel())));
                }
            }

            // Certifications
            if (certifications != null && !certifications.isEmpty()) {
                addSectionHeading(document, "Certifications");
                for (Certification cert : certifications) {
                    document.add(itemTitle(valueOrDefault(cert.getCertificationName())));
                    document.add(detail("Issued by: ", valueOrDefault(cert.getIssuingOrganization())));
                    document.add(meta(buildDateRange(cert.getIssueDate(), cert.getExpiryDate(), false)));
                    if (cert.getCredentialUrl() != null && !cert.getCredentialUrl().isBlank()) {
                        document.add(detail("Credential: ", cert.getCredentialUrl()));
                    }
                    addItemGap(document);
                }
            }

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage());
        }
    }

    private void addSectionHeading(Document document, String heading) {
        document.add(new Paragraph(heading)
                .setBold()
                .setFontSize(14)
                .setFontColor(PRIMARY_COLOR)
                .setMarginTop(16)
                .setMarginBottom(6)
                .setPaddingBottom(3)
                .setBorderBottom(new SolidBorder(PRIMARY_COLOR, 0.8f)));
    }

    private Paragraph detail(String label, String value) {
        return new Paragraph()
                .add(new Text(label).setBold())
                .add(valueOrDefault(value))
                .setFontSize(10)
                .setFontColor(TEXT_COLOR)
                .setMarginTop(0)
                .setMarginBottom(5)
                .setMultipliedLeading(1.15f);
    }

    private Paragraph itemTitle(String value) {
        return new Paragraph(valueOrDefault(value))
                .setBold()
                .setFontSize(11.5f)
                .setFontColor(TEXT_COLOR)
                .setMarginTop(2)
                .setMarginBottom(3);
    }

    private Paragraph meta(String value) {
        return new Paragraph(valueOrDefault(value))
                .setFontSize(9.5f)
                .setFontColor(MUTED_COLOR)
                .setMarginTop(0)
                .setMarginBottom(3);
    }

    private Paragraph bodyText(String value) {
        return new Paragraph(valueOrDefault(value))
                .setFontSize(10)
                .setFontColor(TEXT_COLOR)
                .setMarginTop(2)
                .setMarginBottom(4)
                .setMultipliedLeading(1.2f);
    }

    private void addItemGap(Document document) {
        document.add(new Paragraph("")
                .setMarginTop(0)
                .setMarginBottom(5));
    }

    private String buildDateRange(LocalDate startDate, LocalDate endDate, Boolean currentlyActive) {
        String start = formatYear(startDate);
        String end = Boolean.TRUE.equals(currentlyActive) ? "Present" : formatYear(endDate);

        if ("N/A".equals(start) && "N/A".equals(end)) {
            return "Duration: N/A";
        }

        return "Duration: " + start + " - " + end;
    }

    private String buildLocation(User user) {
        String city = valueOrDefault(user.getCity());
        String country = valueOrDefault(user.getCountry());

        if ("N/A".equals(city)) {
            return country;
        }
        if ("N/A".equals(country)) {
            return city;
        }
        return city + ", " + country;
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
