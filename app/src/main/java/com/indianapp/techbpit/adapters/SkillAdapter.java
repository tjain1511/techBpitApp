package com.indianapp.techbpit.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indianapp.techbpit.databinding.ItemCustomSkillBinding;

import java.util.ArrayList;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.SkillViewHolder> {
    private ArrayList<String> skillsList;

    public SkillAdapter(ArrayList<String> skillsList) {
        this.skillsList = skillsList;
    }

    @NonNull
    @Override
    public SkillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SkillViewHolder(ItemCustomSkillBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SkillViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return skillsList.size();
    }

    public class SkillViewHolder extends RecyclerView.ViewHolder {
        private ItemCustomSkillBinding binding;

        public SkillViewHolder(@NonNull ItemCustomSkillBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void onBind(int position) {
            binding.tvSkill.setText(skillsList.get(position));
        }
    }
}
