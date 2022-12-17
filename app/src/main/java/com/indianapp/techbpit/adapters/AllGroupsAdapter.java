package com.indianapp.techbpit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indianapp.techbpit.RESTController;
import com.indianapp.techbpit.databinding.ItemJoinGroupBinding;
import com.indianapp.techbpit.model.GroupResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class AllGroupsAdapter extends RecyclerView.Adapter<AllGroupsAdapter.GroupHolder>  {
    private ArrayList<GroupResponse> groupsList;
    private JoinListener listener;

    public AllGroupsAdapter(ArrayList<GroupResponse> groupsList) {
        this.groupsList = groupsList;
    }

    public void setListener(JoinListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupHolder(ItemJoinGroupBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }




    public class GroupHolder extends RecyclerView.ViewHolder {
        ItemJoinGroupBinding binding;

        public GroupHolder(@NonNull ItemJoinGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void onBind(int position) {
            binding.tvGrpName.setText(groupsList.get(position).groupName);
            binding.tvGrpDescription.setText(groupsList.get(position).description);
            Picasso.get().load(groupsList.get(position).image).into(binding.ivGrpImage);
            if (groupsList.get(position).isJoined) {
                binding.btnJoin.setVisibility(View.VISIBLE);
                binding.btnJoin.setEnabled(false);
                binding.btnJoin.setText("Joined");
                binding.pbJoin.setVisibility(View.GONE);
            }
            binding.btnJoin.setOnClickListener(v -> {
                listener.onJoinClicked(groupsList.get(position)._id, position);
                binding.btnJoin.setVisibility(View.GONE);
                binding.pbJoin.setVisibility(View.VISIBLE);
            });
        }
    }

    public interface JoinListener {
        void onJoinClicked(String groupId, int position);
    }
}
