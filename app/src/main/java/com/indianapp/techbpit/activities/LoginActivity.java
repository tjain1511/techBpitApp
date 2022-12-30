package com.indianapp.techbpit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.indianapp.techbpit.ApiController.BaseData;
import com.indianapp.techbpit.ApiController.RESTController;
import com.indianapp.techbpit.SharedPrefHelper;
import com.indianapp.techbpit.databinding.ActivityLoginBinding;
import com.indianapp.techbpit.model.SignUpRequestModel;
import com.indianapp.techbpit.model.UserModel;

import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements RESTController.OnResponseStatusListener {
    private ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setOnClickListener();
    }

    private void setOnClickListener() {
        binding.button3.setOnClickListener(v -> {
            if (validate()) {
                binding.button3.setText("Logging in...");
                SignUpRequestModel signUpRequestModel = new SignUpRequestModel();
                signUpRequestModel.email = String.valueOf(binding.editTextTextEmailAddress2.getText());
                signUpRequestModel.password = String.valueOf(binding.editTextTextPassword2.getText());
                BaseData<SignUpRequestModel> baseData = new BaseData<>(signUpRequestModel);
                try {
                    RESTController.getInstance(this).execute(RESTController.RESTCommands.REQ_POST_LOG_IN_REQ, baseData, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        binding.ivBack.setOnClickListener(v -> onBackPressed());
    }

    private boolean validate() {
        boolean valid = true;
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.editTextTextEmailAddress2.getText()).matches()) {
            binding.editTextTextEmailAddress2.setError("Enter valid email");
            valid = false;
        }
        if (TextUtils.isEmpty(String.valueOf(binding.editTextTextPassword2.getText()))) {
            binding.editTextTextPassword2.setError("Enter password");
            valid = false;
        }
        return valid;
    }

    @Override
    public void onResponseReceived(RESTController.RESTCommands commands, BaseData<?> request, Response<?> response) {
        switch (commands) {
            case REQ_POST_LOG_IN_REQ:
                if (response.isSuccessful()) {
                    UserModel userModel = (UserModel) response.body();
                    SharedPrefHelper.setUserModel(this, userModel);
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
                    binding.button3.setEnabled(true);
                    binding.button3.setText("Go To Feed");
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
            case REQ_POST_LOG_IN_REQ:
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
                binding.button3.setEnabled(true);
                binding.button3.setText("Go To Feed");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }
}