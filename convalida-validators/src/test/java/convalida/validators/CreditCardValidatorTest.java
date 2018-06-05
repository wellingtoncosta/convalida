package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 31/05/18.
 */
public class CreditCardValidatorTest extends BaseTest {

    // Credit card number generated on http://www.getcreditcardnumbers.com/

    @Test public void nonRequired_emptyValue() {
        CreditCardValidator validator = new CreditCardValidator(
                mockEditText,
                errorMessage,
                true,
                false);
        when(mockEditText.getText().toString()).thenReturn("");
        assertEquals(validator.validate(), true);
    }

    @Test public void required_emptyValue() {
        CreditCardValidator validator = new CreditCardValidator(
                mockEditText,
                errorMessage,
                true,
                true);
        when(mockEditText.getText().toString()).thenReturn("");
        assertEquals(validator.validate(), false);
    }

    @Test public void invalidNumber() {
        CreditCardValidator validator = new CreditCardValidator(
                mockEditText,
                errorMessage,
                true,
                true);
        when(mockEditText.getText().toString()).thenReturn("9999999999999900");
        assertEquals(validator.validate(), false);
    }

    @Test public void validNumber() {
        CreditCardValidator validator = new CreditCardValidator(
                mockEditText,
                errorMessage,
                true,
                true);
        when(mockEditText.getText().toString()).thenReturn("4716418615485100");
        assertEquals(validator.validate(), true);
    }

    @Test public void validNumberWithSpaces() {
        CreditCardValidator validator = new CreditCardValidator(
                mockEditText,
                errorMessage,
                true,
                true);
        when(mockEditText.getText().toString()).thenReturn("4716 4186 1548 5100");
        assertEquals(validator.validate(), true);
    }

}
