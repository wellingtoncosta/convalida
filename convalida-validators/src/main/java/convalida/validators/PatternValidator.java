package convalida.validators;

import android.widget.EditText;
import java.util.regex.Pattern;

/**
 * @author Wellington Costa on 21/06/2017.
 */
public class PatternValidator extends AbstractValidator {

    private String pattern;

    public PatternValidator(
            EditText editText,
            String errorMessage,
            String pattern,
            boolean autoDismiss) {
        super(editText, errorMessage, autoDismiss);
        this.pattern = pattern;
    }

    @Override public boolean isValid(String value) {
        return Pattern.matches(pattern, value);
    }

}
