package com.example.demo.dto;

import java.time.LocalDate;

public class EducationRequest {
    private String institutionName;
    private String degree;
    private String fieldOfStudy;
    private LocalDate startDate;
    private LocalDate endDate;
    private String grade;

    // ===================== MANUAL GETTERS & SETTERS ===================== \\

    public String getInstitutionName() { return institutionName; }
    public void setInstitutionName(String institutionName) { this.institutionName = institutionName; }


    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }


    public String getFieldOfStudy() { return fieldOfStudy; }
    public void setFieldOfStudy(String fieldOfStudy) { this.fieldOfStudy = fieldOfStudy; }


    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }


    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}
