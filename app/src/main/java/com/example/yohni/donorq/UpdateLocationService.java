package com.example.yohni.donorq;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateLocationService extends Service {
    public UpdateLocationService() {
    }

    private static final int REQUEST_CODE = 100;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("users");

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    private Boolean mRequestingLocationUpdates = true;

    FusedLocationProviderClient mFusedLocationProviderClient;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        buildLocationRequest();
        buildLocationCallback();
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        return START_STICKY;
    }

    private void buildLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    LocationQ mLocationQ = new LocationQ(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude());
                    myRef.child(mAuth.getCurrentUser().getEmail().replace(".", "")).child("location").setValue(mLocationQ);
                }

            }
        };
    }

    private void buildLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
}
