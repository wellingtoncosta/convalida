package convalida.validators.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * @author Wellington Costa on 08/02/18.
 */
public final class EditTextUtils {

    private static TextInputLayout getTextInputLayout(EditText editText) {
        ViewParent parent = editText.getParent();

        while (parent instanceof View) {
            if (parent instanceof TextInputLayout) {
                return (TextInputLayout) parent;
            }
            parent = parent.getParent();
        }

        return null;
    }

    public static void setError(EditText editText, String errorMessage) {
        TextInputLayout layout = getTextInputLayout(editText);
        if (layout != null) {
            layout.setErrorEnabled(errorMessage != null);
            layout.setError(errorMessage);
        } else {
            editText.setError(errorMessage);
        }
    }

    public static void addOnTextChangedListener(
            final EditText editText,
            final ExecuteValidationListener listener) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                listener.execute(s.toString());
            }

            @Override public void afterTextChanged(Editable s) {

            }
        });
    }

    public static boolean isVisible(EditText editText) {
        return (editText.getVisibility() == View.GONE ||
                editText.getVisibility() == View.INVISIBLE);
    }

}