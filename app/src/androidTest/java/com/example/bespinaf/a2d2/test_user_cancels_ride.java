package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.widget.Button;

import org.hamcrest.core.IsNot;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;

public class test_user_cancels_ride {
    @Rule
    public ActivityTestRule<RequestRide> mRequestRideActivity = new ActivityTestRule<>(RequestRide.class);

    private RequestRide mActivity;
    private UiDevice mDevice;
    private Instrumentation mInstrumentation;
    private TextInputEditText mPhoneNumberEditText;
    private TextInputEditText mNameEditText;
    private TextInputLayout mPhoneNumberInputLayout;
    private MaterialButton mRequestDriverButton;
    private Instrumentation.ActivityMonitor mRideStatusMonitor;
    @Nullable
    private static String errorMessage;

    @Before
    public void setUp() {
        mActivity = mRequestRideActivity.getActivity();
        mInstrumentation = getInstrumentation();
        mDevice = UiDevice.getInstance(getInstrumentation());
        mRequestDriverButton = mActivity.findViewById(R.id.button_request_driver);
        mPhoneNumberInputLayout = (TextInputLayout) mActivity.findViewById(R.id.activity_request_ride_phone_number_text_input_layout);
        mPhoneNumberEditText = (TextInputEditText) mActivity.findViewById(R.id.activity_ride_request_phone_number_text_edit);
        mNameEditText = (TextInputEditText) mActivity.findViewById(R.id.activity_ride_request_name_text_edit);
        mRideStatusMonitor = mInstrumentation.addMonitor(RideStatus.class.getName(), null, false);
    }

    //ensures the request button exists
    @Test
    public void hasRequestDriverButton() {
        final Button buttonRequestDriver = mActivity.findViewById(R.id.button_request_driver);
        assertNotNull(buttonRequestDriver);
    }

    @Test
    public void clickConfirmButton_rideStatusPageOpens() {


        final Button buttonRequestDriver = mActivity.findViewById(R.id.button_request_driver);
        assertNotNull(buttonRequestDriver);

        getInstrumentation().runOnMainSync(()->{
            mNameEditText.setText(R.string.test_user_name);
            mPhoneNumberEditText.setText(R.string.test_phone_number);

            buttonRequestDriver.performClick();
        });

        onView(withText(R.string.cancel))
                .inRoot(withDecorView(IsNot.not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed())).perform(click());

        onView(withText(R.string.test_user_name))
                .check(matches(isDisplayed()));

        onView(withText(R.string.test_phone_number))
                .check(matches(isDisplayed()));

    }
}
