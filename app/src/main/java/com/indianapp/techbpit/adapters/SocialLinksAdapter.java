package com.indianapp.techbpit.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.indianapp.techbpit.R;
import com.indianapp.techbpit.databinding.ItemSocialLinkBinding;
import com.indianapp.techbpit.model.SocialPlatform;

import java.util.ArrayList;

public class SocialLinksAdapter extends RecyclerView.Adapter<SocialLinksAdapter.SocialLinkViewHolder> {
    private ArrayList<SocialPlatform> socialLinks;
    private Context context;

    public SocialLinksAdapter(Context context, ArrayList<SocialPlatform> socialLinks) {
        this.context = context;
        this.socialLinks = socialLinks;
    }

    @NonNull
    @Override
    public SocialLinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SocialLinkViewHolder(ItemSocialLinkBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SocialLinkViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return socialLinks.size();
    }

    public class SocialLinkViewHolder extends RecyclerView.ViewHolder {
        private ItemSocialLinkBinding binding;

        public SocialLinkViewHolder(@NonNull ItemSocialLinkBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void onBind(int position) {

            if (!TextUtils.isEmpty(socialLinks.get(position).platformLink)) {
                binding.tvPlatformLink.setText(socialLinks.get(position).platformLink);
                binding.getRoot().setVisibility(View.VISIBLE);
            } else {
                binding.getRoot().setVisibility(View.GONE);
            }
            Glide.with(context).load(socialLinks.get(position).platformImg).placeholder(R.drawable.img_5).into(binding.ivPlatformImage);
        }
    }
}
