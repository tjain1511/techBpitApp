package com.indianapp.techbpit.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.indianapp.techbpit.BottomSheetFragment.BottomSheetStartNewChat;
import com.indianapp.techbpit.adapters.SectionPagerAdapter;
import com.indianapp.techbpit.databinding.ActivityAllChatsBinding;
import com.indianapp.techbpit.fragments.AllJoinedGroupsFragment;
import com.indianapp.techbpit.fragments.AllUsersFragment;

public class AllChatsActivity extends AppCompatActivity {
    private ActivityAllChatsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllChatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViewPager();
        binding.ivStartNewChat.setOnClickListener(v -> {
            BottomSheetStartNewChat bottomSheetStartNewChat = new BottomSheetStartNewChat();
            bottomSheetStartNewChat.show(getSupportFragmentManager(), "BottomSheetStartNewChat");
        });

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
