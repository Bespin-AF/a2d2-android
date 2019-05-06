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

    @BindView(R.id.ride_requests_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.ride_requests_tab_layout)
    TabLayout mTabLayout;

    RideRequestListFragmentAdapter requestListFragmentAdapter;
    private Request[] rideRequests = new Request[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_ride_requests);

        requestListFragmentAdapter = new RideRequestListFragmentAdapter(this, getSupportFragmentManager());
    }

    @Override
    protected void onStart() {
        super.onStart();
        DataSourceUtils.requests.setReciever(this);
    }

    @Override
    public void onDataChanged(DataSource dataSource, HashMap<String, Object> data) {
        rideRequests = DataSourceUtils.requestsFromData(data);
        refreshRecyclerViews();
    }


    private void refreshRecyclerViews() {
        requestListFragmentAdapter.updateRequests(rideRequests);
        mViewPager.setAdapter(requestListFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }




}
