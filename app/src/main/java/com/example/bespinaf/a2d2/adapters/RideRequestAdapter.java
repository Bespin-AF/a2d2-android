package com.example.bespinaf.a2d2.adapters;

import android.support.annotation.NonNull;
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
import com.example.bespinaf.a2d2.utilities.FormatUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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

                ActivityUtils.navigateWithData(v.getContext(), RideRequestDetails.class, requestIdData, requestData);
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

        private void populateFields(String groupSize, String timeStamp, String gender){
            groupSizeTextView.setText(groupSize);
            timestampTextView.setText(timeStamp);
            genderTextView.setText(gender);
        }
    }

    // Data Source for the RecyclerView
    private String[] requestIds;
    private HashMap<String, Request> requests;

    public RideRequestAdapter(HashMap<String, Request> adapterRequests) {
        requestIds = convertStringSetToSortedArray(adapterRequests.keySet());
        requests = adapterRequests;
    }

    private String[] convertStringSetToSortedArray(Set<String> set){
        List<String> intermediateList = new ArrayList<>(set);
        Collections.sort(intermediateList, String::compareTo);

        return intermediateList.toArray(new String[set.size()]);
    }


    @Override
    public int getItemCount() {
        return requests.size();
    }


    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int index) {
        View view = LayoutInflater.from(viewGroup.getContext())
                                  .inflate(R.layout.card_view_requests, viewGroup, false);

        return new RequestViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder requestViewHolder, int index) {
        String requestId = requestIds[index];
        Request request = requests.get(requestId);

        if(request == null){ return; }

        String groupLabelFormat = "Group Size: %d";

        String groupText = String.format(FormatUtils.DEFAULT_LOCALE, groupLabelFormat, request.getGroupSize());
        String timestamp = FormatUtils.dateToDisplayFormat(request.getTimestamp());
        String gender = request.getGender();

        requestViewHolder.populateFields(groupText, timestamp, gender);
    }
}