package com.indianapp.techbpit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.indianapp.techbpit.databinding.ActivityOtpverificationBinding;

import retrofit2.Call;
import retrofit2.Response;

public class OTPVerificationActivity extends AppCompatActivity implements RESTController.OnResponseStatusListener {
    private ActivityOtpverificationBinding binding;
    private String email;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpverificationBinding.inflate(getLayoutInflater());
        mPrefs = this.getSharedPreferences("com.indianapp.techbpit", MODE_PRIVATE);
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
    public void onResponseReceived(RESTController.RESTCommands commands, Call<?> request, Response<?> response) {
        switch (commands) {
            case REQ_POST_OTP_VERIFY:
                if (response.isSuccessful()) {
                    UserModel userModel = (UserModel) response.body();
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("my_email", userModel.email);
                    editor.commit();
                    Toast.makeText(this, "Verification successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, AllUsersActivity.class);
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
    public void onResponseFailed(RESTController.RESTCommands commands, Call<?> request, Throwable t) {
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