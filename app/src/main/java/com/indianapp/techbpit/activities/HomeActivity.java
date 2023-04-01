package com.indianapp.techbpit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.indianapp.techbpit.BottomSheetFragment.BottomSheetPostType;
import com.indianapp.techbpit.listeners.EditProfileClickListener;
import com.indianapp.techbpit.R;
import com.indianapp.techbpit.databinding.ActivityHomeBinding;
import com.indianapp.techbpit.fragments.CommunityFragment;
import com.indianapp.techbpit.fragments.ExploreFragment;
import com.indianapp.techbpit.fragments.HomeFragment;
import com.indianapp.techbpit.fragments.ProfileFragment;

import io.ak1.OnBubbleClickListener;

public class HomeActivity extends AppCompatActivity {
    private static Fragment fragment = null;
    private ActivityHomeBinding binding;
    private EditProfileClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.ivCreatePost.setVisibility(View.VISIBLE);
        binding.ivChat.setVisibility(View.VISIBLE);
        binding.ivEditProfile.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(binding.fragmentContainer.getId(), new HomeFragment()).commit();
        binding.bubbleTabBar.addBubbleListener(new OnBubbleClickListener() {
            @Override
            public void onBubbleClick(int i) {
                switch (i) {
                    case R.id.home:
                        binding.tvFragName.setText("HOME FEED");
                        fragment = new HomeFragment();
                        binding.ivCreatePost.setVisibility(View.VISIBLE);
                        binding.ivChat.setVisibility(View.VISIBLE);
                        binding.ivEditProfile.setVisibility(View.GONE);
                        break;
                    case R.id.chats:
                        binding.tvFragName.setText("COMMUNITIES");
                        fragment = new CommunityFragment();
                        binding.ivCreatePost.setVisibility(View.VISIBLE);
                        binding.ivChat.setVisibility(View.VISIBLE);
                        binding.ivEditProfile.setVisibility(View.GONE);
                        break;
                    case R.id.explore:
                        binding.tvFragName.setText("EXPLORE");
                        fragment = new ExploreFragment();
                        binding.ivCreatePost.setVisibility(View.VISIBLE);
                        binding.ivChat.setVisibility(View.VISIBLE);
                        binding.ivEditProfile.setVisibility(View.GONE);
                        break;
                    case R.id.profile:
                        binding.tvFragName.setText("PROFILE");
                        binding.ivCreatePost.setVisibility(View.GONE);
                        binding.ivChat.setVisibility(View.GONE);
                        binding.ivEditProfile.setVisibility(View.VISIBLE);
                        fragment = new ProfileFragment();
                        break;
                    default:
                        break;
                }
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(binding.fragmentContainer.getId(), fragment).commit();
                }
            }
        });
        binding.ivCreatePost.setOnClickListener(v -> {
            BottomSheetPostType bottomSheetPostType = new BottomSheetPostType(this);
            bottomSheetPostType.show(getSupportFragmentManager(), "BottomSheetPostType");
        });

        binding.ivChat.setOnClickListener(v -> {
            Intent intent = new Intent(this, AllChatsActivity.class);
            startActivity(intent);
        });

        binding.ivEditProfile.setOnClickListener(v -> {
           listener.onEditProfileClicked();
        });
    }

    public void setProfileEditListener(EditProfileClickListener listener){
        this.listener = listener;
    }
}