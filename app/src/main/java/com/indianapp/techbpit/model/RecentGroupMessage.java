package com.indianapp.techbpit.model;

import com.google.gson.annotations.SerializedName;

public class RecentGroupMessage {
    @SerializedName("message")
    public String message;

    @SerializedName("timestamp")
    public String timestamp;

    @SerializedName("sender")
    public UserModel sender;

    @SerializedName("receiver")
    public String receiver;

    @SerializedName("imageUrl")
    public String imageUrl;

    //default true to save redundant fields
    public boolean isSent = true;

    public String date;
}
