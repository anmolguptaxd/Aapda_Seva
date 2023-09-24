package com.example.aapdaseva;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;



public class YourDataSource {

    public static void getAgencyList(OnCompleteListener<List<Agency>> onCompleteListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo("usertype", "agency")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Agency> agencyList = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Agency agency = new Agency(
                                document.getString("agencyname"),
                                document.getString("area"),
                                document.getString("agencynumber"),
                                document.getString("contactperson"),
                                document.getString("email"),
                                document.getDouble("latitude"),
                                document.getDouble("longitude")


                        );
                        agencyList.add(agency);
                    }
                    Task<List<Agency>> successTask = Tasks.forResult(agencyList);
                    onCompleteListener.onComplete(successTask);
                })
                .addOnFailureListener(e -> {
                    onCompleteListener.onComplete(null);
                });
    }
}




