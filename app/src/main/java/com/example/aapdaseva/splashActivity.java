package com.example.aapdaseva;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class splashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        SharedPreferences sharedPreferences = getSharedPreferences("registration",MODE_PRIVATE);
//        boolean check = sharedPreferences.getBoolean("registration",false);



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (NetworkUtils.isNetworkAvailable(getApplicationContext())){
                    Intent inext = new Intent(getApplicationContext(),Home_page.class);
                    startActivity(inext);
                    finish();
                }
                else{
                    // The application does not have internet access
                    Toast.makeText(getApplicationContext(), "Running in offline mode", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),OfflineAccessActivity.class);
                    startActivity(intent);

                }

            }
        },2000);



    }
}