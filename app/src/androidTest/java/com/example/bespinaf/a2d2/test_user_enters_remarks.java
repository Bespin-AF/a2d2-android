package com.example.bespinaf.a2d2;

import android.app.Instrumentation;
import android.support.design.widget.TextInputEditText;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.widget.Button;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class test_user_enters_remarks {
    @Rule
    public ActivityTestRule<RequestRide> mRequestRideActivity= new ActivityTestRule<>(RequestRide.class);

    private RequestRide mActivity;
    private int groupSizeId = 0;
    private UiDevice mDevice;
    private Instrumentation mInstrumentation;
    private Instrumentation.ActivityMonitor mRulesMonitor;

    @Before
    public void setUp(){
        mActivity = mRequestRideActivity.getActivity();
        mDevice = UiDevice.getInstance(getInstrumentation());
    }

    @Test
    public void hasActivityLoaded(){ assertNotNull(mActivity);  }

    @Test
    public void hasRemarksLabel(){ //CHANGE THE LABEL
        onView(withText(R.string.remarks_optional));
    }

    @Test
    public void inputHasMaximumCharacterValue(){
        onView(withId(R.id.request_ride_remarks_edit_text));

        final TextInputEditText editTextRemarks = mActivity.findViewById(R.id.request_ride_remarks_edit_text);

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editTextRemarks.setText(R.string.bacon_ipsum);
            }
        });

        assertTrue(editTextRemarks.getText().toString().length() <= 255);
    }

    @Test
    public void requestDriverButtonClicked_ConfirmationPopupShows(){
        //Ensures the request driver button exists and clicks on it
        final Button buttonRequestDriver = mActivity.findViewById(R.id.button_request_driver);
        assertNotNull(buttonRequestDriver);

        mActivity.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                buttonRequestDriver.performClick();
            }
        });

        //Makes sure the the error Toast message appears
        Espresso.onView(withText(R.string.confirm_driver_request_body))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        //makes sure that there is a button
//        final UiObject buttonDenyLocationPermission = mDevice.findObject(
//                new UiSelector()
//                        .clickable(true)
//                        .text(R.string.dialog_okay)
//        );
    }
}
