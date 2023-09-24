package com.example.aapdaseva;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.concurrent.TimeUnit;

public class UserOtpLogin extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_user_login);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.send_otp_button).setOnClickListener(view -> {
            String phoneNumber = ((EditText) findViewById(R.id.phone_number_entry)).getText().toString().trim();
            try {
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNumber, "US"); // Replace "US" with the country code you want
                String formatted = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.E164);
                sendOtp(formatted);
            } catch (NumberParseException e) {
                Toast.makeText(UserOtpLogin.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.login_button).setOnClickListener(view -> {
            String otp = ((EditText) findViewById(R.id.otp_entry)).getText().toString().trim();
            verifyOtp(otp);
        });
    }

    private void sendOtp(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        // Implement logic for instant verification or auto-retrieval here
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(UserOtpLogin.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        Log.e("OTP", "Verification failed: " + e.getMessage());
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        mVerificationId = verificationId;
                        Toast.makeText(UserOtpLogin.this, "Code sent", Toast.LENGTH_SHORT).show();

                        Log.d("OTP", "Code sent");
                    }
                }
        );
    }

    private void verifyOtp(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(UserOtpLogin.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UserOtpLogin.this, "Failed to log in", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
