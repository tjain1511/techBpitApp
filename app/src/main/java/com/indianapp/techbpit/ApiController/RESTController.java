package com.indianapp.techbpit.ApiController;

import android.content.Context;
import android.content.Intent;

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
import com.indianapp.techbpit.model.ProjectResponse;
import com.indianapp.techbpit.model.RefreshTokenRequest;
import com.indianapp.techbpit.model.SetupProfileRequest;
import com.indianapp.techbpit.model.SignUpRequestModel;
import com.indianapp.techbpit.model.SocialPostRequest;
import com.indianapp.techbpit.model.SocialPostResponse;
import com.indianapp.techbpit.model.UserModel;
import com.indianapp.techbpit.utils.SharedPrefHelper;

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
        REQ_PATCH_LEAVE_GROUP,
        REQ_GET_USER_PROJECTS,
        REQ_PATCH_EDIT_PROJECT,
        REQ_DELETE_USER_PROJECT,
        REQ_GET_EXPLORE_PROJECTS,
        REQ_GET_EXPLORE_USERS,
        REQ_GET_GRP_POSTS,
        REQ_GET_ALL_EVENTS,
        REQ_GET_MANAGEABLE_GROUPS,
        REQ_GET_SEARCHED_GROUPS,
        REQ_GET_PROJECT_DETAILS
    }

    public void clearPendingApis() {
        pendingApiCalls.clear();
    }

    public void execute(RESTCommands command, BaseData<?> data, OnResponseStatusListener listener) {
        if (!isRenewTokenCalled) {
            resolveRESTCommand(command, data, listener);
        } else {
            pendingApiCalls.add(new PayLoad(command, data, listener));
        }
    }

    public void fetchParamsFromPayloadAndExecute(PayLoad payLoad) {
        RESTCommands command = payLoad.command;
        BaseData<?> data = (BaseData<?>) payLoad.data;
        OnResponseStatusListener listener = (OnResponseStatusListener) payLoad.listener;
        execute(command, data, listener);
    }

    public void resolveRESTCommand(RESTCommands command, BaseData<?> data, OnResponseStatusListener listener) {
        switch (command) {
            case REQ_POST_SIGN_UP_REQ:
                if (data.getBaseData() instanceof SignUpRequestModel) {
                    postSignUpReq(command, (SignUpRequestModel) data.getBaseData(), listener);
                }
                break;

            case REQ_POST_LOG_IN_REQ:
                if (data.getBaseData() instanceof SignUpRequestModel) {
                    postLoginReq(command, (SignUpRequestModel) data.getBaseData(), listener);
                }
                break;
            case REQ_POST_OTP_VERIFY:
                if (data.getBaseData() instanceof OTPVerifyRequest) {
                    postOTPVerify(command, (OTPVerifyRequest) data.getBaseData(), listener);
                }
                break;
            case REQ_GET_ALL_USERS:
                getAllUsers(command, listener);
                break;
            case REQ_GET_RECENT_USERS:
                getRecentUser(command, listener);
                break;
            case REQ_GET_MESSAGES:
                if (data.getBaseData() instanceof MessageRequest) {
                    getDirectMessages(command, (MessageRequest) data.getBaseData(), listener);
                }
                break;
            case REQ_GET_ALL_GROUPS:
                getAllGroups(command, listener);
                break;
            case REQ_POST_JOIN_GROUP:
                if (data.getBaseData() instanceof JoinGroupRequest) {
                    postJoinGroup(command, (JoinGroupRequest) data.getBaseData(), listener);
                }
                break;
            case REQ_GET_JOINED_GROUPS:
                if (data.getBaseData() instanceof UserModel) {
                    getJoinedGroups(command, (UserModel) data.getBaseData(), listener);
                }
                break;
            case REQ_GET_GROUP_MESSAGES:
                if (data.getBaseData() instanceof GroupMessageRequest) {
                    getGroupMessages(command, (GroupMessageRequest) data.getBaseData(), listener);
                }
                break;
            case REQ_POST_POST:
                if (data.getBaseData() instanceof SocialPostRequest) {
                    postSocialPost(command, (SocialPostRequest) data.getBaseData(), listener);
                }
                break;
            case REQ_GET_ALL_POSTS:
                getAllPosts(command, listener);
                break;
            case REQ_PATCH_UPDATE_USER_PROFILE:
                if (data.getBaseData() instanceof SetupProfileRequest) {
                    updateUserProfile(command, (SetupProfileRequest) data.getBaseData(), listener);
                }
                break;
            case REQ_POST_USER_PROJECTS:
                if (data.getBaseData() instanceof ProjectRequest) {
                    postUserProjects(command, (ProjectRequest) data.getBaseData(), listener);
                }
                break;
            case REQ_GET_USER_PROFILE:
                if (data.getBaseData() instanceof String) {
                    getUserProfile(command, (String) data.getBaseData(), listener);
                }
                break;
            case REQ_GET_SEARCH_USERS:
                if (data.getBaseData() instanceof String) {
                    getSearchesUsers(command, (String) data.getBaseData(), listener);
                }
                break;
            case REQ_GET_GROUP_DATA:
                if (data.getBaseData() instanceof String) {
                    getGroupData(command, (String) data.getBaseData(), listener);
                }
                break;
            case REQ_PATCH_LEAVE_GROUP:
                if (data.getBaseData() instanceof String) {
                    leaveGroup(command, (String) data.getBaseData(), listener);
                }
                break;
            case REQ_GET_COMMUNITY_POSTS:
                getJoinedCommunitiesPosts(command, listener);
                break;
            case REQ_GET_USER_PROJECTS:
                getUserProjects(command, (String) data.getBaseData(), listener);
                break;
            case REQ_PATCH_EDIT_PROJECT:
                if (data.getBaseData() instanceof ProjectRequest) {
                    editUserProject(command, (ProjectRequest) data.getBaseData(), listener);
                }
                break;
            case REQ_DELETE_USER_PROJECT:
                if (data.getBaseData() instanceof String) {
                    deleteUserProject(command, (String) data.getBaseData(), listener);
                }
                break;
            case REQ_GET_EXPLORE_PROJECTS:
                getExploreProjects(command, listener);
                break;
            case REQ_GET_EXPLORE_USERS:
                getExploreUsers(command, listener);
                break;
            case REQ_GET_GRP_POSTS:
                if (data.getBaseData() instanceof String) {
                    getGroupPosts(command, (String) data.getBaseData(), listener);
                }
                break;
            case REQ_GET_ALL_EVENTS:
                getAllEvents(command, listener);
                break;
            case REQ_GET_MANAGEABLE_GROUPS:
                if (data.getBaseData() instanceof String) {
                    getManageableGroups(command, (String) data.getBaseData(), listener);
                }
                break;
            case REQ_GET_SEARCHED_GROUPS:
                if (data.getBaseData() instanceof String) {
                    getSearchedGroups(command, (String) data.getBaseData(), listener);
                }
                break;
            case REQ_GET_PROJECT_DETAILS:
                if (data.getBaseData() instanceof String) {
                    getProjectDetails(command, (String) data.getBaseData(), listener);
                }
                break;
            default:
                break;
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
        Call<List<UserModel>> call = service.getRecentUsers(getAccessToken());
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
        Call<List<MessageModel>> call = service.getDirectMessages(getAccessToken(), messageRequest.receiver);
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
        Call<List<AllGroupResponse>> call = service.getAllGroups(getAccessToken());
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
        Call<UserModel> call = service.getJoinedGroups(getAccessToken());
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
        Call<List<MessageModel>> call = service.getGrpMessages(getAccessToken(), groupId.groupId);
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
        Call<List<UserModel>> call = service.getSearchedUsers(getAccessToken(), query);
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

    private void getAllEvents(final RESTCommands command, final OnResponseStatusListener listener) {
        ((EngineService) EngineClient.getClient().create(EngineService.class)).getAllEvents(getAccessToken()).enqueue(new Callback<List<SocialPostResponse>>() {
            public void onResponse(Call<List<SocialPostResponse>> call, Response<List<SocialPostResponse>> response) {
                handleOnResponse(command, new BaseData(null), response, listener);
            }

            public void onFailure(Call<List<SocialPostResponse>> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, (BaseData<?>) null, t);
                }
            }
        });
    }

    private void getProjectDetails(final RESTCommands command, final String projectId, final OnResponseStatusListener listener) {
        ((EngineService) EngineClient.getClient().create(EngineService.class)).getProjectDetails(getAccessToken(), projectId).enqueue(new Callback<ProjectResponse>() {
            public void onResponse(Call<ProjectResponse> call, Response<ProjectResponse> response) {
                handleOnResponse(command, new BaseData(projectId), response, listener);
            }

            public void onFailure(Call<ProjectResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, (BaseData<?>) null, t);
                }
            }
        });
    }

    private void editUserProject(final RESTCommands command, ProjectRequest projectRequest, final OnResponseStatusListener listener) {
        ((EngineService) EngineClient.getClient().create(EngineService.class)).editProjects(getAccessToken(), projectRequest.id, projectRequest).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleOnResponse(command, (BaseData<?>) null, response, listener);
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
                OnResponseStatusListener onResponseStatusListener = listener;
                if (onResponseStatusListener != null) {
                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
                }
            }
        });
    }

    private void deleteUserProject(final RESTCommands command, String projectId, final OnResponseStatusListener listener) {
        ((EngineService) EngineClient.getClient().create(EngineService.class)).deleteProjects(getAccessToken(), projectId).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleOnResponse(command, (BaseData<?>) null, response, listener);
            }

            public void onFailure(Call<ResponseBody> call, Throwable t) {
                OnResponseStatusListener onResponseStatusListener = listener;
                if (onResponseStatusListener != null) {
                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
                }
            }
        });
    }

    private void getUserProjects(final RESTCommands command, String userId, final OnResponseStatusListener listener) {
        ((EngineService) EngineClient.getClient().create(EngineService.class)).getProjects(getAccessToken(), userId).enqueue(new Callback<List<ProjectResponse>>() {
            public void onResponse(Call<List<ProjectResponse>> call, Response<List<ProjectResponse>> response) {
                handleOnResponse(command, (BaseData<?>) null, response, listener);
            }

            public void onFailure(Call<List<ProjectResponse>> call, Throwable t) {
                OnResponseStatusListener onResponseStatusListener = listener;
                if (onResponseStatusListener != null) {
                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
                }
            }
        });
    }

    private void getSearchedGroups(final RESTCommands command, final String query, final OnResponseStatusListener listener) {
        ((EngineService) EngineClient.getClient().create(EngineService.class)).getSearchedGroups(getAccessToken(), query).enqueue(new Callback<List<AllGroupResponse>>() {
            public void onResponse(Call<List<AllGroupResponse>> call, Response<List<AllGroupResponse>> response) {
                handleOnResponse(command, new BaseData(query), response, listener);
            }

            public void onFailure(Call<List<AllGroupResponse>> call, Throwable t) {
                OnResponseStatusListener onResponseStatusListener = listener;
                if (onResponseStatusListener != null) {
                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
                }
            }
        });
    }

    private void getGroupPosts(final RESTCommands command, final String groupId, final OnResponseStatusListener listener) {
        ((EngineService) EngineClient.getClient().create(EngineService.class)).getGroupPosts(getAccessToken(), groupId).enqueue(new Callback<List<SocialPostResponse>>() {
            public void onResponse(Call<List<SocialPostResponse>> call, Response<List<SocialPostResponse>> response) {
                handleOnResponse(command, new BaseData(groupId), response, listener);
            }

            public void onFailure(Call<List<SocialPostResponse>> call, Throwable t) {
                OnResponseStatusListener onResponseStatusListener = listener;
                if (onResponseStatusListener != null) {
                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
                }
            }
        });
    }

    private void getJoinedCommunitiesPosts(final RESTCommands command, final OnResponseStatusListener listener) {
        ((EngineService) EngineClient.getClient().create(EngineService.class)).getJoinedCommunitiesPosts(getAccessToken()).enqueue(new Callback<List<SocialPostResponse>>() {
            public void onResponse(Call<List<SocialPostResponse>> call, Response<List<SocialPostResponse>> response) {
                handleOnResponse(command, (BaseData<?>) null, response, listener);
            }

            public void onFailure(Call<List<SocialPostResponse>> call, Throwable t) {
                OnResponseStatusListener onResponseStatusListener = listener;
                if (onResponseStatusListener != null) {
                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
                }
            }
        });
    }

    private void getManageableGroups(final RESTCommands command, final String userId, final OnResponseStatusListener listener) {
        ((EngineService) EngineClient.getClient().create(EngineService.class)).getManageableGroups(getAccessToken(), userId).enqueue(new Callback<List<AllGroupResponse>>() {
            public void onResponse(Call<List<AllGroupResponse>> call, Response<List<AllGroupResponse>> response) {
                handleOnResponse(command, new BaseData(userId), response, listener);
            }

            public void onFailure(Call<List<AllGroupResponse>> call, Throwable t) {
                OnResponseStatusListener onResponseStatusListener = listener;
                if (onResponseStatusListener != null) {
                    onResponseStatusListener.onResponseFailed(command, new BaseData(userId), t);
                }
            }
        });
    }

    private void getExploreProjects(final RESTCommands command, final OnResponseStatusListener listener) {
        ((EngineService) EngineClient.getClient().create(EngineService.class)).getExploreProjects(getAccessToken(), "10").enqueue(new Callback<List<ProjectResponse>>() {
            public void onResponse(Call<List<ProjectResponse>> call, Response<List<ProjectResponse>> response) {
                handleOnResponse(command, (BaseData<?>) null, response, listener);
            }

            public void onFailure(Call<List<ProjectResponse>> call, Throwable t) {
                OnResponseStatusListener onResponseStatusListener = listener;
                if (onResponseStatusListener != null) {
                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
                }
            }
        });
    }

    private void getExploreUsers(final RESTCommands command, final OnResponseStatusListener listener) {
        ((EngineService) EngineClient.getClient().create(EngineService.class)).getExploreUsers(getAccessToken(), "10").enqueue(new Callback<List<UserModel>>() {
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                handleOnResponse(command, (BaseData<?>) null, response, listener);
            }

            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                OnResponseStatusListener onResponseStatusListener = listener;
                if (onResponseStatusListener != null) {
                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
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

    public interface OnResponseStatusListener {
        void onResponseReceived(RESTCommands commands, BaseData<?> data, Response<?> response);

        void onResponseFailed(RESTCommands commands, BaseData<?> data, Throwable t);
    }


}
