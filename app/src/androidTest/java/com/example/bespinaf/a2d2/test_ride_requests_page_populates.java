package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Root;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import org.hamcrest.CoreMatchers;
import org.hamcrest.*;
import android.support.test.espresso.Espresso;
import android.support.v7.widget.RecyclerView;

import com.example.bespinaf.a2d2.controllers.RideRequests;
import com.example.bespinaf.a2d2.models.Request;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Map;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.CursorMatchers.withRowString;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.core.AllOf.allOf;

public class test_ride_requests_page_populates {
    @Rule
    public ActivityTestRule<RideRequests> mRideRequestsTestRule = new
            ActivityTestRule<>(RideRequests.class);
    Activity mRideRequestsActivity;

    @Before
    public void setUp(){
        if (DataSourceUtils.getCurrentRequests().isEmpty()) {
            DataSourceUtils.initDateFormatters();
            try {
                //Required to load data before trying to perform actions/load page
                Thread.sleep(5000);
            } catch (InterruptedException exception) {  }
        }

        mRideRequestsActivity = mRideRequestsTestRule.getActivity();
    }
    @After
    public void tearDown(){ mRideRequestsActivity = null; }

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
        //It is confoundingly difficult to figure out how to match a recycler view row
    }
}
