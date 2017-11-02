package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Wellington Costa on 01/11/2017.
 */
public class PatternValidatorTest {

    private static final String LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{1,}+$";

    @Test
    public void valueNotContainsLettersAndNumbers() {
        PatternValidator validator = new PatternValidator(LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX);
        assertEquals(validator.isNotValid("qweQWE"), true);
    }

    @Test
    public void valueContainsLettersAndNumbers() {
        PatternValidator validator = new PatternValidator(LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX);
        assertEquals(validator.isNotValid("qweQWE123"), false);
    }

}