package com.example.bespinaf.a2d2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.bespinaf.a2d2.adapters.RideRequestAdapter;
import com.example.bespinaf.a2d2.utilities.Request;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RideRequests extends AppCompatActivity {

    @BindView(R.id.ride_requests_available_recycler_view)
    RecyclerView rideRequestsAvailableRecyclerView;
    @BindView(R.id.ride_requests_in_progress_recycler_view)
    RecyclerView rideRequestsInProgressRecyclerView;
    @BindView(R.id.ride_requests_completed_recycler_view)
    RecyclerView rideRequestsCompletedRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_requests);
        ButterKnife.bind(this);

        rideRequestsAvailableRecyclerView.setHasFixedSize(true);
        rideRequestsCompletedRecyclerView.setHasFixedSize(true);
        rideRequestsInProgressRecyclerView.setHasFixedSize(true);
        setRecyclerViews();
    }

    private void setRecyclerViews() {
        ArrayList<Request> ourRequests = DataSourceUtils.getCurrentRequests();
        ArrayList<Request> listAvailable = getRequestsWithStatus("Available", ourRequests);
        ArrayList<Request> listInProgress = getRequestsWithStatus("In Progress", ourRequests);
        ArrayList<Request> listCompleted = getRequestsWithStatus("Completed", ourRequests);
        //TODO Handle if there is a status that is not listed

        //inflate the recycler views
        inflateRecyclerView(rideRequestsAvailableRecyclerView, listAvailable);
        inflateRecyclerView(rideRequestsInProgressRecyclerView, listInProgress);
        inflateRecyclerView(rideRequestsCompletedRecyclerView, listCompleted);
    }

    private ArrayList<Request> getRequestsWithStatus(String targetStatus,ArrayList<Request> allRequests){
        ArrayList<Request> targetRequests = new ArrayList<>();
        for (Request request : allRequests) {
            if (request.getStatus() == targetStatus) targetRequests.add(request);
        }
        return targetRequests;
    }

    private void refreshData(){
        setRecyclerViews();
    }

    public void inflateRecyclerView(RecyclerView view, ArrayList<Request> list){
        RideRequestAdapter adapter = new RideRequestAdapter(list);
        LinearLayoutManager llmRequestManager = new LinearLayoutManager(this);
        view.setLayoutManager(llmRequestManager);

        //Sets the dividers between items in the recycler view
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                view.getContext(),
                llmRequestManager.getOrientation());
        view.addItemDecoration(dividerItemDecoration);

        view.setAdapter(adapter);
    }
}
