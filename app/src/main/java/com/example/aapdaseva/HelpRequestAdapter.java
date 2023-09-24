package com.example.aapdaseva;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HelpRequestAdapter extends RecyclerView.Adapter<HelpRequestAdapter.ViewHolder> {

    private List<HelpRequest> helpRequests;

    public HelpRequestAdapter(List<HelpRequest> helpRequests) {
        this.helpRequests = helpRequests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_help_request, parent, false);
        return new ViewHolder(view);
    }


//    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HelpRequest request = helpRequests.get(position);
        holder.messageTextView.setText(request.getMessage()+"");
        //holder.senderIdTextView.setText();
    }

    @Override
    public int getItemCount() {
        return helpRequests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public WindowDecorActionBar.TabImpl senderIdTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }
    }
}
