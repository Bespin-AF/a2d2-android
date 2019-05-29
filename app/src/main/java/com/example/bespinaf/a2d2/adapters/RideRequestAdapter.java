package com.example.bespinaf.a2d2.adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bespinaf.a2d2.R;
import com.example.bespinaf.a2d2.controllers.Driver_RideRequestDetails;
import com.example.bespinaf.a2d2.models.Request;
import com.example.bespinaf.a2d2.utilities.ActivityUtils;
import com.example.bespinaf.a2d2.utilities.FormatUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//Adapts request objects for use in a recycler view
public class RideRequestAdapter extends RecyclerView.Adapter<RideRequestAdapter.RequestViewHolder> {

    // Data source

    private Request[] requests;

    public RideRequestAdapter(Request[] adapterRequests) {
        requests = sortArray(adapterRequests);
    }


    private Request[] sortArray(Request[] arr){
        List tempList = Arrays.asList(arr);
        Collections.sort(tempList);
        return (Request[]) tempList.toArray();
    }


    @Override
    public int getItemCount() {
        return requests.length;
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
        Request request = requests[index];

        if(request == null){ return; }

        String groupText = FormatUtils.formatString("Group Size: %d", request.getGroupSize());
        String timestamp = FormatUtils.dateToDisplayFormat(request.getTimestamp());
        String gender = request.getGender();

        requestViewHolder.populateFields(groupText, timestamp, gender);
    }

    public void SortByDate(){
        List<Request> temp = Arrays.asList(requests);
        Collections.sort(temp, (first, second)-> first.getTimestamp().compareTo(second.getTimestamp()));
        requests = temp.toArray(new Request[temp.size()]);
    }

    //RecyclerView Cell Template
    public class RequestViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout viewLayout;
        TextView groupSizeTextView;
        TextView genderTextView;
        TextView timestampTextView;
        View.OnClickListener navigateToRideRequestDetails = (view) -> {
            int selectedIndex = ((RecyclerView) view.getParent()).getChildAdapterPosition(view);
            Request request = requests[selectedIndex];

            Pair<String, Serializable> requestData = new Pair<>("request", request);

            ActivityUtils.navigateWithData(view.getContext(), Driver_RideRequestDetails.class, requestData);
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

}