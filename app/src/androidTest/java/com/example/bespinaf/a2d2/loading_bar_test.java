package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.example.bespinaf.a2d2.controllers.Driver_Login;
import com.example.bespinaf.a2d2.controllers.Driver_RideRequestList;
import com.example.bespinaf.a2d2.controllers.MainActivity;
import com.example.bespinaf.a2d2.controllers.Rider_RequestRide;
import com.example.bespinaf.a2d2.controllers.Rider_Rules;
import com.example.bespinaf.a2d2.utilities.FormatUtils;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class loading_bar_test {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    private MainActivity mMainActivity;

    private Instrumentation mInstrumentation;

    private Instrumentation.ActivityMonitor mRideRequestListActivityMonitor;
    private Instrumentation.ActivityMonitor mRiderRequestRideActivityMonitor;





    @Before
    public void setup(){
        mInstrumentation = getInstrumentation();
        mRideRequestListActivityMonitor = mInstrumentation.addMonitor(Driver_RideRequestList.class.getName(),null,false);
        mRiderRequestRideActivityMonitor = mInstrumentation.addMonitor(Rider_RequestRide.class.getName(), null, false);
        FormatUtils.initializeDateFormatters();
    }

    @After
    public void teardown(){
        mMainActivity = null;
        mInstrumentation = null;
        mRideRequestListActivityMonitor = null;



    }

    @Test
    public void driverLoginAttemptProgressBarExists(){
       mMainActivity = mMainActivityActivityTestRule.getActivity();
       onView(withId(R.id.button_main_driver_login)).perform(ViewActions.click());
       //Entering in login info
       onView(withId(R.id.activity_driver_login_email_text_edit)).perform(ViewActions.replaceText("1@2.3"));
       onView(withId(R.id.activity_driver_login_password_text_edit)).perform(ViewActions.replaceText("123456"));
       //try{Thread.sleep(5000);}catch (Exception e) {}
       //attempting login
       onView(withId(R.id.button_driver_login)).perform(ViewActions.click());

       onView(withId(R.id.driver_login_progress_bar)).check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void driverRideRequestsListLoadingProgressBarExists(){
        mMainActivity = mMainActivityActivityTestRule.getActivity();
        onView(withId(R.id.button_main_driver_login)).perform(ViewActions.click());
        //Entering in login info
        onView(withId(R.id.activity_driver_login_email_text_edit)).perform(ViewActions.replaceText("1@2.3"));
        onView(withId(R.id.activity_driver_login_password_text_edit)).perform(ViewActions.replaceText("123456"));
        //try{Thread.sleep(5000);}catch (Exception e) {}
        //attempting login
        onView(withId(R.id.button_driver_login)).perform(ViewActions.click());
        Activity driverRideRequestList = mRideRequestListActivityMonitor.waitForActivity();
        assertNotNull(driverRideRequestList);
        /*WHY DOES BREAK*/onView(withId(R.id.request_tabs_loading_bar)).check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void userRuleAgreementProgressBarGPSEnabled() {
        mMainActivity = mMainActivityActivityTestRule.getActivity();
        onView(withId(R.id.button_navigate_to_rules)).perform(ViewActions.click());
        /*WHY YOU BREAK*/onView(withId(R.id.rules_progress_bar)).check(ViewAssertions.matches(isDisplayed()));
        sleep(1500);
        onView(withId(R.id.button_rules_agree)).perform(ViewActions.click());
        onView(withId(R.id.rules_progress_bar)).check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void userRideRequestProgressBarGPSEnabled() {
        mMainActivity = mMainActivityActivityTestRule.getActivity();
        //navigate to ride request page
        onView(withId(R.id.button_navigate_to_rules)).perform(ViewActions.click());
        onView(withId(R.id.rules_progress_bar)).check(ViewAssertions.matches(isDisplayed()));
        sleep(1500);
        onView(withId(R.id.button_rules_agree)).perform(ViewActions.click());
        Activity riderRequestRide = mRiderRequestRideActivityMonitor.waitForActivity();
        assertNotNull(riderRequestRide);
        //enter in ride request details & submit request
        onView(withId(R.id.activity_ride_request_name_text_edit)).perform(ViewActions
                .replaceText("RideRequestProgressBarTest"));
        /*String phoneNumber = mMainActivity.getString(R.string.test_phone_number);*/
        onView(withId(R.id.activity_ride_request_phone_number_text_edit)).perform(ViewActions.replaceText("6162529950"));
        //sleep(1500);
        onView(withId(R.id.button_request_driver)).perform(ViewActions.click());
        //sleep(500);
        onView(withText(R.string.dialog_okay)).inRoot(isDialog()).perform(click());
        //sleep(500);
        onView(withId(R.id.ride_request_progress_bar)).check(ViewAssertions.matches(isDisplayed()));


    }



    private void sleep(int duration){
        try{Thread.sleep(duration);}catch (Exception e) {}
    }


}
