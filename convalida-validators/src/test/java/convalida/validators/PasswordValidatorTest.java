package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 01/11/2017.
 */
public class PasswordValidatorTest extends BaseTest {

    private static final String LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{1,}+$";

    @Test public void emptyPassword() {
        PasswordValidator validator = new PasswordValidator(mockEditText, 0, "", errorMessage, true);
        when(mockEditText.getText().toString()).thenReturn("");
        assertEquals(validator.validate(), false);
    }

    @Test public void passwordLessThan5() {
        PasswordValidator validator = new PasswordValidator(mockEditText, 5, "", errorMessage, true);
        when(mockEditText.getText().toString()).thenReturn("test");
        assertEquals(validator.validate(), false);
    }

    @Test public void passwordGreaterThan5() {
        PasswordValidator validator = new PasswordValidator(mockEditText, 5, "", errorMessage, true);
        when(mockEditText.getText().toString()).thenReturn("test123");
        assertEquals(validator.validate(), true);
    }

    @Test public void passwordNotContainsLettersAndNumbersCaseInsensitive() {
        PasswordValidator validator = new PasswordValidator(mockEditText, 0, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX, errorMessage, true);
        when(mockEditText.getText().toString()).thenReturn("qweQWE");
        assertEquals(validator.validate(), false);
    }

    @Test public void passwordContainsLettersAndNumbersCaseInsensitive() {
        PasswordValidator validator = new PasswordValidator(mockEditText, 0, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX, errorMessage, true);
        when(mockEditText.getText().toString()).thenReturn("qweQWE123");
        assertEquals(validator.validate(), true);
    }

    @Test public void passwordContainsLettersAndNumbersCaseInsensitiveAndLengthLessThan5() {
        PasswordValidator validator = new PasswordValidator(mockEditText, 5, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX, errorMessage, true);
        when(mockEditText.getText().toString()).thenReturn("qW3");
        assertEquals(validator.validate(), false);
    }

    @Test public void passwordContainsLettersAndNumbersCaseInsensitiveAndLengthGreaterThan5() {
        PasswordValidator validator = new PasswordValidator(mockEditText, 5, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX, errorMessage, true);
        when(mockEditText.getText().toString()).thenReturn("qweQWE123");
        assertEquals(validator.validate(), true);
    }

}