package convalida.library.validation.validator;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import convalida.library.validation.Validator;

/**
 * @author  Wellington Costa on 21/06/2017.
 */
public class PasswordValidator extends AbstractValidator implements Validator {

    public PasswordValidator(TextInputLayout layout, String errorMessage) {
        super(layout, errorMessage);
    }

    public PasswordValidator(EditText editText, String errorMessage) {
        super(editText, errorMessage);
    }

    @Override
    boolean isNotValid(String value) {
        return value.isEmpty();
    }

}
