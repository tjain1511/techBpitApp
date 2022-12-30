package com.indianapp.techbpit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.indianapp.techbpit.ApiController.BaseData;
import com.indianapp.techbpit.ApiController.RESTController;
import com.indianapp.techbpit.SharedPrefHelper;
import com.indianapp.techbpit.adapters.AllGroupsAdapter;
import com.indianapp.techbpit.databinding.FragmentExploreBinding;
import com.indianapp.techbpit.model.AllGroupResponse;
import com.indianapp.techbpit.model.JoinGroupRequest;

import java.util.ArrayList;

import retrofit2.Response;

public class ExploreFragment extends Fragment implements RESTController.OnResponseStatusListener, AllGroupsAdapter.JoinListener {
    private FragmentExploreBinding binding;
    private ArrayList<AllGroupResponse> groupsList = new ArrayList<>();
    private AllGroupsAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void initRecyclerView() {
        adapter = new AllGroupsAdapter(groupsList);
        binding.rvGroups.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.rvGroups.setLayoutManager(linearLayoutManager);
        adapter.setListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            RESTController.getInstance(getActivity()).clearPendingApis();
        } catch (Exception e) {
            e.printStackTrace();
        }

        initRecyclerView();
        try {
            RESTController.getInstance(getActivity()).execute(RESTController.RESTCommands.REQ_GET_ALL_GROUPS, null, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    groupsList.addAll((ArrayList<AllGroupResponse>) response.body());
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onResponseFailed(RESTController.RESTCommands commands, BaseData<?> data, Throwable t) {

    }

    @Override
    public void onJoinClicked(String groupId, int position) {
        JoinGroupRequest joinGroupRequest = new JoinGroupRequest();
        joinGroupRequest.groupId = groupId;
        joinGroupRequest.userId = SharedPrefHelper.getUserModel(getActivity())._id;
        joinGroupRequest.position = position;
        try {
            RESTController.getInstance(getActivity()).execute(RESTController.RESTCommands.REQ_POST_JOIN_GROUP, new BaseData<>(joinGroupRequest), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}