package com.example.yohni.donorq;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationViewQ;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FusedLocationProviderClient mFusedLocationProviderClient;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;

    private static final int REQUEST_CODE = 1000;
    private boolean mRequestingLocationUpdates = true;
    private String by;
    private String ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        OneSignal.sendTag("User_ID", mAuth.getCurrentUser().getEmail());

        ref.child("users").child(mAuth.getCurrentUser().getEmail()
        .replace(".","")).child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                by = dataSnapshot.child("by").getValue(String.class);
                ctx = dataSnapshot.child("ctx").getValue(String.class);
                if (ctx.equals("locked")){
                    finish();
                    startActivity(new Intent(MainActivity.this, Locked.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.main_frame_container, new PmiFragment());
        tx.commit();
//        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_container, new PmiFragment()).commit();



        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        runLocationQ();

        bottomNavigationViewQ = findViewById(R.id.nav_bottom);
        bottomNavigationViewQ.setOnNavigationItemSelectedListener(navListener);
        BottomNavHelper.disableShiftingMode(bottomNavigationViewQ);


    }

    private void runLocationQ() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

        } else {
            startService(new Intent(getApplicationContext(), UpdateLocationService.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        startService(new Intent(getApplicationContext(), UpdateLocationService.class));
                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates) {
//            startLocationUpdates();
            startService(new Intent(getApplicationContext(), UpdateLocationService.class));
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.nav_pmi:
                    selectedFragment = new PmiFragment();
                    break;
                case R.id.nav_search:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.nav_event:
                    selectedFragment = new EventFragment();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_container, selectedFragment).commit();

            return true;
        }
    };
}
