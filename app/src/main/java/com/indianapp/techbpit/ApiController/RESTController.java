package com.indianapp.techbpit.ApiController;

import android.content.Context;

import com.indianapp.techbpit.SharedPrefHelper;
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
import retrofit2.Callback;
import retrofit2.Response;

public class RESTController {
    private static RESTController instance = null;
    private Context context;

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

    public void execute(RESTCommands command, BaseData<?> data, OnResponseStatusListener listener) {
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
        Call<List<UserModel>> call = service.getAllUsers();
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (listener != null) {
                    listener.onResponseReceived(command, null, response);
                }
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
        Call<List<MessageModel>> call = service.getDirectMessages(messageRequest);
        call.enqueue(new Callback<List<MessageModel>>() {
            @Override
            public void onResponse(Call<List<MessageModel>> call, Response<List<MessageModel>> response) {
                if (listener != null) {
                    listener.onResponseReceived(command, new BaseData<>(messageRequest), response);
                }
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
        Call<List<GroupResponse>> call = service.getAllGroups(joinGroupRequest);
        call.enqueue(new Callback<List<GroupResponse>>() {
            @Override
            public void onResponse(Call<List<GroupResponse>> call, Response<List<GroupResponse>> response) {
                if (listener != null) {
                    listener.onResponseReceived(command, new BaseData<>(null), response);
                }
            }

            @Override
            public void onFailure(Call<List<GroupResponse>> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, new BaseData<>(null), t);
                }
            }
        });
    }

    private void postJoinGroup(RESTCommands command, JoinGroupRequest joinGroupRequest, OnResponseStatusListener listener) {
        EngineService service = EngineClient.getClient().create(EngineService.class);
        Call<ResponseBody> call = service.postJoinGroup(joinGroupRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (listener != null) {
                    listener.onResponseReceived(command, new BaseData<>(joinGroupRequest), response);
                }
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
        Call<UserModel> call = service.getJoinedGroups(getJoinedReq);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (listener != null) {
                    listener.onResponseReceived(command, new BaseData<>(getJoinedReq), response);
                }
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
        Call<List<MessageModel>> call = service.getGrpMessages(groupId);
        call.enqueue(new Callback<List<MessageModel>>() {
            @Override
            public void onResponse(Call<List<MessageModel>> call, Response<List<MessageModel>> response) {
                if (listener != null) {
                    listener.onResponseReceived(command, new BaseData<>(groupId), response);
                }
            }

            @Override
            public void onFailure(Call<List<MessageModel>> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, new BaseData<>(groupId), t);
                }
            }
        });
    }

    public enum RESTCommands {
        REQ_POST_SIGN_UP_REQ,
        REQ_POST_LOG_IN_REQ,
        REQ_POST_OTP_VERIFY,
        REQ_GET_ALL_USERS,
        REQ_GET_MESSAGES,
        REQ_GET_ALL_GROUPS,
        REQ_POST_JOIN_GROUP,
        REQ_GET_JOINED_GROUPS,
        REQ_GET_GROUP_MESSAGES
    }

    public interface OnResponseStatusListener {
        void onResponseReceived(RESTCommands commands, BaseData<?> data, Response<?> response);

        void onResponseFailed(RESTCommands commands, BaseData<?> data, Throwable t);
    }


}