package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import com.example.bespinaf.a2d2.utilities.Request;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;

public class test_driver_messages_rider {
    @Rule
    public ActivityTestRule<RideRequestDetailsActivity> rideRequestDetailsActivityTestRule = new ActivityTestRule<>(RideRequestDetailsActivity.class, true, false);
    Activity mRideDetailsActivity;
    Instrumentation mInstrumentation;

    @Before
    public void setup(){
        mInstrumentation = getInstrumentation();
    }

    @After
    public void teardown(){
        mRideDetailsActivity = null;
        mInstrumentation = null;
    }

    @Test
    public void messageRider(){
        Request rideRequest = new Request(
                "Available",
                1,
                "Male",
                "Airman Scruffy",
                "8435016944",
                "Please pick up at Waffle House",
                "08/05/1994 08:08:04 +1234",
                -84,
                -84);

        Intent intent = new Intent();
        intent.putExtra("rideRequest", rideRequest);

        mRideDetailsActivity = rideRequestDetailsActivityTestRule.launchActivity(intent);

        mInstrumentation.runOnMainSync(()->{
            mRideDetailsActivity.findViewById(R.id.button_message_driver_ride_request_details).performClick();
        });

        assertTrue(true);
    }
}
