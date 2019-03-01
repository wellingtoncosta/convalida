package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 25/02/2019.
 */
public class IsbnValidatorTest extends BaseTest {

    @Test public void requiredWithEmptyValue() {
        IsbnValidator validator = new IsbnValidator(
                mockEditText,
                errorMessage,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn("");

        assertFalse(validator.validate());
    }

    @Test public void nonRequiredWithEmptyValue() {
        IsbnValidator validator = new IsbnValidator(
                mockEditText,
                errorMessage,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn("");

        assertTrue(validator.validate());
    }

    @Test public void requiredWithValidIsbn10() {
        IsbnValidator validator = new IsbnValidator(
                mockEditText,
                errorMessage,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn("3598215002");

        assertTrue(validator.validate());
    }

    @Test public void nonRequiredWithValidIsbn10() {
        IsbnValidator validator = new IsbnValidator(
                mockEditText,
                errorMessage,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn("3598215002");

        assertTrue(validator.validate());
    }

    @Test public void requiredWithInvalidIsbn10() {
        IsbnValidator validator = new IsbnValidator(
                mockEditText,
                errorMessage,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn("3598215009");

        assertFalse(validator.validate());
    }

    @Test public void nonRequiredWithInvalidIsbn10() {
        IsbnValidator validator = new IsbnValidator(
                mockEditText,
                errorMessage,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn("3598215009");

        assertFalse(validator.validate());
    }

    @Test public void requiredWithValidIsbn13() {
        IsbnValidator validator = new IsbnValidator(
                mockEditText,
                errorMessage,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn("9781234567897");

        assertTrue(validator.validate());
    }

    @Test public void nonRequiredWithValidIsbn13() {
        IsbnValidator validator = new IsbnValidator(
                mockEditText,
                errorMessage,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn("9781234567897");

        assertTrue(validator.validate());
    }

    @Test public void requiredWithInvalidIsbn13() {
        IsbnValidator validator = new IsbnValidator(
                mockEditText,
                errorMessage,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn("9781234567892");

        assertFalse(validator.validate());
    }

    @Test public void nonRequiredWithInvalidIsbn13() {
        IsbnValidator validator = new IsbnValidator(
                mockEditText,
                errorMessage,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn("9781234567892");

        assertFalse(validator.validate());
    }

}
