package com.example.yohni.donorq;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Locked extends AppCompatActivity {

    Button lockedCall;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    String noHp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locked);

        lockedCall = (Button) findViewById(R.id.locked_call);
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();

        mRef.child("users").child(mAuth.getCurrentUser().getEmail().replace(".",""))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        noHp = dataSnapshot.child("noHp").getValue(String.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        lockedCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iCall = new Intent(Intent.ACTION_DIAL);
                iCall.setData(Uri.parse("tel:"+ noHp));
                startActivity(iCall);
            }
        });

    }
}
