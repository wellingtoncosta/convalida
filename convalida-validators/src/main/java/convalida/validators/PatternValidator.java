package convalida.validators;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * @author Wellington Costa on 21/06/2017.
 */
public class PatternValidator extends AbstractValidator {

    private String pattern;

    PatternValidator(String pattern) {
        this.pattern = pattern;
    }

    public PatternValidator(TextInputLayout layout, String errorMessage, String pattern) {
        super(layout, errorMessage);
        this.pattern = pattern;
    }

    public PatternValidator(EditText editText, String errorMessage, String pattern) {
        super(editText, errorMessage);
        this.pattern = pattern;
    }

    @Override
    boolean isNotValid(String value) {
        return value == null || !Pattern.matches(pattern, value);
    }

}
