package com.indianapp.techbpit.BottomSheetFragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.indianapp.techbpit.ApiController.BaseData;
import com.indianapp.techbpit.ApiController.RESTController;
import com.indianapp.techbpit.SharedPrefHelper;
import com.indianapp.techbpit.SocketClient;
import com.indianapp.techbpit.adapters.AllUserAdapter;
import com.indianapp.techbpit.databinding.BottomSheetStartNewChatBinding;
import com.indianapp.techbpit.model.UserModel;

import java.util.ArrayList;

import io.socket.client.Socket;
import retrofit2.Response;

public class BottomSheetStartNewChat extends BottomSheetDialogFragment implements RESTController.OnResponseStatusListener {
    private ArrayList<UserModel> allUsers = new ArrayList<>();
    private AllUserAdapter adapter;
    private BottomSheetStartNewChatBinding binding;
    private SharedPreferences sharedPreferences;
    private Socket socket;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = BottomSheetStartNewChatBinding.inflate(inflater, container, false);
        sharedPreferences = getActivity().getSharedPreferences("com.indianapp.techbpit", MODE_PRIVATE);
        initRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        LinearLayout layout = binding.getRoot();
        layout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);

        SocketClient.setUserId(SharedPrefHelper.getUserModel(getActivity())._id);
        socket = SocketClient.getSocket(getActivity());
//        try {
//            RESTController.getInstance(getActivity()).execute(RESTController.RESTCommands.REQ_GET_ALL_USERS, new BaseData<>(SharedPrefHelper.getUserModel(getActivity())), this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        binding.ivCancel.setOnClickListener(v -> dismiss());

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() >= 3) {
                    try {
                        RESTController.getInstance(getActivity()).execute(RESTController.RESTCommands.REQ_GET_SEARCH_USERS, new BaseData<>(s.toString()), BottomSheetStartNewChat.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("khali_h", "kahli h");
                    allUsers = new ArrayList<>();
                    adapter.setAllUsers(allUsers);

                }
            }
        });
    }

    private void filter(String text) {
        ArrayList<UserModel> filteredList = new ArrayList<>();
        for (UserModel userModel : allUsers) {
            if (userModel.username.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(userModel);
            }
        }
        if (filteredList.size() == 0) {
            binding.tvNoResultsFound.setVisibility(View.VISIBLE);
        } else {
            binding.tvNoResultsFound.setVisibility(View.GONE);
        }
        adapter.setAllUsers(filteredList);
    }

    private void initRecyclerView() {
        adapter = new AllUserAdapter(getActivity(), allUsers, sharedPreferences.getString("my_email", ""));
        binding.rvAllUsers.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvAllUsers.setAdapter(adapter);
    }

    @Override
    public void onResponseReceived(RESTController.RESTCommands commands, BaseData<?> request, Response<?> response) {
        switch (commands) {
            case REQ_GET_ALL_USERS:
                if (response.isSuccessful()) {
                    allUsers.addAll((ArrayList<UserModel>) response.body());
                    initRecyclerView();
                }
            case REQ_GET_SEARCH_USERS:
                if (response.isSuccessful() && binding.edtSearch.getText().length() > 0) {
                    allUsers.clear();
                    allUsers.addAll((ArrayList<UserModel>) response.body());
                    adapter.setAllUsers(allUsers);
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
