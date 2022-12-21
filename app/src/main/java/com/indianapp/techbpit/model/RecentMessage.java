package com.indianapp.techbpit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecentMessage implements Serializable {
    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("timestamp")
    @Expose
    public String timestamp;

    @SerializedName("sender")
    @Expose
    public String sender;

    @SerializedName("receiver")
    @Expose
    public String receiver;

    @SerializedName("imageUrl")
    @Expose
    public String imageUrl;

}

