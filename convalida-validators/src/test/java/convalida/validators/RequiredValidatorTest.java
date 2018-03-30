package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 01/11/2017.
 */
public class RequiredValidatorTest extends BaseTest {

    @Test public void emptyValue() {
        RequiredValidator validator = new RequiredValidator(mockEditText, errorMessage, true);
        when(mockEditText.getText().toString()).thenReturn("");
        assertEquals(validator.validate(), false);
    }

    @Test public void validValue() {
        RequiredValidator validator = new RequiredValidator(mockEditText, errorMessage, true);
        when(mockEditText.getText().toString()).thenReturn("test");
        assertEquals(validator.validate(), true);
    }

}