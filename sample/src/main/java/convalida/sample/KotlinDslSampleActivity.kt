package convalida.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import convalida.ktx.*
import convalida.library.util.Patterns.MIXED_CASE_NUMERIC
import convalida.sample.Constants.PHONE_PATTERN
import kotlinx.android.synthetic.main.activity_kotlin_dsl_sample.*

class KotlinDslSampleActivity : AppCompatActivity() {

    private val fieldRequired by lazy { this.getString(R.string.field_required) }
    private val min3Characteres by lazy { this.getString(R.string.min_3_characters) }
    private val onlyNumbers by lazy { this.getString(R.string.only_numbers) }
    private val invalidPhone by lazy { this.getString(R.string.invalid_phone) }
    private val invalidCpf by lazy { this.getString(R.string.invalid_cpf) }
    private val invalidCnpj by lazy { this.getString(R.string.invalid_cnpj) }
    private val invalidIsbn by lazy { this.getString(R.string.invalid_isbn) }
    private val invalidStartValue by lazy { this.getString(R.string.start_value_not_valid) }
    private val invalidLimitValue by lazy { this.getString(R.string.limit_value_not_valid) }
    private val invalidEmail by lazy { this.getString(R.string.invalid_email) }
    private val differentEmails by lazy { this.getString(R.string.emails_not_match) }
    private val invalidPassword by lazy { this.getString(R.string.invalid_password) }
    private val differentPasswords by lazy { this.getString(R.string.passwords_not_match) }
    private val invalidCreditCard by lazy { this.getString(R.string.invalid_credit_card) }
    private val invalidNumericLimit by lazy { this.getString(R.string.invalid_numeric_limit) }
    private val invalidIpv4 by lazy { this.getString(R.string.invalid_ipv4) }
    private val invalidIpv6 by lazy { this.getString(R.string.invalid_ipv6) }
    private val invalidUrl by lazy { this.getString(R.string.invalid_url) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_kotlin_dsl_sample)

        convalida {
            field(name_field) {
                isRequired(errorMessage = fieldRequired)
            }

            field(nickname_field) {
                withLength(min = 3, errorMessage = min3Characteres)
            }

            field(age_field) {
                onlyNumber(errorMessage = onlyNumbers)
            }

            field(phone_field) {
                withPattern(pattern = PHONE_PATTERN, errorMessage = invalidPhone)
            }

            field(cpf_field) {
                isCpf(errorMessage = invalidCpf)
            }

            field(cnpj_field) {
                isCnpj(errorMessage = invalidCnpj)
            }

            field(isbn_field) {
                isIsbn(errorMessage = invalidIsbn)
            }

            field(email_field) {
                isEmail(errorMessage = invalidEmail)
            }

            field(confirm_email_field) {
                isConfirmEmail(
                        emailField = email_field,
                        errorMessage = differentEmails
                )
            }

            field(password_field) {
                isPassword(
                        min = 3,
                        pattern = MIXED_CASE_NUMERIC,
                        errorMessage = invalidPassword
                )
            }

            field(confirm_password_field) {
                isConfirmPassword(
                        passwordField = password_field,
                        errorMessage = differentPasswords
                )
            }

            field(credit_card_field) {
                isCreditCard(errorMessage = invalidCreditCard)
            }

            field(numeric_limit_field) {
                withNumericLimit(
                        min = "0",
                        max = "100",
                        errorMessage = invalidNumericLimit
                )
            }

            between {
                start {
                    field = start_value_field
                    errorMessage = invalidStartValue
                }

                limit {
                    field = limit_value_field
                    errorMessage = invalidLimitValue
                }
            }

            field(ipv4_field) {
                isIpv4(errorMessage = invalidIpv4)
            }

            field(ipv6_field) {
                isIpv6(errorMessage = invalidIpv6)
            }

            field(url_field) {
                isUrl(errorMessage = invalidUrl)
            }

            validateOn(validate_button) {
                onSuccess { onValidationSuccess() }
                onError { onValidationError() }
            }

            clearValidationsOn(clear_button)

        }
    }

    private fun onValidationSuccess() {
        Toast.makeText(this, "Yay!", Toast.LENGTH_SHORT).show()
    }

    private fun onValidationError() {
        Toast.makeText(this, "Something is wrong :(", Toast.LENGTH_SHORT).show()
    }

}
