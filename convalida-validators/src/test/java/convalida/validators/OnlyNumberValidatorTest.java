package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 01/11/2017.
 */
public class OnlyNumberValidatorTest extends BaseTest {

    @Test public void required_emptyValue() {
        OnlyNumberValidator validator = new OnlyNumberValidator(
                mockEditText,
                errorMessage,
                true,
                true);
        when(mockEditText.getText().toString()).thenReturn("");
        assertEquals(validator.validate(), false);
    }

    @Test public void required_validValue() {
        OnlyNumberValidator validator = new OnlyNumberValidator(
                mockEditText,
                errorMessage,
                true,
                true);
        when(mockEditText.getText().toString()).thenReturn("123");
        assertEquals(validator.validate(), true);
    }

    @Test public void nonRequired_emptyValue() {
        OnlyNumberValidator validator = new OnlyNumberValidator(
                mockEditText,
                errorMessage,
                true,
                false);
        when(mockEditText.getText().toString()).thenReturn("");
        assertEquals(validator.validate(), true);
    }

    @Test public void nonRequired_invalidValue() {
        OnlyNumberValidator validator = new OnlyNumberValidator(
                mockEditText,
                errorMessage,
                true,
                false);
        when(mockEditText.getText().toString()).thenReturn("test");
        assertEquals(validator.validate(), false);
    }

    @Test public void nonRequired_validValue() {
        OnlyNumberValidator validator = new OnlyNumberValidator(
                mockEditText,
                errorMessage,
                true,
                false);
        when(mockEditText.getText().toString()).thenReturn("1234");
        assertEquals(validator.validate(), true);
    }

}