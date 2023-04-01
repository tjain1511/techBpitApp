package com.indianapp.techbpit.BottomSheetFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.indianapp.techbpit.databinding.BottomSheetErrorBinding;

public class ErrorBottomSheet extends BottomSheetDialogFragment {
    private BottomSheetErrorBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BottomSheetErrorBinding inflate = BottomSheetErrorBinding.inflate(inflater, container, false);
        this.binding = inflate;
        return inflate.getRoot();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        BottomSheetBehavior.from((View) view.getParent()).setState(3);
        super.onViewCreated(view, savedInstanceState);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }
}