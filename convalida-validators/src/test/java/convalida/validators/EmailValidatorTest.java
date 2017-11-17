package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 01/11/2017.
 */
public class EmailValidatorTest extends BaseTest {

    @Test
    public void emailIsNull() {
        EmailValidator validator = new EmailValidator();
        assertEquals(validator.isNotValid(null), true);
    }
    
    @Test
    public void invalidEmail() {
        String value = "test@email";

        EmailValidator validator = new EmailValidator();
        EmailValidator validatorWithEditText = new EmailValidator(mockEditText, errorMessage);
        EmailValidator validatorWithTextInputLayout = new EmailValidator(mockTextInputLayout, errorMessage);

        when(mockEditText.getText().toString()).thenReturn(value);

        assertEquals(validator.isNotValid(value), true);
        assertEquals(validatorWithEditText.validate(), false);
        assertEquals(validatorWithTextInputLayout.validate(), false);
    }

    @Test
    public void validEmail() {
        String value = "test@email.com";

        EmailValidator validator = new EmailValidator();
        EmailValidator validatorWithEditText = new EmailValidator(mockEditText, errorMessage);
        EmailValidator validatorWithTextInputLayout = new EmailValidator(mockTextInputLayout, errorMessage);

        when(mockEditText.getText().toString()).thenReturn(value);

        assertEquals(validator.isNotValid(value), false);
        assertEquals(validatorWithEditText.validate(), true);
        assertEquals(validatorWithTextInputLayout.validate(), true);
    }

}
