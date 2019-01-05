package com.example.yohni.donorq;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

class PmiAdapter extends RecyclerView.Adapter<PmiAdapter.PmiViewHolder> {

    private ArrayList<DataPmi> dataAdapter;

    private OnItemClickListener mListener;

    private Context ctx;

    public interface OnItemClickListener{
        void onItemClick(int position, ArrayList<DataPmi> data);
    }

    public void setOnClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public PmiAdapter(Context ctx,ArrayList<DataPmi> mDataset) {
        dataAdapter = mDataset;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public PmiAdapter.PmiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View tv = layoutInflater.inflate(R.layout.list_pmi,parent, false);
        return new PmiViewHolder(tv, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PmiAdapter.PmiViewHolder holder, int position) {
        holder.pmiName.setText(dataAdapter.get(position).getName());
        holder.pmiAddress.setText(dataAdapter.get(position).getAddress());

        String url = "https://firebasestorage.googleapis.com/v0/b/donorq-876ed.appspot.com/o/pmi%2F" + dataAdapter.get(position).getImageName() +".jpg?alt=media";
        GlideApp.with(ctx).load(url).centerCrop().into(holder.pmiImage);
    }

    @Override
    public int getItemCount() {
        return dataAdapter.size();
    }

    public class PmiViewHolder extends RecyclerView.ViewHolder{
        TextView pmiName;
        TextView pmiAddress;
        ImageView pmiImage;
        public PmiViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            pmiName = (TextView) itemView.findViewById(R.id.pmi_name);
            pmiAddress = (TextView) itemView.findViewById(R.id.pmi_address);
            pmiImage = (ImageView) itemView.findViewById(R.id.pmi_img);
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position,dataAdapter);
                    }
                }
            });
        }
    }
}
