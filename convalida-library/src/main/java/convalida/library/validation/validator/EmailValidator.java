package convalida.library.validation.validator;

import android.support.design.widget.TextInputLayout;
import android.util.Patterns;
import android.widget.EditText;

/**
 * @author Wellington Costa on 21/06/2017.
 */
public class EmailValidator extends AbstractValidator {

    public EmailValidator(TextInputLayout layout, String errorMessage) {
        super(layout, errorMessage);
    }

    public EmailValidator(EditText editText, String errorMessage) {
        super(editText, errorMessage);
    }

    @Override
    boolean isNotValid(String value) {
        return !Patterns.EMAIL_ADDRESS.matcher(value).matches();
    }

}
