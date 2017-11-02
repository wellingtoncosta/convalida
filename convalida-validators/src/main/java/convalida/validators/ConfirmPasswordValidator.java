package convalida.validators;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

/**
 * @author Wellington Costa on 27/06/2017.
 */
public class ConfirmPasswordValidator extends AbstractValidator {

    private String password;

    ConfirmPasswordValidator(String password) {
        this.password = password;
    }

    public ConfirmPasswordValidator(TextInputLayout passwordLayout, TextInputLayout confirmPasswordLayout, String errorMessage) {
        super(confirmPasswordLayout, errorMessage);

        if (passwordLayout.getEditText() != null) {
            this.password = passwordLayout.getEditText().getText().toString();
        }
    }

    public ConfirmPasswordValidator(EditText passwordEditText, EditText confirmPasswordEditText, String errorMessage) {
        super(confirmPasswordEditText, errorMessage);

        if (passwordEditText != null) {
            this.password = passwordEditText.getText().toString();
        }
    }

    @Override
    boolean isNotValid(String value) {
        return value == null || !password.equals(value);
    }

}
