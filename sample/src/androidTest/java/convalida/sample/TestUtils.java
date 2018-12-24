package convalida.sample;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

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
