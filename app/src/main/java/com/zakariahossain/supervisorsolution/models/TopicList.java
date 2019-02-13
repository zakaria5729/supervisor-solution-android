package com.zakariahossain.supervisorsolution.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TopicList implements Serializable {
    @SerializedName("error")
    private Boolean error;

    @SerializedName("topics")
    private List<Topic> topics = null;

    public Boolean getError() {
        return error;
    }
    public List<Topic> getTopics() {
        return topics;
    }
}