package com.example.yohni.donorq;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.Scanner;

import static com.example.yohni.donorq.SearchFragment.blood;


public class ListUserCatched extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private RecyclerView searchRecycler;
    private CardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutaManager;
    public ArrayList<ProfileQ> mDataset;
    private ArrayList<ProfileQ> mTemp;
    public ProfileQ mAuthData;

    private DatabaseReference mRef;

    private OnFragmentInteractionListener mListener;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public ListUserCatched() {

    }
    public static ListUserCatched newInstance(String param1, String param2) {
        ListUserCatched fragment = new ListUserCatched();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_user_catched, container, false);

        mRef = FirebaseDatabase.getInstance().getReference();

        searchRecycler = (RecyclerView) view.findViewById(R.id.search_recycler);

        setData();
        buildRecycler();

        return view;
    }

    private void setData() {
        mDataset = new ArrayList<ProfileQ>();

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void buildRecycler(){
        searchRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CardAdapter(mDataset);
        searchRecycler.setAdapter(mAdapter);

        mAdapter.setOnClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent iCall = new Intent(Intent.ACTION_DIAL);
                iCall.setData(Uri.parse("tel:"+mDataset.get(position).getNoHP()));
                startActivity(iCall);
            }

            @Override
            public void onMessageClick(int position) {
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address",mDataset.get(position).getNoHP());
                smsIntent.putExtra("sms_body","Mohon bantuannya, saya sedang membutuhkan donor darah dengan golongan darah " + mDataset.get(position).getType());
                startActivity(smsIntent);
            }

            @Override
            public void onWrapperClick(final int position, final ArrayList<ProfileQ> mDataset) {
                ImageView detailImg;
                TextView detailName,detailPoints,detailCall,detailMsg;
                final Button btnRequest;
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.detail_user_dialog);
                dialog.getWindow().getAttributes().windowAnimations = R.style.dialogSlide;
                detailImg = dialog.findViewById(R.id.detail_img);
                detailName = dialog.findViewById(R.id.detail_username);
                detailPoints = dialog.findViewById(R.id.detail_points);
                detailCall = dialog.findViewById(R.id.detail_call);
                detailMsg = dialog.findViewById(R.id.detail_msg);
                btnRequest = dialog.findViewById(R.id.detail_request);

                detailName.setText(mDataset.get(position).getName());
                detailPoints.setText(mDataset.get(position).getPoints() + " points");
                detailCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            Intent iCall = new Intent(Intent.ACTION_DIAL);
                            iCall.setData(Uri.parse("tel:"+mDataset.get(position).getNoHP()));
                            startActivity(iCall);
                    }
                });
                detailMsg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                        smsIntent.setType("vnd.android-dir/mms-sms");
                        smsIntent.putExtra("address",mDataset.get(position).getNoHP());
                        smsIntent.putExtra("sms_body","Mohon bantuannya, saya sedang membutuhkan donor darah dengan golongan darah " + mDataset.get(position).getType());
                        startActivity(smsIntent);
                    }
                });

                btnRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mDataset.get(position).getStatus().getCtx().equals("rilis")){
                            mRef.child("users").child(mDataset.get(position).getEmail()
                                    .replace(".","")).child("status").child("by").setValue(mDataset.get(position).getNoHP());
                            mRef.child("users").child(mDataset.get(position).getEmail()
                                    .replace(".","")).child("status").child("ctx").setValue("locked");
                            sendNotification(mDataset.get(position));
                            btnRequest.setText("Requested");
                            Intent intent = new Intent(getActivity(),RequestProgress.class);
                            Bundle extras = new Bundle();
                            extras.putString("name",mDataset.get(position).getName());
                            extras.putString("noHp",mDataset.get(position).getNoHP());
                            extras.putString("email",mDataset.get(position).getEmail());
                            intent.putExtras(extras);
                            getActivity().getFragmentManager().popBackStack();
                            startActivity(intent);
                        }

                    }
                });

                dialog.show();
            }
        });
    }

    private void sendNotification(final ProfileQ profileQ) {
        Toast.makeText(getActivity(), "Current Recipients is : " + profileQ.getName() + " ( Just For Demo )", Toast.LENGTH_SHORT).show();

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


    private void showData(DataSnapshot dataSnapshot) {
        ProfileQ profilDetail = null;
        ArrayList<ProfileQ> temp = new ArrayList<ProfileQ>();
        String bloods = null;
        Intent intent = getActivity().getIntent();
        if (intent.getExtras() != null){
            bloods = intent.getStringExtra("goldar");
        }

        for(DataSnapshot ds : dataSnapshot.child("users").getChildren()){
            String sNama = ds.child("name").getValue().toString();
            String sEmail = ds.child("email").getValue().toString();
            String sNoHp = ds.child("noHP").getValue().toString();
            String sNoPMI = ds.child("noPMI").getValue().toString();
            String sType = ds.child("type").getValue().toString();
            LocationQ sLocation = ds.child("location").getValue(LocationQ.class);
            int sPoints = ds.child("points").getValue(Integer.class);
            Status sts = ds.child("status").getValue(Status.class);

            profilDetail = new ProfileQ(sNama, sEmail, sNoHp, sNoPMI, sType, sLocation, sPoints, sts);
            if (!sEmail.equals(mAuth.getCurrentUser().getEmail())){
                temp.add(profilDetail);
            }else{
                mAuthData = profilDetail;
            }
        }

        distanceFromMe(temp);
        mAdapter.notifyDataSetChanged();


    }

    public void distanceFromMe(ArrayList<ProfileQ> mDataCheck){
            double R = 6371;
            double Result;
            for (ProfileQ a: mDataCheck){
                double dLat = Math.toRadians(mAuthData.getLocation().getLatitude() - a.getLocation().getLatitude());
                double dLong = Math.toRadians(mAuthData.getLocation().getLongitude() - a.getLocation().getLongitude());
                double op = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(a.getLocation().getLatitude()))* Math.cos(Math.toRadians(mAuthData.getLocation().getLatitude())) * Math.sin(dLong/2) * Math.sin(dLong/2);
                double c = 2 * Math.atan2(Math.sqrt(op), Math.sqrt(1-op));
                Result = R * c;

                if(Result < 10 && a.getType().equals(blood)){
                    mDataset.add(a);
                    mAdapter.notifyDataSetChanged();
                }
            }
    }

    public void updateList(ArrayList<ProfileQ> yuhu){
        this.mDataset.clear();
        this.mDataset.addAll(mTemp);
        mAdapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
