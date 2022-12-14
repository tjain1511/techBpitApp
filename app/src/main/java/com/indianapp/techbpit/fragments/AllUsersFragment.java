package com.indianapp.techbpit.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.indianapp.techbpit.BaseData;
import com.indianapp.techbpit.RESTController;
import com.indianapp.techbpit.SharedPrefHelper;
import com.indianapp.techbpit.SocketClient;
import com.indianapp.techbpit.adapters.AllUserAdapter;
import com.indianapp.techbpit.databinding.FragmentAllUsersBinding;
import com.indianapp.techbpit.model.UserModel;

import java.util.ArrayList;

import io.socket.client.Socket;
import retrofit2.Response;

public class AllUsersFragment extends Fragment implements RESTController.OnResponseStatusListener {

    ArrayList<UserModel> allUsers;
    private FragmentAllUsersBinding binding;
    private SharedPreferences sharedPreferences;
    private Socket socket;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAllUsersBinding.inflate(inflater, container, false);
        sharedPreferences = getActivity().getSharedPreferences("com.indianapp.techbpit", MODE_PRIVATE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setStatusBarColor(Color.parseColor("#4169ef"));
        SocketClient.setUserId(SharedPrefHelper.getUserModel(getActivity()).email);
        socket = SocketClient.getSocket(getActivity());
        try {
            RESTController.getInstance(getActivity()).execute(RESTController.RESTCommands.REQ_GET_ALL_USERS, new BaseData<>(null), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRecyclerView() {
        binding.pbUsersLoading.setVisibility(View.GONE);
        AllUserAdapter adapter = new AllUserAdapter(getActivity(), allUsers, sharedPreferences.getString("my_email", ""));
        binding.rvAllUsers.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                Toast.makeText(getActivity(), "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
        }
    }
}