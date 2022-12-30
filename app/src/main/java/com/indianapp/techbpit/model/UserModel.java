package com.indianapp.techbpit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {
    @SerializedName("_id")
    @Expose
    public String _id;

    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("username")
    @Expose
    public String username;

    @SerializedName("isActive")
    @Expose
    public boolean isActive;

    @SerializedName("groupsJoined")
    @Expose
    public List<GroupResponse> groupsJoined;

    @SerializedName("image")
    @Expose
    public String imageUrl;

    @SerializedName("lastMessage")
    @Expose
    public RecentMessage lastMessage;

    public String about;

    public String state;

    public String city;

    public String yearOfStudy;

    @SerializedName("socialLinks")
    @Expose
    public List<SocialPlatform> socialLinks;
    @SerializedName("projects")
    @Expose
    public List<ProjectResponse> projects;
    @SerializedName("skills")
    @Expose
    public List<String> skills;

    public String access_token;

    public String refresh_token;

}
