package com.indianapp.techbpit.BottomSheetFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.indianapp.techbpit.listeners.BottomSheetImageSourceListener;
import com.indianapp.techbpit.activities.CreatePostActivity;
import com.indianapp.techbpit.databinding.BottomSheetPostTypeBinding;

public class BottomSheetPostType extends BottomSheetDialogFragment {
    private BottomSheetPostTypeBinding binding;
    private BottomSheetImageSourceListener listener;
    private Context context;

    public BottomSheetPostType(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetPostTypeBinding.inflate(inflater, container, false);
        setupClickListeners();
        return binding.getRoot();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (BottomSheetImageSourceListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupClickListeners() {
        binding.tvCommunityPost.setOnClickListener(v -> {
            Intent intent = new Intent(context, CreatePostActivity.class);
            intent.putExtra("post_type", PostType.COMMUNITY_POST);
            startActivity(intent);
            dismiss();
        });
        binding.tvEventPost.setOnClickListener(v -> {
            Intent intent = new Intent(context, CreatePostActivity.class);
            intent.putExtra("post_type", PostType.EVENT_POST);
            startActivity(intent);
            dismiss();
        });
        binding.tvResourcePost.setOnClickListener(v -> {
            Intent intent = new Intent(context, CreatePostActivity.class);
            intent.putExtra("post_type", PostType.RESOURCE_POST);
            startActivity(intent);
        });
    }

    public enum PostType {
        EVENT_POST("eventPost", 1),
        RESOURCE_POST("resourcePost", 2),
        COMMUNITY_POST("communityPost", 3);

        public final String label;
        public final int id;

        private PostType(String label, int id) {
            this.label = label;
            this.id = id;
        }
    }
}
