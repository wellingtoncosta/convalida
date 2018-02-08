package convalida.validators

import android.widget.EditText
import java.util.regex.Pattern

/**
 * @author Wellington Costa on 21/06/2017.
 */
class EmailValidator(editText: EditText, errorMessage: String, autoDismiss: Boolean) : AbstractValidator(editText, errorMessage, autoDismiss) {

    override fun isNotValid(value: String): Boolean {
        return !EMAIL_PATTERN.matcher(value).matches()
    }

    companion object {
        private const val EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"
        private val EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE)
    }

}