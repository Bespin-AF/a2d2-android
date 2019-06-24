package af.bespin.a2d2;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import af.bespin.a2d2.controllers.MainActivity;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import org.junit.Rule;
import org.junit.Test;

//TEST REQUIRES OFFLINE MODE
public class offline_main_activity_test{

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void driverLoginButtonAttemptNetworkErrorShow(){
        //Click on driver login button
        onView(withId(R.id.button_main_driver_login)).perform(ViewActions.click());
        //Check to see if toast displays
        onView(withText(R.string.connection_error)).inRoot(new ToastTest())
                .check(matches(isDisplayed()));
    }

    @Test
    public void rideRequestButtonAttemptNetworkErrorShow(){
        //Click on ride request button
        onView(withId(R.id.button_navigate_to_rules)).perform(ViewActions.click());
        //Check to see if toast displays
        onView(withText(R.string.connection_error)).inRoot(new ToastTest())
                .check(matches(isDisplayed()));
    }
}

