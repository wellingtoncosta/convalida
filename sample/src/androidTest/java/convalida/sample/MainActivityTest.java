package convalida.sample;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author Wellington Costa on 12/11/2017.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void showSample1() throws Exception {
        onView(withId(R.id.sample_1))
                .check(matches(isDisplayed()))
                .perform(click());
    }

    @Test
    public void showSample2() throws Exception {
        onView(withId(R.id.sample_2))
                .check(matches(isDisplayed()))
                .perform(click());
    }

}