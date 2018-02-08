package convalida.validators

import android.widget.EditText
import convalida.validators.util.EditTextUtils

/**
 * @author Wellington Costa on 21/06/2017.
 */
abstract class AbstractValidator(private val editText: EditText, private val errorMessage: String, private val autoDismiss: Boolean) : Validator {

    private var hasError: Boolean = false

    internal abstract fun isNotValid(value: String): Boolean

    private fun executeValidation(value: String) {
        hasError = isNotValid(value)

        if (hasError) {
            EditTextUtils.setError(editText, errorMessage)
        } else {
            EditTextUtils.setError(editText, null)
        }
    }

    override fun validate(): Boolean {
        executeValidation(editText.text.toString())
        return !hasError
    }

    override fun clear() {
        EditTextUtils.setError(editText, null)
    }

    init {
        this.hasError = false
        if(autoDismiss) {
            EditTextUtils.addOnTextChangedListener(editText, ::executeValidation)
        }
    }

}