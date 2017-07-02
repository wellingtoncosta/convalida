package convalida.library.validation.validator;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * @author  Wellington Costa on 21/06/2017.
 */
public class PasswordValidator extends AbstractValidator {

    private int min;
    private String pattern;

    public PasswordValidator(TextInputLayout layout, int min, String pattern, String errorMessage) {
        super(layout, errorMessage);
        this.min = min;
        this.pattern = pattern;
    }

    public PasswordValidator(EditText editText, int min, String pattern, String errorMessage) {
        super(editText, errorMessage);
        this.min = min;
        this.pattern = pattern;
    }

    @Override
    boolean isNotValid(String value) {
        boolean hasError = value.isEmpty();

        if(min > 0) {
            hasError |= value.length() < min;
        }

        if(!pattern.isEmpty()) {
            hasError |= !Pattern.matches(pattern, value);
        }

        return hasError;
    }

}
