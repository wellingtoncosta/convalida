package convalida.sample.robot;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.test.espresso.action.ViewActions;

import convalida.sample.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class ConvalidaRobot {

    public TypeText name() {
        onView(withId(R.id.name_field))
                .perform(scrollTo(), clearText());

        return new TypeText(this, R.id.name_field);
    }

    public TypeText nickName() {
        onView(withId(R.id.nickname_field))
                .perform(scrollTo(), clearText());

        return new TypeText(this, R.id.nickname_field);
    }

    public TypeText age() {
        onView(withId(R.id.age_field))
                .perform(scrollTo(), clearText());

        return new TypeText(this, R.id.age_field);
    }

    public TypeText phone() {
        onView(withId(R.id.phone_field))
                .perform(scrollTo(), clearText());

        return new TypeText(this, R.id.phone_field);
    }

    public TypeText cpf() {
        onView(withId(R.id.cpf_field))
                .perform(scrollTo(), clearText());

        return new TypeText(this, R.id.cpf_field);
    }

    public TypeText cnpj() {
        onView(withId(R.id.cnpj_field))
                .perform(scrollTo(), clearText());

        return new TypeText(this, R.id.cnpj_field);
    }

    public TypeText isbn() {
        onView(withId(R.id.isbn_field))
                .perform(scrollTo(), clearText());

        return new TypeText(this, R.id.isbn_field);
    }

    public TypeText email() {
        onView(withId(R.id.email_field))
                .perform(scrollTo(), clearText());

        return new TypeText(this, R.id.email_field);
    }

    public TypeText confirmEmail() {
        onView(withId(R.id.confirm_email_field))
                .perform(scrollTo(), clearText());

        return new TypeText(this, R.id.confirm_email_field);
    }

    public TypeText password() {
        onView(withId(R.id.password_field))
                .perform(scrollTo(), clearText());

        return new TypeText(this, R.id.password_field);
    }

    public TypeText confirmPassword() {
        onView(withId(R.id.confirm_password_field))
                .perform(scrollTo(), clearText());

        return new TypeText(this, R.id.confirm_password_field);
    }

    public TypeText creditCard() {
        onView(withId(R.id.credit_card_field))
                .perform(scrollTo(), clearText());

        return new TypeText(this, R.id.credit_card_field);
    }

    public TypeText numericLimit() {
        onView(withId(R.id.numeric_limit_field))
                .perform(scrollTo(), clearText());

        return new TypeText(this, R.id.numeric_limit_field);
    }

    public TypeText ipv4() {
        onView(withId(R.id.ipv4_field))
                .perform(scrollTo(), clearText());

        return new TypeText(this, R.id.ipv4_field);
    }

    public TypeText ipv6() {
        onView(withId(R.id.ipv6_field))
                .perform(scrollTo(), clearText());

        return new TypeText(this, R.id.ipv6_field);
    }

    public TypeText url() {
        onView(withId(R.id.url_field))
                .perform(scrollTo(), clearText());

        return new TypeText(this, R.id.url_field);
    }

    public TypeText date() {
        onView(withId(R.id.date_field))
                .perform(scrollTo(), clearText());

        return new TypeText(this, R.id.date_field);
    }

    public ConvalidaRobot validate() {
        onView(withId(R.id.validate_button))
                .perform(closeSoftKeyboard())
                .perform(scrollTo(), click());

        return this;
    }

    public ConvalidaRobot clear() {
        onView(withId(R.id.clear_button))
                .perform(closeSoftKeyboard())
                .perform(scrollTo(), click());

        return this;
    }

    public Result result () {
        return new Result();
    }

    public static class TypeText {

        private final ConvalidaRobot _robot;

        private final @IdRes int _fieldResId;

        private TypeText(ConvalidaRobot robot, @IdRes int fieldResId) {
            _robot = robot;
            _fieldResId = fieldResId;
        }

        public ConvalidaRobot typeText(String text) {
            onView(withId(_fieldResId))
                    .perform(ViewActions.typeText(text))
                    .perform(closeSoftKeyboard());

            return _robot;
        }

    }

    public static class Result {

        public void nameIsInvalid() {
            isInvalid(R.id.name_field, R.string.field_required);
        }

        public void nameIsValid() {
            isValid(R.id.name_field, R.string.field_required);
        }

        public void nickNameIsInvalid() {
            isInvalid(R.id.nickname_field, R.string.min_3_characters);
        }

        public void nickNameIsValid() {
            isValid(R.id.nickname_field, R.string.min_3_characters);
        }

        public void ageIsInvalid() {
            isInvalid(R.id.age_field, R.string.only_numbers);
        }

        public void ageIsValid() {
            isValid(R.id.age_field, R.string.only_numbers);
        }

        public void phoneIsInvalid() {
            isInvalid(R.id.phone_field, R.string.invalid_phone);
        }

        public void phoneIsValid() {
            isValid(R.id.phone_field, R.string.invalid_phone);
        }

        public void cpfIsInvalid() {
            isInvalid(R.id.cpf_field, R.string.invalid_cpf);
        }

        public void cpfIsValid() {
            isValid(R.id.cpf_field, R.string.invalid_cpf);
        }

        public void cnpjIsInvalid() {
            isInvalid(R.id.cnpj_field, R.string.invalid_cnpj);
        }

        public void cnpjIsValid() {
            isValid(R.id.cnpj_field, R.string.invalid_cnpj);
        }

        public void isbnIsInvalid() {
            isInvalid(R.id.isbn_field, R.string.invalid_isbn);
        }

        public void isbnIsValid() {
            isValid(R.id.isbn_field, R.string.invalid_isbn);
        }

        public void emailIsInvalid() {
            isInvalid(R.id.email_field, R.string.invalid_email);
        }

        public void emailIsValid() {
            isValid(R.id.email_field, R.string.invalid_email);
        }

        public void confirmEmailIsInvalid() {
            isInvalid(R.id.confirm_email_field, R.string.emails_not_match);
        }

        public void confirmEmailIsValid() {
            isValid(R.id.confirm_email_field, R.string.emails_not_match);
        }

        public void passwordIsInvalid() {
            isInvalid(R.id.password_field, R.string.invalid_password);
        }

        public void passwordIsValid() {
            isValid(R.id.password_field, R.string.invalid_password);
        }

        public void confirmPasswordIsInvalid() {
            isInvalid(R.id.confirm_password_field, R.string.passwords_not_match);
        }

        public void confirmPasswordIsValid() {
            isValid(R.id.confirm_password_field, R.string.passwords_not_match);
        }

        public void creditCardIsInvalid() {
            isInvalid(R.id.credit_card_field, R.string.invalid_credit_card);
        }

        public void creditCardIsValid() {
            isValid(R.id.credit_card_field, R.string.invalid_credit_card);
        }

        public void numericLimitIsInvalid() {
            isInvalid(R.id.numeric_limit_field, R.string.invalid_numeric_limit);
        }

        public void numericLimitIsValid() {
            isValid(R.id.numeric_limit_field, R.string.invalid_numeric_limit);
        }

        public void ipv4IsValid() {
            isValid(R.id.ipv4_field, R.string.invalid_ipv4);
        }

        public void ipv4IsInvalid() {
            isInvalid(R.id.ipv4_field, R.string.invalid_ipv4);
        }

        public void ipv6IsValid() {
            isValid(R.id.ipv6_field, R.string.invalid_ipv6);
        }

        public void ipv6IsInvalid() {
            isInvalid(R.id.ipv6_field, R.string.invalid_ipv6);
        }

        public void urlIsValid() {
            isValid(R.id.url_field, R.string.invalid_url);
        }

        public void urlIsInvalid() {
            isInvalid(R.id.url_field, R.string.invalid_url);
        }

        public void pastDateIsValid() {
            isValid(R.id.date_field, R.string.invalid_past_date);
        }

        public void pastDateIsInvalid() {
            isInvalid(R.id.date_field, R.string.invalid_past_date);
        }

        public void futureDateIsValid() {
            isValid(R.id.date_field, R.string.invalid_future_date);
        }

        public void futureDateIsInvalid() {
            isInvalid(R.id.date_field, R.string.invalid_future_date);
        }

        private void isInvalid(
                @IdRes int fieldResId,
                @StringRes int errorMessageResId
        ) {
            onView(withId(fieldResId))
                    .perform(scrollTo());

            onView(withText(errorMessageResId))
                    .check(matches(isDisplayed()));
        }

        private void isValid(
                @IdRes int fieldResId,
                @StringRes int errorMessageResId
        ) {
            onView(withId(fieldResId))
                    .perform(scrollTo());

            onView(withText(errorMessageResId))
                    .check(doesNotExist());
        }

        public void isAllValid() {
            onView(withText(R.string.field_required))
                    .check(doesNotExist());

            onView(withText(R.string.min_3_characters))
                    .check(doesNotExist());

            onView(withText(R.string.only_numbers))
                    .check(doesNotExist());

            onView(withText(R.string.invalid_phone))
                    .check(doesNotExist());

            onView(withText(R.string.invalid_cpf))
                    .check(doesNotExist());

            onView(withText(R.string.start_value_not_valid))
                    .check(doesNotExist());

            onView(withText(R.string.limit_value_not_valid))
                    .check(doesNotExist());

            onView(withText(R.string.invalid_email))
                    .check(doesNotExist());

            onView(withText(R.string.emails_not_match))
                    .check(doesNotExist());

            onView(withText(R.string.invalid_password))
                    .check(doesNotExist());

            onView(withText(R.string.passwords_not_match))
                    .check(doesNotExist());

            onView(withText(R.string.invalid_credit_card))
                    .check(doesNotExist());

            onView(withText(R.string.invalid_numeric_limit))
                    .check(doesNotExist());
        }

        public void isAllInvalid() {
            onView(withText(R.string.field_required))
                    .perform(scrollTo())
                    .check(matches(isDisplayed()));

            onView(withText(R.string.min_3_characters))
                    .perform(scrollTo())
                    .check(matches(isDisplayed()));

            onView(withText(R.string.only_numbers))
                    .perform(scrollTo())
                    .check(matches(isDisplayed()));

            onView(withText(R.string.invalid_phone))
                    .perform(scrollTo())
                    .check(matches(isDisplayed()));

            onView(withText(R.string.invalid_email))
                    .perform(scrollTo())
                    .check(matches(isDisplayed()));

            onView(withText(R.string.invalid_password))
                    .perform(scrollTo())
                    .check(matches(isDisplayed()));
        }

    }

}
