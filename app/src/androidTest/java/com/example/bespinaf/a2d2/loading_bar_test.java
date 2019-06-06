package com.example.bespinaf.a2d2;

import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;

import com.example.bespinaf.a2d2.controllers.Driver_Login;
import com.example.bespinaf.a2d2.controllers.Driver_RideRequestList;
import com.example.bespinaf.a2d2.controllers.MainActivity;
import com.example.bespinaf.a2d2.controllers.Rider_RequestRide;
import com.example.bespinaf.a2d2.controllers.Rider_Rules;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class loading_bar_test {

    @Rule
    public ActivityTestRule<Driver_Login> mProgressBarTest = new ActivityTestRule<>(Driver_Login.class);

    private Driver_Login mDriverLoginActivity;
    private Driver_RideRequestList mDriverRideRequestListActivity;
    private Rider_RequestRide mRiderRequestRideActivity;
    private Rider_Rules mRiderRulesActivity;
    private Instrumentation mInstrumentation;

    @Before
    public void setup(){
        mInstrumentation = getInstrumentation();
    }

    @After
    public void teardown(){
        mDriverLoginActivity = null;
        mDriverRideRequestListActivity = null;
        mRiderRequestRideActivity = null;
        mRiderRulesActivity = null;
    }

    @Test
    public void driverLoginTest(){
        onView(withId(R.id.ride_request_progress_bar));
    }
}
