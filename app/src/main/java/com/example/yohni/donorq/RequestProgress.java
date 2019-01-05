package com.example.yohni.donorq;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RequestProgress extends AppCompatActivity {

    TextView progressName,cancel;
    Button proCall, proMsg, done;
    private DatabaseReference mRef;
    int points;
    ProfileQ profileQ;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_progress);


        mRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        progressName = (TextView) findViewById(R.id.progress_name);
        proCall = (Button) findViewById(R.id.progress_call);
        proMsg = (Button) findViewById(R.id.progress_msg);
        cancel = (TextView) findViewById(R.id.cancel_request);
        done = (Button) findViewById(R.id.done);

        mRef.child("users").child(mAuth.getCurrentUser().getEmail()
        .replace(".","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profileQ = dataSnapshot.getValue(ProfileQ.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final Bundle extras = getIntent().getExtras();
        final String email = extras.getString("email");

        progressName.setText(extras.getString("name"));
        proCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iCall = new Intent(Intent.ACTION_DIAL);
                iCall.setData(Uri.parse("tel:"+extras.getString("noHp")));
                startActivity(iCall);
            }
        });

        proMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address",extras.getString("noHp"));
                startActivity(smsIntent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef.child("users").child(email.replace(".","")).child("status")
                        .child("by").setValue("none");
                mRef.child("users").child(email.replace(".","")).child("status")
                        .child("ctx").setValue("rilis");
                finish();
                sendNotification(profileQ);
                startActivity(new Intent(RequestProgress.this,MainActivity.class));
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef.child("users").child(email.replace(".","")).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        points = dataSnapshot.child("points").getValue(Integer.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                points = points + 100;

                mRef.child("users").child(email.replace(".","")).child("status")
                        .child("by").setValue("none");
                mRef.child("users").child(email.replace(".","")).child("status")
                        .child("ctx").setValue("rilis");
                mRef.child("users").child(email.replace(".","")).child("points")
                        .setValue(points);
                finish();
                startActivity(new Intent(RequestProgress.this,MainActivity.class));
                sendNotification(profileQ);
            }
        });
    }

    private void sendNotification(final ProfileQ profileQ) {
        Toast.makeText(this, "Current Recipients is : " + profileQ.getName() + " ( Just For Demo )", Toast.LENGTH_SHORT).show();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String send_email;

                    //This is a Simple Logic to Send Notification different Device Programmatically....
                    if (mAuth.getCurrentUser().getEmail().equals(profileQ.getEmail())) {
                        send_email = mAuth.getCurrentUser().getEmail();
                    } else {
                        send_email = profileQ.getEmail();
                    }

                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic NDA0Yjc0ZTMtMjQwMy00NjgzLTgzMDktYmEwNTQzNTQ5NjVl");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"fc9ff746-582b-4568-b8f8-64d1b4c328a8\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_ID\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"

                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \"English Message\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }
}
