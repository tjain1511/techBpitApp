package com.indianapp.techbpit.model;

import java.io.Serializable;
import java.util.List;

public class ProjectResponse implements Serializable {
    public String _id;
    public UserModel createdBy;
    public String description;
    public String duration;
    public String gitLink;
    public String hostedLink;
    public String image;
    public List<UserModel> teamMembers;
    public String title;
}