package com.indianapp.techbpit.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.indianapp.techbpit.R;
import com.indianapp.techbpit.databinding.CustomDateMsgItemBinding;
import com.indianapp.techbpit.databinding.CustomReceiverMsgItemBinding;
import com.indianapp.techbpit.databinding.CustomSenderMsgItemBinding;
import com.indianapp.techbpit.model.MessageModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_SEND = 1;
    private static final int ITEM_RECEIVE = 2;
    private static final int ITEM_DATE = 3;
    private ArrayList<MessageModel> messages;
    private String my_id;
    private Context ctx;

    public ChatAdapter(Context ctx, ArrayList<MessageModel> messages, String my_id) {
        this.ctx = ctx;
        this.messages = messages;
        this.my_id = my_id;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND) {
            return new SentMessageViewHolder(CustomSenderMsgItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == ITEM_RECEIVE) {
            return new ReceivedMessageViewHolder(CustomReceiverMsgItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new MessagesDateViewHolder(CustomDateMsgItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_SEND) {
            ((SentMessageViewHolder) holder).onBind(position);
        } else if (getItemViewType(position) == ITEM_RECEIVE) {
            ((ReceivedMessageViewHolder) holder).onBind(position);
        } else if (getItemViewType(position) == ITEM_DATE) {
            ((MessagesDateViewHolder) holder).onBind(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (!TextUtils.isEmpty(messages.get(position).date)) {
            return ITEM_DATE;
        }

        if (messages.get(position).sender.equalsIgnoreCase(my_id)) {
            return ITEM_SEND;
        } else {
            return ITEM_RECEIVE;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class SentMessageViewHolder extends RecyclerView.ViewHolder {
        CustomSenderMsgItemBinding binding;
        SimpleDateFormat sf;
        SimpleDateFormat sfd;

        public SentMessageViewHolder(CustomSenderMsgItemBinding binding) {
            super(binding.getRoot());
            sfd = new SimpleDateFormat("dd/MM/yyyy",
                    Locale.getDefault());
            sf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
            this.binding = binding;
        }

        public void onBind(int position) {
            Date time = new Date(Long.valueOf(messages.get(position).timestamp));
//            if (position == 0) {
//                binding.date.setVisibility(View.VISIBLE);
//                binding.date.setText("Today");
//            } else {
//                binding.date.setVisibility(View.GONE);
//            }
            if (URLUtil.isValidUrl(messages.get(position).imageUrl)) {
                if (messages.get(position).isSent) {
                    binding.imgTime.setVisibility(View.VISIBLE);
                    binding.imgTimer.setVisibility(View.GONE);
                } else {
                    binding.imgTime.setVisibility(View.GONE);
                    binding.imgTimer.setVisibility(View.VISIBLE);
                }
                binding.imgTime.setText(sf.format(time));
                binding.senderMsg.setVisibility(View.GONE);
                binding.senderImg.setVisibility(View.VISIBLE);
                Glide.with(ctx).load(messages.get(position).imageUrl).placeholder(R.drawable.blank_profile).into(binding.imageView3);
            } else {
                if (messages.get(position).isSent) {
                    binding.time.setVisibility(View.VISIBLE);
                    binding.timer.setVisibility(View.GONE);
                } else {
                    binding.time.setVisibility(View.GONE);
                    binding.timer.setVisibility(View.VISIBLE);
                }
                binding.senderMsg.setVisibility(View.VISIBLE);
                binding.senderImg.setVisibility(View.GONE);
                binding.time.setText(sf.format(time));
                binding.txtMessages.setText(messages.get(position).message);
            }
        }
    }

    public class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        CustomReceiverMsgItemBinding binding;
        SimpleDateFormat sf;
        SimpleDateFormat sfd;

        public ReceivedMessageViewHolder(CustomReceiverMsgItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            sfd = new SimpleDateFormat("dd/MM/yyyy",
                    Locale.getDefault());
            sf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        }

        public void onBind(int position) {
            Date time = new Date(Long.valueOf(messages.get(position).timestamp));
            Log.i("position_size", String.valueOf(messages.size()));
//            if (position == 0) {
//                binding.date.setVisibility(View.VISIBLE);
//                binding.date.setText("Today");
//            } else {
//                binding.date.setVisibility(View.GONE);
//            }
            if (URLUtil.isValidUrl(messages.get(position).imageUrl)) {
                binding.receiverMsg.setVisibility(View.GONE);
                binding.imgTime.setText(sf.format(time));
                binding.receiverImg.setVisibility(View.VISIBLE);
                Glide.with(ctx).load(messages.get(position).imageUrl).placeholder(R.drawable.blank_profile).into(binding.imageView3);
            } else {
                binding.receiverMsg.setVisibility(View.VISIBLE);
                binding.receiverImg.setVisibility(View.GONE);
                binding.time.setText(sf.format(time));
                binding.txtMessages.setText(messages.get(position).message);
            }
        }
    }

    public class MessagesDateViewHolder extends RecyclerView.ViewHolder {
        private SimpleDateFormat sfd;
        private CustomDateMsgItemBinding binding;


        public MessagesDateViewHolder(@NonNull CustomDateMsgItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            sfd = new SimpleDateFormat("dd/MM/yyyy",
                    Locale.getDefault());
        }

        private void onBind(int position) {
            if (!TextUtils.isEmpty(messages.get(position).date)) {
                binding.date.setVisibility(View.VISIBLE);
                if (messages.get(position).date.equalsIgnoreCase(sfd.format(new Date(System.currentTimeMillis())))) {
                    binding.date.setText("Today");
                } else if (messages.get(position).date.equalsIgnoreCase(sfd.format(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24)))) {
                    binding.date.setText("Yesterday");
                } else {
                    binding.date.setText(messages.get(position).date);

                }
            } else {
                binding.date.setVisibility(View.GONE);
            }
        }
    }

}
