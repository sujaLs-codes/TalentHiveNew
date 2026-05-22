package com.example.demo.repository;

import com.example.demo.entity.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {

    List<WorkExperience> findByUserIdOrderByStartDateDesc(Long userId);
}