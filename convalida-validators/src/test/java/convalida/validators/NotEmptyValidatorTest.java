package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 01/11/2017.
 */
public class NotEmptyValidatorTest extends BaseTest {

    @Test public void emptyValue() {
        NotEmptyValidator validator = new NotEmptyValidator(mockEditText, errorMessage, true);
        when(mockEditText.getText().toString()).thenReturn("");
        assertEquals(validator.validate(), false);
    }

    @Test public void validValue() {
        NotEmptyValidator validator = new NotEmptyValidator(mockEditText, errorMessage, true);
        when(mockEditText.getText().toString()).thenReturn("test");
        assertEquals(validator.validate(), true);
    }

}