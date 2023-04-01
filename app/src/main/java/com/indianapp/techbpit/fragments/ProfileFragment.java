package com.indianapp.techbpit.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.indianapp.techbpit.ApiController.BaseData;
import com.indianapp.techbpit.ApiController.RESTController;
import com.indianapp.techbpit.listeners.EditProfileClickListener;
import com.indianapp.techbpit.utils.SharedPrefHelper;
import com.indianapp.techbpit.activities.AddProjectActivity;
import com.indianapp.techbpit.activities.HomeActivity;
import com.indianapp.techbpit.activities.SetupProfileActivity;
import com.indianapp.techbpit.adapters.ProjectsAdapter;
import com.indianapp.techbpit.adapters.SkillAdapter;
import com.indianapp.techbpit.adapters.SocialLinksAdapter;
import com.indianapp.techbpit.databinding.FragmentProfileBinding;
import com.indianapp.techbpit.model.ProjectResponse;
import com.indianapp.techbpit.model.SocialPlatform;
import com.indianapp.techbpit.model.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Response;

public class ProfileFragment extends Fragment implements RESTController.OnResponseStatusListener, EditProfileClickListener {
    private FragmentProfileBinding binding;
    private ArrayList<String> skillsList = new ArrayList<>();
    private ArrayList<SocialPlatform> socialList = new ArrayList<>();
    private ArrayList<ProjectResponse> projectsList = new ArrayList<>();
    private UserModel userModel;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        ((HomeActivity) getActivity()).setProfileEditListener(this);
        binding.ivAddProject.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddProjectActivity.class);
            startActivity(intent);
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.llProfileContent.setVisibility(View.GONE);
        binding.pbProfile.setVisibility(View.VISIBLE);

        try {
            RESTController.getInstance(getActivity()).clearPendingApis();
            RESTController.getInstance(getActivity()).execute(RESTController.RESTCommands.REQ_GET_USER_PROFILE, new BaseData<>(SharedPrefHelper.getUserModel(getActivity())._id), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void intiSkillsRecyclerView() {
        binding.rvSkill.setVisibility(View.VISIBLE);
        binding.tvSkillsEmpty.setVisibility(View.GONE);
        SkillAdapter skillAdapter = new SkillAdapter(skillsList);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        binding.rvSkill.setLayoutManager(layoutManager);
        binding.rvSkill.setAdapter(skillAdapter);
    }

    private void initProjectsRecyclerView() {
        binding.rvProjects.setVisibility(View.VISIBLE);
        binding.llAddProjects.setVisibility(View.GONE);
        ProjectsAdapter projectsAdapter = new ProjectsAdapter(getActivity(), projectsList, userModel._id);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvProjects.setLayoutManager(layoutManager);
        binding.rvProjects.setAdapter(projectsAdapter);
    }

    private void initSocialLinksRecyclerView() {
        binding.rvSocialLink.setVisibility(View.VISIBLE);
        binding.tvLinksEmpty.setVisibility(View.GONE);
        SocialLinksAdapter socialLinksAdapter = new SocialLinksAdapter(getActivity(), socialList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.rvSocialLink.setLayoutManager(layoutManager);
        binding.rvSocialLink.setAdapter(socialLinksAdapter);
    }


    @Override
    public void onResponseReceived(RESTController.RESTCommands commands, BaseData<?> data, Response<?> response) {
        if (response.isSuccessful()) {
            userModel = (UserModel) response.body();
            binding.llProfileContent.setVisibility(View.VISIBLE);
            binding.pbProfile.setVisibility(View.GONE);
            skillsList = (ArrayList<String>) userModel.skills;
            socialList = (ArrayList<SocialPlatform>) userModel.socialLinks;
            projectsList = (ArrayList<ProjectResponse>) userModel.projects;
            binding.tvUserName.setText(userModel.username);
            Picasso.get().load(userModel.imageUrl).into(binding.ivProfilePicture);

            if (!TextUtils.isEmpty(userModel.city) && !TextUtils.isEmpty(userModel.state)) {
                binding.tvLocation.setVisibility(View.VISIBLE);
                binding.tvLocation.setText(userModel.city + ", " + userModel.state);
            }
            if (!TextUtils.isEmpty(userModel.about)) {
                binding.tvAbt.setVisibility(View.VISIBLE);
                binding.tvAbt.setText(userModel.about);
                binding.tvAbtEmpty.setVisibility(View.GONE);
            } else {
                binding.tvAbt.setVisibility(View.GONE);
                binding.tvAbtEmpty.setVisibility(View.VISIBLE);
            }
            if (socialList != null && !socialList.isEmpty()) {
                initSocialLinksRecyclerView();
            } else {
                binding.rvSocialLink.setVisibility(View.GONE);
                binding.tvLinksEmpty.setVisibility(View.VISIBLE);
            }
            if (skillsList != null && !skillsList.isEmpty()) {
                intiSkillsRecyclerView();
            } else {
                binding.rvSkill.setVisibility(View.GONE);
                binding.tvSkillsEmpty.setVisibility(View.VISIBLE);
            }

            if (projectsList != null && !projectsList.isEmpty()) {
                initProjectsRecyclerView();
            } else {
                binding.rvProjects.setVisibility(View.GONE);
                binding.llAddProjects.setVisibility(View.VISIBLE);
                binding.llAddProjects.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), AddProjectActivity.class);
                    startActivity(intent);
                });
            }

        }
    }

    @Override
    public void onResponseFailed(RESTController.RESTCommands commands, BaseData<?> data, Throwable t) {

    }

    @Override
    public void onEditProfileClicked() {
        Intent intent = new Intent(getActivity(), SetupProfileActivity.class);
        intent.putExtra("profile_data", userModel);
        startActivity(intent);
    }
}