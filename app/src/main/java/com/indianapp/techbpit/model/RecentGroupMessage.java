package com.indianapp.techbpit.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecentGroupMessage implements Serializable {
    public String date;
    @SerializedName("imageUrl")
    public String imageUrl;
    public boolean isSent = true;
    @SerializedName("message")
    public String message;
    @SerializedName("receiver")
    public String receiver;
    @SerializedName("sender")
    public UserModel sender;
    @SerializedName("timestamp")
    public String timestamp;
}