package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 01/11/2017.
 */
public class OnlyNumberValidatorTest extends BaseTest {

    @Test public void valueIsNotNumber() {
        OnlyNumberValidator validator = new OnlyNumberValidator(mockEditText, errorMessage, true);
        when(mockEditText.getText().toString()).thenReturn("test");
        assertEquals(validator.validate(), false);
    }

    @Test public void valueIsNumber() {
        OnlyNumberValidator validatorWithEditText = new OnlyNumberValidator(mockEditText, errorMessage, true);
        when(mockEditText.getText().toString()).thenReturn("1234");
        assertEquals(validatorWithEditText.validate(), true);
    }

}