package com.example.aapdaseva;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class UserListofAgencies extends AppCompatActivity {

    private RecyclerView agencyRecyclerView;
    private AgencyAdapter agencyAdapter;
    private List<Agency> agencyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_listof_agencies);

        agencyRecyclerView = findViewById(R.id.agencyRecyclerView);
        agencyList = new ArrayList<>();

        agencyAdapter = new AgencyAdapter(this, agencyList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        agencyRecyclerView.setLayoutManager(layoutManager);
        agencyRecyclerView.setAdapter(agencyAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        agencyList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String agencyName = document.getString("agencyname");
                            String area = document.getString("area");
                            String contactPerson = document.getString("contactperson");
                            String email = document.getString("email");
                            Double latitude = document.getDouble("latitude");
                            Double longitude = document.getDouble("longitude");
                            String agencyNumber= document.getString("agencynumber");
                            Agency agency = new Agency(agencyName, area, agencyNumber, contactPerson, email, latitude, longitude);
                            agencyList.add(agency);
                        }
                        agencyAdapter.notifyDataSetChanged();
                    } else {
                        Log.w("FirestoreError", "Error getting documents.", task.getException());
                    }
                });
    }
}
