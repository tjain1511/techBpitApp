package com.indianapp.techbpit.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.indianapp.techbpit.ApiController.BaseData;
import com.indianapp.techbpit.ApiController.RESTController;
import com.indianapp.techbpit.utils.SharedPrefHelper;
import com.indianapp.techbpit.utils.SocketClient;
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
        SocketClient.setUserId(SharedPrefHelper.getUserModel(getActivity())._id);
        socket = SocketClient.getSocket(getActivity());

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            RESTController.getInstance(getActivity()).execute(RESTController.RESTCommands.REQ_GET_RECENT_USERS, new BaseData<>(SharedPrefHelper.getUserModel(getActivity())), this);
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
            case REQ_GET_RECENT_USERS:
                if (response.isSuccessful()) {
                    allUsers = (ArrayList<UserModel>) response.body();
                    initRecyclerView();
                }
        }
    }

    @Override
    public void onResponseFailed(RESTController.RESTCommands commands, BaseData<?> request, Throwable t) {
        switch (commands) {
            case REQ_GET_RECENT_USERS:
                Toast.makeText(getActivity(), "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
        }
    }
}