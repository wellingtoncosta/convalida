package convalida.validators;

import android.widget.EditText;

import org.apache.commons.validator.routines.ISBNValidator;

/**
 * @author Wellington Costa on 25/02/2019.
 */
public class IsbnValidator extends AbstractValidator {

    private boolean required;

    public IsbnValidator(
            EditText editText,
            String errorMessage,
            boolean autoDismiss,
            boolean required
    ) {
        super(editText, errorMessage, autoDismiss);
        this.required = required;
    }

    @Override public boolean isValid(String value) {
        return (!required || !value.isEmpty())
                && (value.isEmpty() || ISBNValidator.getInstance().isValid(value));
    }

}
