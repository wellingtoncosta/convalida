package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 01/11/2017.
 */
public class OnlyNumberValidatorTest extends BaseTest {

    @Test
    public void valueIsNotNumber() {
        String value = "test";

        OnlyNumberValidator validator = new OnlyNumberValidator();
        OnlyNumberValidator validatorWithEditText = new OnlyNumberValidator(mockEditText, errorMessage);
        OnlyNumberValidator validatorWithTextInputLayout = new OnlyNumberValidator(mockTextInputLayout, errorMessage);

        when(mockEditText.getText().toString()).thenReturn(value);

        assertEquals(validator.isNotValid(null), true);
        assertEquals(validator.isNotValid(value), true);
        assertEquals(validatorWithEditText.validate(), false);
        assertEquals(validatorWithTextInputLayout.validate(), false);
    }

    @Test
    public void valueIsNumber() {
        String value = "1234";

        OnlyNumberValidator validator = new OnlyNumberValidator();
        OnlyNumberValidator validatorWithEditText = new OnlyNumberValidator(mockEditText, errorMessage);
        OnlyNumberValidator validatorWithTextInputLayout = new OnlyNumberValidator(mockTextInputLayout, errorMessage);

        when(mockEditText.getText().toString()).thenReturn(value);

        assertEquals(validator.isNotValid(value), false);
        assertEquals(validatorWithEditText.validate(), true);
        assertEquals(validatorWithTextInputLayout.validate(), true);
    }

}