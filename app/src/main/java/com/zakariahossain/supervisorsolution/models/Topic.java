package com.zakariahossain.supervisorsolution.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Topic implements Serializable {
    @SerializedName("id")
    private Integer id;

    @SerializedName("topic_name")
    private String topicName;

    @SerializedName("image_path")
    private String imagePath;

    @SerializedName("supervisor_initial")
    private String supervisorInitial;

    @SerializedName("description_one")
    private String descriptionOne;

    @SerializedName("description_two")
    private String descriptionTwo;

    @SerializedName("video_path")
    private String videoPath;

    public Topic(Integer id, String topicName, String imagePath, String supervisorInitial, String descriptionOne, String descriptionTwo, String videoPath) {
        this.id = id;
        this.topicName = topicName;
        this.imagePath = imagePath;
        this.supervisorInitial = supervisorInitial;
        this.descriptionOne = descriptionOne;
        this.descriptionTwo = descriptionTwo;
        this.videoPath = videoPath;
    }

    public Integer getId() {
        return id;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getSupervisorInitial() {
        return supervisorInitial;
    }

    public String getDescriptionOne() {
        return descriptionOne;
    }

    public String getDescriptionTwo() {
        return descriptionTwo;
    }

    public String getVideoPath() {
        return videoPath;
    }
}