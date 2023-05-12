package com.indianapp.techbpit.model;

import java.io.Serializable;

public class SocialPostResponse implements Serializable {
    public UserModel author;
    public String description;
    public String eventDate;
    public String eventTime;
    public AllGroupResponse groupId;
    public String imageUrl;
    public String link;
    public String mode;
    public String organizer;
    public String postType;
    public String resourceTime;
    public String timestamp;
    public String topic;
    public String venue;
}