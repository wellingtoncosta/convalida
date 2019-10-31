package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 31/07/2019.
 */
public class PastDateValidatorTest extends BaseTest {

    private final String DATE_FORMAT = "dd/MM/yyyy";

    private final String LIMIT_DATE = "01/01/1990";

    private final String VALID_DATE = "01/01/2000";

    private final String INVALID_DATE = "01/01/1980";

    private final String INVALID_FORMATTED_DATE = "01-01-1980";

    @Test public void required_with_empty_value() {
        PastDateValidator validator = new PastDateValidator(
                mockEditText,
                errorMessage,
                DATE_FORMAT,
                LIMIT_DATE,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn(EMPTY_VALUE);

        assertFalse(validator.validate());
    }

    @Test public void non_required_with_empty_value() {
        PastDateValidator validator = new PastDateValidator(
                mockEditText,
                errorMessage,
                DATE_FORMAT,
                LIMIT_DATE,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn(EMPTY_VALUE);

        assertFalse(validator.validate());
    }

    @Test public void required_with_valid_date() {
        PastDateValidator validator = new PastDateValidator(
                mockEditText,
                errorMessage,
                DATE_FORMAT,
                LIMIT_DATE,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn(VALID_DATE);

        assertTrue(validator.validate());
    }

    @Test public void non_required_with_valid_date() {
        PastDateValidator validator = new PastDateValidator(
                mockEditText,
                errorMessage,
                DATE_FORMAT,
                LIMIT_DATE,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn(VALID_DATE);

        assertTrue(validator.validate());
    }

    @Test public void required_with_invalid_date() {
        PastDateValidator validator = new PastDateValidator(
                mockEditText,
                errorMessage,
                DATE_FORMAT,
                LIMIT_DATE,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn(INVALID_DATE);

        assertFalse(validator.validate());
    }

    @Test public void non_required_with_invalid_date() {
        PastDateValidator validator = new PastDateValidator(
                mockEditText,
                errorMessage,
                DATE_FORMAT,
                LIMIT_DATE,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn(INVALID_DATE);

        assertFalse(validator.validate());
    }

    @Test public void required_with_invalid_formatted_date() {
        PastDateValidator validator = new PastDateValidator(
                mockEditText,
                errorMessage,
                DATE_FORMAT,
                LIMIT_DATE,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn(INVALID_FORMATTED_DATE);

        assertFalse(validator.validate());
    }

    @Test public void non_required_with_invalid_formatted_date() {
        PastDateValidator validator = new PastDateValidator(
                mockEditText,
                errorMessage,
                DATE_FORMAT,
                LIMIT_DATE,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn(INVALID_FORMATTED_DATE);

        assertFalse(validator.validate());
    }

}
