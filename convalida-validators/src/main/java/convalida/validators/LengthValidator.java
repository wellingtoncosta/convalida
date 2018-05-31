package convalida.validators;

import android.widget.EditText;

/**
 * @author Wellington Costa on 27/06/2017.
 */
public class LengthValidator extends AbstractValidator {

    private int min;
    private int max;

    public LengthValidator(
            EditText editText,
            int min,
            int max,
            String errorMessage,
            boolean autoDismiss) {
        super(editText, errorMessage, autoDismiss);
        this.min = min;
        this.max= max;
    }

    @Override public boolean isValid(String value) {
        boolean hasError = value.length() < min;

        if (max > 0) {
            hasError |= value.length() > max;
        }

        return !hasError;
    }

}
