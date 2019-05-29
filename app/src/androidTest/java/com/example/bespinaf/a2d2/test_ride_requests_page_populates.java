package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.design.widget.TabLayout;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.view.ViewPager;

import com.example.bespinaf.a2d2.controllers.Driver_RideRequestDetails;
import com.example.bespinaf.a2d2.controllers.Driver_RideRequestList;
import com.example.bespinaf.a2d2.models.Request;
import com.example.bespinaf.a2d2.models.RequestStatus;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;
import com.example.bespinaf.a2d2.utilities.FormatUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

public class test_ride_requests_page_populates {
    @Rule
    public ActivityTestRule<Driver_RideRequestList> mRideRequestsTestRule = new ActivityTestRule<>(Driver_RideRequestList.class);
    Activity mRideRequestsActivity;
    Instrumentation.ActivityMonitor mRideRequestDetailsMonitor;
    Instrumentation mInstrumentation;

    @Before
    public void setUp(){

        FormatUtils.initializeDateFormatters();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }

        mRideRequestsActivity = mRideRequestsTestRule.getActivity();
        mInstrumentation = getInstrumentation();
        mRideRequestDetailsMonitor = mInstrumentation.addMonitor(Driver_RideRequestDetails.class.getName(), null, false);
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
        onView(allOf(withId(R.id.ride_requests_recyclerview)));
    }


    @Test
    public void requestsDidPopulate(){
        //TODO: Resolve how to perform checks against recyclerview items. Also, due to data and how this page works, it's possible that there are 0 requests
    }

    @Test
    public void completedRideRequest_RequestListIsUpdated() throws InterruptedException {
        //Match details: Group Size, Date, Gender
        Request request = buildRideRequest();
        DataSourceUtils.requests.sendData(request.getData());

        onView(withText((RequestStatus.InProgress.toString())))
                .perform(click());

        onView(  allOf(withId(R.id.ride_requests_recyclerview), isCompletelyDisplayed()) )
                .check(matches(isCompletelyDisplayed()))
                .perform( RecyclerViewActions.actionOnItemAtPosition(0, click()));


        Activity rideRequestDetailsActivity = mRideRequestDetailsMonitor.waitForActivityWithTimeout(1000);
            if(rideRequestDetailsActivity != null){
                onView(withText(request.getStatus().toString()));
                onView(withText(request.getName()));
                onView(withText(request.getGender()));
                onView(withText(request.getGroupSize()));
                onView(withText(request.getPhone()));
                onView(withText(request.getRemarks()));
        } else {
                assert(false);
        }


            onView(withText("Complete Job"))
                    .perform(click());


            onView(withText("CONFIRM"))
                    .perform(click());


            onView(withText(RequestStatus.Completed.toString()))
                    .perform(click());


        onView(  allOf(withId(R.id.ride_requests_recyclerview), isCompletelyDisplayed()) )
                .check(matches(isCompletelyDisplayed()))
                .perform( RecyclerViewActions.actionOnItemAtPosition(0, click()));


        if(rideRequestDetailsActivity != null){
            onView(withText(request.getStatus().toString()));
            onView(withText(request.getName()));
            onView(withText(request.getGender()));
            onView(withText(request.getGroupSize()));
            onView(withText(request.getPhone()));
            onView(withText(request.getRemarks()));
        } else {
            assert(false);
        }
    }

    private Request buildRideRequest() {
        Request rideRequest = new Request();

        rideRequest.setGroupSize(1);
        rideRequest.setTimestamp("1970-01-01 14:16:10 +0000");
        rideRequest.setGender("Male");
        rideRequest.setName("John Doe");
        rideRequest.setPhone("5555555555");
        rideRequest.setRemarks("Test Remarks");
        rideRequest.setStatus(RequestStatus.InProgress);
        rideRequest.setLat(32.368824);
        rideRequest.setLon(-86.270966);
        rideRequest.setDriver("zqu4XosYhgTn5oZy9djN6LL5RUj1");//Corresponds to cooldriver@realandroidemail.com

        return rideRequest;
    }
}
