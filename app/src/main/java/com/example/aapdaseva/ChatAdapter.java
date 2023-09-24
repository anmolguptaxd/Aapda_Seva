package com.example.aapdaseva;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private final Context context;
    private final List<ChatMessage> chatMessageList;
    private final String senderId; // ID of the current user to differentiate between sender and receiver

    public ChatAdapter(Context context, List<ChatMessage> chatMessageList, String currentUserId) {
        this.context = context;
        this.chatMessageList = chatMessageList;
        this.senderId = currentUserId;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_sender, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_reciever, parent, false);
        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessageList.get(position);
        holder.messageTextView.setText(chatMessage.getMessage());

        // Initialize and set the timestamp if your layout contains a timestampTextView
        if (holder.timestampTextView != null) {
            holder.timestampTextView.setText(formatTimestamp(chatMessage.getTimestamp()));
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessageList.get(position).getSenderId().equals(senderId)) {
            return 0; // sender
        } else  {
            return 1; // receiver
        }
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView messageTextView;
        TextView timestampTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            // Initialize timestampTextView if your layout contains it
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
        }
    }

    private String formatTimestamp(Date timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
        return sdf.format(timestamp);
    }
}
