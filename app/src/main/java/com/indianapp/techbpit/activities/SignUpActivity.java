package com.indianapp.techbpit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.indianapp.techbpit.ApiController.BaseData;
import com.indianapp.techbpit.ApiController.RESTController;
import com.indianapp.techbpit.databinding.ActivitySignUpBinding;
import com.indianapp.techbpit.model.SignUpRequestModel;

import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements RESTController.OnResponseStatusListener {
    SignUpRequestModel signUpRequestModel;
    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        binding.button2.setOnClickListener(v -> {
            if (validate()) {
                binding.button2.setEnabled(false);
                binding.button2.setText("Signing up ...");
                signUpRequestModel = new SignUpRequestModel();
                signUpRequestModel.email = String.valueOf(binding.editTextTextEmailAddress.getText());
                signUpRequestModel.username = String.valueOf(binding.editTextTextPersonName.getText());
                signUpRequestModel.password = String.valueOf(binding.editTextTextPassword.getText());
                BaseData<SignUpRequestModel> baseData = new BaseData<>(signUpRequestModel);
                try {
                    RESTController.getInstance(this).execute(RESTController.RESTCommands.REQ_POST_SIGN_UP_REQ, baseData, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        binding.ivBack.setOnClickListener(v -> onBackPressed());
    }

    private boolean validate() {
        boolean valid = true;
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.editTextTextEmailAddress.getText()).matches()) {
            binding.editTextTextEmailAddress.setError("Enter valid email");
            valid = false;
        }
        if (TextUtils.isEmpty(String.valueOf(binding.editTextTextPersonName.getText()))) {
            binding.editTextTextPersonName.setError("Enter username");
            valid = false;
        }
        if (TextUtils.isEmpty(String.valueOf(binding.editTextTextPassword.getText()))) {
            binding.editTextTextPassword.setError("Enter password");
            valid = false;
        }
        return valid;
    }

    @Override
    public void onResponseReceived(RESTController.RESTCommands commands, BaseData<?> request, Response<?> response) {
        switch (commands) {
            case REQ_POST_SIGN_UP_REQ:
                if (response.isSuccessful()) {
                    Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, OTPVerificationActivity.class);
                    intent.putExtra("email", signUpRequestModel.email);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    binding.button2.setEnabled(true);
                    binding.button2.setText("Proceed");
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            RESTController.getInstance(this).clearPendingApis();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponseFailed(RESTController.RESTCommands commands, BaseData<?> request, Throwable t) {
        switch (commands) {
            case REQ_POST_SIGN_UP_REQ:
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                binding.button2.setEnabled(true);
                binding.button2.setText("Proceed");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}