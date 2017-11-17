package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 01/11/2017.
 */
public class LengthValidatorTest extends BaseTest {

    @Test
    public void nullValue() {
        LengthValidator validator = new LengthValidator(0, 5);
        assertEquals(validator.isNotValid(null), true);
    }

    @Test
    public void textLengthLessThan5() {
        String value = "test";

        LengthValidator validator = new LengthValidator(5, 0);
        LengthValidator validatorWithEditText = new LengthValidator(mockEditText, 5, 0, errorMessage);
        LengthValidator validatorWithTextInputLayout = new LengthValidator(mockTextInputLayout, 5, 0,  errorMessage);

        when(mockEditText.getText().toString()).thenReturn(value);

        assertEquals(validator.isNotValid(value), true);
        assertEquals(validatorWithEditText.validate(), false);
        assertEquals(validatorWithTextInputLayout.validate(), false);
    }

    @Test
    public void textLengthGreaterThan5() {
        String value = "test@test";

        LengthValidator validator = new LengthValidator(5, 0);
        LengthValidator validatorWithEditText = new LengthValidator(mockEditText, 5, 0, errorMessage);
        LengthValidator validatorWithTextInputLayout = new LengthValidator(mockTextInputLayout, 5, 0,  errorMessage);

        when(mockEditText.getText().toString()).thenReturn(value);

        assertEquals(validator.isNotValid(value), false);
        assertEquals(validatorWithEditText.validate(), true);
        assertEquals(validatorWithTextInputLayout.validate(), true);
    }

    @Test
    public void textLengthLessThan8() {
        String value = "test@test";

        LengthValidator validator = new LengthValidator(0, 8);
        LengthValidator validatorWithEditText = new LengthValidator(mockEditText, 0, 8, errorMessage);
        LengthValidator validatorWithTextInputLayout = new LengthValidator(mockTextInputLayout, 0, 8,  errorMessage);

        when(mockEditText.getText().toString()).thenReturn(value);

        assertEquals(validator.isNotValid(value), true);
        assertEquals(validatorWithEditText.validate(), false);
        assertEquals(validatorWithTextInputLayout.validate(), false);
    }

    @Test
    public void textLengthGreaterThan8() {
        String value = "test@test";

        LengthValidator validator = new LengthValidator(0, 9);
        LengthValidator validatorWithEditText = new LengthValidator(mockEditText, 0, 9, errorMessage);
        LengthValidator validatorWithTextInputLayout = new LengthValidator(mockTextInputLayout, 0, 9,  errorMessage);

        when(mockEditText.getText().toString()).thenReturn(value);

        assertEquals(validator.isNotValid(value), false);
        assertEquals(validatorWithEditText.validate(), true);
        assertEquals(validatorWithTextInputLayout.validate(), true);
    }

}