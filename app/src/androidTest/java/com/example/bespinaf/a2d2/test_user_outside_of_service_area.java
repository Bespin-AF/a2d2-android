package com.example.bespinaf.a2d2;

import android.app.Instrumentation;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;

import com.example.bespinaf.a2d2.controllers.RequestRide;
import com.example.bespinaf.a2d2.controllers.Rules;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class test_user_outside_of_service_area {
    @Rule
    public ActivityTestRule<Rules> mRuleActivityRule = new ActivityTestRule<>(Rules.class);


    private Rules mActivity;
    private Instrumentation mInstrumentation;
    LocationManager lm;

    @Before
    public void setUp() throws InterruptedException {
        DataSourceUtils.loadA2D2_BaseLocation(null);
        DataSourceUtils.loadA2D2_PhoneNumber(null);
        Thread.sleep(1000);
        mActivity = mRuleActivityRule.getActivity();
        mInstrumentation = getInstrumentation();
        lm = (LocationManager) mActivity.getSystemService(
                Context.LOCATION_SERVICE);
        lm.addTestProvider (LocationManager.GPS_PROVIDER,
                "requiresNetwork" == "",
                "requiresSatellite" == "",
                "requiresCell" == "",
                "hasMonetaryCost" == "",
                "supportsAltitude" == "",
                "supportsSpeed" == "",
                "supportsBearing" == "",
                android.location.Criteria.POWER_LOW,
                android.location.Criteria.ACCURACY_FINE);

        Location testLocation = buildTestLocation(0,0);
        lm.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
        lm.setTestProviderStatus(LocationManager.GPS_PROVIDER,
                LocationProvider.AVAILABLE,
                null,System.currentTimeMillis());
        lm.setTestProviderLocation(LocationManager.GPS_PROVIDER, testLocation);
    }

    @After
    public void tearDown(){
        mActivity = null;
        mInstrumentation = null;
        lm.removeTestProvider(LocationManager.GPS_PROVIDER);
        lm = null;
    }

    public Location buildTestLocation(long latitude, long longitude){
        Location newLocation = new Location(LocationManager.GPS_PROVIDER);

        newLocation.setLatitude(latitude);
        newLocation.setLongitude(longitude);
        newLocation.setAccuracy(1);
        newLocation.setAltitude(10);
        newLocation.setTime(System.currentTimeMillis());
        newLocation.setElapsedRealtimeNanos(System.currentTimeMillis());
        newLocation.setVerticalAccuracyMeters(1);
        newLocation.setSpeedAccuracyMetersPerSecond(1);
        newLocation.setBearingAccuracyDegrees(10);

        return newLocation;
    }

    @Test
    public void riderOutOfRange_ErrorPopUpAppears(){
        onView(withId(R.id.button_rules_agree)).perform(click());

        onView(withText("You are outside of the 25 mile range defined by the A2D2 program rules. If you still require a ride, please call A2D2 Dispatch at (334) 953-2233"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }
}
