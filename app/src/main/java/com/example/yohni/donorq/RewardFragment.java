package com.example.yohni.donorq;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RewardFragment extends Fragment {


    public RewardFragment() {
        // Required empty public constructor
    }

    private RecyclerView rewardRecycler;
    private RecyclerView.Adapter rewardAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Reward> mDataset;

    private DatabaseReference ref;

    private PmiFragment.OnFragmentInteractionListener mListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reward, container, false);
        rewardRecycler = (RecyclerView) view.findViewById(R.id.profile_reward_recycler);
        ref = FirebaseDatabase.getInstance().getReference();

        setData();
        buildRecycler();
        return view;
    }

    private void buildRecycler() {
        rewardRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        rewardAdapter = new RewardAdapter(mDataset);
        rewardRecycler.setAdapter(rewardAdapter);
    }

    private void setData() {
        mDataset = new ArrayList<Reward>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chacthData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void chacthData(DataSnapshot dataSnapshot) {
//        Reward temp = null;
        for (DataSnapshot ds: dataSnapshot.child("Rewards").getChildren()){
            Reward temp = ds.getValue(Reward.class);
            mDataset.add(temp);
            rewardAdapter.notifyDataSetChanged();
        }
    }

    private class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.RewardViewHolder> {
        private ArrayList<Reward> dataAdapter;

        public RewardAdapter(ArrayList<Reward> mDataset) {
            this.dataAdapter = mDataset;
        }

        @NonNull
        @Override
        public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_reward,parent, false);
            return new RewardViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {
            GlideApp.with(getContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/donorq-876ed.appspot.com/o/Rewards%2F"+ dataAdapter.get(position).getImage() +".jpg?alt=media")
                    .centerCrop()
                    .into(holder.imgView);
            holder.rewardName.setText(dataAdapter.get(position).getName());
            holder.rewardPoints.setText(Integer.toString(dataAdapter.get(position).getPoints()) + " points");
        }

        @Override
        public int getItemCount() {
            return dataAdapter.size();
        }

        public class RewardViewHolder extends RecyclerView.ViewHolder{
            TextView rewardName, rewardPoints;
            ImageView imgView;
            public RewardViewHolder(View view) {
                super(view);
                rewardName = (TextView) view.findViewById(R.id.reward_name);
                rewardPoints = (TextView) view.findViewById(R.id.reward_points);
                imgView = (ImageView) view.findViewById(R.id.reward_img);
            }
        }
    }
}
