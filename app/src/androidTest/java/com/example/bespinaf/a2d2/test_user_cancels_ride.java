package com.example.bespinaf.a2d2;

import android.app.Instrumentation;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.widget.Button;

import com.example.bespinaf.a2d2.controllers.RequestRide;
import com.example.bespinaf.a2d2.controllers.RideStatus;

import org.hamcrest.core.IsNot;
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
    public ActivityTestRule<RideStatus> mRideStatusRule = new ActivityTestRule<>(RideStatus.class);

    private RideStatus mActivity;
    private Instrumentation mInstrumentation;
    @Nullable
    private static String errorMessage;

    @Before
    public void setUp() {
        mActivity = mRideStatusRule.getActivity();
        mInstrumentation = getInstrumentation();
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

}
