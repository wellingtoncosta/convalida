package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 30/07/2019.
 */
public class Ipv4ValidatorTest extends BaseTest {

    @Test public void requiredWithEmptyValue() {
        Ipv4Validator validator = new Ipv4Validator(
                mockEditText,
                errorMessage,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn("");

        assertFalse(validator.validate());
    }

    @Test public void nonRequiredWithEmptyValue() {
        Ipv4Validator validator = new Ipv4Validator(
                mockEditText,
                errorMessage,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn("");

        assertFalse(validator.validate());
    }

    @Test public void requiredWithValidIpv4() {
        Ipv4Validator validator = new Ipv4Validator(
                mockEditText,
                errorMessage,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn("192.168.1.1");

        assertTrue(validator.validate());
    }

    @Test public void nonRequiredWithValidIpv4() {
        Ipv4Validator validator = new Ipv4Validator(
                mockEditText,
                errorMessage,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn("192.168.1.1");

        assertTrue(validator.validate());
    }

    @Test public void requiredWithInvalidIpv4() {
        Ipv4Validator validator = new Ipv4Validator(
                mockEditText,
                errorMessage,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn("300.0.0.1");

        assertFalse(validator.validate());
    }

    @Test public void nonRequiredWithInvalidIpv4() {
        Ipv4Validator validator = new Ipv4Validator(
                mockEditText,
                errorMessage,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn("300.0.0.1");

        assertFalse(validator.validate());
    }

}
