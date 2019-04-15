package com.example.bespinaf.a2d2.controllers;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.bespinaf.a2d2.R;
import com.example.bespinaf.a2d2.adapters.RideRequestAdapter;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;
import com.example.bespinaf.a2d2.models.Request;

import java.util.HashMap;

import butterknife.BindView;

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
    }


    @Override
    protected void onResume(){
        super.onResume();
        DataSourceUtils.startRequestSync(this::refreshRecyclerViews);
    }


    @Override
    protected void onPause(){
        super.onPause();
        DataSourceUtils.stopRequestSync();
    }


    //At this time, only available, in progress, or completed job requests will be displayed - 11Apr2019
    private void refreshRecyclerViews() {
        HashMap<String, Request> listAvailable = DataSourceUtils.getRequestsWithStatus("Available");
        HashMap<String, Request> listInProgress = DataSourceUtils.getRequestsWithStatus("In Progress");
        HashMap<String, Request> listCompleted = DataSourceUtils.getRequestsWithStatus("Completed");

        populateRecyclerView(rideRequestsAvailableRecyclerView, listAvailable);
        populateRecyclerView(rideRequestsInProgressRecyclerView, listInProgress);
        populateRecyclerView(rideRequestsCompletedRecyclerView, listCompleted);
    }


    //TODO: Do headers and data layout differently, using Tabs instead of one long list
    public void populateRecyclerView(RecyclerView view, HashMap<String, Request> list){
        RideRequestAdapter adapter = new RideRequestAdapter(list);

        LinearLayoutManager llmRequestManager = new LinearLayoutManager(this);
        view.setLayoutManager(llmRequestManager);
        view.setHasFixedSize(true);

        //Divider line between items in recycler view
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                view.getContext(),
                llmRequestManager.getOrientation()
        );

        Drawable listItemDivider = getDrawable(R.drawable.ride_requests_divideritemdecoration);
        if(listItemDivider != null){ dividerItemDecoration.setDrawable(listItemDivider); }

        view.addItemDecoration(dividerItemDecoration);
        view.setAdapter(adapter);
    }
}
