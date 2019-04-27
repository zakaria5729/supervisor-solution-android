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
    private List<Supervisor> supervisorList = null;

    private int numberOfStudents;
    private String editTextIdOne, editTextNameOne, editTextEmailOne, editTextPhoneOne, editTextIdTwo, editTextNameTwo, editTextEmailTwo, editTextPhoneTwo, editTextIdThree, editTextNameThree, editTextEmailThree, editTextPhoneThree;

    public TitleDefense(String projectInternship, String projectInternshipType, String projectInternshipTitle, String areaOfInterest, String dayEvening, List<Student> studentList, List<Supervisor> supervisorList) {
        this.projectInternship = projectInternship;
        this.projectInternshipType = projectInternshipType;
        this.projectInternshipTitle = projectInternshipTitle;
        this.areaOfInterest = areaOfInterest;
        this.dayEvening = dayEvening;
        this.studentList = studentList;
        this.supervisorList = supervisorList;
    }

    public TitleDefense(int numberOfStudents, String dayEvening, String projectInternship, String projectInternshipType, String projectInternshipTitle) {
        this.numberOfStudents = numberOfStudents;
        this.dayEvening = dayEvening;
        this.projectInternship = projectInternship;
        this.projectInternshipType = projectInternshipType;
        this.projectInternshipTitle = projectInternshipTitle;
    }

    public TitleDefense(int numberOfStudents, String dayEvening, String projectInternship, String projectInternshipType, String projectInternshipTitle, String editTextIdOne, String editTextNameOne, String editTextEmailOne, String editTextPhoneOne) {
        this.numberOfStudents = numberOfStudents;
        this.dayEvening = dayEvening;
        this.projectInternship = projectInternship;
        this.projectInternshipType = projectInternshipType;
        this.projectInternshipTitle = projectInternshipTitle;
        this.editTextIdOne = editTextIdOne;
        this.editTextNameOne = editTextNameOne;
        this.editTextEmailOne = editTextEmailOne;
        this.editTextPhoneOne = editTextPhoneOne;
    }

    public TitleDefense(int numberOfStudents, String dayEvening, String projectInternship, String projectInternshipType, String projectInternshipTitle, String editTextIdOne, String editTextNameOne, String editTextEmailOne, String editTextPhoneOne, String editTextIdTwo, String editTextNameTwo, String editTextEmailTwo, String editTextPhoneTwo) {
        this.numberOfStudents = numberOfStudents;
        this.dayEvening = dayEvening;
        this.projectInternship = projectInternship;
        this.projectInternshipType = projectInternshipType;
        this.projectInternshipTitle = projectInternshipTitle;
        this.editTextIdOne = editTextIdOne;
        this.editTextNameOne = editTextNameOne;
        this.editTextEmailOne = editTextEmailOne;
        this.editTextPhoneOne = editTextPhoneOne;
        this.editTextIdTwo = editTextIdTwo;
        this.editTextNameTwo = editTextNameTwo;
        this.editTextEmailTwo = editTextEmailTwo;
        this.editTextPhoneTwo = editTextPhoneTwo;
    }

    public TitleDefense(int numberOfStudents, String dayEvening, String projectInternship, String projectInternshipType, String projectInternshipTitle, String editTextIdOne, String editTextNameOne, String editTextEmailOne, String editTextPhoneOne, String editTextIdTwo, String editTextNameTwo, String editTextEmailTwo, String editTextPhoneTwo, String editTextIdThree, String editTextNameThree, String editTextEmailThree, String editTextPhoneThree) {
        this.numberOfStudents = numberOfStudents;
        this.dayEvening = dayEvening;
        this.projectInternship = projectInternship;
        this.projectInternshipType = projectInternshipType;
        this.projectInternshipTitle = projectInternshipTitle;
        this.editTextIdOne = editTextIdOne;
        this.editTextNameOne = editTextNameOne;
        this.editTextEmailOne = editTextEmailOne;
        this.editTextPhoneOne = editTextPhoneOne;
        this.editTextIdTwo = editTextIdTwo;
        this.editTextNameTwo = editTextNameTwo;
        this.editTextEmailTwo = editTextEmailTwo;
        this.editTextPhoneTwo = editTextPhoneTwo;
        this.editTextIdThree = editTextIdThree;
        this.editTextNameThree = editTextNameThree;
        this.editTextEmailThree = editTextEmailThree;
        this.editTextPhoneThree = editTextPhoneThree;
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

    public String getEditTextIdOne() {
        return editTextIdOne;
    }

    public String getEditTextNameOne() {
        return editTextNameOne;
    }

    public String getEditTextEmailOne() {
        return editTextEmailOne;
    }

    public String getEditTextPhoneOne() {
        return editTextPhoneOne;
    }

    public String getEditTextIdTwo() {
        return editTextIdTwo;
    }

    public String getEditTextNameTwo() {
        return editTextNameTwo;
    }

    public String getEditTextEmailTwo() {
        return editTextEmailTwo;
    }

    public String getEditTextPhoneTwo() {
        return editTextPhoneTwo;
    }

    public String getEditTextIdThree() {
        return editTextIdThree;
    }

    public String getEditTextNameThree() {
        return editTextNameThree;
    }

    public String getEditTextEmailThree() {
        return editTextEmailThree;
    }

    public String getEditTextPhoneThree() {
        return editTextPhoneThree;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public List<Supervisor> getSupervisorList() {
        return supervisorList;
    }
}