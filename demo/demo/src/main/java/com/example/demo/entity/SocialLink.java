package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "social_links")
public class SocialLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String platform;   // "LinkedIn", "GitHub", "Portfolio", "Twitter" etc.

    private String url;

    private String username;  // optional (e.g., @sujal-codes)

    // ===================== GETTERS & SETTERS ===================== \\

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
