package com.example.demo.repository;

import com.example.demo.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Page<Application> findByApplicantId(Long applicantId, Pageable pageable);

    Page<Application> findByJobId(Long jobId, Pageable pageable);

    boolean existsByJobIdAndApplicantId(Long jobId, Long applicantId);

    void deleteByJobId(Long jobId);
}
