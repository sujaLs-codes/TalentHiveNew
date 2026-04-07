package com.example.demo.entity;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String Location;

    @Enumerated(EnumType.STRING) // \
    @Column(nullable = false) // \
    private JobType employementType; // \
                                     // --> JPA saves enum as text/string.
    @Enumerated(EnumType.STRING) //
    @Column(nullable = false) //
    private WorkMode workMode; //

    @Column(nullable = false)
    private String salaryRange; // e.g., "5-8 LPA", "₹50,000 - ₹80,000"

    private String skillsRequired;

    @Column(nullable = false)
    private String postedBy; // Usernames of anyone

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime postedAt;

    public enum JobType {
        FULL_TIME, PART_TIME, INTERNSHIP, CONTRACT
    }

    public enum WorkMode {
        ON_SITE, REMOTE, HYBRID
    }
}
