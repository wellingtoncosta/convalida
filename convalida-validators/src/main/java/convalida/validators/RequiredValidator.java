package convalida.validators;

import android.widget.EditText;

/**
 * @author Wellington Costa on 21/06/2017.
 */
public class RequiredValidator extends AbstractValidator {

    public RequiredValidator(
            EditText editText,
            String errorMessage,
            boolean autoDismiss) {
        super(editText, errorMessage, autoDismiss);
    }

    @Override public boolean isValid(String value) {
        return !value.isEmpty();
    }

}