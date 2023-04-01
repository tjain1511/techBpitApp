package com.indianapp.techbpit.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.indianapp.techbpit.ApiController.BaseData;
import com.indianapp.techbpit.ApiController.RESTController;
import com.indianapp.techbpit.R;
import com.indianapp.techbpit.utils.SharedPrefHelper;
import com.indianapp.techbpit.adapters.AllUserAdapter;
import com.indianapp.techbpit.databinding.ActivityGroupDetailBinding;
import com.indianapp.techbpit.model.GroupResponse;
import com.indianapp.techbpit.model.JoinGroupRequest;
import com.indianapp.techbpit.model.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Response;

public class GroupDetailActivity extends AppCompatActivity implements RESTController.OnResponseStatusListener {
    private ActivityGroupDetailBinding binding;
    private String groupId;
    private boolean isJoined;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.llGroupContent.setVisibility(View.GONE);
        binding.pbProfile.setVisibility(View.VISIBLE);
        groupId = getIntent().getStringExtra("group_id");
        binding.ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            RESTController.getInstance(this).execute(RESTController.RESTCommands.REQ_GET_GROUP_DATA, new BaseData<>(groupId), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRecyclerView(RecyclerView recyclerView, ArrayList<UserModel> allUsers) {
        AllUserAdapter adapter = new AllUserAdapter(this, allUsers, SharedPrefHelper.getUserModel(this).email);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onResponseReceived(RESTController.RESTCommands commands, BaseData<?> data, Response<?> response) {
        switch (commands) {
            case REQ_GET_GROUP_DATA:
                if (response.isSuccessful()) {
                    GroupResponse groupResponse = (GroupResponse) response.body();
                    isJoined = groupResponse.isJoined;
                    if (isJoined) {
                        binding.btnFollowUnfollow.setText("Following");
                    } else {
                        binding.btnFollowUnfollow.setText("Follow");
                    }
                    binding.btnFollowUnfollow.setOnClickListener(v -> {
                        binding.btnFollowUnfollow.setText("Updating..");
                        if (isJoined) {
                            try {
                                RESTController.getInstance(this).execute(RESTController.RESTCommands.REQ_PATCH_LEAVE_GROUP, new BaseData<>(groupId), this);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            JoinGroupRequest joinGroupRequest = new JoinGroupRequest();
                            joinGroupRequest.groupId = groupId;
                            joinGroupRequest.userId = SharedPrefHelper.getUserModel(this)._id;
                            try {
                                RESTController.getInstance(this).execute(RESTController.RESTCommands.REQ_POST_JOIN_GROUP, new BaseData<>(joinGroupRequest), this);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    binding.llGroupContent.setVisibility(View.VISIBLE);
                    binding.pbProfile.setVisibility(View.GONE);
                    Picasso.get().load(groupResponse.image).into(binding.ivGrpLogo);
                    binding.tvGrpName.setText(groupResponse.groupName);
                    if (TextUtils.isEmpty(groupResponse.description)) {
                        binding.tvGrpDescriptionEmpty.setVisibility(View.VISIBLE);
                        binding.tvGrpDescription.setVisibility(View.GONE);
                    } else {
                        binding.tvGrpDescriptionEmpty.setVisibility(View.GONE);
                        binding.tvGrpDescription.setVisibility(View.VISIBLE);
                        binding.tvGrpDescription.setText(groupResponse.description);
                    }

                    if (groupResponse.usersJoined.isEmpty()) {
                        binding.joinedUsers.getRoot().setVisibility(View.GONE);
                    } else {
                        binding.joinedUsers.tvUserMentor.setText("Participants (" + groupResponse.usersJoined.size() + ")");
                        binding.joinedUsers.getRoot().setOnClickListener(v -> {
                            if (binding.joinedUsers.llUsers.getVisibility() == View.VISIBLE) {
                                binding.joinedUsers.llUsers.setVisibility(View.GONE);
                                binding.joinedUsers.ivDropDown.setImageDrawable(getDrawable(R.drawable.ic_arrow_down));
                            } else {
                                binding.joinedUsers.llUsers.setVisibility(View.VISIBLE);
                                binding.joinedUsers.ivDropDown.setImageDrawable(getDrawable(R.drawable.ic_arrow_up));
                            }
                        });
                    }

                    if (groupResponse.moderators.isEmpty()) {
                        binding.mentors.getRoot().setVisibility(View.GONE);
                    } else {
                        binding.mentors.tvUserMentor.setText("Mentors (" + groupResponse.usersJoined.size() + ")");
                        binding.mentors.getRoot().setOnClickListener(v -> {
                            if (binding.mentors.llUsers.getVisibility() == View.VISIBLE) {
                                binding.mentors.llUsers.setVisibility(View.GONE);
                                binding.mentors.ivDropDown.setImageDrawable(getDrawable(R.drawable.ic_arrow_down));
                            } else {
                                binding.mentors.llUsers.setVisibility(View.VISIBLE);
                                binding.mentors.ivDropDown.setImageDrawable(getDrawable(R.drawable.ic_arrow_up));
                            }
                        });
                    }
                    initRecyclerView(binding.joinedUsers.rvUsers, (ArrayList<UserModel>) groupResponse.usersJoined);
                    initRecyclerView(binding.mentors.rvUsers, (ArrayList<UserModel>) groupResponse.moderators);

                }
                break;
            case REQ_PATCH_LEAVE_GROUP:
                if (response.isSuccessful()) {
                    isJoined = false;
                    if (isJoined) {
                        binding.btnFollowUnfollow.setText("Following");
                    } else {
                        binding.btnFollowUnfollow.setText("Follow");
                    }
                }
                break;
            case REQ_POST_JOIN_GROUP:
                if (response.isSuccessful()) {
                    isJoined = true;
                    if (isJoined) {
                        binding.btnFollowUnfollow.setText("Following");
                    } else {
                        binding.btnFollowUnfollow.setText("Follow");
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseFailed(RESTController.RESTCommands commands, BaseData<?> data, Throwable t) {

    }
}