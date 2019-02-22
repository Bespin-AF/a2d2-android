package com.example.bespinaf.a2d2.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bespinaf.a2d2.R;
import com.example.bespinaf.a2d2.controllers.RideRequests;
import com.example.bespinaf.a2d2.models.Request;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;

import java.util.List;

import butterknife.BindView;


//Adapts a request objects for use in a recycler view
public class RideRequestAdapter extends RecyclerView.Adapter<RideRequestAdapter.RequestViewHolder> {

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        //Incompatible with ButterKnife for an unknown reason; will return null if set with ButterKnife
        RelativeLayout viewLayout;
        TextView groupSizeTextView;
        TextView genderTextView;
        TextView timestampTextView;

        RequestViewHolder(View itemView) {
            super(itemView);
            groupSizeTextView = itemView.findViewById(R.id.card_view_group_size_text_view);
            viewLayout = itemView.findViewById(R.id.item_ride_request);
            genderTextView = itemView.findViewById(R.id.card_view_gender_text_view);
            timestampTextView = itemView.findViewById(R.id.card_view_timestamp_text_view);
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


    //May be able to refine this later with a deeper understanding of what's happening
    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup viewGroup, int index) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.card_view_requests, viewGroup, false);
        RequestViewHolder inflatedViewHolder = new RequestViewHolder(view);
        return inflatedViewHolder;
    }


    @Override
    public void onBindViewHolder(RequestViewHolder requestViewHolder, int index) {
        Request request = requests.get(index);
        String timestamp = DataSourceUtils.dateToDisplayFormat(request.getTimestamp());

        requestViewHolder.groupSizeTextView.setText("Group Size: " + request.getGroupSize());
        requestViewHolder.genderTextView.setText(request.getGender());
        requestViewHolder.timestampTextView.setText(timestamp);
    }
}