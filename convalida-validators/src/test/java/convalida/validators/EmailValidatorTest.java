package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 01/11/2017.
 */
public class EmailValidatorTest extends BaseTest {

    @Test public void nonRequired_emptyValue() {
        EmailValidator validator = new EmailValidator(
                mockEditText,
                errorMessage,
                true,
                false);
        when(mockEditText.getText().toString()).thenReturn("");
        assertTrue(validator.validate());
    }

    @Test public void required_emptyValue() {
        EmailValidator validator = new EmailValidator(
                mockEditText,
                errorMessage,
                true,
                true);
        when(mockEditText.getText().toString()).thenReturn("");
        assertFalse(validator.validate());
    }

    @Test public void required_invalidEmail() {
        EmailValidator validator = new EmailValidator(
                mockEditText,
                errorMessage,
                true,
                true);
        when(mockEditText.getText().toString()).thenReturn("test@email");
        assertFalse(validator.validate());
    }

    @Test public void required_validEmail() {
        EmailValidator validator = new EmailValidator(
                mockEditText,
                errorMessage,
                true,
                true);
        when(mockEditText.getText().toString()).thenReturn("test@email.com");
        assertTrue(validator.validate());
    }

}
