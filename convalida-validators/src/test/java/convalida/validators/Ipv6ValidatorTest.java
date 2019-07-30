package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 30/07/2019.
 */
public class Ipv6ValidatorTest extends BaseTest {

    private final String VALID_IPV6 = "67dc:742c:f48a:2e5e:dc9d:efb1:1210:52d2";
    private final String INVALID_IPV6 = "67dc:742c:f48a:2e5e:dc9d:efb1:1210:52dg";

    @Test public void requiredWithEmptyValue() {
        Ipv6Validator validator = new Ipv6Validator(
                mockEditText,
                errorMessage,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn("");

        assertFalse(validator.validate());
    }

    @Test public void nonRequiredWithEmptyValue() {
        Ipv6Validator validator = new Ipv6Validator(
                mockEditText,
                errorMessage,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn("");

        assertFalse(validator.validate());
    }

    @Test public void requiredWithValidIpv6() {
        Ipv6Validator validator = new Ipv6Validator(
                mockEditText,
                errorMessage,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn(VALID_IPV6);

        assertTrue(validator.validate());
    }

    @Test public void nonRequiredWithValidIpv6() {
        Ipv6Validator validator = new Ipv6Validator(
                mockEditText,
                errorMessage,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn(VALID_IPV6);

        assertTrue(validator.validate());
    }

    @Test public void requiredWithInvalidIpv6() {
        Ipv6Validator validator = new Ipv6Validator(
                mockEditText,
                errorMessage,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn(INVALID_IPV6);

        assertFalse(validator.validate());
    }

    @Test public void nonRequiredWithInvalidIpv6() {
        Ipv6Validator validator = new Ipv6Validator(
                mockEditText,
                errorMessage,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn(INVALID_IPV6);

        assertFalse(validator.validate());
    }

}
