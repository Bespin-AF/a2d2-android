package com.example.bespinaf.a2d2.controllers.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bespinaf.a2d2.R;
import com.example.bespinaf.a2d2.adapters.RideRequestAdapter;
import com.example.bespinaf.a2d2.adapters.RideRequestListFragmentAdapter;
import com.example.bespinaf.a2d2.controllers.Driver_RideRequestList;
import com.example.bespinaf.a2d2.models.Request;
import com.example.bespinaf.a2d2.models.RequestStatus;

import butterknife.BindView;

public class Driver_RideRequestListFragment extends Fragment {
    Context context;
    Request[] requests;
    RecyclerView requestList;

    public Driver_RideRequestListFragment(){

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){


        View recyclerview_tab = inflater.inflate(R.layout.fragment_ride_request_list, container, false);
        context = container.getContext();
        requestList = recyclerview_tab.findViewById(R.id.ride_requests_recyclerview);

        return recyclerview_tab;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if(getArguments() != null){
            requests = (Request[]) getArguments().getSerializable("requests");
        }

        populateRecyclerView();
    }

    public void populateRecyclerView() {
        RideRequestAdapter adapter = new RideRequestAdapter(requests);

        LinearLayoutManager llmRequestManager = new LinearLayoutManager(context);
        requestList.setLayoutManager(llmRequestManager);
        requestList.setHasFixedSize(true);

        //Divider line between items in recycler view
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                context,
                llmRequestManager.getOrientation()
        );

        Drawable listItemDivider = context.getDrawable(R.drawable.ride_requests_divideritemdecoration);
        if (listItemDivider != null) {
            dividerItemDecoration.setDrawable(listItemDivider);
        }

        requestList.addItemDecoration(dividerItemDecoration);
        requestList.setAdapter(adapter);
    }
}
