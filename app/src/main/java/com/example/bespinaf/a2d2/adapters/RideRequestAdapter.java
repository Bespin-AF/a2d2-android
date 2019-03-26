package com.example.bespinaf.a2d2.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bespinaf.a2d2.R;
import com.example.bespinaf.a2d2.controllers.RideRequestDetails;
import com.example.bespinaf.a2d2.models.Request;
import com.example.bespinaf.a2d2.utilities.ActivityUtils;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static java.util.Map.Entry;


//Adapts request objects for use in a recycler view
public class RideRequestAdapter extends RecyclerView.Adapter<RideRequestAdapter.RequestViewHolder> {

    //RecyclerView Cell
    public class RequestViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout viewLayout;
        TextView groupSizeTextView;
        TextView genderTextView;
        TextView timestampTextView;
        View.OnClickListener navigateToRideRequestDetails = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedIndex = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
                String requestId = requestIds[selectedIndex];
                Request request = requests.get(requestId);

                Pair<String, Serializable> requestIdData = new Pair<>("requestId", requestId);
                Pair<String, Serializable> requestData = new Pair<>("request", request);

                ActivityUtils.navigateWithData(v.getContext(), RideRequestDetails.class,requestIdData, requestData);
            }
        };

        RequestViewHolder(View itemView) {
            super(itemView);
            viewLayout = itemView.findViewById(R.id.request_card_view_layout);
            genderTextView = itemView.findViewById(R.id.card_view_gender_text_view);
            timestampTextView = itemView.findViewById(R.id.card_view_timestamp_text_view);
            groupSizeTextView = itemView.findViewById(R.id.card_view_group_size_text_view);

            viewLayout.setOnClickListener(navigateToRideRequestDetails);
        }
    }

    // Data Source for the RecyclerView
    String[] requestIds;
    HashMap<String, Request> requests;

    Comparator<String> keyComparator = (firstKey, secondKey) -> {
        return firstKey.compareTo(secondKey);
    };


    public RideRequestAdapter(HashMap<String, Request> adapterRequests) {
        int requestCount = adapterRequests.keySet().size();
        requests = adapterRequests;

        List<String> mRequestIds = new ArrayList<>(adapterRequests.keySet());
        Collections.sort(mRequestIds, keyComparator);

        requestIds = mRequestIds.toArray(new String[requestCount]);
    }


    @Override
    public int getItemCount() {
        return requests.size();
    }


    //May be able to refine this later with a deeper understanding of what's happening

    //Note: Check out ViewGroup
    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup viewGroup, int index) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.card_view_requests, viewGroup, false);
        return new RequestViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RequestViewHolder requestViewHolder, int index) {
        String requestId = requestIds[index];
        Request request = requests.get(requestId);
        String timestamp = DataSourceUtils.dateToDisplayFormat(request.getTimestamp());

        requestViewHolder.groupSizeTextView.setText("Group Size: " + request.getGroupSize());
        requestViewHolder.genderTextView.setText(request.getGender());
        requestViewHolder.timestampTextView.setText(timestamp);
    }
}