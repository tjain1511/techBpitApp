package com.indianapp.techbpit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.indianapp.techbpit.R;

public class StartActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        sharedPreferences = this.getSharedPreferences("com.indianapp.techbpit", MODE_PRIVATE);
    }
}