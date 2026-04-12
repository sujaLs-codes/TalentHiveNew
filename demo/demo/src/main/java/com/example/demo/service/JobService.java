package com.example.demo.service;

import com.example.demo.entity.Job;
import com.example.demo.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    // Sab jobs list karne ke liye
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // Ek specific job dikhane ke liye (ID se)
    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    //For insert dummy data of jobs
    public void addDummyJobs() {
        if(jobRepository.count() == 0) {  //If there were no jobs then add
            Job j1 = new Job();
            j1.setTitle("Java Backend Developer");
            j1.setDescription("We are looking for experienced Java Spring Boot developer");
            j1.setCompanyName("TechNova Solutions");
            j1.setLocation("Nagpur, Maharashtra");
            j1.setEmployementType(Job.JobType.FULL_TIME);
            j1.setWorkMode(Job.WorkMode.HYBRID);
            j1.setSalaryRange("8-12 LPA");
            j1.setSkillsRequired("Spring Boot, JPA, MySQL, REST API");
            j1.setPostedBy("recruter1");

            Job j2 = new Job();
            j2.setTitle("Frontend Developer (React)");
            j2.setDescription("Looking for React.js developer with good UI/UX skills");
            j2.setCompanyName("PixelCraft Studio");
            j2.setLocation("Remote");
            j2.setEmployementType(Job.JobType.FULL_TIME);
            j2.setWorkMode(Job.WorkMode.REMOTE);
            j2.setSalaryRange("6-10 LPA");
            j2.setSkillsRequired("React, JavaScript, Tailwind CSS");
            j2.setPostedBy("recruiter1");

            jobRepository.save(j1);
            jobRepository.save(j2);

            System.out.println("✅ Dummy jobs added successfully!");
        }
    }
}