package com.example.bespinaf.a2d2.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.bespinaf.a2d2.R;
import com.example.bespinaf.a2d2.controllers.Driver_RideRequestList;
import com.example.bespinaf.a2d2.controllers.fragments.Driver_RideRequestListFragment;
import com.example.bespinaf.a2d2.models.Request;
import com.example.bespinaf.a2d2.models.RequestStatus;

import java.util.ArrayList;

public class RideRequestListFragmentAdapter extends FragmentPagerAdapter {
    //It appears that FragmentStatePagerAdapter works, and is meant for each page to be recreated
    //whereas FragmentPagerAdapter is meant for unchanging data
    //When using it, you can removec notifydatasetchanged and getitemposition since it automatically recreates fragments
    private Request[] requests = new Request[0];
    private Context mContext;

    public RideRequestListFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public RideRequestListFragmentAdapter(Context context, FragmentManager fm){
        super(fm);
        mContext = context;
    }


    public void updateRequests(Request[] requests){
        this.requests = requests;
        notifyDataSetChanged();
    }
/*
    @Override
    public void notifyDataSetChanged() {

    }*/

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
                return getRequestListFragment(availableRequests);
            case 1:
                Request[] inProgressRequests = getRequestsWithStatus(RequestStatus.InProgress);
                return getRequestListFragment(inProgressRequests);
            case 2:
                Request[] completedRequests = getRequestsWithStatus(RequestStatus.Completed);
                return getRequestListFragment(completedRequests);
            default:
                return null;
        }
    }

    private Driver_RideRequestListFragment getRequestListFragment(Request[] data){
        Driver_RideRequestListFragment listFragment = new Driver_RideRequestListFragment();
        Bundle requestData = new Bundle();

        requestData.putSerializable("requests", data);
        listFragment.setArguments(requestData);

        return listFragment;
    }


    private Request[] getRequestsWithStatus(RequestStatus status) {
        ArrayList<Request> results = new ArrayList<>();
        for (Request currentRequest : requests) {
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

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.request_ride_available);
        } else if (position == 1) {
            return mContext.getString(R.string.request_ride_in_progress);
        } else if (position == 2) {
            return mContext.getString(R.string.request_ride_completed);
        } else {
            return "";
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }
}
