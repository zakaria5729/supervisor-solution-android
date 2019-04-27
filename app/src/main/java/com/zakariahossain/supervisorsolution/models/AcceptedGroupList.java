package com.zakariahossain.supervisorsolution.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AcceptedGroupList implements Serializable {
    @SerializedName("error")
    private Boolean error;

    @SerializedName("accepted_group_list")
    private List<List<Student>> acceptedGroupList;

    public AcceptedGroupList(Boolean error, List<List<Student>> acceptedGroupList) {
        this.error = error;
        this.acceptedGroupList = acceptedGroupList;
    }

    public Boolean getError() {
        return error;
    }

    public List<List<Student>> getAcceptedGroupList() {
        return acceptedGroupList;
    }
}
