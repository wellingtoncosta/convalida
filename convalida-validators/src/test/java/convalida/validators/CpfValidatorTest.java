package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author WellingtonCosta on 25/04/18.
 */
public class CpfValidatorTest extends BaseTest {

    // Cpf generated on https://www.4devs.com.br/gerador_de_cpf
    @Test public void validCpf() {
        CpfValidator validator = new CpfValidator(mockEditText, errorMessage, true);
        when(mockEditText.getText().toString()).thenReturn("32454401037");
        assertEquals(validator.validate(), true);
    }

    @Test public void invalidCpf() {
        CpfValidator validator = new CpfValidator(mockEditText, errorMessage, true);
        when(mockEditText.getText().toString()).thenReturn("11122233300");
        assertEquals(validator.validate(), false);
    }

}
