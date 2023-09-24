package com.example.aapdaseva;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class UserHomePage extends AppCompatActivity {
    ImageButton btnLogoutUser, btnMaps,btnProfile;
    HorizontalScrollView horizontalScrollView;
    LinearLayout linearLayout;
    ObjectAnimator animator;
    //private VideoView videoView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user); // Replace with the actual name of your XML layout file

        // Initialize Buttons
        Button btnAgencies = findViewById(R.id.btnAgencies);
        Button btnHelpRequest = findViewById(R.id.btnHelpRequest);
        Button btnDonation = findViewById(R.id.btnDonation);
        horizontalScrollView = findViewById(R.id.horizontalScrollView);
        linearLayout = findViewById(R.id.aniLinearLayout);
       // setupMarquee(20000);


        //videoView = findViewById(R.id.videoView);

        String videoURL = "https://www.youtube.com/watch?v=4Xebwzb3dDE";

        Uri uri = Uri.parse(videoURL);
        //videoView.setVideoURI(uri);
        //videoView.start();



        Button btnOfflineAccess = findViewById(R.id.btnOfflineAccess);

        // Initialize ImageButtons for the Bottom Navigation
         btnLogoutUser = findViewById(R.id.btnLogOutUser);
         btnMaps = findViewById(R.id.btnMaps);
         btnProfile = findViewById(R.id.btnProfile);

        // Set onClickListeners for each button to navigate or perform some action
        btnAgencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(UserHomePage.this, UserListofAgencies.class);
                startActivity(intent);
                // Navigate to the Agency List Page

            }
        });

        btnHelpRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code to handle Help Request action
                Intent intent = new Intent(getApplicationContext(),sendHelpRequest.class);
                startActivity(intent);

            }
        });

        btnDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomePage.this, user_donation_volunteering.class);
                startActivity(intent);
                // Code to handle Donation action
            }
        });



        btnOfflineAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code to handle Offline Access action

                Intent intent=new Intent(getApplicationContext(), OfflineAccessActivity.class);
                startActivity(intent);
            }
        });

        btnLogoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferencesUser = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferencesUser.edit();
                editor.putBoolean("login", false);
                editor.apply();

                Intent intent = new Intent(getApplicationContext(), Home_page.class);
                startActivity(intent);
                finish();


            }
        });

        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHomePage.this, MapActivity.class);
                startActivity(intent);
                // Code to handle Notification button action
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(UserHomePage.this, UserProfile.class);
                startActivity(intent);
                // Code to handle Dashboard button action
            }
        });
    }
//    protected void onPause() {
//        super.onPause();
//        // Pause the video when the activity is paused (e.g., when the user switches to another app)
//        if (videoView.isPlaying()) {
//            videoView.pause();
//        }
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Resume playing the video when the activity is resumed
//        videoView.start();
//    }

    private void setupMarquee(final int duration) {
        horizontalScrollView.post(new Runnable() {
            @Override
            public void run() {
                int scrollWidth = linearLayout.getWidth() - horizontalScrollView.getWidth();
                if (scrollWidth > 0) {
                    animator = ObjectAnimator.ofInt(horizontalScrollView, "scrollX", scrollWidth);
                    animator.setDuration(duration);
                    animator.setInterpolator(new LinearInterpolator());
                    animator.setRepeatMode(ObjectAnimator.RESTART);
                    animator.setRepeatCount(ObjectAnimator.INFINITE);
                    animator.start();
                }
            }
        });
    }
}
