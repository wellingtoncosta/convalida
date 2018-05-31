package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
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
        assertEquals(validator.validate(), true);
    }

    @Test public void required_emptyValue() {
        EmailValidator validator = new EmailValidator(
                mockEditText,
                errorMessage,
                true,
                true);
        when(mockEditText.getText().toString()).thenReturn("");
        assertEquals(validator.validate(), false);
    }

    @Test public void required_invalidEmail() {
        EmailValidator validator = new EmailValidator(
                mockEditText,
                errorMessage,
                true,
                true);
        when(mockEditText.getText().toString()).thenReturn("test@email");
        assertEquals(validator.validate(), false);
    }

    @Test public void required_validEmail() {
        EmailValidator validator = new EmailValidator(
                mockEditText,
                errorMessage,
                true,
                true);
        when(mockEditText.getText().toString()).thenReturn("test@email.com");
        assertEquals(validator.validate(), true);
    }

}
