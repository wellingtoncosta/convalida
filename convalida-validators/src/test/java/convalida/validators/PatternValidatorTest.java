package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 01/11/2017.
 */
public class PatternValidatorTest extends BaseTest {

    private static final String LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{1,}+$";

    @Test public void required_emptyValue() {
        PatternValidator validator = new PatternValidator(
                mockEditText,
                errorMessage,
                LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX,
                true,
                true);
        when(mockEditText.getText().toString()).thenReturn("");
        assertEquals(validator.validate(), false);
    }

    @Test public void valueNotContainsLettersAndNumbers() {
        PatternValidator validator = new PatternValidator(
                mockEditText,
                errorMessage,
                LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX,
                true,
                false);
        when(mockEditText.getText().toString()).thenReturn("qweQWE");
        assertEquals(validator.validate(), false);
    }

    @Test public void valueContainsLettersAndNumbers() {
        PatternValidator validator = new PatternValidator(
                mockEditText,
                errorMessage,
                LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX,
                true,
                false);
        when(mockEditText.getText().toString()).thenReturn("qweQWE123");
        assertEquals(validator.validate(), true);
    }

}