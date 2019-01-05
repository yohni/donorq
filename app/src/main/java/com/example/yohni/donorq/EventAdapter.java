package com.example.yohni.donorq;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    ArrayList<EventQ> mDataAdapter;
    private Context ctx;

    public EventAdapter(Context ctx, ArrayList<EventQ> mDataset) {

        this.ctx = ctx;
        mDataAdapter = mDataset;
    }

    @NonNull
    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View tv = layoutInflater.inflate(R.layout.list_event, parent, false);

        return new EventViewHolder(tv);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.EventViewHolder holder, int position) {
        holder.eventName.setText(mDataAdapter.get(position).getNama());
        holder.eventDate.setText(mDataAdapter.get(position).getTanggal());
        holder.eventAddress.setText(mDataAdapter.get(position).getLokasi());

        GlideApp.with(ctx).load("https://firebasestorage.googleapis.com/v0/b/donorq-876ed.appspot.com/o/Events%2F"+mDataAdapter.get(position).getImageName() +".jpg?alt=media")
        .into(holder.eventImage);
    }

    @Override
    public int getItemCount() {
        return mDataAdapter.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventName, eventDate, eventAddress;
        public EventViewHolder(View itemView) {
            super(itemView);
            eventImage = (ImageView) itemView.findViewById(R.id.event_image);
            eventName = (TextView) itemView.findViewById(R.id.event_name);
            eventDate = (TextView) itemView.findViewById(R.id.event_date);
            eventAddress = (TextView) itemView.findViewById(R.id.event_address);

        }
    }
}
