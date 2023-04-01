//package com.indianapp.techbpit.ApiController;
//
//import android.content.Context;
//import android.content.Intent;
//import com.indianapp.techbpit.SharedPrefHelper;
//import com.indianapp.techbpit.activities.LoginActivity;
//import com.indianapp.techbpit.model.AllGroupResponse;
//import com.indianapp.techbpit.model.GroupMessageRequest;
//import com.indianapp.techbpit.model.GroupResponse;
//import com.indianapp.techbpit.model.JoinGroupRequest;
//import com.indianapp.techbpit.model.MessageModel;
//import com.indianapp.techbpit.model.MessageRequest;
//import com.indianapp.techbpit.model.OTPVerifyRequest;
//import com.indianapp.techbpit.model.PayLoad;
//import com.indianapp.techbpit.model.ProjectRequest;
//import com.indianapp.techbpit.model.ProjectResponse;
//import com.indianapp.techbpit.model.RefreshTokenRequest;
//import com.indianapp.techbpit.model.SetupProfileRequest;
//import com.indianapp.techbpit.model.SignUpRequestModel;
//import com.indianapp.techbpit.model.SocialPostRequest;
//import com.indianapp.techbpit.model.SocialPostResponse;
//import com.indianapp.techbpit.model.UserModel;
//import com.indianapp.techbpit.utils.SharedPrefHelper;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class RESTController {
//    private static RESTController instance = null;
//    /* access modifiers changed from: private */
//    public Context context;
//    /* access modifiers changed from: private */
//    public boolean isRenewTokenCalled = false;
//    /* access modifiers changed from: private */
//    public ArrayList<PayLoad> pendingApiCalls = new ArrayList<>();
//
//    public interface OnResponseStatusListener {
//        void onResponseFailed(RESTCommands rESTCommands, BaseData<?> baseData, Throwable th);
//
//        void onResponseReceived(RESTCommands rESTCommands, BaseData<?> baseData, Response<?> response);
//    }
//
//    public enum RESTCommands {
//        REQ_POST_SIGN_UP_REQ,
//        REQ_POST_LOG_IN_REQ,
//        REQ_POST_OTP_VERIFY,
//        REQ_GET_ALL_USERS,
//        REQ_GET_RECENT_USERS,
//        REQ_GET_MESSAGES,
//        REQ_GET_ALL_GROUPS,
//        REQ_POST_JOIN_GROUP,
//        REQ_GET_JOINED_GROUPS,
//        REQ_GET_GROUP_MESSAGES,
//        REQ_POST_POST,
//        REQ_GET_ALL_POSTS,
//        REQ_PATCH_UPDATE_USER_PROFILE,
//        REQ_GET_USER_PROFILE,
//        REQ_GET_GROUP_DATA,
//        REQ_POST_USER_PROJECTS,
//        REQ_GET_SEARCH_USERS,
//        REQ_GET_COMMUNITY_POSTS,
//        REQ_PATCH_LEAVE_GROUP,
//        REQ_GET_USER_PROJECTS,
//        REQ_PATCH_EDIT_PROJECT,
//        REQ_DELETE_USER_PROJECT,
//        REQ_GET_EXPLORE_PROJECTS,
//        REQ_GET_EXPLORE_USERS,
//        REQ_GET_GRP_POSTS,
//        REQ_GET_ALL_EVENTS,
//        REQ_GET_MANAGEABLE_GROUPS,
//        REQ_GET_SEARCHED_GROUPS,
//        REQ_GET_PROJECT_DETAILS
//    }
//
//    public RESTController(Context context2) {
//        this.context = context2;
//    }
//
//    public static RESTController getInstance(Context context2) throws Exception {
//        if (context2 != null) {
//            if (instance == null) {
//                instance = new RESTController(context2);
//            }
//            return instance;
//        }
//        throw new Exception("Context not provided");
//    }
//
//    public void clearPendingApis() {
//        this.pendingApiCalls.clear();
//    }
//
//    /* renamed from: com.indianapp.techbpit.ApiController.RESTController$31 */
//    static /* synthetic */ class C003031 {
//
//        /* renamed from: $SwitchMap$com$indianapp$techbpit$ApiController$RESTController$RESTCommands */
//        static final /* synthetic */ int[] f8x9a2afa2a;
//
//        static {
//            int[] iArr = new int[RESTCommands.values().length];
//            f8x9a2afa2a = iArr;
//            try {
//                iArr[RESTCommands.REQ_POST_SIGN_UP_REQ.ordinal()] = 1;
//            } catch (NoSuchFieldError e) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_POST_LOG_IN_REQ.ordinal()] = 2;
//            } catch (NoSuchFieldError e2) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_POST_OTP_VERIFY.ordinal()] = 3;
//            } catch (NoSuchFieldError e3) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_GET_ALL_USERS.ordinal()] = 4;
//            } catch (NoSuchFieldError e4) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_GET_RECENT_USERS.ordinal()] = 5;
//            } catch (NoSuchFieldError e5) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_GET_MESSAGES.ordinal()] = 6;
//            } catch (NoSuchFieldError e6) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_GET_ALL_GROUPS.ordinal()] = 7;
//            } catch (NoSuchFieldError e7) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_POST_JOIN_GROUP.ordinal()] = 8;
//            } catch (NoSuchFieldError e8) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_GET_JOINED_GROUPS.ordinal()] = 9;
//            } catch (NoSuchFieldError e9) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_GET_GROUP_MESSAGES.ordinal()] = 10;
//            } catch (NoSuchFieldError e10) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_POST_POST.ordinal()] = 11;
//            } catch (NoSuchFieldError e11) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_GET_ALL_POSTS.ordinal()] = 12;
//            } catch (NoSuchFieldError e12) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_PATCH_UPDATE_USER_PROFILE.ordinal()] = 13;
//            } catch (NoSuchFieldError e13) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_POST_USER_PROJECTS.ordinal()] = 14;
//            } catch (NoSuchFieldError e14) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_GET_USER_PROFILE.ordinal()] = 15;
//            } catch (NoSuchFieldError e15) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_GET_SEARCH_USERS.ordinal()] = 16;
//            } catch (NoSuchFieldError e16) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_GET_GROUP_DATA.ordinal()] = 17;
//            } catch (NoSuchFieldError e17) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_PATCH_LEAVE_GROUP.ordinal()] = 18;
//            } catch (NoSuchFieldError e18) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_GET_COMMUNITY_POSTS.ordinal()] = 19;
//            } catch (NoSuchFieldError e19) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_GET_USER_PROJECTS.ordinal()] = 20;
//            } catch (NoSuchFieldError e20) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_PATCH_EDIT_PROJECT.ordinal()] = 21;
//            } catch (NoSuchFieldError e21) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_DELETE_USER_PROJECT.ordinal()] = 22;
//            } catch (NoSuchFieldError e22) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_GET_EXPLORE_PROJECTS.ordinal()] = 23;
//            } catch (NoSuchFieldError e23) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_GET_EXPLORE_USERS.ordinal()] = 24;
//            } catch (NoSuchFieldError e24) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_GET_GRP_POSTS.ordinal()] = 25;
//            } catch (NoSuchFieldError e25) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_GET_ALL_EVENTS.ordinal()] = 26;
//            } catch (NoSuchFieldError e26) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_GET_MANAGEABLE_GROUPS.ordinal()] = 27;
//            } catch (NoSuchFieldError e27) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_GET_SEARCHED_GROUPS.ordinal()] = 28;
//            } catch (NoSuchFieldError e28) {
//            }
//            try {
//                f8x9a2afa2a[RESTCommands.REQ_GET_PROJECT_DETAILS.ordinal()] = 29;
//            } catch (NoSuchFieldError e29) {
//            }
//        }
//    }
//
//    public void execute(RESTCommands command, BaseData<?> data, OnResponseStatusListener listener) {
//        if (!this.isRenewTokenCalled) {
//            switch (C003031.f8x9a2afa2a[command.ordinal()]) {
//                case 1:
//                    if (data.getBaseData() instanceof SignUpRequestModel) {
//                        postSignUpReq(command, (SignUpRequestModel) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 2:
//                    if (data.getBaseData() instanceof SignUpRequestModel) {
//                        postLoginReq(command, (SignUpRequestModel) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 3:
//                    if (data.getBaseData() instanceof OTPVerifyRequest) {
//                        postOTPVerify(command, (OTPVerifyRequest) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 4:
//                    getAllUsers(command, listener);
//                    return;
//                case 5:
//                    getRecentUser(command, listener);
//                    return;
//                case 6:
//                    if (data.getBaseData() instanceof MessageRequest) {
//                        getDirectMessages(command, (MessageRequest) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 7:
//                    getAllGroups(command, listener);
//                    return;
//                case 8:
//                    if (data.getBaseData() instanceof JoinGroupRequest) {
//                        postJoinGroup(command, (JoinGroupRequest) data.getBaseData(), listener);
//                        break;
//                    }
//                    break;
//                case 9:
//                    break;
//                case 10:
//                    break;
//                case 11:
//                    if (data.getBaseData() instanceof SocialPostRequest) {
//                        postSocialPost(command, (SocialPostRequest) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 12:
//                    getAllPosts(command, listener);
//                    return;
//                case 13:
//                    if (data.getBaseData() instanceof SetupProfileRequest) {
//                        updateUserProfile(command, (SetupProfileRequest) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 14:
//                    if (data.getBaseData() instanceof ProjectRequest) {
//                        postUserProjects(command, (ProjectRequest) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 15:
//                    if (data.getBaseData() instanceof String) {
//                        getUserProfile(command, (String) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 16:
//                    if (data.getBaseData() instanceof String) {
//                        getSearchesUsers(command, (String) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 17:
//                    if (data.getBaseData() instanceof String) {
//                        getGroupData(command, (String) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 18:
//                    if (data.getBaseData() instanceof String) {
//                        leaveGroup(command, (String) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 19:
//                    getJoinedCommunitiesPosts(command, listener);
//                    return;
//                case 20:
//                    getUserProjects(command, (String) data.getBaseData(), listener);
//                    return;
//                case 21:
//                    if (data.getBaseData() instanceof ProjectRequest) {
//                        editUserProject(command, (ProjectRequest) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 22:
//                    deleteUserProject(command, (String) data.getBaseData(), listener);
//                    return;
//                case 23:
//                    getExploreProjects(command, listener);
//                    return;
//                case 24:
//                    getExploreUsers(command, listener);
//                    return;
//                case 25:
//                    getGroupPosts(command, (String) data.getBaseData(), listener);
//                    return;
//                case 26:
//                    getAllEvents(command, listener);
//                    return;
//                case 27:
//                    getManageableGroups(command, (String) data.getBaseData(), listener);
//                    return;
//                case 28:
//                    if (data.getBaseData() instanceof String) {
//                        getSearchedGroups(command, (String) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 29:
//                    getProjectDetails(command, (String) data.getBaseData(), listener);
//                    return;
//                default:
//                    return;
//            }
//            if (data.getBaseData() instanceof UserModel) {
//                getJoinedGroups(command, (UserModel) data.getBaseData(), listener);
//            }
//            if (data.getBaseData() instanceof GroupMessageRequest) {
//                getGroupMessages(command, (GroupMessageRequest) data.getBaseData(), listener);
//                return;
//            }
//            return;
//        }
//        this.pendingApiCalls.add(new PayLoad(command, data, listener));
//    }
//
//    public void fetchParamsFromPayloadAndExecute(PayLoad payLoad) {
//        RESTCommands command = payLoad.command;
//        BaseData<?> data = payLoad.data;
//        OnResponseStatusListener listener = payLoad.listener;
//        if (!this.isRenewTokenCalled) {
//            switch (C003031.f8x9a2afa2a[command.ordinal()]) {
//                case 1:
//                    if (data.getBaseData() instanceof SignUpRequestModel) {
//                        postSignUpReq(command, (SignUpRequestModel) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 2:
//                    if (data.getBaseData() instanceof SignUpRequestModel) {
//                        postLoginReq(command, (SignUpRequestModel) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 3:
//                    if (data.getBaseData() instanceof OTPVerifyRequest) {
//                        postOTPVerify(command, (OTPVerifyRequest) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 4:
//                    getAllUsers(command, listener);
//                    return;
//                case 5:
//                    getRecentUser(command, listener);
//                    return;
//                case 6:
//                    if (data.getBaseData() instanceof MessageRequest) {
//                        getDirectMessages(command, (MessageRequest) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 7:
//                    getAllGroups(command, listener);
//                    return;
//                case 8:
//                    if (data.getBaseData() instanceof JoinGroupRequest) {
//                        postJoinGroup(command, (JoinGroupRequest) data.getBaseData(), listener);
//                        break;
//                    }
//                    break;
//                case 9:
//                    break;
//                case 10:
//                    break;
//                case 11:
//                    if (data.getBaseData() instanceof SocialPostRequest) {
//                        postSocialPost(command, (SocialPostRequest) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 12:
//                    getAllPosts(command, listener);
//                    return;
//                case 13:
//                    if (data.getBaseData() instanceof SetupProfileRequest) {
//                        updateUserProfile(command, (SetupProfileRequest) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 14:
//                    if (data.getBaseData() instanceof ProjectRequest) {
//                        postUserProjects(command, (ProjectRequest) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 15:
//                    if (data.getBaseData() instanceof String) {
//                        getUserProfile(command, (String) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 16:
//                    if (data.getBaseData() instanceof String) {
//                        getSearchesUsers(command, (String) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 17:
//                    if (data.getBaseData() instanceof String) {
//                        getGroupData(command, (String) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 18:
//                    if (data.getBaseData() instanceof String) {
//                        leaveGroup(command, (String) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 19:
//                    getJoinedCommunitiesPosts(command, listener);
//                    return;
//                case 20:
//                    getUserProjects(command, (String) data.getBaseData(), listener);
//                    return;
//                case 21:
//                    if (data.getBaseData() instanceof ProjectRequest) {
//                        editUserProject(command, (ProjectRequest) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 22:
//                    deleteUserProject(command, (String) data.getBaseData(), listener);
//                    return;
//                case 23:
//                    getExploreProjects(command, listener);
//                    return;
//                case 24:
//                    getExploreUsers(command, listener);
//                    return;
//                case 25:
//                    getGroupPosts(command, (String) data.getBaseData(), listener);
//                    return;
//                case 26:
//                    getAllEvents(command, listener);
//                    return;
//                case 27:
//                    getManageableGroups(command, (String) data.getBaseData(), listener);
//                    return;
//                case 28:
//                    if (data.getBaseData() instanceof String) {
//                        getSearchedGroups(command, (String) data.getBaseData(), listener);
//                        return;
//                    }
//                    return;
//                case 29:
//                    getProjectDetails(command, (String) data.getBaseData(), listener);
//                    return;
//                default:
//                    return;
//            }
//            if (data.getBaseData() instanceof UserModel) {
//                getJoinedGroups(command, (UserModel) data.getBaseData(), listener);
//            }
//            if (data.getBaseData() instanceof GroupMessageRequest) {
//                getGroupMessages(command, (GroupMessageRequest) data.getBaseData(), listener);
//                return;
//            }
//            return;
//        }
//        this.pendingApiCalls.add(new PayLoad(command, data, listener));
//    }
//
//    private void getExploreProjects(final RESTCommands command, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).getExploreProjects(getAccessToken(), "10").enqueue(new Callback<List<ProjectResponse>>() {
//            public void onResponse(Call<List<ProjectResponse>> call, Response<List<ProjectResponse>> response) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseReceived(command, (BaseData<?>) null, response);
//                }
//            }
//
//            public void onFailure(Call<List<ProjectResponse>> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
//                }
//            }
//        });
//    }
//
//    private void getExploreUsers(final RESTCommands command, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).getExploreUsers(getAccessToken(), "10").enqueue(new Callback<List<UserModel>>() {
//            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseReceived(command, (BaseData<?>) null, response);
//                }
//            }
//
//            public void onFailure(Call<List<UserModel>> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
//                }
//            }
//        });
//    }
//
//    private void postSignUpReq(final RESTCommands command, final SignUpRequestModel signUpRequestModel, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).sendSignUpReq(signUpRequestModel).enqueue(new Callback<ResponseBody>() {
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseReceived(command, new BaseData(signUpRequestModel), response);
//                }
//            }
//
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, new BaseData(signUpRequestModel), t);
//                }
//            }
//        });
//    }
//
//    private void postLoginReq(final RESTCommands command, final SignUpRequestModel signUpRequestModel, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).sendLoginReq(signUpRequestModel).enqueue(new Callback<UserModel>() {
//            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseReceived(command, new BaseData(signUpRequestModel), response);
//                }
//            }
//
//            public void onFailure(Call<UserModel> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, new BaseData(signUpRequestModel), t);
//                }
//            }
//        });
//    }
//
//    private void postOTPVerify(final RESTCommands command, final OTPVerifyRequest otpVerifyRequest, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).sendOTPVerify(otpVerifyRequest).enqueue(new Callback<UserModel>() {
//            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseReceived(command, new BaseData(otpVerifyRequest), response);
//                }
//            }
//
//            public void onFailure(Call<UserModel> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, new BaseData(otpVerifyRequest), t);
//                }
//            }
//        });
//    }
//
//    private void getAllUsers(final RESTCommands command, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).getAllUsers(getAccessToken()).enqueue(new Callback<List<UserModel>>() {
//            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
//                RESTController.this.handleOnResponse(command, (BaseData<?>) null, response, listener);
//            }
//
//            public void onFailure(Call<List<UserModel>> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
//                }
//            }
//        });
//    }
//
//    private void getRecentUser(final RESTCommands command, final OnResponseStatusListener listener) {
//        new HashMap();
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).getRecentUsers(getAccessToken()).enqueue(new Callback<List<UserModel>>() {
//            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
//                RESTController.this.handleOnResponse(command, (BaseData<?>) null, response, listener);
//            }
//
//            public void onFailure(Call<List<UserModel>> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
//                }
//            }
//        });
//    }
//
//    private void getDirectMessages(final RESTCommands command, final MessageRequest messageRequest, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).getDirectMessages(getAccessToken(), messageRequest.receiver).enqueue(new Callback<List<MessageModel>>() {
//            public void onResponse(Call<List<MessageModel>> call, Response<List<MessageModel>> response) {
//                RESTController.this.handleOnResponse(command, new BaseData(messageRequest), response, listener);
//            }
//
//            public void onFailure(Call<List<MessageModel>> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, new BaseData(messageRequest), t);
//                }
//            }
//        });
//    }
//
//    private void getAllGroups(final RESTCommands command, final OnResponseStatusListener listener) {
//        new JoinGroupRequest().userId = SharedPrefHelper.getUserModel(this.context)._id;
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).getAllGroups(getAccessToken()).enqueue(new Callback<List<AllGroupResponse>>() {
//            public void onResponse(Call<List<AllGroupResponse>> call, Response<List<AllGroupResponse>> response) {
//                RESTController.this.handleOnResponse(command, (BaseData<?>) null, response, listener);
//            }
//
//            public void onFailure(Call<List<AllGroupResponse>> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, new BaseData(null), t);
//                }
//            }
//        });
//    }
//
//    private void postJoinGroup(final RESTCommands command, final JoinGroupRequest joinGroupRequest, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).postJoinGroup(getAccessToken(), joinGroupRequest).enqueue(new Callback<ResponseBody>() {
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                RESTController.this.handleOnResponse(command, new BaseData(joinGroupRequest), response, listener);
//            }
//
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, new BaseData(joinGroupRequest), t);
//                }
//            }
//        });
//    }
//
//    private void getJoinedGroups(final RESTCommands command, final UserModel getJoinedReq, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).getJoinedGroups(getAccessToken()).enqueue(new Callback<UserModel>() {
//            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                RESTController.this.handleOnResponse(command, new BaseData(getJoinedReq), response, listener);
//            }
//
//            public void onFailure(Call<UserModel> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, new BaseData(getJoinedReq), t);
//                }
//            }
//        });
//    }
//
//    private void getGroupMessages(final RESTCommands command, final GroupMessageRequest groupId, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).getGrpMessages(getAccessToken(), groupId.groupId).enqueue(new Callback<List<MessageModel>>() {
//            public void onResponse(Call<List<MessageModel>> call, Response<List<MessageModel>> response) {
//                RESTController.this.handleOnResponse(command, new BaseData(groupId), response, listener);
//            }
//
//            public void onFailure(Call<List<MessageModel>> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, new BaseData(groupId), t);
//                }
//            }
//        });
//    }
//
//    private void getManageableGroups(final RESTCommands command, final String userId, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).getManageableGroups(getAccessToken(), userId).enqueue(new Callback<List<AllGroupResponse>>() {
//            public void onResponse(Call<List<AllGroupResponse>> call, Response<List<AllGroupResponse>> response) {
//                RESTController.this.handleOnResponse(command, new BaseData(userId), response, listener);
//            }
//
//            public void onFailure(Call<List<AllGroupResponse>> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, new BaseData(userId), t);
//                }
//            }
//        });
//    }
//
//    private void postSocialPost(final RESTCommands command, final SocialPostRequest socialPostRequest, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).postSocialPost(getAccessToken(), socialPostRequest).enqueue(new Callback<ResponseBody>() {
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                RESTController.this.handleOnResponse(command, new BaseData(socialPostRequest), response, listener);
//            }
//
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, new BaseData(socialPostRequest), t);
//                }
//            }
//        });
//    }
//
//    private void getAllPosts(final RESTCommands command, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).getAllPosts(getAccessToken()).enqueue(new Callback<List<SocialPostResponse>>() {
//            public void onResponse(Call<List<SocialPostResponse>> call, Response<List<SocialPostResponse>> response) {
//                RESTController.this.handleOnResponse(command, (BaseData<?>) null, response, listener);
//            }
//
//            public void onFailure(Call<List<SocialPostResponse>> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
//                }
//            }
//        });
//    }
//
//    private void getJoinedCommunitiesPosts(final RESTCommands command, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).getJoinedCommunitiesPosts(getAccessToken()).enqueue(new Callback<List<SocialPostResponse>>() {
//            public void onResponse(Call<List<SocialPostResponse>> call, Response<List<SocialPostResponse>> response) {
//                RESTController.this.handleOnResponse(command, (BaseData<?>) null, response, listener);
//            }
//
//            public void onFailure(Call<List<SocialPostResponse>> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
//                }
//            }
//        });
//    }
//
//    private void updateUserProfile(final RESTCommands command, SetupProfileRequest setupProfileRequest, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).updateUserProfile(getAccessToken(), setupProfileRequest).enqueue(new Callback<ResponseBody>() {
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                RESTController.this.handleOnResponse(command, (BaseData<?>) null, response, listener);
//            }
//
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
//                }
//            }
//        });
//    }
//
//    private void postUserProjects(final RESTCommands command, final ProjectRequest projectRequest, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).postProjects(getAccessToken(), projectRequest).enqueue(new Callback<ResponseBody>() {
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                RESTController.this.handleOnResponse(command, new BaseData(projectRequest), response, listener);
//            }
//
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
//                }
//            }
//        });
//    }
//
//    private void getUserProfile(final RESTCommands command, final String userId, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).getUserProfile(getAccessToken(), userId).enqueue(new Callback<UserModel>() {
//            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                RESTController.this.handleOnResponse(command, new BaseData(userId), response, listener);
//            }
//
//            public void onFailure(Call<UserModel> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
//                }
//            }
//        });
//    }
//
//    private void getGroupData(final RESTCommands command, final String groupId, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).getGroupData(getAccessToken(), groupId).enqueue(new Callback<GroupResponse>() {
//            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
//                RESTController.this.handleOnResponse(command, new BaseData(groupId), response, listener);
//            }
//
//            public void onFailure(Call<GroupResponse> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
//                }
//            }
//        });
//    }
//
//    private void getGroupPosts(final RESTCommands command, final String groupId, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).getGroupPosts(getAccessToken(), groupId).enqueue(new Callback<List<SocialPostResponse>>() {
//            public void onResponse(Call<List<SocialPostResponse>> call, Response<List<SocialPostResponse>> response) {
//                RESTController.this.handleOnResponse(command, new BaseData(groupId), response, listener);
//            }
//
//            public void onFailure(Call<List<SocialPostResponse>> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
//                }
//            }
//        });
//    }
//
//    private void getAllEvents(final RESTCommands command, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).getAllEvents(getAccessToken()).enqueue(new Callback<List<SocialPostResponse>>() {
//            public void onResponse(Call<List<SocialPostResponse>> call, Response<List<SocialPostResponse>> response) {
//                RESTController.this.handleOnResponse(command, new BaseData(null), response, listener);
//            }
//
//            public void onFailure(Call<List<SocialPostResponse>> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
//                }
//            }
//        });
//    }
//
//    private void leaveGroup(final RESTCommands command, final String groupId, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).leaveGroup(getAccessToken(), groupId).enqueue(new Callback<ResponseBody>() {
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                RESTController.this.handleOnResponse(command, new BaseData(groupId), response, listener);
//            }
//
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
//                }
//            }
//        });
//    }
//
//    private void getSearchesUsers(final RESTCommands command, final String query, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).getSearchedUsers(getAccessToken(), query).enqueue(new Callback<List<UserModel>>() {
//            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
//                RESTController.this.handleOnResponse(command, new BaseData(query), response, listener);
//            }
//
//            public void onFailure(Call<List<UserModel>> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
//                }
//            }
//        });
//    }
//
//    private void getSearchedGroups(final RESTCommands command, final String query, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).getSearchedGroups(getAccessToken(), query).enqueue(new Callback<List<AllGroupResponse>>() {
//            public void onResponse(Call<List<AllGroupResponse>> call, Response<List<AllGroupResponse>> response) {
//                RESTController.this.handleOnResponse(command, new BaseData(query), response, listener);
//            }
//
//            public void onFailure(Call<List<AllGroupResponse>> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
//                }
//            }
//        });
//    }
//
//    private void getUserProjects(final RESTCommands command, String userId, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).getProjects(getAccessToken(), userId).enqueue(new Callback<List<ProjectResponse>>() {
//            public void onResponse(Call<List<ProjectResponse>> call, Response<List<ProjectResponse>> response) {
//                RESTController.this.handleOnResponse(command, (BaseData<?>) null, response, listener);
//            }
//
//            public void onFailure(Call<List<ProjectResponse>> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
//                }
//            }
//        });
//    }
//
//    private void getProjectDetails(final RESTCommands command, final String projectId, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).getProjectDetails(getAccessToken(), projectId).enqueue(new Callback<ProjectResponse>() {
//            public void onResponse(Call<ProjectResponse> call, Response<ProjectResponse> response) {
//                RESTController.this.handleOnResponse(command, new BaseData(projectId), response, listener);
//            }
//
//            public void onFailure(Call<ProjectResponse> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
//                }
//            }
//        });
//    }
//
//    private void editUserProject(final RESTCommands command, ProjectRequest projectRequest, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).editProjects(getAccessToken(), projectRequest.f4id, projectRequest).enqueue(new Callback<ResponseBody>() {
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                RESTController.this.handleOnResponse(command, (BaseData<?>) null, response, listener);
//            }
//
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
//                }
//            }
//        });
//    }
//
//    private void deleteUserProject(final RESTCommands command, String projectId, final OnResponseStatusListener listener) {
//        ((EngineService) EngineClient.getClient().create(EngineService.class)).deleteProjects(getAccessToken(), projectId).enqueue(new Callback<ResponseBody>() {
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                RESTController.this.handleOnResponse(command, (BaseData<?>) null, response, listener);
//            }
//
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                OnResponseStatusListener onResponseStatusListener = listener;
//                if (onResponseStatusListener != null) {
//                    onResponseStatusListener.onResponseFailed(command, (BaseData<?>) null, t);
//                }
//            }
//        });
//    }
//
//    /* access modifiers changed from: private */
//    public void handleOnResponse(RESTCommands command, BaseData<?> data, Response<?> response, OnResponseStatusListener listener) {
//        if (response.code() == 401) {
//            this.pendingApiCalls.add(new PayLoad(command, data, listener));
//            refreshToken();
//        } else if (listener != null) {
//            listener.onResponseReceived(command, data, response);
//        }
//    }
//
//    private void refreshToken() {
//        if (!this.isRenewTokenCalled) {
//            this.isRenewTokenCalled = true;
//            RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
//            refreshTokenRequest.refresh_token = getRefreshToken();
//            ((EngineService) EngineClient.getClient().create(EngineService.class)).refreshToken(refreshTokenRequest).enqueue(new Callback<UserModel>() {
//                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                    boolean unused = RESTController.this.isRenewTokenCalled = false;
//                    if (response.isSuccessful()) {
//                        SharedPrefHelper.setAccessToken(RESTController.this.context, response.body().access_token);
//                        RESTController.this.callPendingApis();
//                        RESTController.this.pendingApiCalls.clear();
//                    } else if (response.code() == 401) {
//                        RESTController.this.pendingApiCalls.clear();
//                        SharedPrefHelper.setUserModel(RESTController.this.context, new UserModel());
//                        Intent i = new Intent(RESTController.this.context, LoginActivity.class);
//                        i.setFlags(268468224);
//                        RESTController.this.context.startActivity(i);
//                    }
//                }
//
//                public void onFailure(Call<UserModel> call, Throwable t) {
//                }
//            });
//        }
//    }
//
//    /* access modifiers changed from: private */
//    public void callPendingApis() {
//        Iterator<PayLoad> it = this.pendingApiCalls.iterator();
//        while (it.hasNext()) {
//            fetchParamsFromPayloadAndExecute(it.next());
//        }
//    }
//
//    private String getAccessToken() {
//        return "Bearer " + SharedPrefHelper.getUserModel(this.context).access_token;
//    }
//
//    private String getRefreshToken() {
//        return SharedPrefHelper.getUserModel(this.context).refresh_token;
//    }
//}