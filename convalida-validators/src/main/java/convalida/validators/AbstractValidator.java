package convalida.validators;

import android.widget.EditText;

import convalida.validators.util.EditTexts;

import static convalida.validators.util.EditTexts.removeError;
import static convalida.validators.util.EditTexts.setError;

/**
 * @author Wellington Costa on 21/06/2017.
 */
public abstract class AbstractValidator {

    public final EditText editText;
    public final String errorMessage;
    public final boolean autoDismiss;
    private boolean hasError = false;

    public AbstractValidator(EditText editText, String errorMessage, boolean autoDismiss) {
        this.editText = editText;
        this.errorMessage = errorMessage;
        this.autoDismiss = autoDismiss;
    }

    protected abstract boolean isValid(String value);

    private void executeValidation(String value) {
        hasError = !EditTexts.isVisible(editText) && !isValid(value);

        if (hasError) {
            setError(editText, errorMessage);
        } else {
            removeError(editText);
        }
    }

    public boolean validate() {
        executeValidation(editText.getText().toString());
        return !hasError;
    }

    public boolean validate(String value) {
        executeValidation(value);
        return !hasError;
    }

    public void clear() {
        removeError(editText);
    }


}