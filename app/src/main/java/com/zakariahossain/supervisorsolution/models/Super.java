package com.zakariahossain.supervisorsolution.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Super implements Serializable {

    @SerializedName("supervisor_email")
    private String supervisorEmail;

    public Super(String supervisorEmail) {
        this.supervisorEmail = supervisorEmail;
    }

    public String getSupervisorEmail() {
        return supervisorEmail;
    }
}