package convalida.validators;

import android.widget.EditText;
import java.util.regex.Pattern;

/**
 * @author Wellington Costa on 29/06/2017.
 */
public class OnlyNumberValidator extends AbstractValidator{

    private boolean required;

    public OnlyNumberValidator(
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
                && (value.isEmpty() || Pattern.compile("^\\d+$").matcher(value).matches());
    }

}
