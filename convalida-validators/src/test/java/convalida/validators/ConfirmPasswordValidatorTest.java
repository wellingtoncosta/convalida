package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Wellington Costa on 02/11/2017.
 */
public class ConfirmPasswordValidatorTest {

    @Test
    public void passwordsDoesNotMatch() {
        String password = "test@123";
        String confirmPassword = "test@12";
        ConfirmPasswordValidator validator = new ConfirmPasswordValidator(password);
        assertEquals(validator.isNotValid(confirmPassword), true);
    }

    @Test
    public void passwordsMatch() {
        String password = "test@123";
        String confirmPassword = "test@123";
        ConfirmPasswordValidator validator = new ConfirmPasswordValidator(password);
        assertEquals(validator.isNotValid(confirmPassword), false);
    }

}