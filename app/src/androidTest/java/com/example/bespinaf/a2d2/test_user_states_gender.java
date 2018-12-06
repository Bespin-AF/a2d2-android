package com.example.bespinaf.a2d2;

import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.widget.Button;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertNotNull;

public class test_user_states_gender {
    @Rule
    public ActivityTestRule<RequestRide> mRequestRideActivity= new ActivityTestRule<>(RequestRide.class);

    private RequestRide mActivity;
    private int genderId;
    private UiDevice mDevice;
    private Instrumentation mInstrumentation;
    private Instrumentation.ActivityMonitor mRulesMonitor;

    @Before
    public void setUp(){
        mActivity = mRequestRideActivity.getActivity();
        genderId = R.id.gender_spinner;
        mDevice = UiDevice.getInstance(getInstrumentation());
    }

    @Test
    public void hasActivityLoaded(){ assertNotNull(mActivity);  }

    @Test
    public void hasGroupLabel(){
        onView(withText(R.string.tv_gender));
    }

    @Test
    public void hasDropDownList(){
        onView(ViewMatchers.withId(genderId));
    }

    @Test
    public void groupSizeHasMinOneAndMaxOfFour(){
        String[] genders = mActivity.getResources().getStringArray(R.array.string_genders);
        for(int i = 0; i < genders.length; i++){
            onView(ViewMatchers.withId(R.id.gender_spinner)).perform(click());
            Espresso.onData(
                    allOf(
                            is(instanceOf(String.class)),
                            is( genders[i] )
                    )
            ).perform(click());

            onView(ViewMatchers
                    .withId(R.id.gender_spinner))
                    .check(matches(withSpinnerText(containsString(genders[i]))));
        }
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
    }
}