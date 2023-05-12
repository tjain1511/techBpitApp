package com.indianapp.techbpit.model;

import java.io.Serializable;
import java.util.List;

public class GroupResponse implements Serializable {
    public String _id;
    public boolean canEdit;
    public String description;
    public String groupName;
    public String image;
    public boolean isJoined;
    public RecentGroupMessage lastMessage;
    public List<UserModel> moderators;
    public List<UserModel> usersJoined;
}