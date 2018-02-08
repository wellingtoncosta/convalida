package convalida.validators

import android.widget.EditText
import java.util.regex.Pattern

/**
 * @author Wellington Costa on 21/06/2017.
 */
class PatternValidator(editText: EditText, errorMessage: String, pattern: String) : AbstractValidator(editText, errorMessage) {

    private var pattern: String? = pattern

    override fun isNotValid(value: String): Boolean {
        return !Pattern.matches(pattern!!, value)
    }

}
