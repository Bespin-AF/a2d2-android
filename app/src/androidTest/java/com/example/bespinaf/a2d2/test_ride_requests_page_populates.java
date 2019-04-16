package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.example.bespinaf.a2d2.controllers.RideRequests;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;
import com.example.bespinaf.a2d2.utilities.FormatUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

public class test_ride_requests_page_populates {
    @Rule
    public ActivityTestRule<RideRequests> mRideRequestsTestRule = new ActivityTestRule<>(RideRequests.class);
    Activity mRideRequestsActivity;

    @Before
    public void setUp(){
        if (DataSourceUtils.getCurrentRequests().isEmpty()) {
            FormatUtils.initializeDateFormatters();
            try {
                //Required to load data before trying to perform actions/load page
                Thread.sleep(5000);
            } catch (InterruptedException exception) {  }
        }

        mRideRequestsActivity = mRideRequestsTestRule.getActivity();
    }


    @After
    public void tearDown(){
        mRideRequestsActivity = null;
    }


    @Test
    public void activityDidLoad(){
        Assert.assertNotNull(mRideRequestsActivity);
    }


    @Test
    public void recyclerViewsHaveTitles(){
        onView(allOf(ViewMatchers.withText(R.string.request_ride_available),
                ViewMatchers.withText(R.string.request_ride_in_progress),
                ViewMatchers.withText(R.string.request_ride_completed)));
    }


    @Test
    public void recyclerViewsDoExist(){
        onView(allOf(withId(R.id.ride_requests_available_recycler_view),
                withId(R.id.ride_requests_in_progress_recycler_view),
                withId(R.id.ride_requests_completed_recycler_view)));
    }


    @Test
    public void requestsDidPopulate(){
        //TODO: Resolve how to perform checks against recyclerview items. Also, due to data and how this page works, it's possible that there are 0 requests
    }
}
