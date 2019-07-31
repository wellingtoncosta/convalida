package convalida.sample

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import convalida.sample.robot.ConvalidaRobotExtension.age
import convalida.sample.robot.ConvalidaRobotExtension.clears
import convalida.sample.robot.ConvalidaRobotExtension.cnpj
import convalida.sample.robot.ConvalidaRobotExtension.confirmEmail
import convalida.sample.robot.ConvalidaRobotExtension.confirmPassword
import convalida.sample.robot.ConvalidaRobotExtension.convalida
import convalida.sample.robot.ConvalidaRobotExtension.cpf
import convalida.sample.robot.ConvalidaRobotExtension.creditCard
import convalida.sample.robot.ConvalidaRobotExtension.email
import convalida.sample.robot.ConvalidaRobotExtension.isbn
import convalida.sample.robot.ConvalidaRobotExtension.ipv4
import convalida.sample.robot.ConvalidaRobotExtension.ipv6
import convalida.sample.robot.ConvalidaRobotExtension.url
import convalida.sample.robot.ConvalidaRobotExtension.date
import convalida.sample.robot.ConvalidaRobotExtension.name
import convalida.sample.robot.ConvalidaRobotExtension.nickName
import convalida.sample.robot.ConvalidaRobotExtension.numericLimit
import convalida.sample.robot.ConvalidaRobotExtension.password
import convalida.sample.robot.ConvalidaRobotExtension.phone
import convalida.sample.robot.ConvalidaRobotExtension.results
import convalida.sample.robot.ConvalidaRobotExtension.typeText
import convalida.sample.robot.ConvalidaRobotExtension.validates
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Wellington Costa on 25/06/2019.
 */
@RunWith(AndroidJUnit4::class)
class KotlinDslSampleActivityTest {

    @get:Rule val activityTestRule = ActivityTestRule(KotlinDslSampleActivity::class.java)

    @Test fun validateWithEmptyFields() {
        convalida { validate() } results { isAllInvalid() }
    }

    @Test fun clearValidations() {
        convalida { validate() } clears { isAllValid() }
    }

    @Test fun nameField() {
        convalida { name typeText "Wellington" } validates { nameIsValid() }

        convalida { name typeText EMPTY_TEXT } validates { nameIsInvalid() }
    }

    @Test fun nickNameField() {
        convalida { nickName typeText "Well" } validates { nickNameIsValid() }

        convalida { nickName typeText "We" } validates { nickNameIsInvalid() }

        convalida { nickName typeText EMPTY_TEXT } validates { nickNameIsInvalid() }
    }

    @Test fun ageField() {
        convalida { age typeText "20" } validates { ageIsValid() }

        convalida { age typeText "Test" } validates { ageIsInvalid() }

        convalida { age typeText EMPTY_TEXT } validates { ageIsInvalid() }
    }

    @Test fun phoneField() {
        convalida { phone typeText "+99(99)9999-9999" } validates { phoneIsValid() }

        convalida { phone typeText "+999999999999" } validates { phoneIsInvalid() }

        convalida { phone typeText EMPTY_TEXT } validates { phoneIsInvalid() }
    }

    @Test fun cpfField() {
        convalida { cpf typeText "14505408051" } validates { cpfIsValid() }

        convalida { cpf typeText "14505408099" } validates { cpfIsInvalid() }

        convalida { cpf typeText EMPTY_TEXT } validates { cpfIsInvalid() }
    }

    @Test fun cnpjField() {
        convalida { cnpj typeText "85576499000132" } validates { cnpjIsValid() }

        convalida { cnpj typeText "85576499000199" } validates { cnpjIsInvalid() }

        convalida { cnpj typeText EMPTY_TEXT } validates { cnpjIsInvalid() }
    }

    @Test fun isbnField() {
        convalida { isbn typeText "9781234567897" } validates { isbnIsValid() }

        convalida { isbn typeText "9781234567899" } validates { isbnIsInvalid() }

        convalida { isbn typeText EMPTY_TEXT } validates { isbnIsInvalid() }
    }

    @Test fun emailField() {
        convalida { email typeText "wellington@email.com" } validates { emailIsValid() }

        convalida { email typeText "wellington@email" } validates { emailIsInvalid() }

        convalida { email typeText EMPTY_TEXT } validates { emailIsInvalid() }
    }

