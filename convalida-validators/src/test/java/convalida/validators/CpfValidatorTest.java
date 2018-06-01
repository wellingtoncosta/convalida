package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author WellingtonCosta on 25/04/18.
 */
public class CpfValidatorTest extends BaseTest {

    // Cpf generated on https://www.4devs.com.br/gerador_de_cpf

    @Test public void required_emtpyValue() {
        CpfValidator validator = new CpfValidator(
                mockEditText,
                errorMessage,
                true,
                true
        );
        when(mockEditText.getText().toString()).thenReturn("");
        assertEquals(validator.validate(), false);
    }

    @Test public void required_invalidCpf() {
        CpfValidator validator = new CpfValidator(
                mockEditText,
                errorMessage,
                true,
                true
        );
        when(mockEditText.getText().toString()).thenReturn("11122233300");
        assertEquals(validator.validate(), false);
    }

    @Test public void required_validCpf() {
        CpfValidator validator = new CpfValidator(
                mockEditText,
                errorMessage,
                true,
                true
        );
        when(mockEditText.getText().toString()).thenReturn("32454401037");
        assertEquals(validator.validate(), true);
    }

    @Test public void nonRequired_emtpyValue() {
        CpfValidator validator = new CpfValidator(
                mockEditText,
                errorMessage,
                true,
                false
        );
        when(mockEditText.getText().toString()).thenReturn("");
        assertEquals(validator.validate(), true);
    }

    @Test public void nonRequired_invalidCpf() {
        CpfValidator validator = new CpfValidator(
                mockEditText,
                errorMessage,
                true,
                false
        );
        when(mockEditText.getText().toString()).thenReturn("11122233300");
        assertEquals(validator.validate(), false);
    }

    @Test public void nonRequired_invalidCpfWithChars() {
        CpfValidator validator = new CpfValidator(
                mockEditText,
                errorMessage,
                true,
                false
        );
        when(mockEditText.getText().toString()).thenReturn("111abc33300");
        assertEquals(validator.validate(), false);
    }

    @Test public void nonRequired_invalidCpfDigits() {
        CpfValidator validator = new CpfValidator(
                mockEditText,
                errorMessage,
                true,
                false
        );
        when(mockEditText.getText().toString()).thenReturn("32454401017");
        assertEquals(validator.validate(), false);
    }

    @Test public void nonRequired_validCpf() {
        CpfValidator validator = new CpfValidator(
                mockEditText,
                errorMessage,
                true,
                false
        );
        when(mockEditText.getText().toString()).thenReturn("32454401037");
        assertEquals(validator.validate(), true);
    }

    @Test public void nonRequired_cpfWithInvalidSize() {
        CpfValidator validator = new CpfValidator(
                mockEditText,
                errorMessage,
                true,
                false
        );
        when(mockEditText.getText().toString()).thenReturn("123456789");
        assertEquals(validator.validate(), false);
    }

    @Test public void nonRequired_invalidCpf_BlackList() {
        CpfValidator validator = new CpfValidator(
                mockEditText,
                errorMessage,
                true,
                false
        );
        when(mockEditText.getText().toString()).thenReturn("12345678909");
        assertEquals(validator.validate(), false);
    }

    @Test public void nonRequired_invalidCpf_BlackList_2() {
        CpfValidator validator = new CpfValidator(
                mockEditText,
                errorMessage,
                true,
                false
        );
        when(mockEditText.getText().toString()).thenReturn("11111111111");
        assertEquals(validator.validate(), false);
    }

}