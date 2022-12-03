package com.indianapp.techbpit.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.indianapp.techbpit.BaseData;
import com.indianapp.techbpit.RESTController;
import com.indianapp.techbpit.SharedPrefHelper;
import com.indianapp.techbpit.adapters.AllUserAdapter;
import com.indianapp.techbpit.adapters.JoinedGroupsAdapter;
import com.indianapp.techbpit.adapters.SectionPagerAdapter;
import com.indianapp.techbpit.databinding.ActivityAllJoinedGroupsBinding;
import com.indianapp.techbpit.fragments.AllJoinedGroupsFragment;
import com.indianapp.techbpit.fragments.AllUsersFragment;
import com.indianapp.techbpit.model.GroupResponse;
import com.indianapp.techbpit.model.JoinGroupRequest;
import com.indianapp.techbpit.model.UserModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class AllJoinedGroupsActivity extends AppCompatActivity{
    private ActivityAllJoinedGroupsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllJoinedGroupsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViewPager();
    }

    private void initViewPager() {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new AllUsersFragment(), "DMs");
        adapter.addFragment(new AllJoinedGroupsFragment(), "Groups");

        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}