    @Test fun confirmEmailField() {
        convalida { email typeText "wellington@email.com" }

        convalida {
            confirmEmail typeText "wellington@email.com"
        } validates { confirmEmailIsValid() }

        convalida {
            confirmEmail typeText "wellington@email"
        } validates { confirmEmailIsInvalid() }
    }

    @Test fun passwordField() {
        convalida { password typeText "asdASD123" } validates { passwordIsValid() }

        convalida { password typeText "asdASD" } validates { passwordIsInvalid() }

        convalida { password typeText EMPTY_TEXT } validates { passwordIsInvalid() }
    }

    @Test fun confirmPasswordField() {
        convalida { password typeText "asdASD123" }

        convalida {
            confirmPassword typeText "asdASD123"
        } validates { confirmPasswordIsValid() }

        convalida {
            confirmPassword typeText "asdASD"
        } validates {
            confirmPasswordIsInvalid()
        }
    }

    @Test fun creditCardField() {
        convalida { creditCard typeText "5282755437843391" } validates { creditCardIsValid() }

        convalida {creditCard typeText "5282755437843399" } validates { creditCardIsInvalid() }

        convalida { creditCard typeText EMPTY_TEXT } validates { creditCardIsInvalid() }
    }

    @Test fun numericLimitField() {
        convalida { numericLimit typeText "0" } validates { numericLimitIsValid() }

        convalida { numericLimit typeText "1" } validates { numericLimitIsValid() }

        convalida { numericLimit typeText "99" } validates { numericLimitIsValid() }

        convalida { numericLimit typeText "100" } validates { numericLimitIsValid() }

        convalida { numericLimit typeText "101" } validates { numericLimitIsInvalid() }

        convalida { numericLimit typeText "-1" } validates { numericLimitIsInvalid() }

        convalida { numericLimit typeText EMPTY_TEXT } validates { numericLimitIsInvalid() }
    }

    @Test fun ipv4Field() {
        convalida { ipv4 typeText "192.168.1.1" } validates { ipv4IsValid() }

        convalida { ipv4 typeText "300.1.1.256" } validates { ipv4IsInvalid() }

        convalida { ipv4 typeText EMPTY_TEXT } validates { ipv4IsInvalid() }
    }

    @Test fun ipv6Field() {
        convalida { ipv6 typeText VALID_IPV6 } validates { ipv6IsValid() }

        convalida { ipv6 typeText INVALID_IPV6 } validates { ipv6IsInvalid() }

        convalida { ipv6 typeText EMPTY_TEXT } validates { ipv6IsInvalid() }
    }

    @Test fun urlField() {
        convalida { url typeText VALID_URL } validates { urlIsValid() }

        convalida { url typeText INVALID_URL } validates { urlIsInvalid() }

        convalida { url typeText VALID_LOCAL_URL } validates { urlIsValid() }

        convalida { url typeText INVALID_LOCAL_URL } validates { urlIsInvalid() }

        convalida { url typeText EMPTY_TEXT } validates { urlIsInvalid() }
    }

    @Test fun dateField() {
        convalida { date typeText VALID_DATE } validates { pastDateIsValid() }

        convalida { date typeText INVALID_DATE } validates { pastDateIsInvalid() }

        convalida { date typeText INVALID_LOCAL_URL } validates { pastDateIsInvalid() }

        convalida { date typeText INVALID_FORMATTED_DATE } validates { pastDateIsInvalid() }
    }

    companion object {

        const val EMPTY_TEXT = ""

        const val VALID_IPV6 = "67dc:742c:f48a:2e5e:dc9d:efb1:1210:52d2"

        const val INVALID_IPV6 = "67dc:742c:f48a:2e5e:dc9d:efb1:1210:52dg"

        const val VALID_URL = "https://www.google.com"

        const val VALID_LOCAL_URL = "http://localhost"

        const val INVALID_URL = "google.com"

        const val INVALID_LOCAL_URL = "localhost"

        const val VALID_DATE = "01/01/2010"

        const val INVALID_DATE = "01/01/1990"

        const val INVALID_FORMATTED_DATE = "01-01-2010"

    }

}
