package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author wellingtoncosta on 04/06/18
 */
public class NumericLimitValidatorTest extends BaseTest {

    @Test public void nonRequired_emptyValue() {
        NumericLimitValidator validator = new NumericLimitValidator(
                mockEditText,
                errorMessage,
                true,
                "0",
                "10",
                false);
        when(mockEditText.getText().toString()).thenReturn("");
        assertTrue(validator.validate());
    }

    @Test public void required_emptyValue() {
        NumericLimitValidator validator = new NumericLimitValidator(
                mockEditText,
                errorMessage,
                true,
                "0",
                "10",
                true);
        when(mockEditText.getText().toString()).thenReturn("");
        assertFalse(validator.validate());
    }

    @Test public void validValueBetween_0_And_10() {
        NumericLimitValidator validator = new NumericLimitValidator(
                mockEditText,
                errorMessage,
                true,
                "0",
                "10",
                true);
        when(mockEditText.getText().toString()).thenReturn("9");
        assertTrue(validator.validate());
    }

    @Test public void invalidValueBetween_0_And_10() {
        NumericLimitValidator validator = new NumericLimitValidator(
                mockEditText,
                errorMessage,
                true,
                "0",
                "10",
                true);
        when(mockEditText.getText().toString()).thenReturn("10.1");
        assertFalse(validator.validate());
        when(mockEditText.getText().toString()).thenReturn("-1");
        assertFalse(validator.validate());
    }


    @Test public void valueIsNotANumber() {
        NumericLimitValidator validator = new NumericLimitValidator(
                mockEditText,
                errorMessage,
                true,
                "0",
                "10",
                true);
        when(mockEditText.getText().toString()).thenReturn("abc");
        assertFalse(validator.validate());
    }

}