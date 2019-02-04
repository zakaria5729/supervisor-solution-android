package com.zakariahossain.supervisorsolution.models;

import java.io.Serializable;

public class TitleDefenseRegistration implements Serializable {

    private int numberOfStudents;
    private String dayEvening, projectInternship, projectInternshipType, projectInternshipTitle;
    private String editTextIdOne, editTextNameOne, editTextEmailOne, editTextPhoneOne, editTextIdTwo, editTextNameTwo, editTextEmailTwo, editTextPhoneTwo, editTextIdThree, editTextNameThree, editTextEmailThree, editTextPhoneThree;

    public TitleDefenseRegistration(int numberOfStudents, String dayEvening, String projectInternship, String projectInternshipType, String projectInternshipTitle) {
        this.numberOfStudents = numberOfStudents;
        this.dayEvening = dayEvening;
        this.projectInternship = projectInternship;
        this.projectInternshipType = projectInternshipType;
        this.projectInternshipTitle = projectInternshipTitle;
    }

    public TitleDefenseRegistration(int numberOfStudents, String dayEvening, String projectInternship, String projectInternshipType, String projectInternshipTitle, String editTextIdOne, String editTextNameOne, String editTextEmailOne, String editTextPhoneOne) {
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

    public TitleDefenseRegistration(int numberOfStudents, String dayEvening, String projectInternship, String projectInternshipType, String projectInternshipTitle, String editTextIdOne, String editTextNameOne, String editTextEmailOne, String editTextPhoneOne, String editTextIdTwo, String editTextNameTwo, String editTextEmailTwo, String editTextPhoneTwo) {
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

    public TitleDefenseRegistration(int numberOfStudents, String dayEvening, String projectInternship, String projectInternshipType, String projectInternshipTitle, String editTextIdOne, String editTextNameOne, String editTextEmailOne, String editTextPhoneOne, String editTextIdTwo, String editTextNameTwo, String editTextEmailTwo, String editTextPhoneTwo, String editTextIdThree, String editTextNameThree, String editTextEmailThree, String editTextPhoneThree) {
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

    public String getDayEvening() {
        return dayEvening;
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
}
