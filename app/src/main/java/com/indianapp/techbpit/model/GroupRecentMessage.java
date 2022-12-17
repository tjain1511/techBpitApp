package com.indianapp.techbpit.model;

import com.google.gson.annotations.SerializedName;

public class GroupRecentMessage {
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

    //default true to save reduntant fields
    public boolean isSent = true;

    public String date;
}
