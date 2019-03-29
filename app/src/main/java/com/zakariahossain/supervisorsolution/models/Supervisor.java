package com.zakariahossain.supervisorsolution.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Supervisor implements Serializable {

    @SerializedName("supervisor_email")
    private String supervisorEmail;

    public Supervisor(String supervisorEmail) {
        this.supervisorEmail = supervisorEmail;
    }

    public String getSupervisorEmail() {
        return supervisorEmail;
    }
}