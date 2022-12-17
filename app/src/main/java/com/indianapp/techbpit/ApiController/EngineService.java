package com.indianapp.techbpit.ApiController;

import com.indianapp.techbpit.model.GroupMessageRequest;
import com.indianapp.techbpit.model.GroupResponse;
import com.indianapp.techbpit.model.JoinGroupRequest;
import com.indianapp.techbpit.model.MessageModel;
import com.indianapp.techbpit.model.MessageRequest;
import com.indianapp.techbpit.model.OTPVerifyRequest;
import com.indianapp.techbpit.model.SignUpRequestModel;
import com.indianapp.techbpit.model.UserModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface EngineService {
    @POST("/signup")
    Call<ResponseBody> sendSignUpReq(@Body SignUpRequestModel signUpRequestModel);

    @POST("/verify")
    Call<UserModel> sendOTPVerify(@Body OTPVerifyRequest otpVerifyRequest);

    @POST("/login")
    Call<UserModel> sendLoginReq(@Body SignUpRequestModel signUpRequestModel);

    @GET("/users")
    Call<List<UserModel>> getAllUsers();

    @POST("/directMessage")
    Call<List<MessageModel>> getDirectMessages(@Body MessageRequest messageRequest);

    @POST("/getGroups")
    Call<List<GroupResponse>> getAllGroups(@Body JoinGroupRequest joinGroupRequest);

    @POST("/joinGroup")
    Call<ResponseBody> postJoinGroup(@Body JoinGroupRequest joinGroupRequest);

    @POST("/getJoinedGroup")
    Call<UserModel> getJoinedGroups(@Body UserModel JoinedGroups);

    @POST("/groupMessage")
    Call<List<MessageModel>> getGrpMessages(@Body GroupMessageRequest groupId);
}
