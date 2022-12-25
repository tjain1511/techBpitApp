package com.indianapp.techbpit.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indianapp.techbpit.databinding.ItemCustomProjectBinding;
import com.indianapp.techbpit.model.ProjectResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder> {

    ArrayList<ProjectResponse> projectResponseArrayList;

    public ProjectsAdapter(ArrayList<ProjectResponse> projectResponseArrayList) {
        this.projectResponseArrayList = projectResponseArrayList;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProjectViewHolder(ItemCustomProjectBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return projectResponseArrayList.size();
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {
        private ItemCustomProjectBinding binding;

        public ProjectViewHolder(@NonNull ItemCustomProjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(int position) {
            binding.tvTitle.setText(projectResponseArrayList.get(position).title);
            binding.tvDuration.setText(projectResponseArrayList.get(position).duration);
            Picasso.get().load(projectResponseArrayList.get(position).image).into(binding.ivProject);
        }
    }
}
