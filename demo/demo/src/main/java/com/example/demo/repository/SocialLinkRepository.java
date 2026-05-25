package com.example.demo.repository;

import com.example.demo.entity.SocialLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SocialLinkRepository extends JpaRepository<SocialLink, Long> {
    List<SocialLink> findByUserId(Long userId);
}
