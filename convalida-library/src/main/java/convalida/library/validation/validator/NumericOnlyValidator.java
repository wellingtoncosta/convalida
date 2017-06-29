package convalida.library.validation.validator;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * @author Wellington Costa on 29/06/2017.
 */
public class NumericOnlyValidator extends AbstractValidator {

    public NumericOnlyValidator(TextInputLayout layout, String errorMessage) {
        super(layout, errorMessage);
    }

    public NumericOnlyValidator(EditText editText, String errorMessage) {
        super(editText, errorMessage);
    }

    @Override
    boolean isNotValid(String value) {
        return !Pattern.compile("^\\d+$").matcher(value).matches();
    }
}
