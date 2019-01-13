package convalida.ktx

import android.widget.Button
import android.widget.EditText
import convalida.validators.*
import convalida.validators.error.ValidationErrorSet

/**
 * @author Wellington Costa on 10/01/19
 *
 * Definition of the core dsl structure and its extension functions
 * to apply the all Convalida validation rules.
 *
 */

fun validationSet(
        validations: List<AbstractValidator>,
        actions: Actions
) {
    val validatorSet = ValidatorSet()
    validatorSet.validators.addAll(validations)

    actions.validateButton.setOnClickListener {
        if(validatorSet.isValid) {
            actions.onSuccess()
        }
        else {
            actions.onError?.let { it() }
            actions.onErrorWithInvalidFields?.let { it(validatorSet.errors) }
        }
    }

    actions.clearValidationsButton?.setOnClickListener {
        validatorSet.clearValidators()
    }
}

infix fun Actions.validateByClickingOn(
        button: Button
): Actions {
    this.validateButton = button
    return this
}

infix fun Actions.whenOnSuccess(
        callback: () -> Unit
): Actions {
    this.onSuccess = callback
    return this
}

infix fun Actions.whenOnError(
        callback: () -> Unit
): Actions {
    this.onError = callback
    return this
}

infix fun Actions.whenOnError(
        callback: (ValidationErrorSet) -> Unit
): Actions {
    this.onErrorWithInvalidFields = callback
    return this
}

infix fun Actions.clearValidationsByClickingOn(
        button: Button
): Actions {
    this.clearValidationsButton = button
    return this
}

val actions: Actions
    get() = Actions()

class Actions {

    lateinit var validateButton: Button
    lateinit var onSuccess: () -> Unit
    var clearValidationsButton: Button? = null
    var onError: (() -> Unit)? = null
    var onErrorWithInvalidFields: ((ValidationErrorSet) -> Unit)? = null

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
) = NumberLimitValidator(this, errorMessage, autoDismiss, min, max, required)

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

val EditText.isBetween: BetweenBuilder
    get() = BetweenBuilder(this)

class BetweenBuilder(private val startField: EditText) {

    data class Between(val field: EditText, val errorMessage: String, val autoDismiss: Boolean)

    private lateinit var start: Between
    private lateinit var end: Between

     fun start(errorMessage: String, autoDismiss: Boolean = true): BetweenBuilder {
         this.start = Between(startField, errorMessage, autoDismiss)
         return this
     }

    fun end(field: EditText, errorMessage: String, autoDismiss: Boolean = true): BetweenBuilder {
        this.end = Between(field, errorMessage, autoDismiss)
        return this
    }

    fun apply() = BetweenValidator(
            start.field,
            end.field,
            start.errorMessage,
            end.errorMessage,
            start.autoDismiss,
            end.autoDismiss
    )

}
