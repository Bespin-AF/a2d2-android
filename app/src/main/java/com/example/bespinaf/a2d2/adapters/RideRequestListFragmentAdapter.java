package com.example.bespinaf.a2d2.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.bespinaf.a2d2.controllers.fragments.Driver_RideRequestListFragment;
import com.example.bespinaf.a2d2.models.Request;
import com.example.bespinaf.a2d2.models.RequestStatus;

import java.util.ArrayList;

public class RideRequestListFragmentAdapter extends FragmentStatePagerAdapter {
    private Request[] requests;

    public RideRequestListFragmentAdapter(FragmentManager fm, Request[] requests) {
        super(fm);
        this.requests = requests;
    }


    /*
    * Tab 0: Available
    * Tab 1: In Progress
    * Tab 2: Completed
    * */
    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                Request[] availableRequests = getRequestsWithStatus(RequestStatus.Available);
                Driver_RideRequestListFragment availableRequestsList = new Driver_RideRequestListFragment();
                Bundle requestData = new Bundle();
                requestData.putSerializable("requests", availableRequests);
                availableRequestsList.setArguments(requestData);

                return availableRequestsList;
            case 1:
                return new Driver_RideRequestListFragment();
            case 2:
                return new Driver_RideRequestListFragment();
            default:
                return null;
        }
    }

    private Driver_RideRequestListFragment getRequestListFragment(Request[] data){
        
    }


    private Request[] getRequestsWithStatus(RequestStatus status) {
        ArrayList<Request> results = new ArrayList<>();
        for (Request  currentRequest : requests) {
            if (currentRequest.getStatus() == status) {
                results.add(currentRequest);
            }
        }

        return results.toArray(new Request[results.size()]);
    }


    @Override
    public int getCount() {
        return 3;
    }
}
