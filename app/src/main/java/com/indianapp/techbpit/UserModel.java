package com.indianapp.techbpit;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserModel implements Serializable {
    @SerializedName("_id")
    String _id;

    @SerializedName("email")
    String email;

    @SerializedName("username")
    String username;

    @SerializedName("isActive")
    boolean isActive;

    @SerializedName("image")
    String imageUrl;
}
