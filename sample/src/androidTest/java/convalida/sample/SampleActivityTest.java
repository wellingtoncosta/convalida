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
 * @author Wellington Costa on 12/11/2017.
 */
@RunWith(AndroidJUnit4.class)
public class SampleActivityTest {

    @Rule
    public ActivityTestRule<SampleActivity> activityTestRule = new ActivityTestRule<>(SampleActivity.class);

    @Test
    public void executeValidationsWithEmptyFields() {
        onView(withId(R.id.validate_button))
                .perform(closeSoftKeyboard())
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withText(R.string.field_required))
                .check(matches(isDisplayed()));

        onView(withText(R.string.invalid_email))
                .check(matches(isDisplayed()));

        onView(withText(R.string.invalid_password))
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

        onView(withText(R.string.invalid_email))
                .check(doesNotExist());

        onView(withText(R.string.emails_not_match))
                .check(doesNotExist());

        onView(withText(R.string.invalid_password))
                .check(doesNotExist());

        onView(withText(R.string.passwords_not_match))
                .check(doesNotExist());
    }

    @Test
    public void testNameField() {
        TestUtils.testFieldWithEmptyValue(R.id.validate_button, R.string.field_required);

        TestUtils.testFieldWithAValidValue(R.id.name_field, R.string.field_required, "Wellington");
    }

    @Test
    public void testEmailField() {
        TestUtils.testFieldWithEmptyValue(R.id.validate_button, R.string.invalid_email);

        TestUtils.testFieldWithAnInvalidValue(R.id.email_field, R.string.invalid_email, "well@email");

        TestUtils.testFieldWithAValidValue(R.id.email_field, R.string.invalid_email, "well@email.com");
    }

    @Test
    public void testConfirmEmailField() {
        TestUtils.testFieldWithAValidValue(R.id.email_field, R.string.invalid_email, "wellington@email.com");

        TestUtils.testFieldWithAnInvalidValue(R.id.confirm_email_field, R.string.emails_not_match, "wellington@email.co");

        TestUtils.testFieldWithAValidValue(R.id.confirm_email_field, R.string.emails_not_match, "wellington@email.com");
    }

    @Test
    public void testPasswordField() {
        TestUtils.testFieldWithEmptyValue(R.id.validate_button, R.string.invalid_password);

        TestUtils.testFieldWithAnInvalidValue(R.id.password_field, R.string.invalid_password, "asdASD");

        TestUtils.testFieldWithAValidValue(R.id.password_field, R.string.invalid_password, "asdASD123");
    }

    @Test
    public void testConfirmPasswordField() {
        TestUtils.testFieldWithAValidValue(R.id.password_field, R.string.invalid_password, "asdASD123");

        TestUtils.testFieldWithAnInvalidValue(R.id.confirm_password_field, R.string.passwords_not_match, "asdASD");

        TestUtils.testFieldWithAValidValue(R.id.confirm_password_field, R.string.passwords_not_match, "asdASD123");
    }

}