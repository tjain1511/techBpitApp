package com.indianapp.techbpit.model;

import com.google.gson.annotations.SerializedName;

public class MessageModel {
    public String date;
    @SerializedName("_id")

    public String id;
    @SerializedName("imageUrl")
    public String imageUrl;
    public boolean isError = false;
    @SerializedName("isRead")
    public boolean isRead;
    public boolean isSent = true;
    @SerializedName("message")
    public String message;
    @SerializedName("receiver")
    public String receiver;
    @SerializedName("sender")
    public String sender;
    @SerializedName("timestamp")
    public String timestamp;
}