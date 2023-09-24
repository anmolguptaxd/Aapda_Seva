package com.example.aapdaseva;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserLoginActivity extends AppCompatActivity {

    private EditText etEmailOrPhone;
    private EditText etPassword;
    private CheckBox cbUseOtp;
    private Button btnLogin;
    private TextView tvRegister;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        etEmailOrPhone = findViewById(R.id.etEmailOrPhone);
        etPassword = findViewById(R.id.etPassword);
        cbUseOtp = findViewById(R.id.cbUseOtp);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // Login button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user inputs
                final String emailOrPhone = etEmailOrPhone.getText().toString();
                final String password = etPassword.getText().toString();
                boolean useOtp = cbUseOtp.isChecked();

                // Check if OTP is to be used
                if (useOtp) {
                    Intent intent = new Intent(UserLoginActivity.this, UserOtpLogin.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Implement login logic here
                    mAuth.signInWithEmailAndPassword(emailOrPhone, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        SharedPreferences sharedPreferencesUser = getSharedPreferences("login",MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferencesUser.edit();
                                        editor.putBoolean("login",true);
                                        editor.apply();
                                        // Navigate to HomePageActivity
                                        Intent intent = new Intent(UserLoginActivity.this, UserHomePage.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Fetch correct password from Firestore for additional check (not recommended)
                                        db.collection("users")
                                                .document(emailOrPhone)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document.exists()) {
                                                                String correctPassword = document.getString("password");
                                                                if (password.equals(correctPassword)) {
                                                                    // This block should ideally never be reached
                                                                    Toast.makeText(UserLoginActivity.this, "Something went wrong, but the password is correct", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(UserLoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                                                                }
                                                            } else {
                                                                Toast.makeText(UserLoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(UserLoginActivity.this, "Failed to get user details", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                }
            }
        });

        // Register text click listener
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the User Registration Activity
                Intent intent = new Intent(UserLoginActivity.this, user_registration.class);
                startActivity(intent);
            }
        });
    }
}
