package com.indianapp.techbpit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.indianapp.techbpit.SocketClient;
import com.indianapp.techbpit.databinding.ActivityFlowBinding;

public class FlowActivity extends AppCompatActivity {
    private ActivityFlowBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setOnClickListener();
//        Picasso.get().load("https://res.cloudinary.com/dmigta0dz/image/upload/v1667237229/bp7xhe2jyte8srslr60y.jpg").into(binding.imageView2);
    }

    private void setOnClickListener() {

        binding.socketBtn.setOnClickListener(v -> {
            SocketClient.setUserId(String.valueOf(binding.myId2.getText()));
            SocketClient.getSocket(this);
            binding.socketBtn.setVisibility(View.GONE);
            binding.myId2.setVisibility(View.GONE);
            binding.dmBtn.setVisibility(View.VISIBLE);
            binding.frontendBtn.setVisibility(View.VISIBLE);
            binding.backendBtn.setVisibility(View.VISIBLE);
        });
        binding.dmBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        binding.frontendBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, GroupActivity.class);
            intent.putExtra("GroupId","frontEnd");
            startActivity(intent);
        });

        binding.backendBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, GroupActivity.class);
            intent.putExtra("GroupId","backEnd");
            startActivity(intent);
        });
    }
}