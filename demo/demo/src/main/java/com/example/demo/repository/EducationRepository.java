package com.example.demo.repository;

import com.example.demo.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findByUserIdOrderByStartDateDesc(Long userId);
}
