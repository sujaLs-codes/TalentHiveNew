package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
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
        if (request.getLinkedinUrl() != null) {
            loggedInUser.setLinkedinUrl(request.getLinkedinUrl());
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


    @Autowired
    private SocialLinkRepository socialLinkRepository;

    //Add Social Link
    public SocialLink addSocialLink(User user, SocialLinkRequest request) {
        SocialLink link = new SocialLink();
        link.setUser(user);
        link.setPlatform(request.getPlatform());
        link.setUrl(request.getUrl());
        link.setUsername(request.getUsername());

        return socialLinkRepository.save(link);
    }

    // Get All Social Links of User
    public List<SocialLink> getUserSocialLinks(User user) {
        return socialLinkRepository.findByUserId(user.getId());
    }

    // Delete Social Link
    public void deleteSocialLink(Long linkId, User user) {
        SocialLink link = socialLinkRepository.findById(linkId).orElseThrow(() -> new RuntimeException("Social link not found"));

        if (!link.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only delete your own social link");
        }

        socialLinkRepository.delete(link);
    }

    @Autowired
    private EducationRepository educationRepository;

    // Add education
    public Education addEducation(User user, EducationRequest request) {
        Education education = new Education();
        education.setUser(user);
        education.setInstitutionName(request.getInstitutionName());
        education.setDegree(request.getDegree());
        education.setFieldOfStudy(request.getFieldOfStudy());
        education.setStartDate(request.getStartDate());
        education.setEndDate(request.getEndDate());
        education.setGrade(request.getGrade());

        return educationRepository.save(education);
    }

    // Get all educations of user
    public List<Education> getUserEducations(User user) {
        return educationRepository.findByUserIdOrderByStartDateDesc(user.getId());
    }

    // Delete education
    public void deleteEducation(Long educationId, User user) {
        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new RuntimeException("Education not found"));

        if (!education.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only delete your own education");
        }

        educationRepository.delete(education);
    }


    @Autowired
    private SkillRepository skillRepository;

    // Add Skill
    public Skill addSkill(User user, SkillRequest request) {
        Skill skill = new Skill();
        skill.setUser(user);
        skill.setSkillName(request.getSkillName());
        skill.setProficiencyLevel(ProficiencyLevel.valueOf(request.getProficiencyLevel()));

        return skillRepository.save(skill);
    }

    // Get All Skills
    public List<Skill> getUserSkills(User user) {
        return skillRepository.findByUserId(user.getId());
    }

    // Delete Skill
    public void deleteSkill(Long skillId, User user) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        if (!skill.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only delete your own skill");
        }

        skillRepository.delete(skill);
    }

    @Autowired
    private CertificationRepository certificationRepository;

    //Add Certificate
    public Certification addCertification(User user, CertificationRequest request) {
        Certification cert = new Certification();
        cert.setUser(user);
        cert.setCertificationName(request.getCertificationName());
        cert.setIssuingOrganization(request.getIssuingOrganization());
        cert.setIssueDate(request.getIssueDate());
        cert.setExpiryDate(request.getExpiryDate());
        cert.setCredentialUrl(request.getCredentialUrl());

        return certificationRepository.save(cert);
    }

    // Get All Certifications
    public List<Certification> getUserCertifications(User user) {
        return certificationRepository.findByUserId(user.getId());
    }

    // Delete Certification
    public void deleteCertification(Long certId, User user) {
        Certification cert = certificationRepository.findById(certId)
                .orElseThrow(() -> new RuntimeException("Certification not found"));

        if (!cert.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only delete your own certification");
        }

        certificationRepository.delete(cert);
    }
}
