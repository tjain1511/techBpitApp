package com.indianapp.techbpit.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessageRequest implements Serializable {
    @SerializedName("sender")
    public String sender;

    @SerializedName("receiver")
    public String receiver;
}
