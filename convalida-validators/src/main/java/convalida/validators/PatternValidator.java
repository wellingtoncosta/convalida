package convalida.validators;

import android.widget.EditText;
import java.util.regex.Pattern;

/**
 * @author Wellington Costa on 21/06/2017.
 */
public class PatternValidator extends AbstractValidator {

    private String pattern;
    private boolean required;

    public PatternValidator(
            EditText editText,
            String errorMessage,
            String pattern,
            boolean autoDismiss,
            boolean required
    ) {
        super(editText, errorMessage, autoDismiss);
        this.pattern = pattern;
        this.required = required;
    }

    @Override public boolean isValid(String value) {
        return (!required || !value.isEmpty()) && Pattern.matches(pattern, value);
    }

}
