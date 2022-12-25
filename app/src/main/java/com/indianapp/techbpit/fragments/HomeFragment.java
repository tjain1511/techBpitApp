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
import com.indianapp.techbpit.adapters.PostAdapter;
import com.indianapp.techbpit.databinding.FragmentHomeBinding;
import com.indianapp.techbpit.model.SocialPostResponse;

import java.util.ArrayList;

import retrofit2.Response;


public class HomeFragment extends Fragment implements RESTController.OnResponseStatusListener {
    private FragmentHomeBinding binding;
    private PostAdapter adapter;
    private ArrayList<SocialPostResponse> postRequestItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            RESTController.getInstance(getActivity()).clearPendingApis();
            RESTController.getInstance(getActivity()).execute(RESTController.RESTCommands.REQ_GET_ALL_POSTS, null, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRecyclerView() {
        adapter = new PostAdapter(getActivity(), postRequestItems);
        binding.rvAllPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvAllPosts.setAdapter(adapter);
    }


    @Override
    public void onResponseReceived(RESTController.RESTCommands commands, BaseData<?> data, Response<?> response) {
        if (response.isSuccessful()) {
            postRequestItems = (ArrayList<SocialPostResponse>) response.body();
            initRecyclerView();
            binding.pbPosts.setVisibility(View.GONE);
        }
    }


    @Override
    public void onResponseFailed(RESTController.RESTCommands commands, BaseData<?> data, Throwable t) {

    }
}