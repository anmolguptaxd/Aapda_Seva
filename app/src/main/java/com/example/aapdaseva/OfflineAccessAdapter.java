package com.example.aapdaseva;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OfflineAccessAdapter extends RecyclerView.Adapter<OfflineAccessAdapter.AgencyViewHolder> {

    private Context context;
    private List<Agency> agencyList;

    public OfflineAccessAdapter(Context context, List<Agency> agencyList) {
        this.context = context;
        this.agencyList = agencyList;
    }

    @NonNull
    @Override
    public AgencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.agency_item_cache_view, parent, false);
        return new AgencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgencyViewHolder holder, int position) {
        Agency agency = agencyList.get(position);

        holder.textViewAgencyName.setText(agency.getAgencyName());
        holder.textViewArea.setText(agency.getArea());
        holder.textViewContact.setText(agency.getAgencyNumber());

        // Add more bindings for other agency details here if needed
    }

    @Override
    public int getItemCount() {
        return agencyList.size();
    }

    public class AgencyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAgencyName;
        TextView textViewArea;
        TextView textViewContact;

        public AgencyViewHolder(View itemView) {
            super(itemView);
            textViewAgencyName = itemView.findViewById(R.id.textViewAgencyName);
            textViewArea = itemView.findViewById(R.id.textViewArea);
            textViewContact = itemView.findViewById(R.id.textViewContact);
        }
    }
}
