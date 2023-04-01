package com.indianapp.techbpit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.indianapp.techbpit.utils.SharedPrefHelper;
import com.indianapp.techbpit.databinding.ActivityProjectDetailsBinding;
import com.indianapp.techbpit.model.ProjectResponse;
import com.squareup.picasso.Picasso;

public class ProjectDetailsActivity extends AppCompatActivity {
    private ActivityProjectDetailsBinding binding;
    private ProjectResponse project;
    private String projectOwnerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProjectDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getIntentData();
        binding.ivBack.setOnClickListener(v -> onBackPressed());
        setupUI();
    }

    private void getIntentData() {
        if (getIntent() != null && getIntent().getExtras().containsKey("project_data")) {
            project = (ProjectResponse) getIntent().getSerializableExtra("project_data");
        }
        if (getIntent() != null && getIntent().getExtras().containsKey("project_data")) {
            projectOwnerId = getIntent().getStringExtra("project_owner");
        }
    }

    private void setupUI() {
        binding.tvProjectTitle.setText(project.title);
        Picasso.get().load(project.image).into(binding.ivProjectImg);
        binding.tvProjectDescription.setText(project.description);
        binding.tvDuration.setText(project.duration);
        binding.tvGitLink.setText(project.gitLink);
        binding.tvProjectLink.setText(project.hostedLink);
        if (projectOwnerId.equalsIgnoreCase(SharedPrefHelper.getUserModel(this)._id)) {
            binding.ivEditProject.setVisibility(View.VISIBLE);
            binding.ivEditProject.setOnClickListener(v -> {
                Intent intent = new Intent(this, AddProjectActivity.class);
                intent.putExtra("project_edit", true);
                intent.putExtra("project_data", project);
                this.startActivity(intent);
            });
        }
    }

}