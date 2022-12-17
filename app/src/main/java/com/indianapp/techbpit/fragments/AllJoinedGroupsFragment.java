package com.indianapp.techbpit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.indianapp.techbpit.ApiController.BaseData;
import com.indianapp.techbpit.ApiController.RESTController;
import com.indianapp.techbpit.SharedPrefHelper;
import com.indianapp.techbpit.adapters.JoinedGroupsAdapter;
import com.indianapp.techbpit.databinding.FragmentAllJoinedGroupsBinding;
import com.indianapp.techbpit.model.GroupResponse;
import com.indianapp.techbpit.model.UserModel;

import java.util.ArrayList;

import retrofit2.Response;

public class AllJoinedGroupsFragment extends Fragment implements RESTController.OnResponseStatusListener {
    private FragmentAllJoinedGroupsBinding binding;
    private ArrayList<GroupResponse> joinedGroups = new ArrayList<>();
    private JoinedGroupsAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getActivity() fragment
        binding = FragmentAllJoinedGroupsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserModel userModel = new UserModel();
        userModel.email = SharedPrefHelper.getUserModel(getActivity()).email;
        try {
            RESTController.getInstance(getActivity()).execute(RESTController.RESTCommands.REQ_GET_JOINED_GROUPS, new BaseData<>(userModel), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initRecyclerView();
    }

    private void initRecyclerView() {
        binding.pbGroupsLoading.setVisibility(View.GONE);
        adapter = new JoinedGroupsAdapter(getActivity(), joinedGroups);
        binding.rvJoinedGrps.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvJoinedGrps.setAdapter(adapter);

    }

    @Override
    public void onResponseReceived(RESTController.RESTCommands commands, BaseData<?> request, Response<?> response) {
        UserModel userModel = (UserModel) response.body();
        joinedGroups.addAll(userModel.groupsJoined);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResponseFailed(RESTController.RESTCommands commands, BaseData<?> request, Throwable t) {

    }
}