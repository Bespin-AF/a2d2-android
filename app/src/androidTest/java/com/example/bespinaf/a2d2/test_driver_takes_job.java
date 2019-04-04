package com.example.bespinaf.a2d2;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import com.example.bespinaf.a2d2.controllers.RideRequestDetails;
import com.example.bespinaf.a2d2.models.Request;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class test_driver_takes_job {


    @Rule
    public ActivityTestRule<RideRequestDetails> mRideRequestDetailsActivity = new ActivityTestRule<>(RideRequestDetails.class,false,false);

    private RideRequestDetails mActivity;
    private Request request;


    @Before
    public void setUp() {
        DataSourceUtils.initDateFormatters();
        request = buildRideRequest();

        Intent intent = new Intent();
        intent.putExtra("request", request);

        mActivity = mRideRequestDetailsActivity.launchActivity(intent);
    }


    @After
    public void tearDown(){
        mActivity = null;
        request = null;
    }


    private Request buildRideRequest() {
        Request rideRequest = new Request();

        rideRequest.setGroupSize(1);
        rideRequest.setTimestamp(DataSourceUtils.getCurrentDateString());
        rideRequest.setGender("Male");
        rideRequest.setName("John Doe");
        rideRequest.setPhone("1234554321");
        rideRequest.setRemarks("Test Remarks");
        rideRequest.setStatus("Available");
        rideRequest.setLat(32.368824);
        rideRequest.setLon(-86.270966);

        return rideRequest;
    }


    @Test
    public void isTakeJobButtonThere(){
        onView(withId(R.id.materialbutton_riderequestdetails_jobaction));
    }


    @Test
    public void didPopupAppear(){
        onView(withId(R.id.materialbutton_riderequestdetails_jobaction)).perform(click());

        onView(withText(mActivity.getString(R.string.riderequestdetails_driver_takejob)))
                .inRoot(isDialog())
                .perform(click());
    }
}
