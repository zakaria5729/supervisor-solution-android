package com.zakariahossain.supervisorsolution.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("user_role")
    private String userRole;

    @SerializedName("created_at")
    private String createdAt;

    public User(Integer id, String name, String email, String userRole, String createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.userRole = userRole;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUserRole() {
        return userRole;
    }

    public String getCreatedAt() {
        return createdAt;
    }

}