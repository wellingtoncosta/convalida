package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Wellington Costa on 01/11/2017.
 */
public class PasswordValidatorTest {

    private static final String LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{1,}+$";

    @Test
    public void passwordIsNull() {
        PasswordValidator validator = new PasswordValidator();
        assertEquals(validator.isNotValid(null), true);
    }

    @Test
    public void emptyPassword() {
        PasswordValidator validator = new PasswordValidator();
        assertEquals(validator.isNotValid(""), true);
    }

    @Test
    public void passwordLessThan5() {
        PasswordValidator validator = new PasswordValidator(5);
        assertEquals(validator.isNotValid("test"), true);
    }

    @Test
    public void passwordGreaterThan5() {
        PasswordValidator validator = new PasswordValidator(5);
        assertEquals(validator.isNotValid("test123"), false);
    }

    @Test
    public void passwordNotContainsLettersAndNumbersCaseInsensitive() {
        PasswordValidator validator = new PasswordValidator(LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX);
        assertEquals(validator.isNotValid("qweQWE"), true);
    }

    @Test
    public void passwordContainsLettersAndNumbersCaseInsensitive() {
        PasswordValidator validator = new PasswordValidator(LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX);
        assertEquals(validator.isNotValid("qweQWE123"), false);
    }

    @Test
    public void passwordContainsLettersAndNumbersCaseInsensitiveAndLengthLessThan5() {
        PasswordValidator validator = new PasswordValidator(5, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX);
        assertEquals(validator.isNotValid("qW3"), true);
    }

    @Test
    public void passwordContainsLettersAndNumbersCaseInsensitiveAndLengthGreaterThan5() {
        PasswordValidator validator = new PasswordValidator(5, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX);
        assertEquals(validator.isNotValid("qweQWE123"), false);
    }

}