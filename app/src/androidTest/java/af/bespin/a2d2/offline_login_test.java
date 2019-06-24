package af.bespin.a2d2;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import af.bespin.a2d2.controllers.Driver_Login;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import org.junit.Rule;
import org.junit.Test;

//TEST REQUIRES OFFLINE MODE
public class offline_login_test {

    @Rule
    public ActivityTestRule<Driver_Login> mDriverLoginActivityTestRule = new ActivityTestRule<>(Driver_Login.class);

    @Test
    public void driverLoginAttemptNetworkErrorShow(){
        //Input email and password
        onView(withId(R.id.textInputEditText_driverLogin_emailInput)).perform(ViewActions.replaceText("1@2.3"));
        onView(withId(R.id.textInputEditText_driverLogin_passwordInput)).perform(ViewActions.replaceText("123456"));
        onView(withId(R.id.button_main_driver_login)).perform(ViewActions.click());
        //Check to see if toast displays
        onView(withText(R.string.connection_error)).inRoot(new ToastTest())
                .check(matches(isDisplayed()));    }
}
