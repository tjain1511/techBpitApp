package com.indianapp.techbpit;

import com.google.gson.annotations.SerializedName;

public class MessageModel {
    @SerializedName("message")
    String message;

    @SerializedName("timestamp")
    String timestamp;

    @SerializedName("sender")
    String sender;

    @SerializedName("receiver")
    String receiver;
    //default true to save reduntant fields
    boolean isSent = true;
}
