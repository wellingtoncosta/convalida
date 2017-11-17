package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 01/11/2017.
 */
public class PatternValidatorTest extends BaseTest {

    private static final String LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{1,}+$";

    @Test
    public void valueNotContainsLettersAndNumbers() {
        String value = "qweQWE";

        PatternValidator validator = new PatternValidator(LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX);
        PatternValidator validatorWithEditText = new PatternValidator(mockEditText, errorMessage, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX);
        PatternValidator validatorWithTextInputLayout = new PatternValidator(mockTextInputLayout, errorMessage, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX);

        when(mockEditText.getText().toString()).thenReturn(value);

        assertEquals(validator.isNotValid(null), true);
        assertEquals(validator.isNotValid(value), true);
        assertEquals(validatorWithEditText.validate(), false);
        assertEquals(validatorWithTextInputLayout.validate(), false);
    }

    @Test
    public void valueContainsLettersAndNumbers() {
        String value = "qweQWE123";

        PatternValidator validator = new PatternValidator(LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX);
        PatternValidator validatorWithEditText = new PatternValidator(mockEditText, errorMessage, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX);
        PatternValidator validatorWithTextInputLayout = new PatternValidator(mockTextInputLayout, errorMessage, LETTERS_AND_NUMBERS_CASE_INSENSITIVE_REGEX);

        when(mockEditText.getText().toString()).thenReturn(value);

        assertEquals(validator.isNotValid(value), false);
        assertEquals(validatorWithEditText.validate(), true);
        assertEquals(validatorWithTextInputLayout.validate(), true);
    }

}