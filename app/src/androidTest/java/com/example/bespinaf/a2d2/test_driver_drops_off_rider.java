package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.util.Pair;

import com.example.bespinaf.a2d2.controllers.RideRequestDetails;
import com.example.bespinaf.a2d2.controllers.RideRequests;
import com.example.bespinaf.a2d2.models.Request;
import com.example.bespinaf.a2d2.utilities.AuthorizationUtils;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;

import org.hamcrest.core.IsNot;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.Serializable;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;

public class test_driver_drops_off_rider {
    @Rule
    public ActivityTestRule<RideRequestDetails> mRideRequestsActivity = new ActivityTestRule<>(RideRequestDetails.class, false, false);

    private RideRequestDetails mActivity;
    private Activity mRideRequests;
    private Instrumentation mInstrumentation;
    private Instrumentation.ActivityMonitor mRideRequestsMonitor;
    private String requestID;


    @Before
    public void setUp() {
        if (DataSourceUtils.getCurrentRequests().isEmpty()) {
            DataSourceUtils.initDateFormatters();
            try {
                //Required to load data before trying to perform actions/load page
                Thread.sleep(5000);
            } catch (InterruptedException exception) {  }
        }

        Intent initialData = new Intent();
        initialData.putExtra("requestId", "test-request");
        initialData.putExtra("request", buildRideRequest());

        mActivity = mRideRequestsActivity.launchActivity(initialData);
        mInstrumentation = getInstrumentation();
        mRideRequestsMonitor = mInstrumentation.addMonitor(RideRequests.class.getName(), null, false);
    }


    @After
    public void tearDown(){
        mActivity = null;
        mInstrumentation = null;
        mRideRequestsMonitor = null;
        DataSourceUtils.stopRequestSync();
    }

    private Request buildRideRequest() {
        Request rideRequest = new Request();

        rideRequest.setGroupSize(1);
        rideRequest.setTimestamp("2019-04-30 20:20:31 +1234");
        rideRequest.setGender("Female");
        rideRequest.setName("Jane Doe");
        rideRequest.setPhone("0123456789");
        rideRequest.setRemarks("Test remarks");
        rideRequest.setStatus("In Progress");
        rideRequest.setLat(32.411823);
        rideRequest.setLon(-86.243357);
        rideRequest.setDriver("zqu4XosYhgTn5oZy9djN6LL5RUj1");//Corresponds to cooldriver@realandroidemail.com

        return rideRequest;
    }


    @Test
    public void hasActivityLaunched() {
        assertNotNull(mActivity);
    }


    @Test
    public void completeJob() throws InterruptedException {
        AuthorizationUtils.authorizeUser("cooldriver@realandroidemail.com", "SecurePassword", (wasLoginSuccessful -> { }));


        onView(withId(R.id.materialbutton_riderequestdetails_jobaction)).perform(click());

        onView(withText(R.string.dialog_confirm))
                .inRoot(withDecorView(IsNot.not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        Espresso.onView(withText(R.string.dialog_confirm)).inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());

        //Due to how activity stacks work, this test cannot expect the RideRequests activity since it was directly launched
    }
}
