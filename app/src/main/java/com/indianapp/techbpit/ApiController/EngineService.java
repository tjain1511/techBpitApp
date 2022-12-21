package com.indianapp.techbpit.ApiController;

import com.indianapp.techbpit.model.AllGroupResponse;
import com.indianapp.techbpit.model.GroupMessageRequest;
import com.indianapp.techbpit.model.JoinGroupRequest;
import com.indianapp.techbpit.model.MessageModel;
import com.indianapp.techbpit.model.MessageRequest;
import com.indianapp.techbpit.model.OTPVerifyRequest;
import com.indianapp.techbpit.model.SetupProfileRequest;
import com.indianapp.techbpit.model.SignUpRequestModel;
import com.indianapp.techbpit.model.SocialPostRequest;
import com.indianapp.techbpit.model.SocialPostResponse;
import com.indianapp.techbpit.model.UserModel;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EngineService {
    @POST("/signup")
    Call<ResponseBody> sendSignUpReq(@Body SignUpRequestModel signUpRequestModel);

    @POST("/verify")
    Call<UserModel> sendOTPVerify(@Body OTPVerifyRequest otpVerifyRequest);

    @POST("/login")
    Call<UserModel> sendLoginReq(@Body SignUpRequestModel signUpRequestModel);

    @GET("/users")
    Call<List<UserModel>> getAllUsers();

    @POST("/recentPersonalChat")
    Call<List<UserModel>> getRecentUsers(@Body HashMap<String, String> hashMap);

    @POST("/directMessage")
    Call<List<MessageModel>> getDirectMessages(@Body MessageRequest messageRequest);

    @POST("/getGroups")
    Call<List<AllGroupResponse>> getAllGroups(@Body JoinGroupRequest joinGroupRequest);

    @POST("/joinGroup")
    Call<ResponseBody> postJoinGroup(@Body JoinGroupRequest joinGroupRequest);

    @POST("/recentGroupChat")
    Call<UserModel> getJoinedGroups(@Body UserModel JoinedGroups);

    @POST("/groupMessage")
    Call<List<MessageModel>> getGrpMessages(@Body GroupMessageRequest groupId);

    @POST("/createPost")
    Call<ResponseBody> postSocialPost(@Body SocialPostRequest postRequest);

    @GET("/getAllPost")
    Call<List<SocialPostResponse>> getAllPosts();

    @PATCH("/updateUser")
    Call<ResponseBody> updateUserProfile(@Body SetupProfileRequest setupProfileRequest);

    @GET("/user/{userId}")
    Call<UserModel> getUserProfile(@Path("userId") String userId);
}
