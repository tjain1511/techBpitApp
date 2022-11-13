package com.indianapp.techbpit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indianapp.techbpit.databinding.CustomReceiverMsgItemBinding;
import com.indianapp.techbpit.databinding.CustomSenderMsgItemBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MessageModel> messages;
    private static final int ITEM_SEND = 1;
    private static final int ITEM_RECEIVE = 2;
    private String myEmail;

    public MessageAdapter(ArrayList<MessageModel> messages, String myEmail) {
        this.messages = messages;
        this.myEmail = myEmail;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND) {
            return new SentMessageViewHolder(CustomSenderMsgItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new ReceivedMessageViewHolder(CustomReceiverMsgItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_SEND) {
            ((SentMessageViewHolder) holder).onBind(position);
        } else {
            ((ReceivedMessageViewHolder) holder).onBind(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).sender.equalsIgnoreCase(myEmail)) {
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
            if (position == 0) {
                binding.date.setVisibility(View.VISIBLE);
                binding.date.setText("Today");
            } else {
                binding.date.setVisibility(View.GONE);
            }
            if (URLUtil.isValidUrl(messages.get(position).message)) {
//                Picasso.get().load(messages.get(position)).into(binding.);
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
                Picasso.get().load(messages.get(position).message).placeholder(R.drawable.blank_profile).into(binding.imageView3);
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

            if (position == 0) {
                binding.date.setVisibility(View.VISIBLE);
                binding.date.setText("Today");
            } else {
                binding.date.setVisibility(View.GONE);
            }
            if (URLUtil.isValidUrl(messages.get(position).message)) {
                binding.receiverMsg.setVisibility(View.GONE);
                binding.imgTime.setText(sf.format(time));
                binding.receiverMsg.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.get(position).message).placeholder(R.drawable.blank_profile).into(binding.imageView3);
            } else {
                binding.receiverMsg.setVisibility(View.VISIBLE);
                binding.receiverImg.setVisibility(View.GONE);
                binding.time.setText(sf.format(time));
                binding.txtMessages.setText(messages.get(position).message);
            }
        }
    }

}
