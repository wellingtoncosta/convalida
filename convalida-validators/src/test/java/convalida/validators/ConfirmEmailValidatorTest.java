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
public class ConfirmEmailValidatorTest extends BaseTest {

    private EditText emailEditText;

    @Before public void setUpValidators() {
        emailEditText = mock(EditText.class);
        when(emailEditText.getText()).thenReturn(mock(Editable.class));
    }

    @Test public void emptyValues() {
        ConfirmEmailValidator validator = new ConfirmEmailValidator(emailEditText, mockEditText, errorMessage, true);
        when(emailEditText.getText().toString()).thenReturn("");
        when(mockEditText.getText().toString()).thenReturn("");
        assertEquals(validator.validate(), true);
    }

    @Test public void emailsDoesNotMatch() {
        ConfirmEmailValidator validator = new ConfirmEmailValidator(emailEditText, mockEditText, errorMessage, true);
        when(emailEditText.getText().toString()).thenReturn("wellington@email.com");
        when(mockEditText.getText().toString()).thenReturn("wellington@email.co");
        assertEquals(validator.validate(), false);
    }

    @Test public void emailsMatch() {
        ConfirmPasswordValidator validator = new ConfirmPasswordValidator(emailEditText, mockEditText, errorMessage, true);
        when(emailEditText.getText().toString()).thenReturn("wellington@email.com");
        when(mockEditText.getText().toString()).thenReturn("wellington@email.com");
        assertEquals(validator.validate(), true);
    }

}