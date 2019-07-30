package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 30/07/2019.
 */
public class UrlValidatorTest extends BaseTest {

    private final String VALID_URL = "https://www.google.com";
    private final String VALID_LOCAL_URL = "http://localhost";
    
    private final String INVALID_URL = "google.com";
    private final String INVALID_LOCAL_URL = "localhost";

    @Test public void requiredWithEmptyValue() {
        UrlValidator validator = new UrlValidator(
                mockEditText,
                errorMessage,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn("");

        assertFalse(validator.validate());
    }

    @Test public void nonRequiredWithEmptyValue() {
        UrlValidator validator = new UrlValidator(
                mockEditText,
                errorMessage,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn("");

        assertFalse(validator.validate());
    }

    @Test public void requiredWithValidUrl() {
        UrlValidator validator = new UrlValidator(
                mockEditText,
                errorMessage,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn(VALID_URL);

        assertTrue(validator.validate());
    }

    @Test public void nonRequiredWithValidUrl() {
        UrlValidator validator = new UrlValidator(
                mockEditText,
                errorMessage,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn(VALID_URL);

        assertTrue(validator.validate());
    }

    @Test public void requiredWithInvalidUrl() {
        UrlValidator validator = new UrlValidator(
                mockEditText,
                errorMessage,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn(INVALID_URL);

        assertFalse(validator.validate());
    }

    @Test public void nonRequiredWithInvalidUrl() {
        UrlValidator validator = new UrlValidator(
                mockEditText,
                errorMessage,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn(INVALID_URL);

        assertFalse(validator.validate());
    }

    @Test public void requiredWithValidLocalUrl() {
        UrlValidator validator = new UrlValidator(
                mockEditText,
                errorMessage,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn(VALID_LOCAL_URL);

        assertTrue(validator.validate());
    }

    @Test public void nonRequiredWithValidLocalUrl() {
        UrlValidator validator = new UrlValidator(
                mockEditText,
                errorMessage,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn(VALID_LOCAL_URL);

        assertTrue(validator.validate());
    }

    @Test public void requiredWithInvalidLocalUrl() {
        UrlValidator validator = new UrlValidator(
                mockEditText,
                errorMessage,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn(INVALID_LOCAL_URL);

        assertFalse(validator.validate());
    }

    @Test public void nonRequiredWithInvalidLocalUrl() {
        UrlValidator validator = new UrlValidator(
                mockEditText,
                errorMessage,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn(INVALID_LOCAL_URL);

        assertFalse(validator.validate());
    }

}
