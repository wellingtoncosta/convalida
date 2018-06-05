package convalida.validators;

import android.widget.EditText;
import java.util.regex.Pattern;

/**
 * @author Wellington Costa on 21/06/2017.
 */
public class EmailValidator extends AbstractValidator {

    private static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    private boolean required;

    public EmailValidator(
            EditText editText,
            String errorMessage,
            boolean autoDismiss,
            boolean required
    ) {
        super(editText, errorMessage, autoDismiss);
        this.required = required;
    }

     @Override public boolean isValid(String value) {
         value = value.replace(" ", "");
         return (!required || !value.isEmpty())
                 && (value.isEmpty() || EMAIL_PATTERN.matcher(value).matches());
     }

}