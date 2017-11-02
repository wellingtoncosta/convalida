package convalida.validators;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * @author Wellington Costa on 29/06/2017.
 */
public class OnlyNumberValidator extends AbstractValidator {

    OnlyNumberValidator() { }

    public OnlyNumberValidator(TextInputLayout layout, String errorMessage) {
        super(layout, errorMessage);
    }

    public OnlyNumberValidator(EditText editText, String errorMessage) {
        super(editText, errorMessage);
    }

    @Override
    boolean isNotValid(String value) {
        return value == null || !Pattern.compile("^\\d+$").matcher(value).matches();
    }

}
