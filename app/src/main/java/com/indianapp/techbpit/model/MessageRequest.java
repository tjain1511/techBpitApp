package com.indianapp.techbpit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessageRequest implements Serializable {
    @SerializedName("sender")
    @Expose
    public String sender;

    @SerializedName("receiver")
    @Expose
    public String receiver;
}
