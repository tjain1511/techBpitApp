package com.indianapp.techbpit.listeners;

import com.indianapp.techbpit.model.UserModel;

public interface UserClickedListener {
    void onUserClicked(int position, UserModel userModel);
}
