package convalida.validators;

import android.widget.EditText;

/**
 * @author Wellington Costa on 30/07/2019.
 */
public class UrlValidator extends AbstractValidator {

    private boolean required;

    public UrlValidator(
            EditText editText,
            String errorMessage,
            boolean autoDismiss,
            boolean required) {
        super(editText, errorMessage, autoDismiss);

        this.required = required;
    }

    @Override public boolean isValid(String value) {
        value = value.replace(" ", "");

        if(required && value.isEmpty()) return false;

        return new org.apache.commons.validator.routines.UrlValidator(
                org.apache.commons.validator.routines.UrlValidator.ALLOW_LOCAL_URLS
        ).isValid(value);
    }

}
