package convalida.validators;

import android.view.View;
import android.widget.EditText;
import convalida.validators.util.EditTextUtils;
import convalida.validators.util.ExecuteValidationListener;

/**
 * @author Wellington Costa on 21/06/2017.
 */
public abstract class AbstractValidator implements Validator {

    private EditText editText;
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
        hasError = viewIsVisible() && isNotValid(value);

        if (hasError) {
            EditTextUtils.setError(editText, errorMessage);
        } else {
            EditTextUtils.setError(editText, null);
        }
    }

    private Boolean viewIsVisible() {
        return !(editText.getVisibility() == View.GONE ||
                editText.getVisibility() == View.INVISIBLE);
    }

    @Override
    public boolean validate() {
        executeValidation(editText.getText().toString());
        return !hasError;
    }

    @Override
    public void clear() {
        EditTextUtils.setError(editText, null);
    }

}