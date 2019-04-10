package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.widget.Button;

import com.example.bespinaf.a2d2.controllers.MainActivity;
import com.example.bespinaf.a2d2.controllers.RequestRide;
import com.example.bespinaf.a2d2.controllers.RideStatus;
import com.example.bespinaf.a2d2.models.Request;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;

import org.hamcrest.core.IsNot;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;

public class test_user_cancels_ride {
    @Rule
    public ActivityTestRule<RideStatus> mRideStatusRule = new ActivityTestRule<>(RideStatus.class, false, false);

    private RideStatus mActivity;
    private Instrumentation mInstrumentation;
    private Instrumentation.ActivityMonitor mHomeActivityMonitor;

    @Nullable
    private static String errorMessage;

    @Before
    public void setUp() {
        DataSourceUtils.initializeDateFormatters();
        mInstrumentation = getInstrumentation();
        mHomeActivityMonitor = mInstrumentation.addMonitor(MainActivity.class.getName(), null, false);

        Intent data = new Intent();
        data.putExtra("request", buildRideRequest());

        String requestId = DataSourceUtils.addRequest(buildRideRequest());

        data.putExtra("requestId", requestId);

        mRideStatusRule.launchActivity(data);
    }

    @After
    public void tearDown(){

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

    //ensures the request button exists
    @Test
    public void hasCancelButton() {
        onView(withId(R.id.button_cancel_ride)).check(matches(isDisplayed()));
    }

    @Test
    public void cancelButtonClicked_CancelDialogIsDisplayed(){
        onView(withId(R.id.button_cancel_ride)).perform(click());

        onView(withText("Are you sure you want to cancel your ride request?"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    @Test
    public void cancelDialogOk_CancelConfirmationAppears(){
        onView(withId(R.id.button_cancel_ride)).perform(click());

        onView(withText("CONFIRM")).perform(click());

        onView(withText("Your request has been successfully cancelled. You will now be taken back to the home screen."))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    @Test
    public void cancellationConfirmed_NavigatedToHomeScreen(){
        onView(withId(R.id.button_cancel_ride)).perform(click());

        onView(withText("CONFIRM")).perform(click());

        onView(withText("OKAY")).perform(click());

        Activity mHomeActivity = mInstrumentation.waitForMonitorWithTimeout(mHomeActivityMonitor, 1000);
        assertNotNull(mHomeActivity);
    }

}
