package com.example.aapdaseva;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Initialize Firebase
        FirebaseApp.initializeApp(this);
    }
}

