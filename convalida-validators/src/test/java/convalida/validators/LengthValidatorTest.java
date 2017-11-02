package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Wellington Costa on 01/11/2017.
 */
public class LengthValidatorTest {

    @Test
    public void textLengthGreaterThan5() {
        LengthValidator validator = new LengthValidator(5, 0);
        assertEquals(validator.isNotValid("test"), true);
    }

    @Test
    public void textLengthLessThan5() {
        LengthValidator validator = new LengthValidator(4, 0);
        assertEquals(validator.isNotValid("test"), false);
    }

    @Test
    public void textLengthLessThan8() {
        LengthValidator validator = new LengthValidator(0, 8);
        assertEquals(validator.isNotValid("test@test"), true);
    }

    @Test
    public void textLengthGreaterThan8() {
        LengthValidator validator = new LengthValidator(0, 9);
        assertEquals(validator.isNotValid("test@test"), false);
    }

}