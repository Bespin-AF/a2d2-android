package com.example.bespinaf.a2d2;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.test.rule.ActivityTestRule;

import com.example.bespinaf.a2d2.controllers.RideRequestDetails;
import com.example.bespinaf.a2d2.models.Request;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;

import org.hamcrest.core.IsNot;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;

import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class test_driver_takes_job {


    @Rule
    public ActivityTestRule<RideRequestDetails> mRideRequestDetailsActivity = new ActivityTestRule<>(RideRequestDetails.class,false,false);

    private RideRequestDetails mActivity;
    private Instrumentation mInstrumentation;
    private MaterialButton mButtonTakeJob;
    private Request request;


    @Before
    public void setUp() {
        DataSourceUtils.initializeDateFormatters();
        request = buildRideRequest();

        Intent intent = new Intent();
        intent.putExtra("request", request);

        mActivity = mRideRequestDetailsActivity.launchActivity(intent);
        mInstrumentation = getInstrumentation();
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



    @After
    public void tearDown(){
        mActivity = null;
        mInstrumentation = null;
    }


    @Test
    public void isTakeJobButtonThere(){
        mButtonTakeJob = mActivity.findViewById(R.id.materialbutton_riderequestdetails_jobaction);
        assertNotNull(mButtonTakeJob);
    }


    @Test
    public void didPopupAppear(){
        mButtonTakeJob = mActivity.findViewById(R.id.materialbutton_riderequestdetails_jobaction);

        getInstrumentation().runOnMainSync(()->{
            mButtonTakeJob.performClick();
                }
        );

        onView(withText(R.string.dialog_confirm))
                .inRoot(withDecorView(IsNot.not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        onView(withText(R.string.cancel))
                .inRoot(withDecorView(IsNot.not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

}
