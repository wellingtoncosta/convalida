package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Wellington Costa on 01/11/2017.
 */
public class EmailValidatorTest {

    @Test
    public void emptyEmail() {
        EmailValidator validator = new EmailValidator();
        assertEquals(validator.isNotValid(""), true);
    }

    @Test
    public void invalidEmail() throws Exception {
        EmailValidator validator = new EmailValidator();
        assertEquals(validator.isNotValid("test@email"), true);
    }

    @Test
    public void validEmail() throws Exception {
        EmailValidator validator = new EmailValidator();
        assertEquals(validator.isNotValid("test@email.com"), false);
    }

}