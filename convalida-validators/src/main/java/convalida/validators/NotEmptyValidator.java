package convalida.validators;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

/**
 * @author Wellington Costa on 21/06/2017.
 */
public class NotEmptyValidator extends AbstractValidator {

    NotEmptyValidator() { }

    public NotEmptyValidator(TextInputLayout layout, String errorMessage) {
        super(layout, errorMessage);
    }

    public NotEmptyValidator(EditText editText, String errorMessage) {
        super(editText, errorMessage);
    }

    @Override
    boolean isNotValid(String value) {
        return value == null || value.isEmpty();
    }

}