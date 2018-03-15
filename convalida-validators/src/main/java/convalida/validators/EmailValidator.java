package convalida.validators;

import android.widget.EditText;
import java.util.regex.Pattern;

/**
 * @author Wellington Costa on 21/06/2017.
 */
public class EmailValidator extends AbstractValidator {

    private static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    public EmailValidator(
            EditText editText,
            String errorMessage,
            boolean autoDismiss) {
        super(editText, errorMessage, autoDismiss);
    }

     @Override public boolean isNotValid(String value) {
        return !EMAIL_PATTERN.matcher(value).matches();
    }

}