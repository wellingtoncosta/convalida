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
    private val invalidInitialPeriod by lazy { this.getString(R.string.initial_period_not_valid) }
    private val invalidFinalPeriod by lazy { this.getString(R.string.final_period_not_valid) }
    private val invalidEmail by lazy { this.getString(R.string.invalid_email) }
    private val differentEmails by lazy { this.getString(R.string.emails_not_match) }
    private val invalidPassword by lazy { this.getString(R.string.invalid_password) }
    private val differentPasswords by lazy { this.getString(R.string.passwords_not_match) }
    private val invalidCreditCard by lazy { this.getString(R.string.invalid_credit_card) }
    private val invalidNumericLimit by lazy { this.getString(R.string.invalid_number_limit) }

    private val validations by lazy {
        validationSet(
                validations = listOf(
                        name_field.isRequired(errorMessage = fieldRequired),
                        nickname_field.withLength(min = 3, errorMessage = min3Characteres),
                        age_field.onlyNumber(errorMessage = onlyNumbers),
                        phone_field.withPattern(pattern = PHONE_PATTERN, errorMessage = invalidPhone),
                        cpf_field.isCpf(errorMessage = invalidCpf),
                        initial_period_field.isBetween
                                .start(errorMessage = invalidInitialPeriod)
                                .end(field = final_period_field, errorMessage = invalidFinalPeriod)
                                .apply(),
                        email_field.isEmail(errorMessage = invalidEmail),
                        confirm_email_field.isConfirmEmail(
                                emailField = email_field,
                                errorMessage = differentEmails
                        ),
                        password_field.isPassword(errorMessage = invalidPassword),
                        confirm_password_field.isConfirmPassword(
                                passwordField = password_field,
                                errorMessage = differentPasswords
                        ),
                        credit_card_field.isCreditCard(errorMessage = invalidCreditCard),
                        number_limit_field.withNumericLimit(
                                min = "0",
                                max = "100",
                                errorMessage = invalidNumericLimit
                        )
                ),
                actions = actions
                        validateByClickingOn validate_button
                        clearValidationsByClickingOn clear_button
                        whenOnSuccess ::onValidationSuccess
                        whenOnError ::onValidationError
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_dsl_sample)
        apply { validations }
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
