package com.indianapp.techbpit.fragments;

import android.content.Intent;
import android.os.Bundle;
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
import com.indianapp.techbpit.activities.AddProjectActivity;
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

public class ProfileFragment extends Fragment implements RESTController.OnResponseStatusListener {
    private FragmentProfileBinding binding;
    private ArrayList<String> skillsList = new ArrayList<>();
    private ArrayList<SocialPlatform> socialList = new ArrayList<>();
    private ArrayList<ProjectResponse> projectsList = new ArrayList<>();

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
        intiSkillsRecyclerView();
        initSocialLinksRecyclerView();
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
            RESTController.getInstance(getActivity()).execute(RESTController.RESTCommands.REQ_GET_USER_PROFILE, null, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void intiSkillsRecyclerView() {
//        prepareSkillList();
        SkillAdapter skillAdapter = new SkillAdapter(skillsList);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        binding.rvSkill.setLayoutManager(layoutManager);
        binding.rvSkill.setAdapter(skillAdapter);
    }

    private void initProjectsRecyclerView() {
        ProjectsAdapter projectsAdapter = new ProjectsAdapter(projectsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvProjects.setLayoutManager(layoutManager);
        binding.rvProjects.setAdapter(projectsAdapter);
    }

    private void initSocialLinksRecyclerView() {
        SocialLinksAdapter socialLinksAdapter = new SocialLinksAdapter(getActivity(), socialList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.rvSocialLink.setLayoutManager(layoutManager);
        binding.rvSocialLink.setAdapter(socialLinksAdapter);
    }

//    private void prepareSocialList() {
//        SocialLink socialLink = new SocialLink();
//        socialLink.platformLink = "https://www.codechef.com/user";
//        socialLink.platformImage = "https://img.icons8.com/ios7/2x/000000/codechef.png";
//
//        SocialLink socialLink1 = new SocialLink();
//        socialLink1.platformLink = "https://in.linkedin.com/";
//        socialLink1.platformImage = "https://cdn-icons-png.flaticon.com/512/174/174857.png";
//
//        SocialLink socialLink2 = new SocialLink();
//        socialLink2.platformLink = "https://leetcode.com/";
//        socialLink2.platformImage = "https://cdn.iconscout.com/icon/free/png-256/leetcode-3628885-3030025.png";
//
//
//        socialList.add(socialLink);
//        socialList.add(socialLink1);
//        socialList.add(socialLink2);
//    }

//    private void prepareSkillList() {
//        skillsList.add("Android Development");
//        skillsList.add("Android");
//        skillsList.add("Node");
//        skillsList.add("Django");
//        skillsList.add("Redis");
//        skillsList.add("SQL");
//        skillsList.add("MongoDB");
//        skillsList.add("XML");
//        skillsList.add("Web Development");
//        skillsList.add("Java");
//        skillsList.add("JavaScript");
//        skillsList.add("Dependency Injection");
//    }

    @Override
    public void onResponseReceived(RESTController.RESTCommands commands, BaseData<?> data, Response<?> response) {
        if (response.isSuccessful()) {
            UserModel userModel = (UserModel) response.body();
            binding.llProfileContent.setVisibility(View.VISIBLE);
            binding.pbProfile.setVisibility(View.GONE);
            skillsList = (ArrayList<String>) userModel.skills;
            socialList = (ArrayList<SocialPlatform>) userModel.socialLinks;
            projectsList = (ArrayList<ProjectResponse>) userModel.projects;
            binding.tvUserName.setText(userModel.username);
            binding.tvLocation.setText(userModel.city + ", " + userModel.state);
            binding.tvAbt.setText(userModel.about);
            Picasso.get().load(userModel.imageUrl).into(binding.ivProfilePicture);
            initSocialLinksRecyclerView();
            intiSkillsRecyclerView();
            initProjectsRecyclerView();
        }
    }

    @Override
    public void onResponseFailed(RESTController.RESTCommands commands, BaseData<?> data, Throwable t) {

    }
}