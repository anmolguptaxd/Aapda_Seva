package com.example.aapdaseva;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Home_page extends AppCompatActivity {
    Button  btn1,btn2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        btn1=findViewById(R.id.btn_main_RescueTeam);
        btn2=findViewById(R.id.btn_main_User);
        SharedPreferences sharedPreferences = getSharedPreferences("registration",MODE_PRIVATE);
        boolean check = sharedPreferences.getBoolean("registration",false);

        SharedPreferences sharedPreferencesUser = getSharedPreferences("login",MODE_PRIVATE);
        boolean checkUser = sharedPreferencesUser.getBoolean("login",false);


        //rescue
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inext;
                if(!check){
                    inext = new Intent(getApplicationContext(), AgencyLoginActivity.class);
                }
                else{
                    inext   = new Intent(getApplicationContext(), dashboard.class);
                }
                startActivity(inext);
//                finish();
            }
        });

        //user
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    // The application has internet access
                    Intent inext;
                    if(!checkUser){
                        inext = new Intent(getApplicationContext(), UserLoginActivity.class);
                    }
                    else{
                        inext   = new Intent(getApplicationContext(), UserHomePage.class);
                    }
                    startActivity(inext);


//                    finish();
//                } else {
//                    // The application does not have internet access
//                    Toast.makeText(Home_page.this, "Running in offline mode", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getApplicationContext(),OfflineAccessActivity.class);
//                    startActivity(intent);
//                }
            }
        });
    }
}