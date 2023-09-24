package com.example.aapdaseva;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OfflineAccessActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView textViewData;
    ImageButton call, message;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_access_user);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        textViewData = findViewById(R.id.textViewData);
        call = findViewById(R.id.btnCall);
        message = findViewById(R.id.btnMessage);

        // Query all documents in the "Users" collection
        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> documentIds = new ArrayList<>();
                            List<String> agencyDetailsList = new ArrayList<>();

                            for (DocumentSnapshot document : task.getResult()) {
                                // Get the document ID and data
                                String documentId = document.getId();
                                String agencyName = document.getString("agencyname");
                                String area = document.getString("area");
                                String contactNumber = document.getString("agencynumber");

                                // Store the document ID and data in cache or display them as needed
                                documentIds.add(documentId);
                                String agencyDetails = "Agency Name: " + agencyName + "\n"
                                        + "Area: " + area + "\n"
                                        + "Contact Number: " + contactNumber;
                                agencyDetailsList.add(agencyDetails);
                            }

                            // Cache the document IDs and data for offline access
                            // You can store them in a local database, SharedPreferences, or any other preferred method
                            // For demonstration, we'll display the data in the TextView
                            StringBuilder builder = new StringBuilder();
                            for (int i = 0; i < documentIds.size(); i++) {
                                builder.append("Document ID: ").append(documentIds.get(i)).append("\n");
                                builder.append(agencyDetailsList.get(i)).append("\n\n");
                            }
                            textViewData.setText(builder.toString());
                        } else {
                            // An error occurred while fetching the documents
                            Log.d("OfflineAccessActivity", "Error getting documents: ", task.getException());
                            textViewData.setText("Error: " + task.getException().getMessage());
                        }
                    }
                });
        String phoneNumber = "+918217425856";

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String phoneNumber = "8217425856";

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    // Handle the case where no app can handle the intent (e.g., no phone app installed)
                    // You can display an error message or take other appropriate action.
                }
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("smsto:" + phoneNumber);
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "In Emergency Please Help!");

                // Check if there's an app that can handle the intent before starting it
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    // Handle the case where no app can handle the intent (e.g., no messaging app installed)
                    // You can display an error message or take other appropriate action.
                }

            }
        });


    }
}
