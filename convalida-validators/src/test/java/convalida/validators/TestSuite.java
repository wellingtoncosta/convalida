package convalida.validators;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Wellington Costa on 01/11/2017.
 */
@RunWith(Suite.class)
@SuiteClasses({
        NotEmptyValidatorTest.class,
        EmailValidatorTest.class,
        LengthValidatorTest.class,
        OnlyNumberValidatorTest.class,
        PatternValidatorTest.class,
        PasswordValidatorTest.class,
        ConfirmPasswordValidatorTest.class
})
public class TestSuite {

}