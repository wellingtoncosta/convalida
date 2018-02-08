package convalida.validators.util

import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText

/**
 * @author Wellington Costa on 08/02/18.
 */
class EditTextUtils {

    companion object {

        private fun getTextInputLayout(editText: EditText): TextInputLayout? {
            var parent = editText.parent

            while (parent is View) {
                if (parent is TextInputLayout) {
                    return parent
                }
                parent = parent.getParent()
            }

            return null
        }

        fun setError(editText: EditText, errorMessage: String?) {
            val layout = getTextInputLayout(editText)

            if (layout != null) {
                layout.isErrorEnabled = errorMessage != null
                layout.error = errorMessage
            } else {
                editText.error = errorMessage
            }
        }

        fun addOnTextChangedListener(editText: EditText, validateFun: (value: String) -> Unit) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    validateFun(s.toString())
                }

                override fun afterTextChanged(s: Editable) {}
            })
        }

    }

}
