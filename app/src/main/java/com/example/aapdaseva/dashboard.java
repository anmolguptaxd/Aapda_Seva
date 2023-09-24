package com.example.aapdaseva;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class dashboard extends AppCompatActivity {

    private RecyclerView agencyRecyclerView;
    private AgencyAdapter agencyAdapter;
    private List<Agency> agencyList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private TextView agencyNameTextView;  // Declare the TextView
    private String selectedCalamity = "rescue";
    ImageButton btnLogout,btnnotification;

    FloatingActionButton location,profile;
    Spinner calamitySpinner;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        agencyNameTextView = findViewById(R.id.agencyNameTextView);  // Initialize the TextView
        calamitySpinner=findViewById(R.id.calamitySpinner);

        String[] calamities = new String[]{"All Agency","Rescue", "Medical", "Firefighting", "Engineering"};
        ArrayAdapter<String> calamityAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, calamities);
        calamityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        calamitySpinner.setAdapter(calamityAdapter);

        calamitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  selectedCalamity=calamities[position];
                  filterAgenciesByCalamity(selectedCalamity);

              }

              @Override
              public void onNothingSelected(AdapterView<?> parent) {
              }
          });

        btnnotification=findViewById(R.id.btnNotifications);
        btnnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getApplicationContext(),NotificationActivity.class);
                startActivity(intent);
            }
        });




        location = findViewById(R.id.locationFab);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
            }
        });
        profile=findViewById(R.id.profileFab);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inext = new Intent(getApplicationContext(), AgencyProfileActivity.class);
                startActivity(inext);

            }
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        agencyRecyclerView = findViewById(R.id.agencyRecyclerView);
        agencyRecyclerView.setHasFixedSize(true);
        agencyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        agencyList = new ArrayList<>();
        agencyAdapter = new AgencyAdapter(this, agencyList);
        agencyRecyclerView.setAdapter(agencyAdapter);

        agencyAdapter.setOnItemClickListener(new AgencyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Agency agency) {
                // Handle the item click here
                // You can start the ChatActivity and pass the agency details as extras
                Log.d("DashboardActivity", "Agency clicked: " + agency.getAgencyName());
                Log.d("DashboardActivity", "ReceiverUserId: " + agency.getEmail());
                Intent intent = new Intent(dashboard.this, ChatActivity.class);
                intent.putExtra("agencyname",agency.getAgencyName());
                intent.putExtra("email", agency.getEmail()); // If you have an agencyId field
                startActivity(intent);
            }
        });

        agencyRecyclerView.setAdapter(agencyAdapter);
        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("registration", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("registration", false);
                editor.apply();
                mAuth.signOut();
                Intent intent = new Intent(dashboard.this, AgencyLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        db = FirebaseFirestore.getInstance();

        // Fetch the agency name from Firestore
        String agencyEmail = mAuth.getCurrentUser().getEmail();  // Get the email of the current user
        db.collection("Users")
                .whereEqualTo("email", agencyEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            String agencyName = document.getString("agencyname");
                            agencyNameTextView.setText("Welcome, " + agencyName);  // Set the agency name to the TextView
                        }
                    } else {
                        Log.d("AgencyDashboard", "Error getting documents: ", task.getException());
                    }
                });

        db.collection("Users")
                .whereEqualTo("usertype", "agency")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Agency> agencyList = new ArrayList<>(); // Create a new list to store agencies
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (DocumentSnapshot document : querySnapshot) {
                                String agencyName = document.getString("agencyname");
                                String area = document.getString("area");
                                String contactPerson = document.getString("contactperson");
                                String email = document.getString("email");
                                Double latitude = document.getDouble("latitude");
                                Double longitude = document.getDouble("longitude");
                                String agencyNumber= document.getString("agencynumber");

                                // Check for null values before converting to double
                                if (latitude != null && longitude != null) {
                                    Agency agency = new Agency(agencyName, area, agencyNumber, contactPerson, email, latitude, longitude);
                                    agencyList.add(agency);
                                } else {
                                    // Handle the case where latitude or longitude is null
                                    // You can choose to skip this agency or add default values
                                }
                            }
                            if (agencyAdapter != null) { // Check if agencyAdapter is not null
                                agencyAdapter.setAgencyList(agencyList);
                                agencyAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        Log.d("AgencyDashboard", "Error getting documents: ", task.getException());
                    }
                });


    }

    public void fetchAgenies(){
        db.collection("Users")
                .whereEqualTo("usertype", "agency")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Agency> agencyList = new ArrayList<>();
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (DocumentSnapshot document : querySnapshot) {
                                // Fetch agency data here
                                Agency agency = document.toObject(Agency.class);
                                agencyList.add(agency);
                            }
                            // Apply the initial calamity filter
                            filterAgenciesByCalamity(selectedCalamity);
                        }
                    } else {
                        Log.d("AgencyDashboard", "Error getting documents: ", task.getException());
                    }
                });

    }

    private void filterAgenciesByCalamity(String calamity) {
        List<Agency> filteredList = new ArrayList<>();
        for (Agency agency : agencyList) {
            if(calamity == "All Agency"){
                filteredList = agencyList;
            }
            if (agency.getArea().equals(calamity)) {
                filteredList.add(agency);
            }
        }
        agencyAdapter.setAgencyList(filteredList);
        //agencyAdapter.notifyDataSetChanged();
        List<Agency> finalFilteredList = filteredList;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                agencyAdapter.filterList(finalFilteredList);
            }
        });
    }
}


