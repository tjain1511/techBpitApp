package com.indianapp.techbpit;

import com.indianapp.techbpit.model.UserModel;

import java.util.SortedSet;

public interface MemberAddedListener {
    void onDoneClicked(SortedSet<UserModel> teamMembers);
}
