package convalida.validators;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Wellington Costa on 01/11/2017.
 */
public class NotEmptyValidatorTest {

    @Test
    public void isEmptyValue() throws Exception {
        NotEmptyValidator validator = new NotEmptyValidator();
        assertEquals(validator.isNotValid(""), true);
    }

    @Test
    public void isValidValue() throws Exception {
        NotEmptyValidator validator = new NotEmptyValidator();
        assertEquals(validator.isNotValid("test"), false);
    }

}