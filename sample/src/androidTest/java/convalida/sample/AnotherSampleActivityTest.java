package convalida.sample;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * @author Wellington Costa on 14/11/2017.
 */
@RunWith(AndroidJUnit4.class)
public class AnotherSampleActivityTest {

    @Rule
    public ActivityTestRule<AnotherSampleActivity> activityTestRule = new ActivityTestRule<>(AnotherSampleActivity.class);

    @Test
    public void executeValidationsWithEmptyFields() {
        onView(withId(R.id.validate_button))
                .perform(closeSoftKeyboard())
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withText(R.string.field_required))
                .check(matches(isDisplayed()));

        onView(withText(R.string.min_3_characters))
                .check(matches(isDisplayed()));

        onView(withText(R.string.only_numbers))
                .check(matches(isDisplayed()));

        onView(withText(R.string.invalid_phone))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clearAllValidations() {
        onView(withId(R.id.validate_button))
                .perform(closeSoftKeyboard())
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.clear_button))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withText(R.string.field_required))
                .check(doesNotExist());

        onView(withText(R.string.min_3_characters))
                .check(doesNotExist());

        onView(withText(R.string.only_numbers))
                .check(doesNotExist());

        onView(withText(R.string.invalid_phone))
                .check(doesNotExist());
    }

    @Test
    public void testNameField() {
        TestUtils.testFieldWithEmptyValue(R.id.validate_button, R.string.field_required);

        TestUtils.testFieldWithAValidValue(R.id.name_field, R.string.field_required, "Wellington");
    }

    @Test
    public void testNicknameField() {
        TestUtils.testFieldWithEmptyValue(R.id.validate_button, R.string.min_3_characters);

        TestUtils.testFieldWithAnInvalidValue(R.id.nickname_field, R.string.min_3_characters, "We");

        TestUtils.testFieldWithAValidValue(R.id.nickname_field, R.string.min_3_characters, "Well");
    }

    @Test
    public void testAgeField() {
        TestUtils.testFieldWithEmptyValue(R.id.validate_button, R.string.only_numbers);

        TestUtils.testFieldWithAnInvalidValue(R.id.age_field, R.string.only_numbers, "abc");

        TestUtils.testFieldWithAValidValue(R.id.age_field, R.string.only_numbers, "21");
    }

    @Test
    public void testPhoneField() {
        TestUtils.testFieldWithEmptyValue(R.id.validate_button, R.string.invalid_phone);

        TestUtils.testFieldWithAnInvalidValue(R.id.phone_field, R.string.invalid_phone, "558586846409");

        TestUtils.testFieldWithAValidValue(R.id.phone_field, R.string.invalid_phone, "+55(85)8684-6409");
    }

}