package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 01/11/2017.
 */
public class LengthValidatorTest extends BaseTest {

    @Test public void required_emptyValue() {
        LengthValidator validator = new LengthValidator(
                mockEditText,
                errorMessage,
                5,
                0,
                true,
                true);
        when(mockEditText.getText().toString()).thenReturn("");
        assertFalse(validator.validate());
    }

    @Test public void required_validaValue() {
        LengthValidator validator = new LengthValidator(
                mockEditText,
                errorMessage,
                1,
                0,
                true,
                true);
        when(mockEditText.getText().toString()).thenReturn("test");
        assertTrue(validator.validate());
    }

    @Test public void nonRequired_emptyValue() {
        LengthValidator validator = new LengthValidator(
                mockEditText,
                errorMessage,
                5,
                0,
                true,
                false);
        when(mockEditText.getText().toString()).thenReturn("");
        assertTrue(validator.validate());
    }

    @Test public void nonRequired_textLengthLessThan5() {
        LengthValidator validator = new LengthValidator(
                mockEditText,
                errorMessage,
                5,
                0,
                true,
                false);
        when(mockEditText.getText().toString()).thenReturn("test");
        assertFalse(validator.validate());
    }

    @Test public void nonRequired_textLengthGreaterThan5() {
        LengthValidator validator = new LengthValidator(
                mockEditText,
                errorMessage,
                5,
                0,
                true,
                false);
        when(mockEditText.getText().toString()).thenReturn("test@test");
        assertTrue(validator.validate());
    }

    @Test public void nonRequired_textLengthLessThan8() {
        LengthValidator validatorWithEditText = new LengthValidator(
                mockEditText,
                errorMessage,
                0,
                8,
                true,
                false);
        when(mockEditText.getText().toString()).thenReturn("test@test");
        assertFalse(validatorWithEditText.validate());
    }

    @Test public void nonRequired_textLengthGreaterThan8() {
        LengthValidator validatorWithEditText = new LengthValidator(
                mockEditText,
                errorMessage,

                0,
                9,
                true,
                false);
        when(mockEditText.getText().toString()).thenReturn("test@test");
        assertTrue(validatorWithEditText.validate());
    }

}