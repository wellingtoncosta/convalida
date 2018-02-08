package convalida.validators

import android.widget.EditText
import java.util.regex.Pattern

/**
 * @author  Wellington Costa on 21/06/2017.
 */
class PasswordValidator(editText: EditText, private var min: Int, private val pattern: String, errorMessage: String) : AbstractValidator(editText, errorMessage) {

    override fun isNotValid(value: String): Boolean {
        var hasError = value.isEmpty()

        if (min > 0) {
            hasError = hasError or (value.length < min)
        }

        if (!pattern.isEmpty()) {
            hasError = hasError or !Pattern.matches(pattern, value)
        }

        return hasError
    }

}
