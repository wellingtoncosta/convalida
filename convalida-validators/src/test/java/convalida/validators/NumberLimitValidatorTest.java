package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author wellingtoncosta on 04/06/18
 */
public class NumberLimitValidatorTest extends BaseTest {

    @Test public void nonRequired_emptyValue() {
        NumberLimitValidator validator = new NumberLimitValidator(
                mockEditText,
                errorMessage,
                true,
                "0",
                "10",
                false);
        when(mockEditText.getText().toString()).thenReturn("");
        assertEquals(validator.validate(), true);
    }

    @Test public void required_emptyValue() {
        NumberLimitValidator validator = new NumberLimitValidator(
                mockEditText,
                errorMessage,
                true,
                "0",
                "10",
                true);
        when(mockEditText.getText().toString()).thenReturn("");
        assertEquals(validator.validate(), false);
    }

    @Test public void validValueBetween_0_And_10() {
        NumberLimitValidator validator = new NumberLimitValidator(
                mockEditText,
                errorMessage,
                true,
                "0",
                "10",
                true);
        when(mockEditText.getText().toString()).thenReturn("9");
        assertEquals(validator.validate(), true);
    }

    @Test public void invalidValueBetween_0_And_10() {
        NumberLimitValidator validator = new NumberLimitValidator(
                mockEditText,
                errorMessage,
                true,
                "0",
                "10",
                true);
        when(mockEditText.getText().toString()).thenReturn("10.1");
        assertEquals(validator.validate(), false);
        when(mockEditText.getText().toString()).thenReturn("-1");
        assertEquals(validator.validate(), false);
    }


    @Test public void valueIsNotANumber() {
        NumberLimitValidator validator = new NumberLimitValidator(
                mockEditText,
                errorMessage,
                true,
                "0",
                "10",
                true);
        when(mockEditText.getText().toString()).thenReturn("abc");
        assertEquals(validator.validate(), false);
    }

}