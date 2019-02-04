package com.zakariahossain.supervisorsolution.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Topic implements Serializable {
    @SerializedName("id")
    private String topicId;

    @SerializedName("topic_name")
    private String topicName;

    @SerializedName("supervisor_initial")
    private String supervisorInitial;

    @SerializedName("description_one")
    private String topicDescriptionOne;

    @SerializedName("description_two")
    private String topicDescriptionTwo;

    @SerializedName("video_path")
    private String topicVideoLink;

    @SerializedName("image_path")
    private String topicImage;

    public Topic(String topicId, String topicName, String supervisorInitial, String topicDescriptionOne, String topicDescriptionTwo, String topicVideoLink, String topicImage) {
        this.topicId = topicId;
        this.topicName = topicName;
        this.supervisorInitial = supervisorInitial;
        this.topicDescriptionOne = topicDescriptionOne;
        this.topicDescriptionTwo = topicDescriptionTwo;
        this.topicVideoLink = topicVideoLink;
        this.topicImage = topicImage;
    }

    public String getTopicId() {
        return topicId;
    }

    public String getSupervisorInitial() {
        return supervisorInitial;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getTopicDescriptionOne() {
        return topicDescriptionOne;
    }

    public String getTopicDescriptionTwo() {
        return topicDescriptionTwo;
    }

    public String getTopicImage() {
        return topicImage;
    }

    public String getTopicVideoLink() {
        return topicVideoLink;
    }
}
