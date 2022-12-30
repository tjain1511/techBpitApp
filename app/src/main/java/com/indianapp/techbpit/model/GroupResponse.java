package com.indianapp.techbpit.model;

import java.io.Serializable;
import java.util.List;

public class GroupResponse implements Serializable {
    public String _id;
    public String groupName;
    public String image;
    public String description;
    public boolean isJoined;
    public RecentGroupMessage lastMessage;
    public List<UserModel> usersJoined;
    public List<UserModel> moderators;
    public boolean canEdit;
}
