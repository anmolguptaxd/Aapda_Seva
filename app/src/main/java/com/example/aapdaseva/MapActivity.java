package com.example.aapdaseva;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import java.util.List;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialize osmdroid configuration
        Configuration.getInstance().setUserAgentValue(getPackageName());

        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        btnBack = findViewById(R.id.btn_mapBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iback = new Intent(getApplicationContext(), dashboard.class);
                startActivity(iback);
                finish();
            }
        });

        // Get agency data (latitude, longitude, agencyname) from your data source (e.g., Firestore)
        getAgencyData();
    }

    // Replace this with your actual data retrieval logic
    public void getAgencyData() {
        YourDataSource.getAgencyList(new OnCompleteListener<List<Agency>>() {
            @Override
            public void onComplete(@NonNull Task<List<Agency>> task) {
                if (task.isSuccessful()) {
                    List<Agency> agencyList = task.getResult();
                    if (agencyList != null) {
                        // Agency data retrieval successful
                        // Proceed to add markers to the map using agencyList
                        addMarkersToMap(agencyList);
                    } else {
                        // Handle the case where agencyList is null
                    }
                } else {
                    // Handle the error case
                }
            }
        });
    }

    // Add markers to the map based on the agencyList
    private void addMarkersToMap(List<Agency> agencyList) {
        for (Agency agency : agencyList) {
            GeoPoint agencyLocation = new GeoPoint(agency.getLatitude(), agency.getLongitude());
            Marker agencyMarker = new Marker(mapView);
            agencyMarker.setPosition(agencyLocation);
            agencyMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            agencyMarker.setTitle(agency.getAgencyName());
            mapView.getOverlays().add(agencyMarker);
        }

        // Set the map center and zoom level
        if (!agencyList.isEmpty()) {
            mapView.getController().setCenter(new GeoPoint(agencyList.get(0).getLatitude(), agencyList.get(0).getLongitude()));
            mapView.getController().setZoom(10.0);
        }
    }
}


