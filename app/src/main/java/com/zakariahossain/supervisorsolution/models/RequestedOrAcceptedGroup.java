package com.zakariahossain.supervisorsolution.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestedOrAcceptedGroup implements Serializable {

    @SerializedName("student_id")
    private Integer studentId;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    public Integer getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
