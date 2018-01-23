package convalida.validators;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author Wellington Costa on 22/01/2018.
 */
public class ConvalidaValidatorTest {

    @Test
    public void executeValidations() {
        assertEquals(ConvalidaValidator.EMPTY.validateFields(), true);
    }

    @Test
    public void clearValidations() {
        ConvalidaValidator.EMPTY.clearValidations();
    }


}