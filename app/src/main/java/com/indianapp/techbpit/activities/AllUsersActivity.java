package com.indianapp.techbpit.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.indianapp.techbpit.SharedPrefHelper;
import com.indianapp.techbpit.adapters.AllUserAdapter;
import com.indianapp.techbpit.BaseData;
import com.indianapp.techbpit.RESTController;
import com.indianapp.techbpit.SocketClient;
import com.indianapp.techbpit.model.UserModel;
import com.indianapp.techbpit.databinding.ActivityAllUsersBinding;

import java.util.ArrayList;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Response;

public class AllUsersActivity extends AppCompatActivity implements RESTController.OnResponseStatusListener {
    ArrayList<UserModel> allUsers;
    private ActivityAllUsersBinding binding;
    private SharedPreferences sharedPreferences;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllUsersBinding.inflate(getLayoutInflater());
        sharedPreferences = this.getSharedPreferences("com.indianapp.techbpit", MODE_PRIVATE);
        getWindow().setStatusBarColor(Color.parseColor("#4169ef"));
        setContentView(binding.getRoot());
        SocketClient.setUserId(SharedPrefHelper.getUserModel(this)._id);
        socket = SocketClient.getSocket(this);
        try {
            RESTController.getInstance(this).execute(RESTController.RESTCommands.REQ_GET_ALL_USERS, new BaseData<>(null), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.btnGrp.setOnClickListener(v -> {
            Intent intent = new Intent(this,AllJoinedGroupsActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void initRecyclerView() {
        AllUserAdapter adapter = new AllUserAdapter(this, allUsers, sharedPreferences.getString("my_email", ""));
        binding.rvAllUsers.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAllUsers.setAdapter(adapter);

    }

    @Override
    public void onResponseReceived(RESTController.RESTCommands commands, BaseData<?> request, Response<?> response) {
        switch (commands) {
            case REQ_GET_ALL_USERS:
                if (response.isSuccessful()) {
                    allUsers = (ArrayList<UserModel>) response.body();
                    initRecyclerView();
                }
        }
    }

    @Override
    public void onResponseFailed(RESTController.RESTCommands commands, BaseData<?> request, Throwable t) {
        switch (commands) {
            case REQ_GET_ALL_USERS:
                Toast.makeText(this, "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
        }
    }
}