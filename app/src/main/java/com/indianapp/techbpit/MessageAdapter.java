package com.indianapp.techbpit;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indianapp.techbpit.databinding.ItemCustomMessageBinding;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private ArrayList<String> messages;

    public MessageAdapter(ArrayList<String> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(ItemCustomMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        ItemCustomMessageBinding binding;

        public MessageViewHolder(ItemCustomMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(int position) {
            Log.i("aksgf","ajsdhfvjh");
            binding.message.setText(messages.get(position));
        }
    }
}
