package com.indianapp.techbpit.ApiController;

import com.indianapp.techbpit.model.AllGroupResponse;
import com.indianapp.techbpit.model.GroupResponse;
import com.indianapp.techbpit.model.JoinGroupRequest;
import com.indianapp.techbpit.model.MessageModel;
import com.indianapp.techbpit.model.OTPVerifyRequest;
import com.indianapp.techbpit.model.ProjectRequest;
import com.indianapp.techbpit.model.ProjectResponse;
import com.indianapp.techbpit.model.RefreshTokenRequest;
import com.indianapp.techbpit.model.SetupProfileRequest;
import com.indianapp.techbpit.model.SignUpRequestModel;
import com.indianapp.techbpit.model.SocialPostRequest;
import com.indianapp.techbpit.model.SocialPostResponse;
import com.indianapp.techbpit.model.UserModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EngineService {

    //GET REQUESTS
    @GET("/post/all/event")
    Call<List<SocialPostResponse>> getAllEvents(@Header("Authorization") String authorization);

    @GET("/project/{projectId}")
    Call<ProjectResponse> getProjectDetails(@Header("Authorization") String authorization, @Path("projectId") String projectId);

    @GET("/project/all/{userId}")
    Call<List<ProjectResponse>> getProjects(@Header("Authorization") String authorization, @Path("userId") String userId);

    @GET("/chat/recent/personal")
    Call<List<UserModel>> getRecentUsers(@Header("Authorization") String authorization);

    @GET("/explore/all")
    Call<List<AllGroupResponse>> getSearchedGroups(@Header("Authorization") String authorization, @Query("search") String search);

    @GET("/user/search")
    Call<List<UserModel>> getSearchedUsers(@Header("Authorization") String authorization, @Query("search") String search);

    @GET("/user/{userId}")
    Call<UserModel> getUserProfile(@Header("Authorization") String authorization, @Path("userId") String userId);

    @GET("/group/get/{groupId}")
    Call<GroupResponse> getGroupData(@Header("Authorization") String authorization, @Path("groupId") String groupId);

    @GET("/post/all/{groupId}")
    Call<List<SocialPostResponse>> getGroupPosts(@Header("Authorization") String authorization, @Path("groupId") String groupId);

    @GET("/chat/group/{groupId}")
    Call<List<MessageModel>> getGrpMessages(@Header("Authorization") String authorization, @Path("groupId") String groupId);

    @GET("/post/community")
    Call<List<SocialPostResponse>> getJoinedCommunitiesPosts(@Header("Authorization") String authorization);

    @GET("/chat/recent/group")
    Call<UserModel> getJoinedGroups(@Header("Authorization") String authorization);

    @GET("/group/{userId}")
    Call<List<AllGroupResponse>> getManageableGroups(@Header("Authorization") String authorization, @Path("userId") String userId);

    @GET("/explore/group")
    Call<List<AllGroupResponse>> getAllGroups(@Header("Authorization") String authorization);

    @GET("/post/all")
    Call<List<SocialPostResponse>> getAllPosts(@Header("Authorization") String authorization);

    @GET("/user/all")
    Call<List<UserModel>> getAllUsers(@Header("Authorization") String authorization);

    @GET("/chat/personal/{receiver}")
    Call<List<MessageModel>> getDirectMessages(@Header("Authorization") String authorization, @Path("receiver") String receiver);

    @GET("/explore/project")
    Call<List<ProjectResponse>> getExploreProjects(@Header("Authorization") String authorization, @Query("count") String count);

    @GET("/explore/user")
    Call<List<UserModel>> getExploreUsers(@Header("Authorization") String authorization, @Query("count") String count);


    //POST REQUESTS
    @POST("/post/create")
    Call<ResponseBody> postSocialPost(@Header("Authorization") String authorization, @Body SocialPostRequest socialPostRequest);

    @POST("/auth/access_token/renew")
    Call<UserModel> refreshToken(@Body RefreshTokenRequest refreshTokenRequest);

    @POST("/auth/login")
    Call<UserModel> sendLoginReq(@Body SignUpRequestModel signUpRequestModel);

    @POST("/auth/verify")
    Call<UserModel> sendOTPVerify(@Body OTPVerifyRequest oTPVerifyRequest);

    @POST("/auth/signup")
    Call<ResponseBody> sendSignUpReq(@Body SignUpRequestModel signUpRequestModel);

    @POST("/group/join")
    Call<ResponseBody> postJoinGroup(@Header("Authorization") String authorization, @Body JoinGroupRequest joinGroupRequest);

    @POST("/project/create")
    Call<ResponseBody> postProjects(@Header("Authorization") String authorization, @Body ProjectRequest projectRequest);


    //DELETE REQUESTS
    @DELETE("/project/delete/{projectId}")
    Call<ResponseBody> deleteProjects(@Header("Authorization") String authorization, @Path("projectId") String projectId);


    //PATCH REQUESTS
    @PATCH("/group/leave/{groupId}")
    Call<ResponseBody> leaveGroup(@Header("Authorization") String authorization, @Path("groupId") String groupId);


    @PATCH("/user/update ")
    Call<ResponseBody> updateUserProfile(@Header("Authorization") String authorization, @Body SetupProfileRequest setupProfileRequest);

    @PATCH("/project/update/{projectId}")
    Call<ResponseBody> editProjects(@Header("Authorization") String authorization, @Path("projectId") String projectId, @Body ProjectRequest projectRequest);
}
