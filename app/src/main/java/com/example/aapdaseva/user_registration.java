package com.example.aapdaseva;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class user_registration extends AppCompatActivity {

    // Declare UI elements
    private EditText etFullName, etUsername, etEmail, etPhone, etPassword;
    private RadioGroup rgGender;
    private Button btnRegister;

    // Declare Firebase components
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        etFullName = findViewById(R.id.etFullName);
        rgGender = findViewById(R.id.rgGender);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        // Handle Register Button Click
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmail.getText().toString().trim();
                final String password = etPassword.getText().toString().trim();

                // Create a new user with Firebase Authentication
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // Store additional user info in Firestore
                                String fullName = etFullName.getText().toString();
                                int selectedId = rgGender.getCheckedRadioButtonId();
                                RadioButton selectedGender = findViewById(selectedId);
                                String gender = selectedGender.getText().toString();
                                String username = etUsername.getText().toString();
                                String phone = etPhone.getText().toString();

                                Map<String, Object> user = new HashMap<>();
                                user.put("fullName", fullName);
                                user.put("gender", gender);
                                user.put("username", username);
                                user.put("phone", phone);
                                user.put("email", email);  // Adding email
                                user.put("password", password);  // Adding password

                                db.collection("users")
                                        .document(mAuth.getCurrentUser().getUid())
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                SharedPreferences sharedPreferencesUser = getSharedPreferences("login", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferencesUser.edit();
                                                editor.putBoolean("login", false);
                                                editor.apply();

                                                // Navigate to HomePageActivity
                                                Intent intent = new Intent(user_registration.this, UserHomePage.class);
                                                startActivity(intent);
                                                Toast.makeText(user_registration.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(user_registration.this, "Failed to add user details", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(user_registration.this, "Authentication Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}
