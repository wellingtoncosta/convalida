package convalida.sample;

import org.junit.Test;

import convalida.sample.robot.ConvalidaRobot;

/**
 * @author Wellington Costa on 25/06/2019.
 */
public class ConvalidaBaseActivityTest {

    @Test public void validateWithEmptyFields() {
        new ConvalidaRobot()
                .validate()
                .result()
                .isAllInvalid();
    }

    @Test public void clearValidations() {
        new ConvalidaRobot()
                .validate()
                .clear()
                .result()
                .isAllValid();
    }

    @Test public void nameField() {
        new ConvalidaRobot()
                .name().typeText("Wellington")
                .validate().result()
                .nameIsValid();

        new ConvalidaRobot()
                .name().typeText("")
                .validate().result()
                .nameIsInvalid();
    }

    @Test public void nickNameField() {
        new ConvalidaRobot()
                .nickName().typeText("Well")
                .validate().result()
                .nickNameIsValid();

        new ConvalidaRobot()
                .nickName().typeText("We")
                .validate().result()
                .nickNameIsInvalid();

        new ConvalidaRobot()
                .nickName().typeText("")
                .validate().result()
                .nickNameIsInvalid();
    }

    @Test public void ageField() {
        new ConvalidaRobot()
                .age().typeText("23")
                .validate().result()
                .ageIsValid();

        new ConvalidaRobot()
                .age().typeText("Test")
                .validate().result()
                .ageIsInvalid();

        new ConvalidaRobot()
                .age().typeText("")
                .validate().result()
                .ageIsInvalid();
    }

    @Test public void phoneField() {
        new ConvalidaRobot()
                .phone().typeText("+55(85)8684-6409")
                .validate().result()
                .phoneIsValid();

        new ConvalidaRobot()
                .phone().typeText("558586846409")
                .validate().result()
                .phoneIsInvalid();

        new ConvalidaRobot()
                .phone().typeText("")
                .validate().result()
                .phoneIsInvalid();
    }

    @Test public void cpfField() {
        new ConvalidaRobot()
                .cpf().typeText("14505408051")
                .validate().result()
                .cpfIsValid();

        new ConvalidaRobot()
                .cpf().typeText("14505408099")
                .validate().result()
                .cpfIsInvalid();

        new ConvalidaRobot()
                .cpf().typeText("")
                .validate().result()
                .cpfIsInvalid();
    }

    @Test public void cnpjField() {
        new ConvalidaRobot()
                .cnpj().typeText("85576499000132")
                .validate().result()
                .cnpjIsValid();

        new ConvalidaRobot()
                .cnpj().typeText("85576499000199")
                .validate().result()
                .cnpjIsInvalid();

        new ConvalidaRobot()
                .cnpj().typeText("")
                .validate().result()
                .cnpjIsInvalid();
    }

    @Test public void isbnField() {
        new ConvalidaRobot()
                .isbn().typeText("9781234567897")
                .validate().result()
                .isbnIsValid();

        new ConvalidaRobot()
                .isbn().typeText("9781234567899")
                .validate().result()
                .isbnIsInvalid();

        new ConvalidaRobot()
                .isbn().typeText("")
                .validate().result()
                .isbnIsInvalid();

    }

    @Test public void emailField() {
        new ConvalidaRobot()
                .email().typeText("wellington@email.com")
                .validate().result()
                .emailIsValid();

        new ConvalidaRobot()
                .email().typeText("wellington@email")
                .validate().result()
                .emailIsInvalid();

        new ConvalidaRobot()
                .email().typeText("")
                .validate().result()
                .emailIsInvalid();
    }

    @Test public void confirmEmailField() {
        new ConvalidaRobot()
                .email().typeText("wellington@email.com");

        new ConvalidaRobot()
                .confirmEmail().typeText("wellington@email.com")
                .validate().result()
                .confirmEmailIsValid();

        new ConvalidaRobot()
                .confirmEmail().typeText("wellington@email")
                .validate().result()
                .confirmEmailIsInvalid();
    }

    @Test public void passwordField() {
        new ConvalidaRobot()
                .password().typeText("asdASD123")
                .validate().result()
                .passwordIsValid();

        new ConvalidaRobot()
                .password().typeText("asdASD")
                .validate().result()
                .passwordIsInvalid();

        new ConvalidaRobot()
                .password().typeText("")
                .validate().result()
                .passwordIsInvalid();
    }

    @Test public void confirmPasswordField() {
        new ConvalidaRobot()
                .password().typeText("asdASD123");

        new ConvalidaRobot()
                .confirmPassword().typeText("asdASD123")
                .validate().result()
                .confirmPasswordIsValid();

        new ConvalidaRobot()
                .confirmPassword().typeText("asdASD12")
                .validate().result()
                .confirmPasswordIsInvalid();
    }

    @Test public void creditCardField() {
        new ConvalidaRobot()
                .creditCard().typeText("5282755437843391")
                .validate().result()
                .creditCardIsValid();

        new ConvalidaRobot()
                .creditCard().typeText("5282755437843399")
                .validate().result()
                .creditCardIsInvalid();

        new ConvalidaRobot()
                .creditCard().typeText("")
                .validate().result()
                .creditCardIsInvalid();
    }

    @Test public void numericLimitField() {
        new ConvalidaRobot()
                .numericLimit().typeText("0")
                .validate().result()
                .numericLimitIsValid();

        new ConvalidaRobot()
                .numericLimit().typeText("1")
                .validate().result()
                .numericLimitIsValid();

        new ConvalidaRobot()
                .numericLimit().typeText("99")
                .validate().result()
                .numericLimitIsValid();

        new ConvalidaRobot()
                .numericLimit().typeText("100")
                .validate().result()
                .numericLimitIsValid();

        new ConvalidaRobot()
                .numericLimit().typeText("101")
                .validate().result()
                .numericLimitIsInvalid();

        new ConvalidaRobot()
                .numericLimit().typeText("-1")
                .validate().result()
                .numericLimitIsInvalid();

        new ConvalidaRobot()
                .numericLimit().typeText("")
                .validate().result()
                .numericLimitIsInvalid();
    }

    @Test public void ipv4Field() {
        new ConvalidaRobot()
                .ipv4().typeText("192.168.1.1")
                .validate().result()
                .ipv4IsValid();

        new ConvalidaRobot()
                .ipv4().typeText("300.1.1.256")
                .validate().result()
                .ipv4IsInvalid();

        new ConvalidaRobot()
                .ipv4().typeText("")
                .validate().result()
                .ipv4IsInvalid();

    }

}