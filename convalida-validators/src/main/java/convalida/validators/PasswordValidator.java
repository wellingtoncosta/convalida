package convalida.validators;

import android.widget.EditText;
import java.util.regex.Pattern;

/**
 * @author  Wellington Costa on 21/06/2017.
 */
public class PasswordValidator extends AbstractValidator {

    private int min;
    private String pattern;

    public PasswordValidator(
            EditText editText,
            int min,
            String pattern,
            String errorMessage,
            boolean autoDismiss) {
        super(editText, errorMessage, autoDismiss);
        this.min = min;
        this.pattern = pattern;
    }

    @Override public boolean isNotValid(String value) {
        boolean hasError = value.isEmpty();

        if (min > 0) {
            hasError |= value.length() < min;
        }

        if (!pattern.isEmpty()) {
            hasError |= !Pattern.matches(pattern, value);
        }

        return hasError;
    }

}
