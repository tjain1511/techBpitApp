package com.indianapp.techbpit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indianapp.techbpit.listeners.MemberRemovedClickListener;
import com.indianapp.techbpit.utils.SharedPrefHelper;
import com.indianapp.techbpit.databinding.ItemTeamMemberBinding;
import com.indianapp.techbpit.model.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TeamMembersAdapter extends RecyclerView.Adapter<TeamMembersAdapter.TeamMembersViewHolder> {
    private Context context;
    private ArrayList<UserModel> teamMembers;
    private MemberRemovedClickListener listener;

    public TeamMembersAdapter(Context context, ArrayList<UserModel> teamMembers) {
        this.context = context;
        this.teamMembers = teamMembers;
    }

    public void setListener(MemberRemovedClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TeamMembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TeamMembersViewHolder(ItemTeamMemberBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TeamMembersViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return teamMembers.size();
    }

    public class TeamMembersViewHolder extends RecyclerView.ViewHolder {
        private ItemTeamMemberBinding binding;

        public TeamMembersViewHolder(@NonNull ItemTeamMemberBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void onBind(int position) {
            binding.tvMemberName.setText(teamMembers.get(position).username);
            Picasso.get().load(teamMembers.get(position).imageUrl).into(binding.ivMemberPic);
            if (teamMembers.get(position)._id.equalsIgnoreCase(SharedPrefHelper.getUserModel(context)._id)) {
                binding.ivCancel.setVisibility(View.GONE);
            }
            binding.ivCancel.setOnClickListener(v -> {
                if (listener != null)
                    listener.onRemovedClick(teamMembers.get(position));
            });
        }
    }
}
