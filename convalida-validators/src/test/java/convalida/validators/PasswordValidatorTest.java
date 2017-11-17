package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 01/11/2017.
 */
public class PasswordValidatorTest extends BaseTest {

    private static final String LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{1,}+$";

    @Test
    public void passwordIsNull() {
        PasswordValidator validator = new PasswordValidator();
        assertEquals(validator.isNotValid(null), true);
    }

    @Test
    public void emptyPassword() {
        String value = "";

        PasswordValidator validator = new PasswordValidator();
        PasswordValidator validatorWithEditText = new PasswordValidator(mockEditText, 0, "", errorMessage);
        PasswordValidator validatorWithTextInputLayout = new PasswordValidator(mockTextInputLayout, 0, "", errorMessage);

        when(mockEditText.getText().toString()).thenReturn(value);

        assertEquals(validator.isNotValid(value), true);
        assertEquals(validatorWithEditText.validate(), false);
        assertEquals(validatorWithTextInputLayout.validate(), false);
    }

    @Test
    public void passwordLessThan5() {
        String value = "test";

        PasswordValidator validator = new PasswordValidator(5);
        PasswordValidator validatorWithEditText = new PasswordValidator(mockEditText, 5, "", errorMessage);
        PasswordValidator validatorWithTextInputLayout = new PasswordValidator(mockTextInputLayout, 5, "", errorMessage);

        when(mockEditText.getText().toString()).thenReturn(value);

        assertEquals(validator.isNotValid(value), true);
        assertEquals(validatorWithEditText.validate(), false);
        assertEquals(validatorWithTextInputLayout.validate(), false);
    }

    @Test
    public void passwordGreaterThan5() {
        String value = "test123";

        PasswordValidator validator = new PasswordValidator(5);
        PasswordValidator validatorWithEditText = new PasswordValidator(mockEditText, 5, "", errorMessage);
        PasswordValidator validatorWithTextInputLayout = new PasswordValidator(mockTextInputLayout, 5, "", errorMessage);

        when(mockEditText.getText().toString()).thenReturn(value);

        assertEquals(validator.isNotValid(value), false);
        assertEquals(validatorWithEditText.validate(), true);
        assertEquals(validatorWithTextInputLayout.validate(), true);
    }

    @Test
    public void passwordNotContainsLettersAndNumbersCaseInsensitive() {
        String value = "qweQWE";

        PasswordValidator validator = new PasswordValidator(LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX);
        PasswordValidator validatorWithEditText = new PasswordValidator(mockEditText, 0, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX, errorMessage);
        PasswordValidator validatorWithTextInputLayout = new PasswordValidator(mockTextInputLayout, 0, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX, errorMessage);

        when(mockEditText.getText().toString()).thenReturn(value);

        assertEquals(validator.isNotValid(value), true);
        assertEquals(validatorWithEditText.validate(), false);
        assertEquals(validatorWithTextInputLayout.validate(), false);
    }

    @Test
    public void passwordContainsLettersAndNumbersCaseInsensitive() {
        String value = "qweQWE123";

        PasswordValidator validator = new PasswordValidator(LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX);
        PasswordValidator validatorWithEditText = new PasswordValidator(mockEditText, 0, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX, errorMessage);
        PasswordValidator validatorWithTextInputLayout = new PasswordValidator(mockTextInputLayout, 0, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX, errorMessage);

        when(mockEditText.getText().toString()).thenReturn(value);

        assertEquals(validator.isNotValid(value), false);
        assertEquals(validatorWithEditText.validate(), true);
        assertEquals(validatorWithTextInputLayout.validate(), true);
    }

    @Test
    public void passwordContainsLettersAndNumbersCaseInsensitiveAndLengthLessThan5() {
        String value = "qW3";

        PasswordValidator validator = new PasswordValidator(5, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX);
        PasswordValidator validatorWithEditText = new PasswordValidator(mockEditText, 5, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX, errorMessage);
        PasswordValidator validatorWithTextInputLayout = new PasswordValidator(mockTextInputLayout, 5, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX, errorMessage);

        when(mockEditText.getText().toString()).thenReturn(value);

        assertEquals(validator.isNotValid(value), true);
        assertEquals(validatorWithEditText.validate(), false);
        assertEquals(validatorWithTextInputLayout.validate(), false);
    }

    @Test
    public void passwordContainsLettersAndNumbersCaseInsensitiveAndLengthGreaterThan5() {
        String value = "qweQWE123";

        PasswordValidator validator = new PasswordValidator(5, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX);
        PasswordValidator validatorWithEditText = new PasswordValidator(mockEditText, 5, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX, errorMessage);
        PasswordValidator validatorWithTextInputLayout = new PasswordValidator(mockTextInputLayout, 5, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX, errorMessage);

        when(mockEditText.getText().toString()).thenReturn(value);

        assertEquals(validator.isNotValid(value), false);
        assertEquals(validatorWithEditText.validate(), true);
        assertEquals(validatorWithTextInputLayout.validate(), true);
    }

}