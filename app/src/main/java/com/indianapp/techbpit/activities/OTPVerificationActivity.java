package com.indianapp.techbpit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.indianapp.techbpit.ApiController.BaseData;
import com.indianapp.techbpit.ApiController.RESTController;
import com.indianapp.techbpit.SharedPrefHelper;
import com.indianapp.techbpit.databinding.ActivityOtpverificationBinding;
import com.indianapp.techbpit.model.OTPVerifyRequest;
import com.indianapp.techbpit.model.UserModel;

import retrofit2.Response;

public class OTPVerificationActivity extends AppCompatActivity implements RESTController.OnResponseStatusListener {
    private ActivityOtpverificationBinding binding;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpverificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        email = getIntent().getStringExtra("email");
        setOnClickListener();
    }

    private void setOnClickListener() {
        binding.verifyOtpBtn.setOnClickListener(v -> {
            binding.verifyOtpBtn.setEnabled(false);
            OTPVerifyRequest otpVerifyRequest = new OTPVerifyRequest();
            otpVerifyRequest.otp = String.valueOf(binding.pinView.getText());
            otpVerifyRequest.email = email;
            BaseData<OTPVerifyRequest> baseData = new BaseData<>(otpVerifyRequest);
            try {
                RESTController.getInstance(this).execute(RESTController.RESTCommands.REQ_POST_OTP_VERIFY, baseData, this);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        binding.ivBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onResponseReceived(RESTController.RESTCommands commands, BaseData<?> request, Response<?> response) {
        switch (commands) {
            case REQ_POST_OTP_VERIFY:
                if (response.isSuccessful()) {
                    UserModel userModel = (UserModel) response.body();
                    SharedPrefHelper.setUserModel(this, userModel);
                    Toast.makeText(this, "Verification successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, JoinGroupActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    binding.verifyOtpBtn.setEnabled(true);
                }
                break;
        }
    }

    @Override
    public void onResponseFailed(RESTController.RESTCommands commands, BaseData<?> request, Throwable t) {
        switch (commands) {
            case REQ_POST_OTP_VERIFY:
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                binding.verifyOtpBtn.setEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}