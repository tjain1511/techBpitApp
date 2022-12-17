package com.indianapp.techbpit;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.indianapp.techbpit.model.UserModel;

public class SharedPrefHelper {
    private static SharedPreferences getSharedPref(Context context) {
        return context.getSharedPreferences("com.indianapp.techbpit", MODE_PRIVATE);
    }

    public static void setUserModel(Context context, UserModel userModel) {
        getSharedPref(context).edit().putString("my_id", userModel._id).apply();
        getSharedPref(context).edit().putString("my_email", userModel.email).apply();
        getSharedPref(context).edit().putString("my_username", userModel.username).apply();
        getSharedPref(context).edit().putString("my_image", userModel.imageUrl).apply();
    }

    public static UserModel getUserModel(Context context) {
        UserModel userModel = new UserModel();
        userModel._id = getSharedPref(context).getString("my_id", null);
        userModel.email = getSharedPref(context).getString("my_email", null);
        userModel.username = getSharedPref(context).getString("my_username", null);
        userModel.imageUrl = getSharedPref(context).getString("my_image", null);
        return userModel;
    }
}
