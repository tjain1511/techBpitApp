package com.indianapp.techbpit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.indianapp.techbpit.ApiController.BaseData;
import com.indianapp.techbpit.ApiController.RESTController;
import com.indianapp.techbpit.SharedPrefHelper;
import com.indianapp.techbpit.adapters.AllGroupsAdapter;
import com.indianapp.techbpit.databinding.ActivityJoinGroupBinding;
import com.indianapp.techbpit.model.GroupResponse;
import com.indianapp.techbpit.model.JoinGroupRequest;

import java.util.ArrayList;

import retrofit2.Response;

public class JoinGroupActivity extends AppCompatActivity implements RESTController.OnResponseStatusListener, AllGroupsAdapter.JoinListener {
    private ActivityJoinGroupBinding binding;
    private ArrayList<GroupResponse> groupsList = new ArrayList<>();
    private AllGroupsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJoinGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initRecyclerView();
        try {
            RESTController.getInstance(this).execute(RESTController.RESTCommands.REQ_GET_ALL_GROUPS, null, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.btnGoToGroups.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void initRecyclerView() {
        adapter = new AllGroupsAdapter(groupsList);
        binding.rvGroups.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvGroups.setLayoutManager(linearLayoutManager);
        adapter.setListener(this);
    }

    @Override
    public void onResponseReceived(RESTController.RESTCommands commands, BaseData<?> request, Response<?> response) {
        switch (commands) {
            case REQ_POST_JOIN_GROUP:
                if (response.isSuccessful()) {
                    int position = ((JoinGroupRequest) request.getBaseData()).position;
                    groupsList.get(position).isJoined = true;
                    adapter.notifyDataSetChanged();
                }
                break;
            case REQ_GET_ALL_GROUPS:
                if (response.isSuccessful()) {
                    groupsList.addAll((ArrayList<GroupResponse>) response.body());
                    adapter.notifyDataSetChanged();
                    binding.btnGoToGroups.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    @Override
    public void onResponseFailed(RESTController.RESTCommands commands, BaseData<?> request, Throwable t) {

    }

    @Override
    public void onJoinClicked(String groupId, int position) {
        JoinGroupRequest joinGroupRequest = new JoinGroupRequest();
        joinGroupRequest.groupId = groupId;
        joinGroupRequest.userId = SharedPrefHelper.getUserModel(this)._id;
        joinGroupRequest.position = position;
        try {
            RESTController.getInstance(this).execute(RESTController.RESTCommands.REQ_POST_JOIN_GROUP, new BaseData<>(joinGroupRequest), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        groupsList.get(position).isJoined = true;
//        adapter.notifyDataSetChanged();
    }
}