package com.zakariahossain.supervisorsolution.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TitleDefense implements Serializable {

    @SerializedName("project_internship")
    private String projectInternship;

    @SerializedName("project_internship_type")
    private String projectInternshipType;

    @SerializedName("project_internship_title")
    private String projectInternshipTitle;

    @SerializedName("area_of_interest")
    private String areaOfInterest;

    @SerializedName("day_evening")
    private String dayEvening;

    @SerializedName("student_list")
    private List<Student> studentList = null;

    @SerializedName("supervisor_list")
    private List<Super> supervisorList = null;

    public TitleDefense(String projectInternship, String projectInternshipType, String projectInternshipTitle, String areaOfInterest, String dayEvening, List<Student> studentList, List<Super> supervisorList) {
        this.projectInternship = projectInternship;
        this.projectInternshipType = projectInternshipType;
        this.projectInternshipTitle = projectInternshipTitle;
        this.areaOfInterest = areaOfInterest;
        this.dayEvening = dayEvening;
        this.studentList = studentList;
        this.supervisorList = supervisorList;
    }

    public String getProjectInternship() {
        return projectInternship;
    }

    public String getProjectInternshipType() {
        return projectInternshipType;
    }

    public String getProjectInternshipTitle() {
        return projectInternshipTitle;
    }

    public String getAreaOfInterest() {
        return areaOfInterest;
    }

    public String getDayEvening() {
        return dayEvening;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public List<Super> getSupervisorList() {
        return supervisorList;
    }
}