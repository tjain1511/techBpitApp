package com.indianapp.techbpit.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indianapp.techbpit.activities.ProjectDetailsActivity;
import com.indianapp.techbpit.databinding.ItemCustomProjectBinding;
import com.indianapp.techbpit.model.ProjectResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder> {

    private ArrayList<ProjectResponse> projectResponseArrayList;
    private Context context;
    private String projectOwnerId;

    public ProjectsAdapter(Context context, ArrayList<ProjectResponse> projectResponseArrayList, String projectOwnerId) {
        this.context = context;
        this.projectResponseArrayList = projectResponseArrayList;
        this.projectOwnerId = projectOwnerId;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProjectViewHolder(ItemCustomProjectBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        holder.onBind(projectResponseArrayList.get(position));
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

        public void onBind(ProjectResponse projectResponse) {
            Picasso.get().load(projectResponse.image).into(binding.ivProject);

            if (!TextUtils.isEmpty(projectResponse.title)) {
                binding.tvTitle.setText(projectResponse.title);
            }
            if (!TextUtils.isEmpty(projectResponse.duration)) {
                binding.tvDuration.setText(projectResponse.duration);
            }

//            if (!TextUtils.isEmpty(projectResponse.description)) {
//                binding.tvDescription.setText(projectResponse.description);
//            }
//
//            binding.ivGithub.setOnClickListener(v -> {
//                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(projectResponse.gitLink)));
//            });
//            binding.ivProjectLink.setOnClickListener(v -> {
//                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(projectResponse.hostedLink)));
//            });
            binding.tvViewProject.setOnClickListener(v -> {
                Intent intent = new Intent(context, ProjectDetailsActivity.class);
                intent.putExtra("project_owner", projectOwnerId);
                intent.putExtra("project_data", projectResponse);
                context.startActivity(intent);
            });
        }
    }
}
