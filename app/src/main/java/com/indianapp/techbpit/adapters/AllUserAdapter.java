package com.indianapp.techbpit.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indianapp.techbpit.SharedPrefHelper;
import com.indianapp.techbpit.UserClickedListener;
import com.indianapp.techbpit.activities.ChatActivity;
import com.indianapp.techbpit.databinding.ItemAllUserBinding;
import com.indianapp.techbpit.model.UserModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.UserViewHolder> {
    private Context ctx;
    private String myEmail;
    private ArrayList<UserModel> allUsers;
    private UserClickedListener listener;

    public AllUserAdapter(Context ctx, ArrayList<UserModel> allUsers, String myEmail) {
        this.allUsers = allUsers;
        this.ctx = ctx;
        this.myEmail = myEmail;
    }

    public void setAllUsers(ArrayList<UserModel> allUsers) {
        this.allUsers = allUsers;
        notifyDataSetChanged();
    }

    public void setListener(UserClickedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(ItemAllUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return allUsers.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        ItemAllUserBinding binding;
        SimpleDateFormat sfd = new SimpleDateFormat("hh:mm aa",
                Locale.getDefault());

        public UserViewHolder(ItemAllUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(int position) {
            if (!allUsers.get(position)._id.equalsIgnoreCase(SharedPrefHelper.getUserModel(ctx)._id)) {
                binding.cardView3.setVisibility(View.VISIBLE);
                binding.userName.setText(allUsers.get(position).username);
                String recentMsg = "You: ";
                if (allUsers.get(position).lastMessage != null && allUsers.get(position).lastMessage.sender != null) {
                    if (!allUsers.get(position).lastMessage.sender.equalsIgnoreCase(SharedPrefHelper.getUserModel(ctx)._id)) {
                        recentMsg = allUsers.get(position).username + ": ";
                    }
                    recentMsg += allUsers.get(position).lastMessage.message;
                    binding.tvRecentMsg.setText(recentMsg);
                    Date time = new Date(Long.valueOf(allUsers.get(position).lastMessage.timestamp));
                    binding.tvTime.setText(sfd.format(time));
                }
                Picasso.get().load(allUsers.get(position).imageUrl).into(binding.circleImageView);
                binding.cl.setOnClickListener(v -> {
                    if (listener == null) {
                        Intent intent = new Intent(ctx, ChatActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("current_user", allUsers.get(position));
                        intent.putExtra("current_user", allUsers.get(position));
                        ctx.startActivity(intent);
                    } else {
                        listener.onUserClicked(position, allUsers.get(position));
                    }
                });
            } else {
                binding.cardView3.setVisibility(View.GONE);
            }
            if (allUsers.size() - 1 == position) {
                binding.ivLineBreak.setVisibility(View.GONE);
            }
        }
    }
}
