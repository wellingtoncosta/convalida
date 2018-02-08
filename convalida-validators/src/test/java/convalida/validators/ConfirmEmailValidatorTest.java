package convalida.validators;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 02/11/2017.
 */
public class ConfirmEmailValidatorTest extends BaseTest {

    private EditText emailEditText;
    private TextInputLayout emailTextInputLayout;

    @Before
    public void setUpValidators() {
        emailEditText = mock(EditText.class);
        emailTextInputLayout = mock(TextInputLayout.class);

        Editable emailEditable = mock(Editable.class);

        when(emailEditText.getText()).thenReturn(emailEditable);
        when(emailTextInputLayout.getEditText()).thenReturn(emailEditText);
    }

    @Test
    public void emailsDoesNotMatch() {
        String email = "wellington@email.com";
        String confirmEmail = "wellington@email.co";

        ConfirmEmailValidator validator = new ConfirmEmailValidator(email);
        ConfirmEmailValidator validatorWithEditText = new ConfirmEmailValidator(emailEditText, mockEditText, errorMessage);
        ConfirmEmailValidator validatorWithTextInputLayout = new ConfirmEmailValidator(emailTextInputLayout, mockTextInputLayout, errorMessage);

        when(emailEditText.getText().toString()).thenReturn(email);
        when(mockEditText.getText().toString()).thenReturn(confirmEmail);

        assertEquals(validator.isNotValid(confirmEmail), true);
        assertEquals(validatorWithEditText.validate(), false);
        assertEquals(validatorWithTextInputLayout.validate(), false);
    }

    @Test
    public void emailsMatch() {
        String email = "wellington@email.com";
        String confirmEmail = "wellington@email.com";

        ConfirmPasswordValidator validator = new ConfirmPasswordValidator(email);
        ConfirmPasswordValidator validatorWithEditText = new ConfirmPasswordValidator(emailEditText, mockEditText, errorMessage);
        ConfirmPasswordValidator validatorWithTextInputLayout = new ConfirmPasswordValidator(emailTextInputLayout, mockTextInputLayout, errorMessage);

        when(emailEditText.getText().toString()).thenReturn(email);
        when(mockEditText.getText().toString()).thenReturn(confirmEmail);

        assertEquals(validator.isNotValid(confirmEmail), false);
        assertEquals(validatorWithEditText.validate(), true);
        assertEquals(validatorWithTextInputLayout.validate(), true);

    }

}