package convalida.validators;

import android.widget.EditText;

import convalida.validators.util.EditTexts;
import convalida.validators.util.ExecuteValidationListener;

/**
 * @author Wellington Costa on 21/06/2017.
 */
public abstract class AbstractValidator {

    protected EditText editText;
    protected String errorMessage;
    private boolean hasError = false;

    public AbstractValidator(EditText editText, String errorMessage, boolean autoDismiss) {
        this.editText = editText;
        this.errorMessage = errorMessage;

        if(autoDismiss) {
            EditTexts.addOnTextChangedListener(editText, new ExecuteValidationListener() {
                @Override
                public void execute(String value) {
                    executeValidation(value);
                }
            });
        }
    }

    public abstract boolean isValid(String value);

    private void executeValidation(String value) {
        hasError = !EditTexts.isVisible(editText) && !isValid(value);

        if (hasError) {
            EditTexts.setError(editText, errorMessage);
        } else {
            EditTexts.setError(editText, null);
        }
    }

    public boolean validate() {
        executeValidation(editText.getText().toString());
        return !hasError;
    }

    public void clear() {
        EditTexts.setError(editText, null);
    }

}