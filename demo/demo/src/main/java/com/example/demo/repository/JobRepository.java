package com.example.demo.repository;

import com.example.demo.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByPostedBy(String postedBy);

    //Search in title, company, description
    List<Job> findByTitleContainingIgnoreCaseOrCompanyNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String companyName, String description);

    //Advanced Filter
    List<Job> findByLocationContainingIgnoreCase(String location);

    List<Job> findByEmploymentType(Job.JobType employmentType);

    List<Job> findByWorkMode(Job.WorkMode workMode);
}
