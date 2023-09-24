package com.example.aapdaseva;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AgencyLoginActivity extends AppCompatActivity {

    // Declare UI elements
    private FirebaseAuth mAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    TextView txtRegistration ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agency_login);  // Replace with your XML layout name

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        editTextEmail = findViewById(R.id.etEmailOrPhone);
        editTextPassword = findViewById(R.id.etPassword);
        buttonLogin = findViewById(R.id.btnLogin);


        txtRegistration = findViewById(R.id.tvRegister);

        txtRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgencyLoginActivity.this,RegistrationActivity.class);
                startActivity(intent);

            }
        });

        // Login button click event
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from the EditText fields
                final String email = editTextEmail.getText().toString().trim();
                final String password = editTextPassword.getText().toString().trim();

                // Validate input
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(AgencyLoginActivity.this, "Email and password are required", Toast.LENGTH_SHORT).show();
                } else {
                    // Firebase Authentication for the agency
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(AgencyLoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Login successful
                                        Toast.makeText(AgencyLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                                        // for directly shifting without registration
                                        SharedPreferences sharedPreferences = getSharedPreferences("registration",MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean("registration",true);
                                        editor.apply();

                                        // Navigate to the next activity
                                        Intent intent = new Intent(AgencyLoginActivity.this, dashboard.class); // Replace NextActivity with your next activity
                                        startActivity(intent);
                                        finish();

                                        //sending email to send request
                                        Intent iemail = new Intent(getApplicationContext(), sendHelpRequest.class);
                                        iemail.putExtra("email",email);



                                    } else {
                                        if (task.getException() != null) {
                                            Log.e("FirebaseAuth", task.getException().getMessage());
                                            Toast.makeText(AgencyLoginActivity.this, "Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });



                }
            }
        });
    }
}
