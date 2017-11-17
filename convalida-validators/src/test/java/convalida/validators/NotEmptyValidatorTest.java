package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 01/11/2017.
 */
public class NotEmptyValidatorTest extends BaseTest {

    @Test
    public void emptyValue() {
        String value = "";

        NotEmptyValidator validator = new NotEmptyValidator();
        NotEmptyValidator validatorWithEditText = new NotEmptyValidator(mockEditText, errorMessage);
        NotEmptyValidator validatorWithTextInputLayout = new NotEmptyValidator(mockTextInputLayout, errorMessage);

        when(mockEditText.getText().toString()).thenReturn(value);

        assertEquals(validator.isNotValid(null), true);
        assertEquals(validator.isNotValid(value), true);
        assertEquals(validatorWithEditText.validate(), false);
        assertEquals(validatorWithTextInputLayout.validate(), false);
    }

    @Test
    public void validValue() {
        String value = "test";

        NotEmptyValidator validator = new NotEmptyValidator();
        NotEmptyValidator validatorWithEditText = new NotEmptyValidator(mockEditText, errorMessage);
        NotEmptyValidator validatorWithTextInputLayout = new NotEmptyValidator(mockTextInputLayout, errorMessage);

        when(mockEditText.getText().toString()).thenReturn(value);

        assertEquals(validator.isNotValid(value), false);
        assertEquals(validatorWithEditText.validate(), true);
        assertEquals(validatorWithTextInputLayout.validate(), true);
    }

}