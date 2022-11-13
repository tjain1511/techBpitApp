package com.indianapp.techbpit;

import android.content.Context;
import android.icu.text.MessagePattern;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RESTController {
    private static RESTController instance = null;
    private Context context;

    public enum RESTCommands {
        REQ_POST_SIGN_UP_REQ,
        REQ_POST_LOG_IN_REQ,
        REQ_POST_OTP_VERIFY,
        REQ_GET_ALL_USERS,
        REQ_GET_MESSAGES
    }

    public interface OnResponseStatusListener {
        void onResponseReceived(RESTCommands commands, Call<?> request, Response<?> response);

        void onResponseFailed(RESTCommands commands, Call<?> request, Throwable t);
    }

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
                if(data.getBaseData() instanceof MessageRequest){
                    messageRequest = (MessageRequest) data.getBaseData();
                    getDirectMessages(command,messageRequest, listener);
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
                    listener.onResponseReceived(command, call, response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, call, t);
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
                    listener.onResponseReceived(command, call, response);
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, call, t);
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
                    listener.onResponseReceived(command, call, response);
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, call, t);
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
                    listener.onResponseReceived(command, call, response);
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, call, t);
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
                    listener.onResponseReceived(command, call, response);
                }
            }

            @Override
            public void onFailure(Call<List<MessageModel>> call, Throwable t) {
                if (listener != null) {
                    listener.onResponseFailed(command, call, t);
                }
            }
        });
    }


}
