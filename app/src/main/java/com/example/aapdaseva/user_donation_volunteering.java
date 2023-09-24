package com.example.aapdaseva;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class user_donation_volunteering extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_donation_volunteering);  // Make sure this name matches your layout's XML filename
    }

    // Method to handle the "Donate Resources" button click
    public void onDonateResourcesClick(View view) {
        // Perform action for Donate Resources
        // For example, navigate to another activity
        // Intent intent = new Intent(this, DonateResourcesActivity.class);
//        startActivity(intent);
    }

    // Method to handle the "Donate Funds" button click
    public void onDonateFundsClick(View view) {
        // Perform action for Donate Funds
        // For example, navigate to another activity
//        Intent intent = new Intent(this, DonateFundsActivity.class);
//        startActivity(intent);
    }

    // Method to handle the "Volunteer" button click


    // Method to handle the "Other Options" button click
    public void onOtherOptionsClick(View view) {
        // Perform action for Other Options
        // For example, navigate to another activity
//        Intent intent = new Intent(this, OtherOptionsActivity.class);
//        startActivity(intent);
    }
}
