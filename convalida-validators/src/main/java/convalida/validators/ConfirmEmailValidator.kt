package convalida.validators

import android.widget.EditText

/**
 * @author Wellington Costa on 07/02/18.
 */
class ConfirmEmailValidator(val emailField: EditText, confirmEmailField: EditText, errorMessage: String) : AbstractValidator(confirmEmailField, errorMessage) {

    override fun isNotValid(value: String): Boolean {
        return emailField.text.toString() != value
    }

}
