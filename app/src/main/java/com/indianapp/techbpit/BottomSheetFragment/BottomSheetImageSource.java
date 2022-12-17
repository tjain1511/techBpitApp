package com.indianapp.techbpit.BottomSheetFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.indianapp.techbpit.BottomSheetImageSourceListener;
import com.indianapp.techbpit.databinding.BottomSheetImageSourceBinding;

public class BottomSheetImageSource extends BottomSheetDialogFragment {
    private BottomSheetImageSourceBinding binding;
    private BottomSheetImageSourceListener listener;

    public BottomSheetImageSource(BottomSheetImageSourceListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetImageSourceBinding.inflate(inflater, container, false);
        setupUI();
        setupClickListeners();
        return binding.getRoot();
    }

    private void setupUI() {
        binding.rlCamera.setVisibility(View.VISIBLE);
        binding.rlGallery.setVisibility(View.VISIBLE);
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
        binding.rlCamera.setOnClickListener(v -> {
            listener.onSourceClicked(SourceType.CAMERA);
            dismiss();
        });
        binding.rlGallery.setOnClickListener(v -> {
            listener.onSourceClicked(SourceType.GALLERY);
            dismiss();
        });
    }

    public enum SourceType {
        CAMERA,
        GALLERY
    }

}
