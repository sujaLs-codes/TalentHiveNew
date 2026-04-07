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
}