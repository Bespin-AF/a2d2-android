package af.bespin.a2d2.adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import af.bespin.a2d2.R;
import af.bespin.a2d2.controllers.Driver_RideRequestDetails;
import af.bespin.a2d2.models.Request;
import af.bespin.a2d2.utilities.ActivityUtils;
import af.bespin.a2d2.utilities.FormatUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//Adapts request objects for use in a recycler view
public class RideRequestAdapter extends RecyclerView.Adapter<RideRequestAdapter.RequestViewHolder> {

    // Data source

    private Request[] mRequests;

    public RideRequestAdapter(Request[] adapterRequests) {
        mRequests = adapterRequests;
    }


    @Override
    public int getItemCount() {
        return mRequests.length;
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
        Request request = mRequests[index];

        if(request == null){ return; }

        String groupText = FormatUtils.formatString("Group Size: %d", request.getGroupSize());
        String timestamp = FormatUtils.dateToDisplayFormat(request.getTimestamp());
        String gender = request.getGender();

        requestViewHolder.populateFields(groupText, timestamp, gender);
    }

    public void SortByDateOldToNew(){
        List<Request> temp = Arrays.asList(mRequests);
        Collections.sort(temp, (first, second)-> first.getTimestamp().compareTo(second.getTimestamp()));
        mRequests = temp.toArray(new Request[temp.size()]);
    }

    public void sortByDate(List<Request> requests, boolean sortByOldest){
        Comparator<Request> oldestToNewest = (first, second) ->
           first.getTimestamp().compareTo(second.getTimestamp());

        Comparator<Request> newestToOldest = (first, second) ->
           second.getTimestamp().compareTo(first.getTimestamp());

        if(sortByOldest) {
            Collections.sort(requests, oldestToNewest);
        } else {
            Collections.sort(requests, newestToOldest);
        }
    }

    //RecyclerView Cell Template
    public class RequestViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout viewLayout;
        TextView groupSizeTextView;
        TextView genderTextView;
        TextView timestampTextView;
        View.OnClickListener navigateToRideRequestDetails = (view) -> {
            ActivityUtils.navigateWithData(view.getContext(), Driver_RideRequestDetails.class, getRequestData(view));
        };

        private Pair<String, Serializable> getRequestData(View view){
            int selectedIndex = ((RecyclerView) view.getParent()).getChildAdapterPosition(view);
            Request request = mRequests[selectedIndex];
            return new Pair<>("request", request);
        }


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