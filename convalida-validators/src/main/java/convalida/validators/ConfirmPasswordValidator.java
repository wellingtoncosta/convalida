package convalida.validators;

import android.widget.EditText;

/**
 * @author Wellington Costa on 27/06/2017.
 */
public class ConfirmPasswordValidator extends AbstractValidator {

    private EditText passwordEditText;

    public ConfirmPasswordValidator(
            EditText passwordEditText,
            EditText confirmPasswordEditText,
            String errorMessage,
            boolean autoDismiss) {
        super(confirmPasswordEditText, errorMessage,autoDismiss);
        this.passwordEditText = passwordEditText;
    }

    @Override public boolean isValid(String value) {
        return passwordEditText.getText().toString().equals(value);
    }

}
