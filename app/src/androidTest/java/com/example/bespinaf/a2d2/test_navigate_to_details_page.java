package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;

import com.example.bespinaf.a2d2.controllers.DriverLogin;
import com.example.bespinaf.a2d2.controllers.MainActivity;
import com.example.bespinaf.a2d2.controllers.RideRequestDetails;
import com.example.bespinaf.a2d2.controllers.RideRequests;
import com.example.bespinaf.a2d2.models.Request;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;
import com.example.bespinaf.a2d2.utilities.IndexedHashMap;

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
    private Activity mRideRequestDetailsActivity;
    private Instrumentation mInstrumentation;
    private Instrumentation.ActivityMonitor mRequestDetailsMonitor;
    private String requestID;


    @Before
    public void setUp() {
        //Sets request ID for the test
        if (DataSourceUtils.getCurrentRequests().isEmpty()) {
            DataSourceUtils.initDateFormatters();
            try {
                //Required to load data before trying to perform actions/load page
                Thread.sleep(5000);
                requestID = DataSourceUtils.getCurrentRequests().get(0).getKey();
            } catch (InterruptedException exception) {  }
        } else {
            requestID = DataSourceUtils.getCurrentRequests().get(0).getKey();
        }

        mActivity = mRideRequestsActivity.getActivity();
        mInstrumentation = getInstrumentation();
        mRequestDetailsMonitor = mInstrumentation.addMonitor(RideRequestDetails.class.getName(), null, false);

    }

    //TODO: Write the test

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
        Thread.sleep(10000);
        onView(withId(R.id.ride_requests_available_recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));

    }
}
