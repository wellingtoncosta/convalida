package convalida.validators

import android.widget.EditText

/**
 * @author Wellington Costa on 07/02/18.
 */
class ConfirmEmailValidator(private val emailField: EditText, confirmEmailField: EditText, errorMessage: String, autoDismiss: Boolean) : AbstractValidator(confirmEmailField, errorMessage, autoDismiss) {

    override fun isNotValid(value: String): Boolean {
        return emailField.text.toString() != value
    }

}
