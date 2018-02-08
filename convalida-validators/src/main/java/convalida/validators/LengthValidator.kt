package convalida.validators

import android.widget.EditText

/**
 * @author Wellington Costa on 27/06/2017.
 */
class LengthValidator(editText: EditText, private var min: Int, private var max: Int, errorMessage: String) : AbstractValidator(editText, errorMessage) {

    override fun isNotValid(value: String): Boolean {
        var hasError = value.length < min

        if (max > 0) {
            hasError = hasError or (value.length > max)
        }

        return hasError
    }

}
