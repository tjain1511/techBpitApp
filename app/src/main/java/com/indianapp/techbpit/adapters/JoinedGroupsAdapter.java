package com.indianapp.techbpit.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indianapp.techbpit.activities.MainActivity;
import com.indianapp.techbpit.databinding.ItemJoinedGroupsBinding;
import com.indianapp.techbpit.model.GroupResponse;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class JoinedGroupsAdapter extends RecyclerView.Adapter<JoinedGroupsAdapter.GroupViewHolder> {
    private ArrayList<GroupResponse> joinedGroups;
    private Context ctx;

    public JoinedGroupsAdapter(Context ctx, ArrayList<GroupResponse> joinedGroups) {
        this.ctx = ctx;
        this.joinedGroups = joinedGroups;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupViewHolder(ItemJoinedGroupsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return joinedGroups.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        ItemJoinedGroupsBinding binding;

        public GroupViewHolder(@NonNull ItemJoinedGroupsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void onBind(int position) {
            binding.cardView3.setVisibility(View.VISIBLE);
            binding.tvGrpName.setText(joinedGroups.get(position).groupName);
            Picasso.get().load(joinedGroups.get(position).image).into(binding.circleImageView);
            binding.cl.setOnClickListener(v -> {
                Intent intent = new Intent(ctx, MainActivity.class);
                intent.putExtra("is_grp_chat", true);
                intent.putExtra("group_id", joinedGroups.get(position)._id);
                intent.putExtra("group_name",joinedGroups.get(position).groupName);
                intent.putExtra("group_image",joinedGroups.get(position).image);
                ctx.startActivity(intent);
            });
        }
    }
}