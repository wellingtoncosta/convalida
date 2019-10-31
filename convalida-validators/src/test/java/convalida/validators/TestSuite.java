package convalida.validators;

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
        NumericLimitValidatorTest.class,
        IsbnValidatorTest.class,
        CnpjValidatorTest.class,
        ValidationErrorTest.class,
        StringsTest.class,
        Ipv4ValidatorTest.class,
        Ipv6ValidatorTest.class,
        UrlValidatorTest.class,
        PastDateValidatorTest.class,
        FutureDateValidatorTest.class
})
public class TestSuite { }