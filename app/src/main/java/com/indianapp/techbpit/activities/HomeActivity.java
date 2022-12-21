package com.indianapp.techbpit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.indianapp.techbpit.BottomSheetFragment.BottomSheetPostType;
import com.indianapp.techbpit.R;
import com.indianapp.techbpit.databinding.ActivityHomeBinding;
import com.indianapp.techbpit.fragments.ChatsFragment;
import com.indianapp.techbpit.fragments.ExploreFragment;
import com.indianapp.techbpit.fragments.HomeFragment;
import com.indianapp.techbpit.fragments.ProfileFragment;

import io.ak1.OnBubbleClickListener;

public class HomeActivity extends AppCompatActivity {
    private static Fragment fragment = null;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportFragmentManager().beginTransaction().replace(binding.fragmentContainer.getId(), new HomeFragment()).commit();
        binding.bubbleTabBar.addBubbleListener(new OnBubbleClickListener() {
            @Override
            public void onBubbleClick(int i) {
                switch (i) {
                    case R.id.home:
                        binding.tvFragName.setText("HOME FEED");
                        fragment = new HomeFragment();
                        break;
                    case R.id.chats:
                        binding.tvFragName.setText("CHATS");
                        fragment = new ChatsFragment();
                        break;
                    case R.id.explore:
                        binding.tvFragName.setText("EXPLORE");
                        fragment = new ExploreFragment();
                        break;
                    case R.id.profile:
                        binding.tvFragName.setText("PROFILE");
                        binding.ivCreatePost.setVisibility(View.GONE);
                        binding.ivChat.setVisibility(View.GONE);
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
            Intent intent = new Intent(this, SetupProfileActivity.class);
            startActivity(intent);
        });
    }
}