package com.example.bespinaf.a2d2.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bespinaf.a2d2.R;
import com.example.bespinaf.a2d2.utilities.Request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RideRequestAdapter extends RecyclerView.Adapter<RideRequestAdapter.RequestViewHolder> {
    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        static RelativeLayout cv;
        static TextView groupSize;
        static TextView gender;
        static TextView timeStamp;

        RequestViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.item_ride_request);
            groupSize = itemView.findViewById(R.id.card_view_group_size_text_view);
            gender = itemView.findViewById(R.id.card_view_gender_text_view);
            timeStamp = itemView.findViewById(R.id.card_view_timestamp_text_view);
        }
    }

    List<Request> requests;

    public RideRequestAdapter(List<Request> adapterRequests){
        requests = adapterRequests;
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup viewGroup, int index) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_requests, viewGroup, false);
        RequestViewHolder pvh = new RequestViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(RequestViewHolder requestViewHolder, int index) {
        requestViewHolder.groupSize.setText("Group Size: " + requests.get(index).getGroupSize());
        requestViewHolder.gender.setText(requests.get(index).getGender());

        String displayDateFormat = "MMM dd, HHmm";
        String databaseDateFormat = "yyyy-MM-dd HH:mm:ss +SSSS";

        SimpleDateFormat displayDateFormatter =  new SimpleDateFormat(displayDateFormat);
        SimpleDateFormat databaseDateFormatter = new SimpleDateFormat(databaseDateFormat);
        String timestamp = "";

        try {
            Date date = databaseDateFormatter.parse(requests.get(index).getTimestamp());
            timestamp = displayDateFormatter.format(date);
        } catch (ParseException pException){
            Log.e("Parse Exception", "Database contains invalid date format");
        }

        requestViewHolder.timeStamp.setText(timestamp);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}