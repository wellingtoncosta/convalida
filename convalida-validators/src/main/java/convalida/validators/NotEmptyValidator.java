package convalida.validators;

import android.widget.EditText;

/**
 * @author Wellington Costa on 21/06/2017.
 */
public class NotEmptyValidator extends AbstractValidator {

    public NotEmptyValidator(
            EditText editText,
            String errorMessage,
            boolean autoDismiss) {
        super(editText, errorMessage, autoDismiss);
    }

    @Override public boolean isNotValid(String value) {
        return value.isEmpty();
    }

}