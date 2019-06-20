package af.bespin.a2d2.controllers;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;

import af.bespin.a2d2.R;
import af.bespin.a2d2.adapters.RideRequestListFragmentAdapter;
import af.bespin.a2d2.models.DataReceiver;
import af.bespin.a2d2.models.DataSource;
import af.bespin.a2d2.utilities.DataSourceUtils;
import af.bespin.a2d2.models.Request;

import java.util.HashMap;

import butterknife.BindView;

public class Driver_RideRequestList extends ButterKnifeActivity implements DataReceiver {

    @BindView(R.id.ride_requests_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.ride_requests_tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.request_tabs_loading_bar)
    ProgressBar mLoadingBar;

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
        mLoadingBar.setVisibility(View.VISIBLE);
        DataSourceUtils.requests.setReciever(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        DataSourceUtils.requests.removeReceiver(this);
    }

    @Override
    public void onDataChanged(DataSource dataSource, HashMap<String, Object> data) {
        mLoadingBar.setVisibility(View.VISIBLE);
        rideRequests = DataSourceUtils.requestsFromData(data);
        refreshRecyclerViews();
        mLoadingBar.setVisibility(View.INVISIBLE);
    }


    private void refreshRecyclerViews() {
        requestListFragmentAdapter.updateRequests(rideRequests);
        mViewPager.setAdapter(requestListFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
