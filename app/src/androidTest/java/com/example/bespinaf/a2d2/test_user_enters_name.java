package com.example.bespinaf.a2d2;

import android.app.Instrumentation;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.widget.Button;

import com.example.bespinaf.a2d2.controllers.RequestRide;

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
    private TextInputEditText mPhoneNumberEditText;
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
        mPhoneNumberEditText = (TextInputEditText) mActivity.findViewById(R.id.activity_ride_request_phone_number_text_edit);
    }

    @Test
    public void hasActivityLoaded(){ assertNotNull(mActivity);  }

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

    //ensures the request button exists
    @Test
    public void hasRequestDriverButton(){
        final Button buttonRequestDriver = mActivity.findViewById(R.id.button_request_driver);
        assertNotNull(buttonRequestDriver);
    }

    @Test
    public void requestDriverButtonClicked_ConfirmationPopupShows(){
        final Button buttonRequestDriver = mActivity.findViewById(R.id.button_request_driver);
        //enters name into name field
        mActivity.runOnUiThread(()->{ mNameEditText.setText(R.string.bacon_ipsum); });

        //enters number into number field
        mActivity.runOnUiThread(() -> {
            mPhoneNumberEditText.setText("1234567890");
        });
        //clicks button to request ride
        mInstrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                buttonRequestDriver.performClick();
            }
        });
        //Verifies the that the confirmation popup displays
        Espresso.onView(withText(R.string.dialog_okay))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }
}
