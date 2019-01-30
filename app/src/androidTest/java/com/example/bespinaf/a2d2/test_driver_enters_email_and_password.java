package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.text.InputType;
import android.widget.ArrayAdapter;

import com.example.bespinaf.a2d2.adapters.RideRequestAdapter;
import com.example.bespinaf.a2d2.utilities.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.hamcrest.Matchers;
import org.hamcrest.core.AllOf;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withInputType;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;

public class test_driver_enters_email_and_password {
    @Rule
    public ActivityTestRule<DriverLogin> mDriverLoginActivity = new ActivityTestRule<>(DriverLogin.class);

    private DriverLogin mActivity;
    private Instrumentation mInstrumentation;
    private Instrumentation.ActivityMonitor mRideRequestsMonitor;

    @Before
    public void setUp(){
        mActivity = mDriverLoginActivity.getActivity();
        mInstrumentation = getInstrumentation();
        mRideRequestsMonitor = mInstrumentation.addMonitor(RideRequests.class.getName(),null,false);
    }

    @Test
    public void hasEmailField(){
        Espresso.onView(ViewMatchers.withId(R.id.activity_driver_login_email_text_edit));
    }
    @Test
    public void isEmailField_EmailInput(){
        TextInputEditText mEmailInput = (TextInputEditText)
                mActivity.findViewById(R.id.activity_driver_login_email_text_edit);

        /*
        * Getting type of email address field returns 33; email type is 32 per:
        * https://developer.android.com/reference/android/text/InputType
        * However, this can be matched by doing a bitwise operation per:
        * https://stackoverflow.com/questions/20781087/edittext-inputtype-constant-value-doesnot-match
        * */
        Assert.assertTrue(mEmailInput.getInputType() ==
                (InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_CLASS_TEXT ));
    }

    @Test
    public void hasPasswordField(){
        Espresso.onView(ViewMatchers.withId(R.id.activity_driver_login_password_text_edit));
    }

    @Test
    public void isPasswordField_PasswordInput(){
        TextInputEditText mPasswordInput = (TextInputEditText)
                mActivity.findViewById(R.id.activity_driver_login_password_text_edit);

        Assert.assertTrue(mPasswordInput.getInputType() ==
                (InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT));
    }

    @Test
    public void hasLoginButton(){
        Espresso.onView(ViewMatchers.withId(R.id.button_driver_login));
    }

    @Test
    public void canEnterEmailIntoTextField(){
        mInstrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                TextInputEditText mEmailInput = (TextInputEditText)
                        mActivity.findViewById(R.id.activity_driver_login_email_text_edit);
                mEmailInput.setText("testemail@gmail.com");
            }
        });

        Espresso.onView(withId(R.id.activity_driver_login_email_text_edit))
                .check(matches(withText("testemail@gmail.com")));
    }

    @Test
    public void canEnterPasswordIntoTextField(){
        mInstrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                TextInputEditText mPasswordInput = (TextInputEditText)
                        mActivity.findViewById(R.id.activity_driver_login_password_text_edit);
                mPasswordInput.setText("testpassword");
            }
        });

        Espresso.onView(withId(R.id.activity_driver_login_password_text_edit))
                .check(matches(withText("testpassword")));
    }

    @Test
    public void doesDriverLoginButtonExist(){
        Espresso.onView(ViewMatchers.withId(R.id.button_driver_login));
    }

    @Test
    public void doesDriverLoginButtonNavigateToJobRequests(){
        mInstrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                TextInputEditText mEmailInput = (TextInputEditText)
                        mActivity.findViewById(R.id.activity_driver_login_email_text_edit);
                mEmailInput.setText(R.string.TEST_DRIVER_EMAIL);

                TextInputEditText mPasswordInput = (TextInputEditText)
                        mActivity.findViewById(R.id.activity_driver_login_password_text_edit);
                mPasswordInput.setText(R.string.TEST_DRIVER_PASSWORD);
            }
        });

        Espresso.onView(ViewMatchers.withId(R.id.button_driver_login)).perform(ViewActions.click());

        Activity RideRequests = mInstrumentation.waitForMonitorWithTimeout(mRideRequestsMonitor, 10000);
        Assert.assertNotNull(RideRequests);
    }

    @After
    public void tearDown(){
        mInstrumentation.removeMonitor(mRideRequestsMonitor);
    }
}
