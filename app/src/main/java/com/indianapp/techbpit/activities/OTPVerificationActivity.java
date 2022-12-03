package com.indianapp.techbpit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.indianapp.techbpit.BaseData;
import com.indianapp.techbpit.SharedPrefHelper;
import com.indianapp.techbpit.databinding.ActivityOtpverificationBinding;
import com.indianapp.techbpit.model.OTPVerifyRequest;
import com.indianapp.techbpit.RESTController;
import com.indianapp.techbpit.model.UserModel;

import retrofit2.Call;
import retrofit2.Response;

public class OTPVerificationActivity extends AppCompatActivity implements RESTController.OnResponseStatusListener {
    private ActivityOtpverificationBinding binding;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpverificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        email = getIntent().getExtras().getString("email");
        binding.otpDescriptionText.setText("Enter one time password sent on " + email);
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
        Intent intent = new Intent(this, AppStartActivity.class);
        startActivity(intent);
        finish();
    }
}