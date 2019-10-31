package convalida.ktx

import android.widget.Button
import android.widget.EditText
import convalida.validators.BetweenValidator
import convalida.validators.CnpjValidator
import convalida.validators.ConfirmEmailValidator
import convalida.validators.ConfirmPasswordValidator
import convalida.validators.CpfValidator
import convalida.validators.CreditCardValidator
import convalida.validators.EmailValidator
import convalida.validators.FutureDateValidator
import convalida.validators.Ipv4Validator
import convalida.validators.Ipv6Validator
import convalida.validators.IsbnValidator
import convalida.validators.LengthValidator
import convalida.validators.NumericLimitValidator
import convalida.validators.OnlyNumberValidator
import convalida.validators.PasswordValidator
import convalida.validators.PastDateValidator
import convalida.validators.PatternValidator
import convalida.validators.RequiredValidator
import convalida.validators.UrlValidator
import convalida.validators.ValidatorSet
import convalida.validators.error.ValidationErrorSet

/**
 * Definition of the core dsl structure and its extension functions
 * to apply the all Convalida validation rules.
 *
 * @author Wellington Costa on 10/01/19
 */

class ConvalidaBuilder {

    private val validatorSet = ValidatorSet()

    fun <E> field(field: E, validator: FieldValidatorBuilder.() -> Unit) where E : EditText {
        validator(FieldValidatorBuilder(field, validatorSet))
    }

    fun between(
            body: BetweenValidatorBuilder.() -> Unit
    ) = BetweenValidatorBuilder().apply(body)

    fun <B> validateOn(
            button: B,
            result: SubmitActionBuilder.() -> Unit
    ) where B : Button = SubmitActionBuilder().apply {
        result()
    }.let { action ->
        button.setOnClickListener {
            if (validatorSet.isValid) {
                action.onSuccessAction()
            } else {
                action.onErrorAction(validatorSet.errors)
            }
        }
    }

    fun <B> clearValidationsOn(button: B) where B : Button {
        button.setOnClickListener { validatorSet.clearValidators() }
    }
}

fun convalida(
        body: ConvalidaBuilder.() -> Unit
) = ConvalidaBuilder().apply(body)

class FieldValidatorBuilder(
        private val field: EditText,
        private val validatorSet: ValidatorSet
) {

    fun isRequired(
            errorMessage: String, autoDismiss: Boolean = true
    ) = validatorSet.addValidator(RequiredValidator(field, errorMessage, autoDismiss))

    fun isEmail(
            errorMessage: String, autoDismiss: Boolean = true, required: Boolean = true
    ) = validatorSet.addValidator(EmailValidator(field, errorMessage, autoDismiss, required))

    fun isConfirmEmail(
            emailField: EditText, errorMessage: String, autoDismiss: Boolean = true
    ) = validatorSet.addValidator(
            ConfirmEmailValidator(emailField, field,  errorMessage, autoDismiss))

    fun withPattern(
            pattern: String, errorMessage: String,
            autoDismiss: Boolean = true, required: Boolean = true
    ) = validatorSet.addValidator(
            PatternValidator(field, errorMessage, pattern, autoDismiss, required))

    fun withLength(
            min: Int, max: Int = 0, errorMessage: String,
            autoDismiss: Boolean = true, required: Boolean = true
    ) = validatorSet.addValidator(
            LengthValidator(field, errorMessage, min, max, autoDismiss, required))

    fun onlyNumber(
            errorMessage: String, autoDismiss: Boolean = true, required: Boolean = true
    ) = validatorSet.addValidator(OnlyNumberValidator(field, errorMessage, autoDismiss, required))

    fun withNumericLimit(
            min: String, max: String, errorMessage: String,
            autoDismiss: Boolean = true, required: Boolean = true
    ) = validatorSet.addValidator(
            NumericLimitValidator(field, errorMessage, autoDismiss, min, max, required))

    fun isCreditCard(
            errorMessage: String, autoDismiss: Boolean = true, required: Boolean = true
    ) = validatorSet.addValidator(
            CreditCardValidator(field, errorMessage, autoDismiss, required))

    fun isPassword(
            errorMessage: String, min: Int = 0, pattern: String = "", autoDismiss: Boolean = true
    ) = validatorSet.addValidator(
            PasswordValidator(field, errorMessage, min, pattern, autoDismiss))

    fun isConfirmPassword(
            passwordField: EditText, errorMessage: String, autoDismiss: Boolean = true
    ) = validatorSet.addValidator(
            ConfirmPasswordValidator(passwordField, field, errorMessage, autoDismiss))

    fun isCpf(
            errorMessage: String, autoDismiss: Boolean = true, required: Boolean = true
    ) = validatorSet.addValidator(CpfValidator(field, errorMessage, autoDismiss, required))

    fun isCnpj(
            errorMessage: String, autoDismiss: Boolean = true, required: Boolean = true
    ) = validatorSet.addValidator(CnpjValidator(field, errorMessage, autoDismiss, required))

    fun isIsbn(
            errorMessage: String, autoDismiss: Boolean = true, required: Boolean = true
    ) = validatorSet.addValidator(IsbnValidator(field, errorMessage, autoDismiss, required))

    fun isIpv4(
            errorMessage: String, autoDismiss: Boolean = true, required: Boolean = true
    ) = validatorSet.addValidator(Ipv4Validator(field, errorMessage, autoDismiss, required))

    fun isIpv6(
            errorMessage: String, autoDismiss: Boolean = true, required: Boolean = true
    ) = validatorSet.addValidator(Ipv6Validator(field, errorMessage, autoDismiss, required))

    fun isUrl(
            errorMessage: String, autoDismiss: Boolean = true, required: Boolean = true
    ) = validatorSet.addValidator(UrlValidator(field, errorMessage, autoDismiss, required))

    fun pastDate(
            errorMessage: String, dateFormat: String, limitDate: String,
            autoDismiss: Boolean = true, required: Boolean = true
    ) = validatorSet.addValidator(
            PastDateValidator(field, errorMessage, dateFormat, limitDate, autoDismiss, required))

    fun futureDate(
            errorMessage: String, dateFormat: String, limitDate: String,
            autoDismiss: Boolean = true, required: Boolean = true
    ) = validatorSet.addValidator(
            FutureDateValidator(field, errorMessage, dateFormat, limitDate, autoDismiss, required))

}

class SubmitActionBuilder {

    lateinit var onSuccessAction: () -> Unit private set

    lateinit var onErrorAction: (ValidationErrorSet) -> Unit private set

    fun onSuccess(func: () -> Unit) {
        onSuccessAction = func
    }

    fun onError(func: (ValidationErrorSet) -> Unit) {
        onErrorAction = func
    }

}

class BetweenValidatorBuilder {

    private var validator: BetweenValidator? = null

    private var start: Between? = null

    private var limit: Between? = null

    fun start(body: Between.() -> Unit) = Between().apply(body).also {
        start = it
    }

    fun limit(body: Between.() -> Unit) = Between().apply(body).also {
        limit = it
    }.also {
        start?.let { start ->
            limit?.let { limit ->
                validator = BetweenValidator(
                        start.field,
                        limit.field,
                        start.errorMessage,
                        limit.errorMessage,
                        start.autoDismiss,
                        limit.autoDismiss
                )
            }
        }
    }

}

class Between {
    var field: EditText? = null
    var errorMessage: String? = null
    var autoDismiss: Boolean = true
}
