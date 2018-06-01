package convalida.validators;

import android.widget.EditText;

/**
 * @author Wellington Costa on 31/05/18.
 */
public class CreditCardValidator extends AbstractValidator {

    private boolean required;

    public CreditCardValidator(
            EditText editText,
            String errorMessage,
            boolean autoDismiss,
            boolean required
    ) {
        super(editText, errorMessage, autoDismiss);
        this.required = required;
    }

    @Override
    public boolean isValid(String value) {
        value = value.replace(" ", "");
        if(required && value.isEmpty()) {
            return false;
        } else {
            org.apache.commons.validator.routines.CreditCardValidator validator = new org.apache.commons.validator.routines.CreditCardValidator();
            return (value.isEmpty() || validator.isValid(value));
        }

    }

}