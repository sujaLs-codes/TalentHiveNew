package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "languages")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String languageName;

    @Enumerated(EnumType.STRING)
    private ProficiencyLevel proficiency;   // We can reuse ProficiencyLevel enum

    // ===================== GETTERS & SETTERS =====================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getLanguageName() { return languageName; }
    public void setLanguageName(String languageName) { this.languageName = languageName; }

    public ProficiencyLevel getProficiency() { return proficiency; }
    public void setProficiency(ProficiencyLevel proficiency) { this.proficiency = proficiency; }
}