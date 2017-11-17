package convalida.validators;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 02/11/2017.
 */
public class ConfirmPasswordValidatorTest extends BaseTest {

    private EditText passwordEditText;
    private TextInputLayout passwordTextInputLayout;

    @Before
    public void setUpValidators() {
        passwordEditText = mock(EditText.class);
        passwordTextInputLayout = mock(TextInputLayout.class);

        Editable passwordEditable = mock(Editable.class);

        when(passwordEditText.getText()).thenReturn(passwordEditable);
        when(passwordTextInputLayout.getEditText()).thenReturn(passwordEditText);
    }

    @Test
    public void passwordsDoesNotMatch() {
        String password = "test@123";
        String confirmPassword = "test@12";

        ConfirmPasswordValidator validator = new ConfirmPasswordValidator(password);
        ConfirmPasswordValidator validatorWithEditText = new ConfirmPasswordValidator(passwordEditText, mockEditText, errorMessage);
        ConfirmPasswordValidator validatorWithTextInputLayout = new ConfirmPasswordValidator(passwordTextInputLayout, mockTextInputLayout, errorMessage);

        when(passwordEditText.getText().toString()).thenReturn(password);
        when(mockEditText.getText().toString()).thenReturn(confirmPassword);

        assertEquals(validator.isNotValid(confirmPassword), true);
        assertEquals(validatorWithEditText.validate(), false);
        assertEquals(validatorWithTextInputLayout.validate(), false);
    }

    @Test
    public void passwordsMatch() {
        String password = "test@123";
        String confirmPassword = "test@123";

        ConfirmPasswordValidator validator = new ConfirmPasswordValidator(password);
        ConfirmPasswordValidator validatorWithEditText = new ConfirmPasswordValidator(passwordEditText, mockEditText, errorMessage);
        ConfirmPasswordValidator validatorWithTextInputLayout = new ConfirmPasswordValidator(passwordTextInputLayout, mockTextInputLayout, errorMessage);

        when(passwordEditText.getText().toString()).thenReturn(password);
        when(mockEditText.getText().toString()).thenReturn(confirmPassword);

        assertEquals(validator.isNotValid(confirmPassword), false);
        assertEquals(validatorWithEditText.validate(), true);
        assertEquals(validatorWithTextInputLayout.validate(), true);

    }

}