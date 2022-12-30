package com.indianapp.techbpit.ApiController;

import android.content.Context;
import android.content.Intent;

import com.indianapp.techbpit.SharedPrefHelper;
import com.indianapp.techbpit.activities.LoginActivity;
import com.indianapp.techbpit.model.AllGroupResponse;
import com.indianapp.techbpit.model.GroupMessageRequest;
import com.indianapp.techbpit.model.GroupResponse;
import com.indianapp.techbpit.model.JoinGroupRequest;
import com.indianapp.techbpit.model.MessageModel;
import com.indianapp.techbpit.model.MessageRequest;
import com.indianapp.techbpit.model.OTPVerifyRequest;
import com.indianapp.techbpit.model.PayLoad;
import com.indianapp.techbpit.model.ProjectRequest;
import com.indianapp.techbpit.model.RefreshTokenRequest;
import com.indianapp.techbpit.model.SetupProfileRequest;
import com.indianapp.techbpit.model.SignUpRequestModel;
import com.indianapp.techbpit.model.SocialPostRequest;
import com.indianapp.techbpit.model.SocialPostResponse;
import com.indianapp.techbpit.model.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RESTController {
    private static RESTController instance = null;
    private Context context;
    private boolean isRenewTokenCalled = false;
    private ArrayList<PayLoad> pendingApiCalls = new ArrayList<>();

    public RESTController(Context context) {
        this.context = context;
    }

    public static RESTController getInstance(Context context) throws Exception {
        if (context == null) {
            throw new Exception("Context not provided");
        }
        if (instance == null) {
            instance = new RESTController(context);
        }
        return instance;
    }

    public void clearPendingApis() {
        pendingApiCalls.clear();
    }

    public void execute(RESTCommands command, BaseData<?> data, OnResponseStatusListener listener) {
        if (!isRenewTokenCalled) {
            switch (command) {
                case REQ_POST_SIGN_UP_REQ:
                    SignUpRequestModel signUpRequestModel;
                    if (data.getBaseData() instanceof SignUpRequestModel) {
                        signUpRequestModel = (SignUpRequestModel) data.getBaseData();
                        postSignUpReq(command, signUpRequestModel, listener);
                    }
                    break;

                case REQ_POST_LOG_IN_REQ:
                    SignUpRequestModel loginRewModel;
                    if (data.getBaseData() instanceof SignUpRequestModel) {
                        signUpRequestModel = (SignUpRequestModel) data.getBaseData();
                        postLoginReq(command, signUpRequestModel, listener);
                    }
                    break;
                case REQ_POST_OTP_VERIFY:
                    OTPVerifyRequest otpVerifyRequest;
                    if (data.getBaseData() instanceof OTPVerifyRequest) {
                        otpVerifyRequest = (OTPVerifyRequest) data.getBaseData();
                        postOTPVerify(command, otpVerifyRequest, listener);
                    }
                    break;
                case REQ_GET_ALL_USERS:
                    getAllUsers(command, listener);
                    break;
                case REQ_GET_RECENT_USERS:
                    getRecentUser(command, listener);
                    break;
                case REQ_GET_MESSAGES:
                    MessageRequest messageRequest;
                    if (data.getBaseData() instanceof MessageRequest) {
                        messageRequest = (MessageRequest) data.getBaseData();
                        getDirectMessages(command, messageRequest, listener);
                    }
                    break;
                case REQ_GET_ALL_GROUPS:
                    getAllGroups(command, listener);
                    break;
                case REQ_POST_JOIN_GROUP:
                    JoinGroupRequest joinGroupRequest;
                    if (data.getBaseData() instanceof JoinGroupRequest) {
                        joinGroupRequest = (JoinGroupRequest) data.getBaseData();
                        postJoinGroup(command, joinGroupRequest, listener);
                    }
                case REQ_GET_JOINED_GROUPS:
                    UserModel getJoinedReq;
                    if (data.getBaseData() instanceof UserModel) {
                        getJoinedReq = (UserModel) data.getBaseData();
                        getJoinedGroups(command, getJoinedReq, listener);
                    }
                case REQ_GET_GROUP_MESSAGES:
                    GroupMessageRequest groupMessageRequest;
                    if (data.getBaseData() instanceof GroupMessageRequest) {
                        groupMessageRequest = (GroupMessageRequest) data.getBaseData();
                        getGroupMessages(command, groupMessageRequest, listener);
                    }

                    break;
                case REQ_POST_POST:
                    SocialPostRequest socialPostRequest;
                    if (data.getBaseData() instanceof SocialPostRequest) {
                        socialPostRequest = (SocialPostRequest) data.getBaseData();
                        postSocialPost(command, socialPostRequest, listener);
                    }
                    break;
                case REQ_GET_ALL_POSTS:
                    getAllPosts(command, listener);
                    break;
                case REQ_PATCH_UPDATE_USER_PROFILE:
                    SetupProfileRequest setupProfileRequest;
                    if (data.getBaseData() instanceof SetupProfileRequest) {
                        setupProfileRequest = (SetupProfileRequest) data.getBaseData();
                        updateUserProfile(command, setupProfileRequest, listener);
                    }
                    break;
                case REQ_POST_USER_PROJECTS:
                    ProjectRequest projectRequest;
                    if (data.getBaseData() instanceof ProjectRequest) {
                        projectRequest = (ProjectRequest) data.getBaseData();
                        postUserProjects(command, projectRequest, listener);
                    }
                    break;
                case REQ_GET_USER_PROFILE:
                    String userId;
                    if (data.getBaseData() instanceof String) {
                        userId = (String) data.getBaseData();
                        getUserProfile(command, userId, listener);
                    }
                    break;
                case REQ_GET_SEARCH_USERS:
                    String query;
                    if (data.getBaseData() instanceof String) {
                        query = (String) data.getBaseData();
                        getSearchesUsers(command, query, listener);
                    }
                    break;
                case REQ_GET_GROUP_DATA:
                    String groupId;
                    if (data.getBaseData() instanceof String) {
                        groupId = (String) data.getBaseData();
                        getGroupData(command, groupId, listener);
                    }
                    break;
                case REQ_PATCH_LEAVE_GROUP:
                    String groupID;
                    if (data.getBaseData() instanceof String) {
                        groupID = (String) data.getBaseData();
                        leaveGroup(command, groupID, listener);
                    }
                    break;
                case REQ_GET_COMMUNITY_POSTS:
                    break;
                default:
                    break;
            }
        } else {
            pendingApiCalls.add(new PayLoad(command, data, listener));
        }
    }

    public void fetchParamsFromPayloadAndExecute(PayLoad payLoad) {
        RESTCommands command = payLoad.command;
        BaseData<?> data = (BaseData<?>) payLoad.data;
        OnResponseStatusListener listener = (OnResponseStatusListener) payLoad.listener;
        if (!isRenewTokenCalled) {
            switch (payLoad.command) {
                case REQ_POST_SIGN_UP_REQ:
                    SignUpRequestModel signUpRequestModel;
                    if (data.getBaseData() instanceof SignUpRequestModel) {
                        signUpRequestModel = (SignUpRequestModel) data.getBaseData();
                        postSignUpReq(command, signUpRequestModel, listener);
                    }
                    break;

                case REQ_POST_LOG_IN_REQ:
                    SignUpRequestModel loginRewModel;
                    if (data.getBaseData() instanceof SignUpRequestModel) {
                        signUpRequestModel = (SignUpRequestModel) data.getBaseData();
                        postLoginReq(command, signUpRequestModel, listener);
                    }
                    break;
                case REQ_POST_OTP_VERIFY:
                    OTPVerifyRequest otpVerifyRequest;
                    if (data.getBaseData() instanceof OTPVerifyRequest) {
                        otpVerifyRequest = (OTPVerifyRequest) data.getBaseData();
                        postOTPVerify(command, otpVerifyRequest, listener);
                    }
                    break;
                case REQ_GET_ALL_USERS:
                    getAllUsers(command, listener);
                    break;
                case REQ_GET_RECENT_USERS:
                    getRecentUser(command, listener);
                    break;
                case REQ_GET_MESSAGES:
                    MessageRequest messageRequest;
                    if (data.getBaseData() instanceof MessageRequest) {
                        messageRequest = (MessageRequest) data.getBaseData();
                        getDirectMessages(command, messageRequest, listener);
                    }
                    break;
                case REQ_GET_ALL_GROUPS:
                    getAllGroups(command, listener);
                    break;
                case REQ_POST_JOIN_GROUP:
                    JoinGroupRequest joinGroupRequest;
                    if (data.getBaseData() instanceof JoinGroupRequest) {
                        joinGroupRequest = (JoinGroupRequest) data.getBaseData();
                        postJoinGroup(command, joinGroupRequest, listener);
                    }
                    break;
                case REQ_GET_JOINED_GROUPS:
                    UserModel getJoinedReq;
                    if (data.getBaseData() instanceof UserModel) {
                        getJoinedReq = (UserModel) data.getBaseData();
                        getJoinedGroups(command, getJoinedReq, listener);
                    }
                    break;
                case REQ_GET_GROUP_MESSAGES:
                    GroupMessageRequest groupMessageRequest;
                    if (data.getBaseData() instanceof GroupMessageRequest) {
                        groupMessageRequest = (GroupMessageRequest) data.getBaseData();
                        getGroupMessages(command, groupMessageRequest, listener);
                    }

                    break;
                case REQ_POST_POST:
                    SocialPostRequest socialPostRequest;
                    if (data.getBaseData() instanceof SocialPostRequest) {
                        socialPostRequest = (SocialPostRequest) data.getBaseData();
                        postSocialPost(command, socialPostRequest, listener);
                    }
                    break;
                case REQ_GET_ALL_POSTS:
                    getAllPosts(command, listener);
                    break;
                case REQ_PATCH_UPDATE_USER_PROFILE:
                    SetupProfileRequest setupProfileRequest;
                    if (data.getBaseData() instanceof SetupProfileRequest) {
                        setupProfileRequest = (SetupProfileRequest) data.getBaseData();
                        updateUserProfile(command, setupProfileRequest, listener);
                    }
                    break;
                case REQ_POST_USER_PROJECTS:
                    ProjectRequest projectRequest;
                    if (data.getBaseData() instanceof ProjectRequest) {
                        projectRequest = (ProjectRequest) data.getBaseData();
                        postUserProjects(command, projectRequest, listener);
                    }
                    break;
                case REQ_GET_USER_PROFILE:
                    String userId;
                    if (data.getBaseData() instanceof String) {
                        userId = (String) data.getBaseData();
                        getUserProfile(command, userId, listener);
                    }
                    break;
                case REQ_GET_SEARCH_USERS:
                    String query;
                    if (data.getBaseData() instanceof String) {
                        query = (String) data.getBaseData();
                        getSearchesUsers(command, query, listener);
                    }
                    break;
                case REQ_GET_GROUP_DATA:
                    String groupId;
                    if (data.getBaseData() instanceof String) {
                        groupId = (String) data.getBaseData();
                        getGroupData(command, groupId, listener);
                    }
                    break;
                case REQ_PATCH_LEAVE_GROUP:
                    String groupID;
                    if (data.getBaseData() instanceof String) {
                        groupID = (String) data.getBaseData();
                        leaveGroup(command, groupID, listener);
                    }
                    break;
                default:
                    break;
            }
        } else {
            pendingApiCalls.add(new PayLoad(command, data, listener));
        }
    }

    private void postSignUpReq(RESTCommands command, SignUpRequestModel signUpRequestModel, OnResponseStatusListener listener) {
        EngineService service = EngineClient.getClient().create(EngineService.class);
        Call<ResponseBody> call = service.sendSignUpReq(signUpRequestModel);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (listener != null) {
                    listener.onResponseReceived(command, new BaseData<>(signUpRequestModel), response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, new BaseData<>(signUpRequestModel), t);
                }
            }
        });
    }

    private void postLoginReq(RESTCommands command, SignUpRequestModel signUpRequestModel, OnResponseStatusListener listener) {
        EngineService service = EngineClient.getClient().create(EngineService.class);
        Call<UserModel> call = service.sendLoginReq(signUpRequestModel);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (listener != null) {
                    listener.onResponseReceived(command, new BaseData<>(signUpRequestModel), response);
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, new BaseData<>(signUpRequestModel), t);
                }
            }
        });
    }

    private void postOTPVerify(RESTCommands command, OTPVerifyRequest otpVerifyRequest, OnResponseStatusListener listener) {
        EngineService service = EngineClient.getClient().create(EngineService.class);
        Call<UserModel> call = service.sendOTPVerify(otpVerifyRequest);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (listener != null) {
                    listener.onResponseReceived(command, new BaseData<>(otpVerifyRequest), response);
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, new BaseData<>(otpVerifyRequest), t);
                }
            }
        });
    }

    private void getAllUsers(RESTCommands command, OnResponseStatusListener listener) {
        EngineService service = EngineClient.getClient().create(EngineService.class);
        Call<List<UserModel>> call = service.getAllUsers(getAccessToken());
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                handleOnResponse(command, null, response, listener);
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, null, t);
                }
            }
        });
    }

    private void getRecentUser(RESTCommands command, OnResponseStatusListener listener) {
        EngineService service = EngineClient.getClient().create(EngineService.class);
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put("userId", SharedPrefHelper.getUserModel(context)._id);
        Call<List<UserModel>> call = service.getRecentUsers(getAccessToken(), hashMap);
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                handleOnResponse(command, null, response, listener);

            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, null, t);
                }
            }
        });
    }

    private void getDirectMessages(RESTCommands command, MessageRequest messageRequest, OnResponseStatusListener listener) {
        EngineService service = EngineClient.getClient().create(EngineService.class);
        Call<List<MessageModel>> call = service.getDirectMessages(getAccessToken(), messageRequest);
        call.enqueue(new Callback<List<MessageModel>>() {
            @Override
            public void onResponse(Call<List<MessageModel>> call, Response<List<MessageModel>> response) {
                handleOnResponse(command, new BaseData<>(messageRequest), response, listener);

            }

            @Override
            public void onFailure(Call<List<MessageModel>> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, new BaseData<>(messageRequest), t);
                }
            }
        });
    }

    private void getAllGroups(RESTCommands command, OnResponseStatusListener listener) {
        EngineService service = EngineClient.getClient().create(EngineService.class);
        JoinGroupRequest joinGroupRequest = new JoinGroupRequest();
        joinGroupRequest.userId = SharedPrefHelper.getUserModel(context)._id;
        Call<List<AllGroupResponse>> call = service.getAllGroups(getAccessToken(), joinGroupRequest);
        call.enqueue(new Callback<List<AllGroupResponse>>() {
            @Override
            public void onResponse(Call<List<AllGroupResponse>> call, Response<List<AllGroupResponse>> response) {
                handleOnResponse(command, null, response, listener);
            }

            @Override
            public void onFailure(Call<List<AllGroupResponse>> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, new BaseData<>(null), t);
                }
            }
        });
    }

    private void postJoinGroup(RESTCommands command, JoinGroupRequest joinGroupRequest, OnResponseStatusListener listener) {
        EngineService service = EngineClient.getClient().create(EngineService.class);
        Call<ResponseBody> call = service.postJoinGroup(getAccessToken(), joinGroupRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleOnResponse(command, new BaseData<>(joinGroupRequest), response, listener);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, new BaseData<>(joinGroupRequest), t);
                }
            }
        });
    }

    private void getJoinedGroups(RESTCommands command, UserModel getJoinedReq, OnResponseStatusListener listener) {
        EngineService service = EngineClient.getClient().create(EngineService.class);
        Call<UserModel> call = service.getJoinedGroups(getAccessToken(), getJoinedReq);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                handleOnResponse(command, new BaseData<>(getJoinedReq), response, listener);

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, new BaseData<>(getJoinedReq), t);
                }
            }
        });
    }

    private void getGroupMessages(RESTCommands command, GroupMessageRequest groupId, OnResponseStatusListener listener) {
        EngineService service = EngineClient.getClient().create(EngineService.class);
        Call<List<MessageModel>> call = service.getGrpMessages(getAccessToken(), groupId);
        call.enqueue(new Callback<List<MessageModel>>() {
            @Override
            public void onResponse(Call<List<MessageModel>> call, Response<List<MessageModel>> response) {
                handleOnResponse(command, new BaseData<>(groupId), response, listener);
            }

            @Override
            public void onFailure(Call<List<MessageModel>> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, new BaseData<>(groupId), t);
                }
            }
        });
    }

    private void postSocialPost(RESTCommands command, SocialPostRequest socialPostRequest, OnResponseStatusListener listener) {
        EngineService service = EngineClient.getClient().create(EngineService.class);
        Call<ResponseBody> call = service.postSocialPost(getAccessToken(), socialPostRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleOnResponse(command, new BaseData<>(socialPostRequest), response, listener);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, new BaseData<>(socialPostRequest), t);
                }
            }
        });
    }

    private void getAllPosts(RESTCommands command, OnResponseStatusListener listener) {
        EngineService service = EngineClient.getClient().create(EngineService.class);
        Call<List<SocialPostResponse>> call = service.getAllPosts(getAccessToken());
        call.enqueue(new Callback<List<SocialPostResponse>>() {
            @Override
            public void onResponse(Call<List<SocialPostResponse>> call, Response<List<SocialPostResponse>> response) {
                handleOnResponse(command, null, response, listener);

            }

            @Override
            public void onFailure(Call<List<SocialPostResponse>> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, null, t);
                }
            }
        });
    }

    private void getCommunityPost(RESTCommands command, OnResponseStatusListener listener) {
        EngineService service = EngineClient.getClient().create(EngineService.class);
        Call<List<SocialPostResponse>> call = service.getAllPosts(getAccessToken());
        call.enqueue(new Callback<List<SocialPostResponse>>() {
            @Override
            public void onResponse(Call<List<SocialPostResponse>> call, Response<List<SocialPostResponse>> response) {
                handleOnResponse(command, null, response, listener);

            }

            @Override
            public void onFailure(Call<List<SocialPostResponse>> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, null, t);
                }
            }
        });
    }

    private void updateUserProfile(RESTCommands command, SetupProfileRequest setupProfileRequest, OnResponseStatusListener listener) {
        EngineService service = EngineClient.getClient().create(EngineService.class);
        Call<ResponseBody> call = service.updateUserProfile(getAccessToken(), setupProfileRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleOnResponse(command, null, response, listener);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, null, t);
                }
            }
        });
    }

    private void postUserProjects(RESTCommands command, ProjectRequest projectRequest, OnResponseStatusListener listener) {
        EngineService service = EngineClient.getClient().create(EngineService.class);
        Call<ResponseBody> call = service.postProjects(getAccessToken(), projectRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleOnResponse(command, new BaseData<>(projectRequest), response, listener);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, null, t);
                }
            }
        });
    }

    private void getUserProfile(RESTCommands command, String userId, OnResponseStatusListener listener) {
        EngineService service = EngineClient.getClient().create(EngineService.class);
        Call<UserModel> call = service.getUserProfile(getAccessToken(), userId);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                handleOnResponse(command, new BaseData<>(userId), response, listener);
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, null, t);
                }
            }
        });
    }

    private void getGroupData(RESTCommands command, String groupId, OnResponseStatusListener listener) {
        EngineService service = EngineClient.getClient().create(EngineService.class);
        Call<GroupResponse> call = service.getGroupData(getAccessToken(), groupId);
        call.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                handleOnResponse(command, new BaseData<>(groupId), response, listener);
            }

            @Override
            public void onFailure(Call<GroupResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, null, t);
                }
            }
        });
    }

    private void leaveGroup(RESTCommands command, String groupId, OnResponseStatusListener listener) {
        EngineService service = EngineClient.getClient().create(EngineService.class);
        Call<ResponseBody> call = service.leaveGroup(getAccessToken(), groupId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleOnResponse(command, new BaseData<>(groupId), response, listener);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, null, t);
                }
            }
        });
    }

    private void getSearchesUsers(RESTCommands command, String query, OnResponseStatusListener listener) {
        EngineService service = EngineClient.getClient().create(EngineService.class);
        Call<List<UserModel>> call = service.getSearchedUsers(query);
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                handleOnResponse(command, null, response, listener);
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, null, t);
                }
            }
        });
    }

    private void handleOnResponse(RESTCommands command, BaseData<?> data, Response<?> response, OnResponseStatusListener listener) {
        if (response.code() == 401) {
            pendingApiCalls.add(new PayLoad(command, data, listener));
            refreshToken();
        } else {
            if (listener != null) {
                listener.onResponseReceived(command, data, response);
            }
        }
    }

    private void refreshToken() {
        if (!isRenewTokenCalled) {
            isRenewTokenCalled = true;
            RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
            refreshTokenRequest.refresh_token = getRefreshToken();
            EngineService service = EngineClient.getClient().create(EngineService.class);
            Call<UserModel> call = service.refreshToken(refreshTokenRequest);
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    isRenewTokenCalled = false;
                    if (response.isSuccessful()) {
                        SharedPrefHelper.setAccessToken(context, response.body().access_token);
                        callPendingApis();
                        pendingApiCalls.clear();
                    } else if (response.code() == 401) {
                        pendingApiCalls.clear();
                        SharedPrefHelper.setUserModel(context, new UserModel());
                        Intent i = new Intent(context, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(i);
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                }
            });
        }
    }

    private void callPendingApis() {
        for (PayLoad payLoad : pendingApiCalls) {
            fetchParamsFromPayloadAndExecute(payLoad);
        }
    }

    private String getAccessToken() {
        return "Bearer " + SharedPrefHelper.getUserModel(context).access_token;
    }

    private String getRefreshToken() {
        return SharedPrefHelper.getUserModel(context).refresh_token;
    }

    public enum RESTCommands {
        REQ_POST_SIGN_UP_REQ,
        REQ_POST_LOG_IN_REQ,
        REQ_POST_OTP_VERIFY,
        REQ_GET_ALL_USERS,
        REQ_GET_RECENT_USERS,
        REQ_GET_MESSAGES,
        REQ_GET_ALL_GROUPS,
        REQ_POST_JOIN_GROUP,
        REQ_GET_JOINED_GROUPS,
        REQ_GET_GROUP_MESSAGES,
        REQ_POST_POST,
        REQ_GET_ALL_POSTS,
        REQ_PATCH_UPDATE_USER_PROFILE,
        REQ_GET_USER_PROFILE,
        REQ_GET_GROUP_DATA,
        REQ_POST_USER_PROJECTS,
        REQ_GET_SEARCH_USERS,
        REQ_GET_COMMUNITY_POSTS,
        REQ_PATCH_LEAVE_GROUP
    }

    public interface OnResponseStatusListener {
        void onResponseReceived(RESTCommands commands, BaseData<?> data, Response<?> response);

        void onResponseFailed(RESTCommands commands, BaseData<?> data, Throwable t);
    }


}
