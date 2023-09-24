package com.example.aapdaseva;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AgencyAdapter extends RecyclerView.Adapter<AgencyAdapter.AgencyViewHolder> {

    private Context context;
    private List<Agency> agencyList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Agency agency);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public AgencyAdapter(Context context, List<Agency> agencyList) {
        this.context = context;
        this.agencyList = agencyList;
    }

    @NonNull
    @Override
    public AgencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.agency_item, parent, false);
        return new AgencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgencyViewHolder holder, int position) {
        Agency agency = agencyList.get(position);
        Log.d("AgencyAdapter", "Agency Name from list: " + agency.getAgencyName());  // Debugging statement
        holder.agencyNameTextView.setText(agency.getAgencyName());
        holder.agencyNumberTextView.setText(agency.getAgencyNumber());
        holder.agencyContactTextView.setText(agency.getContactPerson());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    Log.d("AgencyAdapter", "Item clicked: " + agency.getAgencyName());
                    onItemClickListener.onItemClick(agency); // Trigger the click listener
                }
            }
        });
    }

    public void setAgencyList(List<Agency> agencyList) {
        this.agencyList = agencyList;
    }

    @Override
    public int getItemCount() {
        return agencyList.size();
    }

    public static class AgencyViewHolder extends RecyclerView.ViewHolder {

        TextView agencyNameTextView;
        TextView agencyNumberTextView;
        TextView agencyContactTextView;

        public AgencyViewHolder(@NonNull View itemView) {
            super(itemView);
            agencyNameTextView = itemView.findViewById(R.id.textViewAgencyName);
            agencyNumberTextView = itemView.findViewById(R.id.textViewArea);
            agencyContactTextView = itemView.findViewById(R.id.textViewContact);
        }
    }
    public void filterList(List<Agency> filteredList) {
        agencyList = filteredList;
        notifyDataSetChanged();
    }

}