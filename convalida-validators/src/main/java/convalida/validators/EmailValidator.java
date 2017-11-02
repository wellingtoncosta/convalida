package convalida.validators;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * @author Wellington Costa on 21/06/2017.
 */
public class EmailValidator extends AbstractValidator {

    private static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    EmailValidator() { }

    public EmailValidator(TextInputLayout layout, String errorMessage) {
        super(layout, errorMessage);
    }

    public EmailValidator(EditText editText, String errorMessage) {
        super(editText, errorMessage);
    }

    @Override
    boolean isNotValid(String value) {
        return value == null || !EMAIL_PATTERN.matcher(value).matches();
    }

}