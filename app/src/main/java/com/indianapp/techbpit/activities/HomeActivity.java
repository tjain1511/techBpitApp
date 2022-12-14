package com.indianapp.techbpit.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.indianapp.techbpit.R;
import com.indianapp.techbpit.databinding.ActivityHomeBinding;
import com.indianapp.techbpit.fragments.ChatsFragment;
import com.indianapp.techbpit.fragments.ExploreFragment;
import com.indianapp.techbpit.fragments.HomeFragment;
import com.indianapp.techbpit.fragments.ProfileFragment;

import io.ak1.OnBubbleClickListener;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private static Fragment fragment = null;

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
                        fragment = new HomeFragment();
                        break;
                    case R.id.chats:
                        fragment = new ChatsFragment();
                        break;
                    case R.id.explore:
                        fragment = new ExploreFragment();
                        break;
                    case R.id.profile:
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
    }
}