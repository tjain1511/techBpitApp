package com.indianapp.techbpit;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessageRequest implements Serializable {
    @SerializedName("senderId")
    String senderId;

    @SerializedName("receiverId")
    String receiverId;
}
