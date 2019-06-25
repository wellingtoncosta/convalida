package convalida.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import convalida.ktx.*
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
    private val invalidNumericLimit by lazy { this.getString(R.string.invalid_number_limit) }

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
                isPassword(errorMessage = invalidPassword)
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

            field(number_limit_field) {
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

            validateOn(validate_button) {
                onSuccess { onValidationSuccess() }
                onError { onValidationError() }
            }

            clearValidationsOn(clear_button)

        }
    }

    private fun onValidationSuccess() {
        Snackbar.make(
                activity_kotlin_dsl_sample,
                "Yay!",
                Snackbar.LENGTH_LONG
        ).show()
    }

    private fun onValidationError() {
        Snackbar.make(
                activity_kotlin_dsl_sample,
                "Something is wrong :(",
                Snackbar.LENGTH_LONG
        ).show()
    }

}
