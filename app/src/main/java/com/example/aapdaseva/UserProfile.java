package com.example.aapdaseva;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfile extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private ImageView profilePicture;
    private TextView userNameRight;
    private TextView tvUsername;
    private TextView tvEmail;
    private TextView tvPhoneNumber;
    private TextView tvGender;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);  // Assuming this XML is named activity_user_profile.xml

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Initialize UI elements
        profilePicture = findViewById(R.id.profilePicture);
        userNameRight = findViewById(R.id.userNameRight);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvGender = findViewById(R.id.tvGender);
        btnBack = findViewById(R.id.btnBack);

        // Fetch User Data from Firestore
        if (currentUser != null) {
            String uid = currentUser.getUid();

            db.collection("users").document(uid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Update the TextViews with the fetched data
                                userNameRight.setText(document.getString("name"));
                                tvUsername.setText("Username - " + document.getString("username"));
                                tvEmail.setText("Email - " + document.getString("email"));
                                tvPhoneNumber.setText("Phone Number - " + document.getString("phone"));
                                tvGender.setText("Gender - " + document.getString("gender"));

                                // TODO: Load the profile picture using Glide or Picasso
                            }
                        } else {
                            // Log the exception or show an error dialog
                        }
                    });
        }

        // Back button functionality
        btnBack.setOnClickListener(v -> finish());
    }
}
