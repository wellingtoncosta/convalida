package convalida.validators;

import android.widget.EditText;

import org.apache.commons.validator.routines.InetAddressValidator;

/**
 * @author Wellington Costa on 30/07/2019.
 */
public class Ipv4Validator extends AbstractValidator {

    private boolean required;

    public Ipv4Validator(
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

        return InetAddressValidator.getInstance().isValidInet4Address(value);
    }

}
