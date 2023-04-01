package com.indianapp.techbpit.BottomSheetFragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.indianapp.techbpit.adapters.AllUserAdapter;
import com.indianapp.techbpit.adapters.TeamMembersAdapter;
import com.indianapp.techbpit.databinding.BottomSheetAddTeamMembersBinding;
import com.indianapp.techbpit.listeners.MemberAddedListener;
import com.indianapp.techbpit.listeners.MemberRemovedClickListener;
import com.indianapp.techbpit.listeners.UserClickedListener;
import com.indianapp.techbpit.model.UserModel;
import com.indianapp.techbpit.utils.SharedPrefHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import retrofit2.Response;

public class BottomSheetAddTeamMembers extends BottomSheetDialogFragment implements RESTController.OnResponseStatusListener, UserClickedListener, MemberRemovedClickListener {
    private ArrayList<UserModel> allUsers;
    private SortedSet<UserModel> teamMembers = new TreeSet<UserModel>(new Comparator<UserModel>() {
        @Override
        public int compare(UserModel userModel1, UserModel userModel2) {
            return userModel1.username.toLowerCase().compareTo(userModel2.username.toLowerCase());
        }
    });
    private ArrayList<UserModel> addedMembers = new ArrayList<>();
    private TeamMembersAdapter teamMembersAdapter;
    private AllUserAdapter allUserAdapter;
    private BottomSheetAddTeamMembersBinding binding;
    private SharedPreferences sharedPreferences;
    private MemberAddedListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = BottomSheetAddTeamMembersBinding.inflate(inflater, container, false);
        sharedPreferences = getActivity().getSharedPreferences("com.indianapp.techbpit", MODE_PRIVATE);
        return binding.getRoot();
    }

    public void setListener(MemberAddedListener listener) {
        this.listener = listener;
    }

    public void setTeamMembers(SortedSet<UserModel> teamMembers) {
        this.teamMembers = teamMembers;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        LinearLayout layout = binding.getRoot();
        layout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
        initAddedUserRecyclerView();
        try {
            RESTController.getInstance(getActivity()).execute(RESTController.RESTCommands.REQ_GET_ALL_USERS, new BaseData<>(SharedPrefHelper.getUserModel(getActivity())), this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.tvDone.setOnClickListener(v -> {
            listener.onDoneClicked(teamMembers);
            dismiss();
        });

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
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
        allUserAdapter.setAllUsers(filteredList);
    }

    private void initAllUserRecyclerView() {
        allUserAdapter = new AllUserAdapter(getActivity(), allUsers, sharedPreferences.getString("my_email", ""));
        allUserAdapter.setListener(this);
        binding.rvAllUsers.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvAllUsers.setAdapter(allUserAdapter);

    }

    private void initAddedUserRecyclerView() {
        getAddedMembers();
        teamMembersAdapter = new TeamMembersAdapter(getActivity(), addedMembers);
        teamMembersAdapter.setListener(this);
        binding.rvAddedMembers.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvAddedMembers.setAdapter(teamMembersAdapter);
    }

    private void getAddedMembers() {
        addedMembers.clear();
        for (UserModel userModel : teamMembers) {
            addedMembers.add(userModel);
        }

    }

    @Override
    public void onResponseReceived(RESTController.RESTCommands commands, BaseData<?> request, Response<?> response) {
        switch (commands) {
            case REQ_GET_ALL_USERS:
                if (response.isSuccessful()) {
                    allUsers = (ArrayList<UserModel>) response.body();
                    initAllUserRecyclerView();
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

    @Override
    public void onUserClicked(int position, UserModel userModel) {
        teamMembers.add(userModel);
        getAddedMembers();
        teamMembersAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRemovedClick(UserModel userModel) {
        teamMembers.remove(userModel);
        getAddedMembers();
        teamMembersAdapter.notifyDataSetChanged();

    }
}
