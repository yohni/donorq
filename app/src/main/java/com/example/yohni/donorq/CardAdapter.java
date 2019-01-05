package com.example.yohni.donorq;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    ArrayList<ProfileQ> adapterdataset;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onMessageClick(int position);
        void onWrapperClick(int position, ArrayList<ProfileQ> mData);
    }

    public void setOnClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public CardAdapter(ArrayList<ProfileQ> mDataset) {
        adapterdataset = mDataset;
    }


    @Override
    public CardAdapter.CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View tv = layoutInflater.inflate(R.layout.list_user_available, parent, false);
        return new CardViewHolder(tv, mListener);
    }

    @Override
    public void onBindViewHolder(CardAdapter.CardViewHolder holder, int position) {
        holder.nama.setText(adapterdataset.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return adapterdataset.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        private TextView nama;
        private Button btnMessage,btnCall;
        private LinearLayout wrapper;
        private ImageView imgDonator;


        public CardViewHolder(View tv, final OnItemClickListener listener) {
            super(tv);
            nama = (TextView) tv.findViewById(R.id.userName);
            btnMessage = (Button) tv.findViewById(R.id.btn_message);
            btnCall = (Button) tv.findViewById(R.id.btn_call);
            wrapper = (LinearLayout) tv.findViewById(R.id.search_wrapper);
            imgDonator = (ImageView) tv.findViewById(R.id.list_donator_img);

            GlideApp.with(tv).load("https://firebasestorage.googleapis.com/v0/b/donorq-876ed.appspot.com/o/users%2Fuser_default.jpg?alt=media")
                    .into(imgDonator);

            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });

            btnMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onMessageClick(position);
                    }
                }
            });

            wrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onWrapperClick(position, adapterdataset);
                    }
                }
            });
        }
    }
}
