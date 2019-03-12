package com.zakariahossain.supervisorsolution.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GroupStatus implements Serializable {
    @SerializedName("supervisor_email")
    private String supervisorEmail;

    @SerializedName("is_accepted")
    private int isAccepted;

    public String getSupervisorEmail() {
        return supervisorEmail;
    }

    public int getIsAccepted() {
        return isAccepted;
    }
}
