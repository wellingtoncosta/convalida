package convalida.library.validation.validator;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

/**
 * @author Wellington Costa on 27/06/2017.
 */
public class ConfirmPasswordValidator extends AbstractValidator {

    private EditText passwordEditText;

    public ConfirmPasswordValidator(TextInputLayout passwordLayout, TextInputLayout confirmPasswordLayout, String errorMessage) {
        super(confirmPasswordLayout, errorMessage);
        this.passwordEditText = passwordLayout.getEditText();
    }

    public ConfirmPasswordValidator(EditText passwordEditText, EditText confirmPasswordEditText, String errorMessage) {
        super(confirmPasswordEditText, errorMessage);
        this.passwordEditText = passwordEditText;
    }

    @Override
    boolean isNotValid(String value) {
        return !passwordEditText.getText().toString().equals(value);
    }
}
