package com.example.bespinaf.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.design.widget.TextInputEditText;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.text.InputType;

import com.example.bespinaf.a2d2.controllers.DriverLogin;
import com.example.bespinaf.a2d2.controllers.RideRequests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.action.ViewActions.replaceText;

public class test_driver_enters_email_and_password {
    @Rule
    public ActivityTestRule<DriverLogin> mDriverLoginActivity = new ActivityTestRule<>(DriverLogin.class);

    private DriverLogin mActivity;
    private Instrumentation mInstrumentation;
    private Instrumentation.ActivityMonitor mRideRequestsMonitor;
    private String email;
    private String password;

    @Before
    public void setUp(){
        mActivity = mDriverLoginActivity.getActivity();
        mInstrumentation = getInstrumentation();
        mRideRequestsMonitor = mInstrumentation.addMonitor(RideRequests.class.getName(),null,false);

        email = mActivity.getString(R.string.TEST_DRIVER_EMAIL);
        password = mActivity.getString(R.string.TEST_DRIVER_PASSWORD);
    }

    @Test
    public void hasEmailField(){
        Espresso.onView(ViewMatchers.withId(R.id.activity_driver_login_email_text_edit));
    }
    @Test
    public void isEmailField_EmailInput(){
        TextInputEditText mEmailInput = mActivity.findViewById(R.id.activity_driver_login_email_text_edit);

        /*
        * Getting type of email address field returns 33; email type is 32 per:
        * https://developer.android.com/reference/android/text/InputType
        * However, this can be matched by doing a bitwise operation per:
        * https://stackoverflow.com/questions/20781087/edittext-inputtype-constant-value-doesnot-match
        * */
        Assert.assertEquals(mEmailInput.getInputType(), (InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_CLASS_TEXT ));
    }

    @Test
    public void hasPasswordField(){
        Espresso.onView(ViewMatchers.withId(R.id.activity_driver_login_password_text_edit));
    }

    @Test
    public void isPasswordField_PasswordInput(){
        TextInputEditText mPasswordInput = mActivity.findViewById(R.id.activity_driver_login_password_text_edit);
        Assert.assertEquals(mPasswordInput.getInputType(), (InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT));
    }

    @Test
    public void hasLoginButton(){
        Espresso.onView(ViewMatchers.withId(R.id.button_driver_login));
    }

    @Test
    public void canEnterEmailIntoTextField(){
        Espresso.onView(withId(R.id.activity_driver_login_email_text_edit))
                .perform(replaceText(email))
                .check(matches(withText(email)));
    }

    @Test
    public void canEnterPasswordIntoTextField(){
        Espresso.onView(withId(R.id.activity_driver_login_password_text_edit))
                .perform(replaceText(password))
                .check(matches(withText(password)));
    }

    @Test
    public void doesDriverLoginButtonExist(){
        Espresso.onView(ViewMatchers.withId(R.id.button_driver_login));
    }

    @Test
    public void doesDriverLoginButtonNavigateToJobRequests(){
        Espresso.onView(withId(R.id.activity_driver_login_email_text_edit))
                .perform(replaceText(email));
        Espresso.onView(withId(R.id.activity_driver_login_password_text_edit))
                .perform(replaceText(password));

        Espresso.onView(ViewMatchers.withId(R.id.button_driver_login)).perform(ViewActions.click());

        Activity RideRequests = mInstrumentation.waitForMonitorWithTimeout(mRideRequestsMonitor, 10000);
        Assert.assertNotNull(RideRequests);
    }

    @After
    public void tearDown(){
        mInstrumentation.removeMonitor(mRideRequestsMonitor);
    }
}
