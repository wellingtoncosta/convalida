package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 01/11/2017.
 */
public class PatternValidatorTest extends BaseTest {

    private static final String LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{1,}+$";

    @Test public void valueNotContainsLettersAndNumbers() {
        PatternValidator validatorWithEditText = new PatternValidator(mockEditText, errorMessage, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX, true);
        when(mockEditText.getText().toString()).thenReturn("qweQWE");
        assertEquals(validatorWithEditText.validate(), false);
    }

    @Test public void valueContainsLettersAndNumbers() {
        PatternValidator validatorWithEditText = new PatternValidator(mockEditText, errorMessage, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX, true);
        when(mockEditText.getText().toString()).thenReturn("qweQWE123");
        assertEquals(validatorWithEditText.validate(), true);
    }

}