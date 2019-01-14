package convalida.validators;

import convalida.validators.error.ValidationErrorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Wellington Costa on 01/11/2017.
 */
@RunWith(Suite.class)
@SuiteClasses({
        RequiredValidatorTest.class,
        EmailValidatorTest.class,
        ConfirmEmailValidatorTest.class,
        LengthValidatorTest.class,
        OnlyNumberValidatorTest.class,
        PatternValidatorTest.class,
        PasswordValidatorTest.class,
        ConfirmPasswordValidatorTest.class,
        CpfValidatorTest.class,
        BetweenValidatorTest.class,
        ValidatorSetTest.class,
        CreditCardValidatorTest.class,
        NumberLimitValidatorTest.class,
        ValidationErrorTest.class
})
public class TestSuite { }