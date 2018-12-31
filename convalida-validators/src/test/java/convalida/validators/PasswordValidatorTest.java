package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 01/11/2017.
 */
public class PasswordValidatorTest extends BaseTest {

    private static final String LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{1,}+$";

    @Test public void emptyPassword() {
        PasswordValidator validator = new PasswordValidator(
                mockEditText,
                errorMessage,
                0,
                "",
                true
        );
        when(mockEditText.getText().toString()).thenReturn("");
        assertFalse(validator.validate());
    }

    @Test public void passwordLessThan5() {
        PasswordValidator validator = new PasswordValidator(
                mockEditText,
                errorMessage,
                5,
                "",
                true
        );
        when(mockEditText.getText().toString()).thenReturn("test");
        assertFalse(validator.validate());
    }

    @Test public void passwordGreaterThan5() {
        PasswordValidator validator = new PasswordValidator(
                mockEditText,
                errorMessage,
                5,
                "",
                true
        );
        when(mockEditText.getText().toString()).thenReturn("test123");
        assertTrue(validator.validate());
    }

    @Test public void passwordNotContainsLettersAndNumbersCaseInsensitive() {
        PasswordValidator validator = new PasswordValidator(
                mockEditText,
                errorMessage,
                0,
                LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX,
                true
        );
        when(mockEditText.getText().toString()).thenReturn("qweQWE");
        assertFalse(validator.validate());
    }

    @Test public void passwordContainsLettersAndNumbersCaseInsensitive() {
        PasswordValidator validator = new PasswordValidator(
                mockEditText,
                errorMessage,
                0,
                LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX,
                true
        );
        when(mockEditText.getText().toString()).thenReturn("qweQWE123");
        assertTrue(validator.validate());
    }

    @Test public void passwordContainsLettersAndNumbersCaseInsensitiveAndLengthLessThan5() {
        PasswordValidator validator = new PasswordValidator(
                mockEditText,
                errorMessage,
                5,
                LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX,
                true
        );
        when(mockEditText.getText().toString()).thenReturn("qW3");
        assertFalse(validator.validate());
    }

    @Test public void passwordContainsLettersAndNumbersCaseInsensitiveAndLengthGreaterThan5() {
        PasswordValidator validator = new PasswordValidator(
                mockEditText,
                errorMessage,
                5,
                LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX,
                true
        );
        when(mockEditText.getText().toString()).thenReturn("qweQWE123");
        assertTrue(validator.validate());
    }

}