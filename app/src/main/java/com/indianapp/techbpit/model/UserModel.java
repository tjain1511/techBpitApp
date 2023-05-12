package com.indianapp.techbpit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {
    @SerializedName("_id")
    @Expose
    public String _id;

    @SerializedName("about")
    @Expose
    public String about;
    @SerializedName("access_token")
    @Expose
    public String access_token;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("groupsJoined")
    @Expose
    public List<GroupResponse> groupsJoined;
    @SerializedName("image")
    @Expose
    public String imageUrl;
    @SerializedName("isActive")
    @Expose
    public boolean isActive;
    @SerializedName("lastMessage")
    @Expose
    public RecentMessage lastMessage;
    @SerializedName("projects")
    @Expose
    public List<ProjectResponse> projects;
    public String refresh_token;
    @SerializedName("skills")
    @Expose
    public List<String> skills;
    @SerializedName("socialLinks")
    @Expose
    public List<SocialPlatform> socialLinks;
    public String state;
    @SerializedName("username")
    @Expose
    public String username;
    public String yearOfStudy;
}