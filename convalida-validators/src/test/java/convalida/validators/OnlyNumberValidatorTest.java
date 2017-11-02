package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Wellington Costa on 01/11/2017.
 */
public class OnlyNumberValidatorTest {

    @Test
    public void valueIsNotNumber() {
        OnlyNumberValidator validator = new OnlyNumberValidator();
        assertEquals(validator.isNotValid("test"), true);
    }

    @Test
    public void valueIsNumber() {
        OnlyNumberValidator validator = new OnlyNumberValidator();
        assertEquals(validator.isNotValid("1234"), false);
    }

}