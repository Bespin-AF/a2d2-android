package af.bespin.a2d2;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import af.bespin.a2d2.controllers.Rider_RequestRide;
import af.bespin.a2d2.utilities.FormatUtils;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.CoreMatchers.is;
import org.hamcrest.core.IsNot;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

//TEST REQUIRES OFFLINE MODE
public class offline_submit_request_test {

    @Rule
    public ActivityTestRule<Rider_RequestRide> mRiderRequestRideActivityTestRule = new ActivityTestRule<>(Rider_RequestRide.class);
    private Rider_RequestRide mRiderRequestRide;

    @Before
    public void setup(){
        FormatUtils.initializeDateFormatters();
    }

    @After
    public void teardown(){
        mRiderRequestRide = null;
    }

    @Test
    public void driverLoginAttemptNetworkErrorShow() {
        mRiderRequestRide = mRiderRequestRideActivityTestRule.getActivity();
        //Input name and phone number
        onView(withId(R.id.activity_ride_request_name_text_edit)).perform(ViewActions
                .replaceText("RideRequestProgressBarTest"));
        onView(withId(R.id.activity_ride_request_phone_number_text_edit)).perform(ViewActions.replaceText("6162529950"));
        onView(withId(R.id.button_request_driver)).perform(ViewActions.click());
        //Check to see if dialog box displays
        onView(withText(R.string.dialog_okay))
                .inRoot(withDecorView(IsNot.not(is(mRiderRequestRide.getWindow().getDecorView()))))
                .check(matches(isDisplayed())).perform(click());
    }

}
