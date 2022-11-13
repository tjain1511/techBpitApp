package com.indianapp.techbpit;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
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
}
