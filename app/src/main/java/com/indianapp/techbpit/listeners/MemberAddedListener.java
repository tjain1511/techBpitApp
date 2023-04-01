package com.indianapp.techbpit.listeners;

import com.indianapp.techbpit.model.UserModel;

import java.util.SortedSet;

public interface MemberAddedListener {
    void onDoneClicked(SortedSet<UserModel> teamMembers);
}
