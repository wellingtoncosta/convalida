package convalida.sample;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * @author Wellington Costa on 14/11/2017.
 */

class TestUtils {

    static void testFieldWithEmptyValue(int validateButtonResId, int errorMessageResId) {
        onView(withId(validateButtonResId))
                .perform(scrollTo(), closeSoftKeyboard())
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withText(errorMessageResId))
                .check(matches(isDisplayed()));
    }

    static void testFieldWithAValidValue(int fieldResId, int errorMessageResId, String value) {
        onView(withId(fieldResId))
                .perform(scrollTo(), clearText(), typeText(value))
                .perform(closeSoftKeyboard())
                .check(matches(withText(value)));

        onView(withText(errorMessageResId))
                .check(doesNotExist());
    }

    static void testFieldWithAnInvalidValue(int fieldResId, int errorMessageResId, String value) {
        onView(withId(fieldResId))
                .perform(scrollTo(), clearText(), typeText(value))
                .perform(closeSoftKeyboard())
                .check(matches(withText(value)));

        onView(withText(errorMessageResId))
                .check(matches(isDisplayed()));
    }

}
