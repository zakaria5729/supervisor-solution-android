package com.zakariahossain.supervisorsolution.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GroupStatusList implements Serializable {
    @SerializedName("error")
    private Boolean error;

    @SerializedName("group_list_status")
    private List<GroupStatus> groupStatusList;

    public Boolean getError() {
        return error;
    }

    public List<GroupStatus> getGroupStatusList() {
        return groupStatusList;
    }

    public static class GroupStatus implements Serializable {
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
}
