package com.example.aapdaseva;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AgencyProfileActivity extends AppCompatActivity {

    private static final String TAG = "AgencyProfileActivity";

    private TextView agencyNameTextView;
    private TextView agencyPhoneNumberTextView;
    private TextView agencyContactPersonTextView;
    private TextView agencyEmailTextView;
    private TextView agencyAreaOfExpertiseTextView;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agency_profile);

        // Initialize TextViews
        agencyNameTextView = findViewById(R.id.agencyNameTextView);
        agencyPhoneNumberTextView = findViewById(R.id.agencyPhoneNumberTextView);
        agencyContactPersonTextView = findViewById(R.id.agencyContactPersonTextView);
        agencyEmailTextView = findViewById(R.id.agencyEmailTextView);
        agencyAreaOfExpertiseTextView = findViewById(R.id.agencyAreaOfExpertiseTextView);
        btnBack = findViewById(R.id.btn_profileBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iback = new Intent(getApplicationContext(), dashboard.class);
                startActivity(iback);
                finish();
            }
        });
        // Initialize Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (currentUser != null) {
            // Get the UID of the current user
            String currentUserId = currentUser.getUid();

            // Fetch agency data for the current user
            db.collection("Users")
                    .document(currentUserId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Update the TextViews with the fetched data
                            agencyNameTextView.setText(documentSnapshot.getString("agencyname"));
                            agencyContactPersonTextView.setText(documentSnapshot.getString("contactperson"));
                            agencyEmailTextView.setText(documentSnapshot.getString("email"));
                            agencyPhoneNumberTextView.setText(documentSnapshot.getString("agencynumber"));
                            agencyAreaOfExpertiseTextView.setText(documentSnapshot.getString("area"));
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    })
                    .addOnFailureListener(e -> Log.w(TAG, "Error getting document", e));
        } else {
            // User is not authenticated, handle accordingly (e.g., redirect to login)
            Log.d(TAG, "User is not authenticated");
        }
    }
}
