package convalida.validators;

import android.widget.EditText;

import convalida.validators.util.EditTextUtils;
import convalida.validators.util.ExecuteValidationListener;

/**
 * @author Wellington Costa on 21/06/2017.
 */
public abstract class AbstractValidator {

    protected EditText editText;
    private String errorMessage;
    private boolean hasError = false;

    public AbstractValidator(EditText editText, String errorMessage, boolean autoDismiss) {
        this.editText = editText;
        this.errorMessage = errorMessage;

        if(autoDismiss) {
            EditTextUtils.addOnTextChangedListener(editText, new ExecuteValidationListener() {
                @Override
                public void execute(String value) {
                    executeValidation(value);
                }
            });
        }
    }

    public abstract boolean isNotValid(String value);

    private void executeValidation(String value) {
        hasError = !EditTextUtils.isVisible(editText) && isNotValid(value);

        if (hasError) {
            EditTextUtils.setError(editText, errorMessage);
        } else {
            EditTextUtils.setError(editText, null);
        }
    }

    public boolean validate() {
        executeValidation(editText.getText().toString());
        return !hasError;
    }

    public void clear() {
        EditTextUtils.setError(editText, null);
    }

}