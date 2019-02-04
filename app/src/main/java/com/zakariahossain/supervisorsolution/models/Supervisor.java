package com.zakariahossain.supervisorsolution.models;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Supervisor implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("supervisor_name")
    private String supervisorName;

    @SerializedName("supervisor_initial")
    private String supervisorInitial;

    @SerializedName("designation")
    private String designation;

    @SerializedName("supervisor_image")
    private String supervisorImage;

    @SerializedName("phone")
    private String phone;

    @SerializedName("email")
    private String email;

    @SerializedName("research_area")
    private String researchArea;

    @SerializedName("training_experience")
    private String trainingExperience;

    @SerializedName("membership")
    private String membership;

    @SerializedName("publication_project")
    private String publicationProject;

    @SerializedName("profile_link")
    private String profileLink;

    public Supervisor(String id, String supervisorName, String supervisorInitial, String designation, String supervisorImage, String phone, String email, String researchArea, String trainingExperience, String membership, String publicationProject, String profileLink) {
        this.id = id;
        this.supervisorName = supervisorName;
        this.supervisorInitial = supervisorInitial;
        this.designation = designation;
        this.supervisorImage = supervisorImage;
        this.phone = phone;
        this.email = email;
        this.researchArea = researchArea;
        this.trainingExperience = trainingExperience;
        this.membership = membership;
        this.publicationProject = publicationProject;
        this.profileLink = profileLink;
    }

    public String getId() {
        return id;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public String getSupervisorInitial() {
        return supervisorInitial;
    }

    public String getDesignation() {
        return designation;
    }

    public String getSupervisorImage() {
        return supervisorImage;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getResearchArea() {
        return researchArea;
    }

    public String getTrainingExperience() {
        return trainingExperience;
    }

    public String getMembership() {
        return membership;
    }

    public String getPublicationProject() {
        return publicationProject;
    }

    public String getProfileLink() {
        return profileLink;
    }
}
