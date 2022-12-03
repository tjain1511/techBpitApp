package com.indianapp.techbpit.model;

import android.hardware.lights.LightState;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {
    @SerializedName("_id")
    public String _id;

    @SerializedName("email")
    public String email;

    @SerializedName("username")
    public String username;

    @SerializedName("isActive")
    public boolean isActive;

    @SerializedName("groupsJoined")
    public List<GroupResponse> groupsJoined;

    @SerializedName("image")
    public String imageUrl;


}
