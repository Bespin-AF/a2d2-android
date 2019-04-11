package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.example.bespinaf.a2d2.controllers.RequestRide;
import com.example.bespinaf.a2d2.controllers.RideStatus;
import com.example.bespinaf.a2d2.utilities.DataSourceUtils;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class test_user_populates_request_form {
    @Rule
    public ActivityTestRule<RequestRide> mRequestRideActivity= new ActivityTestRule<>(RequestRide.class);

    private RequestRide mActivity;
    private Instrumentation mInstrumentation;
    private Instrumentation.ActivityMonitor mRideStatusMonitor;
    private TextInputLayout mNameInputLayout;
    private TextInputLayout mPhoneNumberInputLayout;
    private String errorRequired;
    private String name;
    private String phoneNumber;
    private String[] groupSizes;
    private String[] genders;
    private String remarks;
    private String long_text;
    private int NAME_MAX_LENGTH = 100;
    private int PHONE_MAX_LENGTH = 10;
    private int REMARKS_MAX_LENGTH = 255;


    @Before
    public void setUp(){
        mActivity = mRequestRideActivity.getActivity();
        mInstrumentation = getInstrumentation();
        mRideStatusMonitor = mInstrumentation.addMonitor(RideStatus.class.getName(), null, false);
        DataSourceUtils.initializeDateFormatters();
        DataSourceUtils.loadA2D2_PhoneNumber(null);
        DataSourceUtils.loadA2D2_BaseLocation(null);

        mNameInputLayout = mActivity.findViewById(R.id.activity_request_ride_name_text_input_layout);
        mPhoneNumberInputLayout = mActivity.findViewById(R.id.activity_request_ride_phone_number_text_input_layout);

        errorRequired = mActivity.getString(R.string.a2d2_field_required);

        name = mActivity.getString(R.string.test_user_name);
        phoneNumber = mActivity.getString(R.string.test_phone_number);
        genders = mActivity.getResources().getStringArray(R.array.string_genders);
        groupSizes = mActivity.getResources().getStringArray(R.array.string_group_sizes);
        remarks = mActivity.getString(R.string.bacon_ipsum);
        long_text = mActivity.getString(R.string.bacon_ipsum);
    }


    @After
    public void tearDown(){
        mActivity = null;
        mInstrumentation.removeMonitor(mRideStatusMonitor);
        mInstrumentation = null;
        mNameInputLayout = null;
        mPhoneNumberInputLayout = null;
        name = null;
        phoneNumber = null;
        genders = null;
        groupSizes = null;
        remarks = null;
        long_text = null;
    }


    @Test
    public void hasActivityLoaded(){ assertNotNull(mActivity);  }

    @Test
    public void hasNameField(){ onView(withId(R.id.activity_ride_request_name_text_edit)); }

    @Test
    public void hasPhoneNumberField(){ onView(withId(R.id.activity_ride_request_phone_number_text_edit)); }

    @Test
    public void hasGenderLabel(){
        onView(withText(R.string.tv_gender));
    }

    @Test
    public void hasGenderList(){
        onView(ViewMatchers.withId(R.id.gender_spinner));
    }

    @Test
    public void hasGroupSizeLabel(){ onView(withId(R.string.tv_group_size)); }

    @Test
    public void hasGroupSizeList(){ onView(withId(R.id.group_size_spinner)); }

    @Test
    public void hasRequestButton(){ onView(withId(R.id.button_request_driver)); }


    /** Name Field **/
    @Test
    public void doesNameHintAppear(){
        onView(withId(R.id.activity_ride_request_name_text_edit))
                .check(matches(withHint(R.string.activity_request_ride_name)));
    }


    @Test
    public void doesNameHaveMaximumCharacterValue(){
        final TextInputEditText nameField = mActivity.findViewById(R.id.activity_ride_request_name_text_edit);

        onView(withId(R.id.request_ride_remarks_edit_text))
                .perform(replaceText(long_text));

        boolean isRemarksTextLengthInvalid = (nameField.getText() != null && nameField.getText().length() <= NAME_MAX_LENGTH);
        assertTrue(isRemarksTextLengthInvalid);
    }


    @Test
    public void nameFieldRequiredIfEmpty(){
        onView(withId(R.id.button_request_driver)).perform(click());

        if(mNameInputLayout.getError() == null){
            fail();
        } else {
            assertEquals(errorRequired, mNameInputLayout.getError().toString());
        }
    }


    /** Phone Number Field **/
    @Test
    public void doesPhoneNumberHintAppear(){
        onView(withId(R.id.activity_ride_request_phone_number_text_edit))
                .check(matches(withHint(R.string.activity_request_ride_phone_number)));
    }


    @Test
    public void canEnterPhoneNumber(){
        onView(withId(R.id.activity_ride_request_phone_number_text_edit)).perform(replaceText(phoneNumber));
        onView(allOf(withId(R.id.activity_ride_request_phone_number_text_edit), withText(phoneNumber)));
    }


    @Test
    public void phoneNumberRequiredIfEmpty(){
        onView(withId(R.id.button_request_driver)).perform(click());

        if(mPhoneNumberInputLayout.getError() == null){
            fail();
        } else {
            assertEquals(errorRequired, mPhoneNumberInputLayout.getError().toString());
        }
    }


    @Test
    public void phoneNumberErrorAppearsIfInvalid(){
        String errorInvalid = mActivity.getString(R.string.error_phone_number);

        onView(withId(R.id.activity_ride_request_phone_number_text_edit)).perform(replaceText("123456"));
        onView(withId(R.id.button_request_driver)).perform(click());

        if(mPhoneNumberInputLayout.getError() == null){
            fail();
        } else {
            assertEquals(errorInvalid, mPhoneNumberInputLayout.getError().toString());
        }
    }

    @Test
    public void groupSpinnerHasCorrectValues(){
        for(String size : groupSizes){
            onView(ViewMatchers.withId(R.id.group_size_spinner)).perform(click());
            Espresso.onData(
                    is(size)
            ).perform(click());

            onView(withId(R.id.group_size_spinner))
                    .check(
                        matches(
                            withSpinnerText(containsString(size))
                        )
                    );
        }
    }


    @Test
    public void genderSpinnerHasCorrectValues(){
        for(String gender : genders){
            onView(ViewMatchers.withId(R.id.gender_spinner)).perform(click());
            Espresso.onData(
                    allOf(
                            is(instanceOf(String.class)),
                            is( gender )
                    )
            ).perform(click());

            onView(ViewMatchers
                    .withId(R.id.gender_spinner))
                    .check(matches(withSpinnerText(containsString(gender))));
        }
    }


    @Test
    public void isRemarksOptionalMessageDisplayed(){
        onView(withId(R.id.request_ride_remarks_edit_text))
                .check(matches(withHint(R.string.remarks_optional)));
    }


    @Test
    public void doesRemarksHaveMaximumCharacterValue(){
        final TextInputEditText editTextRemarks = mActivity.findViewById(R.id.request_ride_remarks_edit_text);

        onView(withId(R.id.request_ride_remarks_edit_text))
                .perform(replaceText(remarks));

        boolean isRemarksTextLengthInvalid = (editTextRemarks.getText() != null && editTextRemarks.getText().length() <= REMARKS_MAX_LENGTH);
        assertTrue(isRemarksTextLengthInvalid);
    }


    @Test
    public void confirmationDialogAppears(){
        onView(withId(R.id.activity_ride_request_name_text_edit)).perform(replaceText(name));
        onView(withId(R.id.activity_ride_request_phone_number_text_edit)).perform(replaceText(phoneNumber));
        onView(withId(R.id.button_request_driver)).perform(click());

        onView(withText(R.string.dialog_okay))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }


    @Test
    public void clickConfirmButton_rideStatusPageOpens() {
        int randomGenderIndex = getRandomNumber(0, genders.length - 1);
        int randomGroupIndex = getRandomNumber(0, groupSizes.length - 1);

        onView(withId(R.id.activity_ride_request_name_text_edit)).perform(replaceText(name));
        onView(withId(R.id.activity_ride_request_phone_number_text_edit)).perform(replaceText(phoneNumber));
        onView(withId(R.id.gender_spinner)).perform(click());
        onData(is(genders[randomGenderIndex])).perform(click());
        onView(withId(R.id.group_size_spinner)).perform(click());
        onData(is(groupSizes[randomGroupIndex])).perform(click());
        onView(withId(R.id.request_ride_remarks_edit_text)).perform(replaceText(remarks));

        onView(withId(R.id.button_request_driver)).perform(click());

        onView(withText(R.string.dialog_okay))
                .inRoot(isDialog())
                .perform(click());

        Activity mRideStatusActivity = mInstrumentation.waitForMonitorWithTimeout(mRideStatusMonitor, 1000);
        TestCase.assertNotNull(mRideStatusActivity);
    }


    /** Utility **/
    private int getRandomNumber(int minimum, int maximum){
        Random randomNumberGenerator = new Random();
        return randomNumberGenerator.nextInt((maximum - minimum) + 1) + minimum;
    }
}
