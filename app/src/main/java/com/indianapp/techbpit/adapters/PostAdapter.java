package com.indianapp.techbpit.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indianapp.techbpit.BottomSheetFragment.BottomSheetPostType;
import com.indianapp.techbpit.utils.DateTimeUtils;
import com.indianapp.techbpit.databinding.ItemCommunityPostBinding;
import com.indianapp.techbpit.databinding.ItemEventPostBinding;
import com.indianapp.techbpit.databinding.ItemResourcePostBinding;
import com.indianapp.techbpit.model.SocialPostResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<SocialPostResponse> postRequestItems;

    public PostAdapter(Context context, ArrayList<SocialPostResponse> postRequestItems) {
        this.context = context;
        this.postRequestItems = postRequestItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == BottomSheetPostType.PostType.COMMUNITY_POST.id) {
            return new CommunityPostHolder(ItemCommunityPostBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == BottomSheetPostType.PostType.EVENT_POST.id) {
            return new EventPostHolder(ItemEventPostBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new ResourcePostHolder(ItemResourcePostBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == BottomSheetPostType.PostType.COMMUNITY_POST.id) {
            ((CommunityPostHolder) holder).onBind(position);
        } else if (getItemViewType(position) == BottomSheetPostType.PostType.EVENT_POST.id) {
            ((EventPostHolder) holder).onBind(position);
        } else {
            ((ResourcePostHolder) holder).onBind(position);
        }
    }

    @Override
    public int getItemCount() {
        return postRequestItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (postRequestItems.get(position).postType.equalsIgnoreCase(BottomSheetPostType.PostType.COMMUNITY_POST.label)) {
            return BottomSheetPostType.PostType.COMMUNITY_POST.id;
        } else if (postRequestItems.get(position).postType.equalsIgnoreCase(BottomSheetPostType.PostType.EVENT_POST.label)) {
            return BottomSheetPostType.PostType.EVENT_POST.id;
        } else {
            return BottomSheetPostType.PostType.RESOURCE_POST.id;
        }
    }

    public class EventPostHolder extends RecyclerView.ViewHolder {
        ItemEventPostBinding binding;

        public EventPostHolder(@NonNull ItemEventPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(int position) {
            if (URLUtil.isValidUrl(postRequestItems.get(position).imageUrl)) {
                Picasso.get().load(postRequestItems.get(position).imageUrl).into(binding.ivPost);
            }
            Picasso.get().load(postRequestItems.get(position).groupId.image).into(binding.ivLogo);
            binding.tvHeading.setText(Html.fromHtml("A new <b>event</b> was posted in the <b>" + postRequestItems.get(position).groupId.groupName + "</b> Community"));
            binding.tvDate.setText(DateTimeUtils.getDateFromFormattedDate(postRequestItems.get(position).eventDate));
            binding.tvMonth.setText(DateTimeUtils.getMonthFromFormattedDate(postRequestItems.get(position).eventDate));
            binding.tvTime.setText(postRequestItems.get(position).eventTime + " Onwards");
            if (postRequestItems.get(position).mode.equalsIgnoreCase("online")) {
                binding.tvMeetingVenue.setText("Online Meeting");
            } else {
                binding.tvMeetingVenue.setText(postRequestItems.get(position).venue);
            }
        }
    }

    public class CommunityPostHolder extends RecyclerView.ViewHolder {
        ItemCommunityPostBinding binding;

        public CommunityPostHolder(@NonNull ItemCommunityPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(int position) {
            if (URLUtil.isValidUrl(postRequestItems.get(position).imageUrl)) {
                Picasso.get().load(postRequestItems.get(position).imageUrl).into(binding.ivPost);
            }
            Picasso.get().load(postRequestItems.get(position).author.imageUrl).into(binding.ivLogo);
            binding.tvHeading.setText(postRequestItems.get(position).author.username);
            binding.tvPostTime.setText(android.text.format.DateUtils.getRelativeTimeSpanString(Long.parseLong(postRequestItems.get(position).timestamp)).toString());
        }
    }

    public class ResourcePostHolder extends RecyclerView.ViewHolder {
        ItemResourcePostBinding binding;

        public ResourcePostHolder(@NonNull ItemResourcePostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(int position) {
//            if (URLUtil.isValidUrl(postRequestItems.get(position).imageUrl)) {
//                Picasso.get().load(postRequestItems.get(position).imageUrl).into(binding.ivPost);
//            }
            Picasso.get().load(postRequestItems.get(position).groupId.image).into(binding.ivLogo);
            binding.tvLblResourceAdded.setText(Html.fromHtml("A new <b>resource</b> was added to the <b>" + postRequestItems.get(position).groupId.groupName + "</b> Community"));
            binding.tvReadTime.setText(postRequestItems.get(position).resourceTime);
        }
    }
}
