package com.example.bespinaf.a2d2;

import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class test_driver_enters_email_and_password {
    @Rule
    public ActivityTestRule<DriverLogin> mDriverLoginActivity = new ActivityTestRule<>(DriverLogin.class);

    private DriverLogin mActivity;
    private Instrumentation mInstrumentation;
    private Instrumentation.ActivityMonitor mRideRequestsMonitor;

    @Before
    public void setUp(){
        mActivity = mDriverLoginActivity.getActivity();
        mInstrumentation = getInstrumentation();
        mRideRequestsMonitor = mInstrumentation.addMonitor(Rules.class.getName(),null,false);
    }

    @After
    public void tearDown(){
        mInstrumentation.removeMonitor(mRideRequestsMonitor);
    }
}
