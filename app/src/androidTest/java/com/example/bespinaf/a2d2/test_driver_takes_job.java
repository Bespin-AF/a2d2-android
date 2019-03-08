package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.example.bespinaf.a2d2.controllers.DriverLogin;
import com.example.bespinaf.a2d2.controllers.RideRequestDetails;
import com.example.bespinaf.a2d2.controllers.RideRequests;
import com.example.bespinaf.a2d2.models.Request;
import com.example.bespinaf.a2d2.utilities.IndexedHashMap;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;

import org.hamcrest.core.IsNot;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

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
    private IndexedHashMap mockDatabase;
    private MaterialButton mButtonTakeJob;
    private String requestID;


    @Before
    public void setUp() {
        //Sets request ID for the test
        if (DataSourceUtils.getCurrentRequests().isEmpty()) {
            DataSourceUtils.startRequestSync();
            try {
                //Required to load data before trying to perform actions/load page
                Thread.sleep(3000);
                requestID = DataSourceUtils.getCurrentRequests().get(0).getKey();
            } catch (InterruptedException exception) {  }
        } else {
            requestID = DataSourceUtils.getCurrentRequests().get(0).getKey();
        }

        Intent i = new Intent();
        i.putExtra("request", requestID);

        mActivity = mRideRequestDetailsActivity.launchActivity(i);
        mInstrumentation = getInstrumentation();
    }


    @After
    public void tearDown(){
        mActivity = null;
        mInstrumentation = null;
    }


    @Test
    public void isTakeJobButtonThere(){
        mButtonTakeJob = mActivity.findViewById(R.id.materialbutton_riderequestdetails_takejob);
        assertNotNull(mButtonTakeJob);
    }


    @Test
    public void didPopupAppear(){
        mButtonTakeJob = mActivity.findViewById(R.id.materialbutton_riderequestdetails_takejob);

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
