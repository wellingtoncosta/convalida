package convalida.validators;

import android.widget.EditText;

/**
 * @author Wellington Costa on 07/02/18.
 */
public class ConfirmEmailValidator extends AbstractValidator {

    private EditText emailEditText;

    public ConfirmEmailValidator(
            EditText emailEditText,
            EditText confirmEmailEditText,
            String errorMessage,
            boolean autoDismiss) {
        super(confirmEmailEditText, errorMessage, autoDismiss);
        this.emailEditText = emailEditText;
    }

    @Override public boolean isNotValid(String value) {
        return !emailEditText.getText().toString().equals(value);
    }

}
