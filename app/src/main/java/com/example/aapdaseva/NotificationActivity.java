package com.example.aapdaseva;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HelpRequestAdapter adapter;
    private List<HelpRequest> helpRequests;
    private FirebaseFirestore db;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        helpRequests = new ArrayList<>();
        adapter = new HelpRequestAdapter(helpRequests);
        recyclerView.setAdapter(adapter);
        btnBack=findViewById(R.id.btn_notiBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iback = new Intent(getApplicationContext(), dashboard.class);
                startActivity(iback);
                finish();
            }
        });

        db = FirebaseFirestore.getInstance();

        // Fetch data from Firestore
//        db.collection("helpRequst")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            String message = document.getString("message");
//                            String senderId = document.getString("senderID"); // Fetch senderId
//
//                            HelpRequest helpRequest = new HelpRequest(message, senderId);
//                            helpRequests.add(helpRequest);
//                        }
//                        adapter.notifyDataSetChanged();
//                    } else {
//                        // Handle errors
//                    }
//                });
    }
}
