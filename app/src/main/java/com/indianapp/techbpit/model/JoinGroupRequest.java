package com.indianapp.techbpit.model;

public class JoinGroupRequest {
    public String userId;
    public String groupId;
    public transient int position; // to implement the loader on join button for groups
}
