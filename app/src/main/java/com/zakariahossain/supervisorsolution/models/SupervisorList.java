package com.zakariahossain.supervisorsolution.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SupervisorList implements Serializable
{
    @SerializedName("error")
    private Boolean error;

    @SerializedName("supervisors")
    private List<Supervisor> supervisors = null;

    public Boolean getError() {
        return error;
    }

    public List<Supervisor> getSupervisors() {
        return supervisors;
    }
}