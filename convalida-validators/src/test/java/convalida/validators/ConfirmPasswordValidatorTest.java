package convalida.validators;

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
public class ConfirmPasswordValidatorTest extends BaseTest {

    private EditText passwordEditText;

    @Before public void setUpValidators() {
        passwordEditText = mock(EditText.class);
        when(passwordEditText.getText()).thenReturn(mock(Editable.class));
    }

    @Test public void emptyValues() {
        ConfirmEmailValidator validator = new ConfirmEmailValidator(passwordEditText, mockEditText, errorMessage, true);
        when(passwordEditText.getText().toString()).thenReturn("");
        when(mockEditText.getText().toString()).thenReturn("");
        assertEquals(validator.validate(), true);
    }

    @Test public void passwordsDoesNotMatch() {
        ConfirmPasswordValidator validator = new ConfirmPasswordValidator(passwordEditText, mockEditText, errorMessage, true);
        when(passwordEditText.getText().toString()).thenReturn("test@123");
        when(mockEditText.getText().toString()).thenReturn("test@12");
        assertEquals(validator.validate(), false);
    }

    @Test public void passwordsMatch() {
        ConfirmPasswordValidator validatorWithEditText = new ConfirmPasswordValidator(passwordEditText, mockEditText, errorMessage, true);
        when(passwordEditText.getText().toString()).thenReturn("test@123");
        when(mockEditText.getText().toString()).thenReturn("test@123");
        assertEquals(validatorWithEditText.validate(), true);
    }

}