package com.example.demo.dto;

public class SocialLinkRequest {

    private String platform;
    private String url;
    private String username;


    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }


    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }


    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
