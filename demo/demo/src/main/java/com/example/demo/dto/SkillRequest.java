package com.example.demo.dto;

public class SkillRequest {

    private String skillName;
    private String proficiencyLevel; // BEGINNER, INTERMEDIATE, ADVANCED, EXPERT


    //Getters & Setters
    public String getSkillName() {
        return skillName;
    }
    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }


    public String getProficiencyLevel() {
        return proficiencyLevel;
    }
    public void setProficiencyLevel(String proficiencyLevel) {
        this.proficiencyLevel  = proficiencyLevel;
    }
}
