package com.example.yohni.donorq;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PmiFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PmiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PmiFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context ctx;

    private RecyclerView pmiRecycler;
    private PmiAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<DataPmi> mDataset;

    private DatabaseReference ref;

    private OnFragmentInteractionListener mListener;

    private LocationQ mAuthLoc;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public PmiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PmiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PmiFragment newInstance(String param1, String param2) {
        PmiFragment fragment = new PmiFragment();
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pmi, container, false);

        ref = FirebaseDatabase.getInstance().getReference();
        pmiRecycler = (RecyclerView) view.findViewById(R.id.pmi_recycler);
        ctx = this.getContext();
        catchMyLoc();
        setData();
        buildRecycler();
        return view;
    }

    private void catchMyLoc() {
        ref.child("users").child(mAuth.getCurrentUser().getEmail().replace(".",""))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mAuthLoc = dataSnapshot.child("location").getValue(LocationQ.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void buildRecycler() {
        pmiRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new PmiAdapter(ctx,mDataset);
        pmiRecycler.setAdapter(mAdapter);

        mAdapter.setOnClickListener(new PmiAdapter.OnItemClickListener() {
            private ImageView pmiImage;
            private TextView pmiName, pmiAddress, pmiStokA, pmiStokB, pmiStokAB, pmiStokO;
            private Button btnDir;
            private double lat,longi;
            @Override
            public void onItemClick(final int position, final ArrayList<DataPmi> mDataset) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.pmi_dialog);
                dialog.getWindow().getAttributes().windowAnimations = R.style.dialogSlide;
                pmiName = dialog.findViewById(R.id.pmi_dialog_name);
                pmiAddress = dialog.findViewById(R.id.pmi_dialog_address);
                pmiStokA = dialog.findViewById(R.id.pmi_dialog_blood_a);
                pmiStokB = dialog.findViewById(R.id.pmi_dialog_blood_b);
                pmiStokAB = dialog.findViewById(R.id.pmi_dialog_blood_ab);
                pmiStokO = dialog.findViewById(R.id.pmi_dialog_blood_o);
                btnDir = dialog.findViewById(R.id.pmi_dialog_btn);


                pmiName.setText(mDataset.get(position).getName());
                pmiAddress.setText(mDataset.get(position).getAddress());
                pmiStokA.setText(Integer.toString(mDataset.get(position).getStok().getA()));
                pmiStokB.setText(Integer.toString(mDataset.get(position).getStok().getB()));
                pmiStokAB.setText(Integer.toString(mDataset.get(position).getStok().getAb()));
                pmiStokO.setText(Integer.toString(mDataset.get(position).getStok().getO()));

                lat = mDataset.get(position).getLocation().getLatitude();
                longi = mDataset.get(position).getLocation().getLongitude();

                btnDir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        String uri = String.format(Locale.ENGLISH,"geo:%f,%f", -7.88051, 110.33249);
                        String uri = "google.navigation:q=" + mDataset.get(position).getNameOnMap();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        getContext().startActivity(intent);
                    }
                });

                dialog.show();
            }
        });
    }

    private void setData() {
        mDataset = new ArrayList<DataPmi>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chacthData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void chacthData(DataSnapshot dataSnapshot) {
        DataPmi temp = null;
        ArrayList<DataPmi> tempList = new ArrayList<>();

        for(DataSnapshot ds: dataSnapshot.child("PMI").getChildren()){
            String name = ds.child("nama").getValue().toString();
            String address = ds.child("address").getValue().toString();
            String noTelp = ds.child("noTelp").getValue().toString();
            LocationQ location = ds.child("location").getValue(LocationQ.class);
            Stok stok = ds.child("stok").getValue(Stok.class);
            String img = ds.child("imageName").getValue(String.class);
            String nameOnMap = ds.child("nameOnMap").getValue(String.class);

            temp = new DataPmi(name,address,noTelp,stok,location,img, nameOnMap);
            tempList.add(temp);
        }

        distanceFromMe(tempList);
    }

    private void distanceFromMe(ArrayList<DataPmi> tempList) {
        double R = 6371;
        double Result;
        for (DataPmi dp: tempList){
            double dLat = Math.toRadians(mAuthLoc.getLatitude() - dp.getLocation().getLatitude());
            double dLong = Math.toRadians(mAuthLoc.getLongitude() - dp.getLocation().getLongitude());
            double op = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(dp.getLocation().getLatitude()))* Math.cos(Math.toRadians(mAuthLoc.getLatitude())) * Math.sin(dLong/2) * Math.sin(dLong/2);
            double c = 2 * Math.atan2(Math.sqrt(op), Math.sqrt(1-op));
            Result = R * c;

            if(Result < 10){
                mDataset.add(dp);
                mAdapter.notifyDataSetChanged();
            }
        }

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
