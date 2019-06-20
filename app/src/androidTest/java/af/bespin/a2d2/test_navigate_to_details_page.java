package af.bespin.a2d2;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import af.bespin.a2d2.controllers.Driver_RideRequestDetails;
import af.bespin.a2d2.controllers.Driver_RideRequestList;
import af.bespin.a2d2.utilities.DataSourceUtils;
import af.bespin.a2d2.utilities.FormatUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertNotNull;

public class test_navigate_to_details_page {
    @Rule
    public ActivityTestRule<Driver_RideRequestList> mRideRequestsActivity = new ActivityTestRule<>(Driver_RideRequestList.class);

    private Driver_RideRequestList mActivity;
    private Instrumentation mInstrumentation;
    private Instrumentation.ActivityMonitor mRequestDetailsMonitor;


    @Before
    public void setUp() {
        //Sets request ID for the test

        //Required to load data before trying to perform actions/load page
        FormatUtils.initializeDateFormatters();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e){

        }


        mActivity = mRideRequestsActivity.getActivity();
        mInstrumentation = getInstrumentation();
        mRequestDetailsMonitor = mInstrumentation.addMonitor(Driver_RideRequestDetails.class.getName(), null, false);
    }


    @After
    public void tearDown(){
        mActivity = null;
        mInstrumentation = null;
        mRequestDetailsMonitor = null;
    }


    @Test
    public void hasActivityLaunched() {
        assertNotNull(mActivity);
    }


    @Test
    public void navigateToDetails() throws InterruptedException{
        onView(  allOf(withId(R.id.ride_requests_recyclerview),isCompletelyDisplayed()) ).check( ViewAssertions.matches(isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));


        Activity requestDetailsActivity = mInstrumentation.waitForMonitorWithTimeout(mRequestDetailsMonitor, 1000);
        assertNotNull(requestDetailsActivity);
    }
}
