package convalida.validators;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 22/01/2018.
 */
public class ValidatorSetTest extends BaseTest {

    private ValidatorSet validatorSet;

    @Before
    public void setupValidator() {
        validatorSet = new ValidatorSet();
    }

    @Test
    public void addOneValidator() {
        validatorSet.addValidator(new NotEmptyValidator());
        assertEquals(validatorSet.getValidatorsSize(), 1);
    }

    @Test
    public void addTwoValidators() {
        validatorSet.addValidator(new NotEmptyValidator());
        validatorSet.addValidator(new EmailValidator());
        assertEquals(validatorSet.getValidatorsSize(), 2);
    }

    @Test
    public void addThreeValidators() {
        validatorSet.addValidator(new NotEmptyValidator());
        validatorSet.addValidator(new EmailValidator());
        validatorSet.addValidator(new LengthValidator(0, 5));
        assertEquals(validatorSet.getValidatorsSize(), 3);
    }

    @Test
    public void executeValidationsWithSuccess() {
        validatorSet.addValidator(new NotEmptyValidator(mockEditText, errorMessage));
        when(mockEditText.getText().toString()).thenReturn("test");
        assertEquals(validatorSet.isValid(), true);
    }

    @Test
    public void executeValidationsWithError() {
        validatorSet.addValidator(new NotEmptyValidator(mockEditText, errorMessage));
        when(mockEditText.getText().toString()).thenReturn("");
        assertEquals(validatorSet.isValid(), false);
    }

    @Test
    public void clearValidations() {
        validatorSet.addValidator(new NotEmptyValidator(mockEditText, errorMessage));
        validatorSet.clearValidators();
        assertEquals(mockEditText.getError(), null);
    }

}