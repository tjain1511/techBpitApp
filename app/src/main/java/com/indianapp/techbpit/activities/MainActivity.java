package com.indianapp.techbpit.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.indianapp.techbpit.databinding.ActivityAppStartBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityAppStartBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = this.getSharedPreferences("com.indianapp.techbpit", MODE_PRIVATE);
        if (TextUtils.isEmpty(sharedPreferences.getString("my_email", ""))) {
            binding.wholeLayout.setVisibility(View.VISIBLE);
        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        binding.button3.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });
        binding.textView15.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}