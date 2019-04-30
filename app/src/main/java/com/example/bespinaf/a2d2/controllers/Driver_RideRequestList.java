package com.example.bespinaf.a2d2.controllers;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.bespinaf.a2d2.R;
import com.example.bespinaf.a2d2.adapters.RideRequestAdapter;
import com.example.bespinaf.a2d2.adapters.RideRequestListFragmentAdapter;
import com.example.bespinaf.a2d2.models.DataReceiver;
import com.example.bespinaf.a2d2.models.DataSource;
import com.example.bespinaf.a2d2.models.RequestStatus;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;
import com.example.bespinaf.a2d2.models.Request;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

public class Driver_RideRequestList extends ButterKnifeActivity implements DataReceiver {

    @BindView(R.id.ride_requests_available_recycler_view)
    RecyclerView rideRequestsAvailableRecyclerView;
    @BindView(R.id.ride_requests_in_progress_recycler_view)
    RecyclerView rideRequestsInProgressRecyclerView;
    @BindView(R.id.ride_requests_completed_recycler_view)
    RecyclerView rideRequestsCompletedRecyclerView;
    @BindView(R.id.ride_requests_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.ride_requests_tab_layout)
    TabLayout mTabLayout;

    private Request[] rideRequests = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_ride_requests);

        RideRequestListFragmentAdapter requestListFragmentAdapter = new RideRequestListFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(requestListFragmentAdapter);

        mTabLayout.setupWithViewPager(mViewPager, true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DataSourceUtils.requests.setReciever(this);
    }

    @Override
    public void onDataChanged(DataSource dataSource, HashMap<String, Object> data) {
        rideRequests = DataSourceUtils.requestsFromData(data);
        //refreshRecyclerViews();
    }


    /*private void refreshRecyclerViews() {
        Request[] listAvailable = getRequestsWithStatus(RequestStatus.Available);
        Request[] listInProgress = getRequestsWithStatus(RequestStatus.InProgress);
        Request[] listCompleted = getRequestsWithStatus(RequestStatus.Completed);
    }*/




}
