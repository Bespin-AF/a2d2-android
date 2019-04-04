package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import com.example.bespinaf.a2d2.controllers.RideRequestDetails;
import com.example.bespinaf.a2d2.controllers.RideRequests;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

public class test_navigate_to_details_page {
    @Rule
    public ActivityTestRule<RideRequests> mRideRequestsActivity = new ActivityTestRule<>(RideRequests.class);

    private RideRequests mActivity;
    private Instrumentation mInstrumentation;
    private Instrumentation.ActivityMonitor mRequestDetailsMonitor;


    @Before
    public void setUp() {
        //Sets request ID for the test
        if (DataSourceUtils.getCurrentRequests().isEmpty()) {
            DataSourceUtils.initDateFormatters();
            try {
                //Required to load data before trying to perform actions/load page
                Thread.sleep(5000);
            } catch (InterruptedException exception) {  }
        }

        mActivity = mRideRequestsActivity.getActivity();
        mInstrumentation = getInstrumentation();
        mRequestDetailsMonitor = mInstrumentation.addMonitor(RideRequestDetails.class.getName(), null, false);
    }


    @After
    public void tearDown(){
        mActivity = null;
        mInstrumentation = null;
        mRequestDetailsMonitor = null;
        DataSourceUtils.stopRequestSync();
    }


    @Test
    public void hasActivityLaunched() {
        assertNotNull(mActivity);
    }


    @Test
    public void navigateToDetails() throws InterruptedException{
        onView(withId(R.id.ride_requests_available_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));


        Activity requestDetailsActivity = mInstrumentation.waitForMonitorWithTimeout(mRequestDetailsMonitor, 1000);
        assertNotNull(requestDetailsActivity);
    }
}
