package com.example.bespinaf.a2d2;

import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;

import com.example.bespinaf.a2d2.controllers.Driver_Login;
import com.example.bespinaf.a2d2.controllers.Driver_RideRequestList;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class test_driver_profile {
    @Rule
    public ActivityTestRule<Driver_Login> mDriverLoginActivity = new ActivityTestRule<>(Driver_Login.class);

    private Driver_Login mActivity;

    @Before
    public void setUp(){
        mActivity = mDriverLoginActivity.getActivity();

    }


    @After
    public void tearDown(){
        mActivity = null;

    }

    @Test
    public void driverNavigationHamburgerExists() {
        Espresso.onView(withId(R.id.driverNavigationHamburger)).check(ViewAssertions.matches(isCompletelyDisplayed()));
    }

}
