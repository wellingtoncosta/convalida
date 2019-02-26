package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class CnpjValidatorTest extends BaseTest {

    @Test public void requiredWithEmptyValue() {
        CnpjValidator validator = new CnpjValidator(
                mockEditText,
                errorMessage,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn("");

        assertFalse(validator.validate());
    }

    @Test public void nonRequiredWithEmptyValue() {
        CnpjValidator validator = new CnpjValidator(
                mockEditText,
                errorMessage,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn("");

        assertTrue(validator.validate());
    }

    @Test public void requiredWithValidCnpj() {
        CnpjValidator validator = new CnpjValidator(
                mockEditText,
                errorMessage,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn("39321557000160");

        assertTrue(validator.validate());
    }

    @Test public void nonRequiredWithValidCnpj() {
        CnpjValidator validator = new CnpjValidator(
                mockEditText,
                errorMessage,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn("39321557000160");

        assertTrue(validator.validate());
    }

    @Test public void requiredWithInvalidCnpj() {
        CnpjValidator validator = new CnpjValidator(
                mockEditText,
                errorMessage,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn("39321557000169");

        assertFalse(validator.validate());
    }

    @Test public void nonRequiredWithInvalidCnpj() {
        CnpjValidator validator = new CnpjValidator(
                mockEditText,
                errorMessage,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn("39321557000169");

        assertFalse(validator.validate());
    }

    @Test public void requiredWithInvalidCnpjLength() {
        CnpjValidator validator = new CnpjValidator(
                mockEditText,
                errorMessage,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn("393215570001");

        assertFalse(validator.validate());
    }

    @Test public void nonRequiredWithInvalidCnpjLength() {
        CnpjValidator validator = new CnpjValidator(
                mockEditText,
                errorMessage,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn("393215570001");

        assertFalse(validator.validate());
    }

    @Test public void requiredWithRepeatedCnpj() {
        CnpjValidator validator = new CnpjValidator(
                mockEditText,
                errorMessage,
                true,
                true);

        when(mockEditText.getText().toString()).thenReturn("11111111111111");

        assertFalse(validator.validate());
    }

    @Test public void nonRequiredWithRepeatedCnpj() {
        CnpjValidator validator = new CnpjValidator(
                mockEditText,
                errorMessage,
                true,
                false);

        when(mockEditText.getText().toString()).thenReturn("11111111111111");

        assertFalse(validator.validate());
    }

}
