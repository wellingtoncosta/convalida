package convalida.ktx

import android.widget.Button
import android.widget.EditText
import convalida.validators.AbstractValidator
import convalida.validators.BetweenValidator
import convalida.validators.CnpjValidator
import convalida.validators.ConfirmEmailValidator
import convalida.validators.ConfirmPasswordValidator
import convalida.validators.CpfValidator
import convalida.validators.CreditCardValidator
import convalida.validators.EmailValidator
import convalida.validators.Ipv4Validator
import convalida.validators.Ipv6Validator
import convalida.validators.IsbnValidator
import convalida.validators.LengthValidator
import convalida.validators.NumericLimitValidator
import convalida.validators.OnlyNumberValidator
import convalida.validators.PasswordValidator
import convalida.validators.PatternValidator
import convalida.validators.RequiredValidator
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

    fun <E, V> field(view: E, validator: E.() -> V) where E : EditText, V : AbstractValidator {
        validatorSet.addValidator(validator(view))
    }

    fun between(
            body: BetweenValidatorBuilder.() -> Unit
    ) = BetweenValidatorBuilder().apply { body() }

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
) = ConvalidaBuilder().apply { body() }

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

    fun start(body: Between.() -> Unit) = Between().apply { body() }.also {
        start = it
    }

    fun limit(body: Between.() -> Unit) = Between().apply { body() }.also {
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


fun EditText.isRequired(
        errorMessage: String,
        autoDismiss: Boolean = true
) = RequiredValidator(this, errorMessage, autoDismiss)

fun EditText.isEmail(
        errorMessage: String,
        autoDismiss: Boolean = true,
        required: Boolean = true
) = EmailValidator(this, errorMessage, autoDismiss, required)

fun EditText.isConfirmEmail(
        emailField: EditText,
        errorMessage: String,
        autoDismiss: Boolean = true
) = ConfirmEmailValidator(emailField, this,  errorMessage, autoDismiss)

fun EditText.withPattern(
        pattern: String,
        errorMessage: String,
        autoDismiss: Boolean = true,
        required: Boolean = true
) = PatternValidator(this, errorMessage, pattern, autoDismiss, required)

fun EditText.withLength(
        min: Int,
        max: Int = 0,
        errorMessage: String,
        autoDismiss: Boolean = true,
        required: Boolean = true
) = LengthValidator(this, errorMessage, min, max, autoDismiss, required)

fun EditText.onlyNumber(
        errorMessage: String,
        autoDismiss: Boolean = true,
        required: Boolean = true
) = OnlyNumberValidator(this, errorMessage, autoDismiss, required)

fun EditText.withNumericLimit(
        min: String,
        max: String,
        errorMessage: String,
        autoDismiss: Boolean = true,
        required: Boolean = true
) = NumericLimitValidator(this, errorMessage, autoDismiss, min, max, required)

fun EditText.isCreditCard(
        errorMessage: String,
        autoDismiss: Boolean = true,
        required: Boolean = true
) = CreditCardValidator(this, errorMessage, autoDismiss, required)

fun EditText.isCpf(
        errorMessage: String,
        autoDismiss: Boolean = true,
        required: Boolean = true
) = CpfValidator(this, errorMessage, autoDismiss, required)

fun EditText.isCnpj(
        errorMessage: String,
        autoDismiss: Boolean = true,
        required: Boolean = true
) = CnpjValidator(this, errorMessage, autoDismiss, required)

fun EditText.isIsbn(
        errorMessage: String,
        autoDismiss: Boolean = true,
        required: Boolean = true
) = IsbnValidator(this, errorMessage, autoDismiss, required)

fun EditText.isPassword(
        errorMessage: String,
        min: Int = 0,
        pattern: String = "",
        autoDismiss: Boolean = true
) = PasswordValidator(this, errorMessage, min, pattern, autoDismiss)

fun EditText.isConfirmPassword(
        passwordField: EditText,
        errorMessage: String,
        autoDismiss: Boolean = true
) = ConfirmPasswordValidator(passwordField, this, errorMessage, autoDismiss)

fun EditText.isIpv4(
        errorMessage: String,
        autoDismiss: Boolean = true,
        required: Boolean = true
) = Ipv4Validator(this, errorMessage, autoDismiss, required)

fun EditText.isIpv6(
        errorMessage: String,
        autoDismiss: Boolean = true,
        required: Boolean = true
) = Ipv6Validator(this, errorMessage, autoDismiss, required)
