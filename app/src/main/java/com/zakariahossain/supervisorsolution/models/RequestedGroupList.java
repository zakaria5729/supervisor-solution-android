package com.zakariahossain.supervisorsolution.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RequestedGroupList implements Serializable {

    @SerializedName("error")
    private Boolean error;

    @SerializedName("requested_group_list")
    private List<List<RequestedOrAcceptedGroup>> requestedGroupList = null;

    public Boolean getError() {
        return error;
    }

    public List<List<RequestedOrAcceptedGroup>> getRequestedGroupList() {
        return requestedGroupList;
    }
}
