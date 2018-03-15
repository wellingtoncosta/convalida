package convalida.validators;

import android.widget.EditText;

/**
 * @author Wellington Costa on 07/02/18.
 */
public class ConfirmEmailValidator extends AbstractValidator {

    private EditText emailField;

    public ConfirmEmailValidator(
            EditText emailField,
            EditText confirmEmailField,
            String errorMessage,
            boolean autoDismiss) {
        super(confirmEmailField, errorMessage, autoDismiss);
        this.emailField = emailField;
    }

    @Override public boolean isNotValid(String value) {
        return !emailField.getText().toString().equals(value);
    }

}
