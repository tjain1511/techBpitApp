package com.indianapp.techbpit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indianapp.techbpit.databinding.ItemAllUserBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.UserViewHolder> {
    private ArrayList<UserModel> allUsers;
    Context ctx;
    String myEmail;

    public AllUserAdapter(Context ctx, ArrayList<UserModel> allUsers, String myEmail) {
        this.allUsers = allUsers;
        this.ctx = ctx;
        this.myEmail = myEmail;
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

        public UserViewHolder(ItemAllUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(int position) {
            if (!allUsers.get(position).email.equalsIgnoreCase(myEmail)) {
                binding.cardView3.setVisibility(View.VISIBLE);
                binding.userName.setText(allUsers.get(position).username);
                Picasso.get().load(allUsers.get(position).imageUrl).into(binding.circleImageView);
                binding.cl.setOnClickListener(v -> {
                    Intent intent = new Intent(ctx, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("current_user", allUsers.get(position));
                    intent.putExtra("bundle", bundle);
                    ctx.startActivity(intent);
                });
            }else{
                binding.cardView3.setVisibility(View.GONE);
            }
        }
    }
}
