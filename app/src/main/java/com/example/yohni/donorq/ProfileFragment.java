package com.example.yohni.donorq;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private static final int NUM_PAGES = 2;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    private TabAdapter tabAdapter;
    private TabLayout tabLayout;

    private ProfileQ myAcc;
    private TextView myAcc_name, myAcc_points;

    ImageView img;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private OnFragmentInteractionListener mListener;
    private DatabaseReference mRef;

    public ProfileFragment() {

    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        img = view.findViewById(R.id.profile_picture);
        mRef = FirebaseDatabase.getInstance().getReference("users");


        myAcc_name = (TextView) view.findViewById(R.id.myAcc_name);
        myAcc_points = (TextView) view.findViewById(R.id.myAcc_points);


        //pager
        viewPager = (ViewPager) view.findViewById(R.id.profile_pager);
        tabLayout = (TabLayout) view.findViewById(R.id.profile_tabs);

        tabAdapter = new TabAdapter(getFragmentManager());
        tabAdapter.addFragment(new HistoryFragment(), "History");
        tabAdapter.addFragment(new RewardFragment(),"Reward");
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

//        setData();
        mRef.child(mAuth.getCurrentUser().getEmail().replace(".","")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String AccName = dataSnapshot.child("name").getValue(String.class);
                int AccPoints = dataSnapshot.child("points").getValue(Integer.class);
                myAcc_name.setText(AccName);
                myAcc_points.setText(AccPoints + " points");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void setData() {
        myAcc = new ProfileQ();
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
