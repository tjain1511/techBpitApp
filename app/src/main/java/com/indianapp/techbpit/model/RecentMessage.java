package com.indianapp.techbpit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecentMessage implements Serializable {
    @SerializedName("imageUrl")
    @Expose
    public String imageUrl;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("receiver")
    @Expose
    public String receiver;
    @SerializedName("sender")
    @Expose
    public String sender;
    @SerializedName("timestamp")
    @Expose
    public String timestamp;
}