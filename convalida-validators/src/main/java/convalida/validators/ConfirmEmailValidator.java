package convalida.validators;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

/**
 * @author Wellington Costa on 07/02/18.
 */
public class ConfirmEmailValidator extends AbstractValidator {

    private EditText emailField;
    private String email;

    ConfirmEmailValidator(String email) {
        this.email = email;
    }

    public ConfirmEmailValidator(TextInputLayout emailLayout, TextInputLayout confirmEmailLayout, String errorMessage) {
        super(confirmEmailLayout, errorMessage);
        this.emailField = emailLayout.getEditText();
    }

    public ConfirmEmailValidator(EditText emailField, EditText confirmEmailField, String errorMessage) {
        super(confirmEmailField, errorMessage);
        this.emailField = emailField;
    }

    @Override
    boolean isNotValid(String value) {
        if (emailField != null) {
            email = emailField.getText().toString();
        }

        return !email.equals(value);
    }

}
