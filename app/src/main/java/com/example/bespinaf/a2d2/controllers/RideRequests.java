package com.example.bespinaf.a2d2.controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.bespinaf.a2d2.R;
import com.example.bespinaf.a2d2.adapters.RideRequestAdapter;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;
import com.example.bespinaf.a2d2.models.Request;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RideRequests extends ButterKnifeActivity {

    @BindView(R.id.ride_requests_available_recycler_view)
    RecyclerView rideRequestsAvailableRecyclerView;
    @BindView(R.id.ride_requests_in_progress_recycler_view)
    RecyclerView rideRequestsInProgressRecyclerView;
    @BindView(R.id.ride_requests_completed_recycler_view)
    RecyclerView rideRequestsCompletedRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_ride_requests);
        DataSourceUtils.setToastContext(this);
        DataSourceUtils.startRequestSync(()-> refreshRecyclerViews());
    }

    private void refreshRecyclerViews() {
        ArrayList<Request> listAvailable = DataSourceUtils.getRequestsWithStatus("Available");
        ArrayList<Request> listInProgress = DataSourceUtils.getRequestsWithStatus("In Progress");
        ArrayList<Request> listCompleted = DataSourceUtils.getRequestsWithStatus("Completed");
        //TODO Handle if there is a status that is not listed

        populateRecyclerView(rideRequestsAvailableRecyclerView, listAvailable);
        populateRecyclerView(rideRequestsInProgressRecyclerView, listInProgress);
        populateRecyclerView(rideRequestsCompletedRecyclerView, listCompleted);
    }


    //TODO Do headers and data layout differently
    public void populateRecyclerView(RecyclerView view, ArrayList<Request> list){
        RideRequestAdapter adapter = new RideRequestAdapter(list);

        LinearLayoutManager llmRequestManager = new LinearLayoutManager(this);
        view.setLayoutManager(llmRequestManager);
        view.setHasFixedSize(true);

        //Sets the dividers between items in the recycler view
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                view.getContext(),
                llmRequestManager.getOrientation());
        view.addItemDecoration(dividerItemDecoration);

        view.setAdapter(adapter);
    }
}
