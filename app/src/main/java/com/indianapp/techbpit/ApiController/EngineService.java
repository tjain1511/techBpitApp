package com.indianapp.techbpit.ApiController;

import com.indianapp.techbpit.model.AllGroupResponse;
import com.indianapp.techbpit.model.GroupMessageRequest;
import com.indianapp.techbpit.model.GroupResponse;
import com.indianapp.techbpit.model.JoinGroupRequest;
import com.indianapp.techbpit.model.MessageModel;
import com.indianapp.techbpit.model.MessageRequest;
import com.indianapp.techbpit.model.OTPVerifyRequest;
import com.indianapp.techbpit.model.ProjectRequest;
import com.indianapp.techbpit.model.RefreshTokenRequest;
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
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EngineService {
    @POST("/signup")
    Call<ResponseBody> sendSignUpReq(@Body SignUpRequestModel signUpRequestModel);

    @POST("/verify")
    Call<UserModel> sendOTPVerify(@Body OTPVerifyRequest otpVerifyRequest);

    @POST("/login")
    Call<UserModel> sendLoginReq(@Body SignUpRequestModel signUpRequestModel);

    @GET("/users")
    Call<List<UserModel>> getAllUsers(@Header("Authorization") String authorization);

    @POST("/recentPersonalChat")
    Call<List<UserModel>> getRecentUsers(@Header("Authorization") String authorization, @Body HashMap<String, String> hashMap);

    @POST("/directMessage")
    Call<List<MessageModel>> getDirectMessages(@Header("Authorization") String authorization, @Body MessageRequest messageRequest);

    @POST("/getGroups")
    Call<List<AllGroupResponse>> getAllGroups(@Header("Authorization") String authorization, @Body JoinGroupRequest joinGroupRequest);

    @POST("/joinGroup")
    Call<ResponseBody> postJoinGroup(@Header("Authorization") String authorization, @Body JoinGroupRequest joinGroupRequest);

    @POST("/recentGroupChat")
    Call<UserModel> getJoinedGroups(@Header("Authorization") String authorization, @Body UserModel JoinedGroups);

    @POST("/groupMessage")
    Call<List<MessageModel>> getGrpMessages(@Header("Authorization") String authorization, @Body GroupMessageRequest groupId);

    @POST("/createPost")
    Call<ResponseBody> postSocialPost(@Header("Authorization") String authorization, @Body SocialPostRequest postRequest);

    @GET("/getAllPost")
    Call<List<SocialPostResponse>> getAllPosts(@Header("Authorization") String authorization);

    @PATCH("/updateUser")
    Call<ResponseBody> updateUserProfile(@Header("Authorization") String authorization, @Body SetupProfileRequest setupProfileRequest);

    @POST("/updateUser")
    Call<ResponseBody> postProjects(@Header("Authorization") String authorization, @Body ProjectRequest projectRequest);

    @GET("/user/{userId}")
    Call<UserModel> getUserProfile(@Header("Authorization") String authorization, @Path("userId") String userId);

    @POST("/auth/access_token/renew")
    Call<UserModel> refreshToken(@Body RefreshTokenRequest refreshTokenRequest);

    @GET("/searchUser")
    Call<List<UserModel>> getSearchedUsers(@Query("search") String search);

    @GET("/group/{groupId}")
    Call<GroupResponse> getGroupData(@Header("Authorization") String authorization, @Path("groupId") String groupId);

    @PATCH("/group/leave/{groupId}")
    Call<ResponseBody> leaveGroup(@Header("Authorization") String authorization, @Path("groupId") String groupId);
}
