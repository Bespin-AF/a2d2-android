package com.example.bespinaf.a2d2;

import android.app.Instrumentation;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.util.Log;
import android.widget.Button;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class test_user_enters_name {
    @Rule
    public ActivityTestRule<RequestRide> mRequestRideActivity= new ActivityTestRule<>(RequestRide.class);

    private RequestRide mActivity;
    private UiDevice mDevice;
    private Instrumentation mInstrumentation;
    private Instrumentation.ActivityMonitor mRulesMonitor;
    private TextInputEditText mNameEditText;
    private TextInputLayout mNameInputLayout;
    private MaterialButton mRequestDriverButton;
    @Nullable
    private static String errorMessage;

    @Before
    public void setUp(){
        mActivity = mRequestRideActivity.getActivity();
        mInstrumentation = getInstrumentation();
        mDevice = UiDevice.getInstance(getInstrumentation());
        mRequestDriverButton = mActivity.findViewById(R.id.button_request_driver);
        mNameInputLayout = (TextInputLayout) mActivity.findViewById(R.id.activity_request_ride_name_text_input_layout);
        mNameEditText = (TextInputEditText) mActivity.findViewById(R.id.activity_ride_request_name_text_edit);
    }

    @Test
    public void hasActivityLoaded(){ assertNotNull(mActivity);  }

    @Test
    public void hasNameLabel(){
        onView(withText(R.string.activity_request_ride_name));
    }

    @Test
    public void hasNameTextEdit(){ onView(withId(R.id.activity_ride_request_name_text_edit)); }

    @Test
    public void canEnterNameIntoNameTextEdit(){
        mActivity.runOnUiThread(()->{ mNameEditText.setText(R.string.bacon_ipsum); });
        onView(allOf(withId(R.id.activity_ride_request_name_text_edit),withText(R.string.bacon_ipsum)));
    }

    @Test
    public void nameFieldRequiredIfEmpty(){
        mInstrumentation.runOnMainSync(()->{ mRequestDriverButton.performClick(); });
        assertEquals(mActivity.getResources().getString(R.string.a2d2_field_required),mNameInputLayout.getError().toString());
    }

    @Test
    public void requestDriverButtonClicked_ConfirmationPopupShows(){
        //Ensures the request driver button exists and clicks on it
        final Button buttonRequestDriver = mActivity.findViewById(R.id.button_request_driver);
        assertNotNull(buttonRequestDriver);

        mInstrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                buttonRequestDriver.performClick();
            }
        });

        /*mActivity.runOnUiThread(()->{
            System.out.println("A2D2: mNameInputLayout error in UI Thread - " + mNameInputLayout.getError());
            errorMessage = mNameInputLayout.getError().toString();

            assertNotNull(errorMessage);
            //String errorText = mNameInputLayout.getError().toString();
        });*/

        //onView(withText("Required*"));

        //makes sure that there is a button
//        final UiObject buttonDenyLocationPermission = mDevice.findObject(
//                new UiSelector()
//                        .clickable(true)
//                        .text(R.string.dialog_okay)
//        );
    }
}
