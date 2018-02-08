package convalida.validators

import android.widget.EditText
import java.util.regex.Pattern

/**
 * @author Wellington Costa on 29/06/2017.
 */
class OnlyNumberValidator(editText: EditText, errorMessage: String, autoDismiss: Boolean) : AbstractValidator(editText, errorMessage, autoDismiss) {

    override fun isNotValid(value: String): Boolean {
        return !Pattern.compile("^\\d+$").matcher(value).matches()
    }

}
