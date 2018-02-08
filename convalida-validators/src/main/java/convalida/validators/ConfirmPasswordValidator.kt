package convalida.validators

import android.widget.EditText

/**
 * @author Wellington Costa on 27/06/2017.
 */
class ConfirmPasswordValidator(val passwordEditText: EditText, confirmPasswordEditText: EditText, errorMessage: String) : AbstractValidator(confirmPasswordEditText, errorMessage) {

    override fun isNotValid(value: String): Boolean {
        return passwordEditText.text.toString() != value
    }

}
