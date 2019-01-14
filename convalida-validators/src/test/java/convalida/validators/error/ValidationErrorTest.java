package convalida.validators.error;

import android.widget.EditText;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

/**
 * @author Wellington Costa on 31/12/18
 */
@RunWith(MockitoJUnitRunner.class)
public class ValidationErrorTest {

    private ValidationError error;
    private EditText editText;
    private String errorMessage = "Field required";

    @Before public void setup() {
        editText = mock(EditText.class);
        error = new ValidationError(editText, errorMessage);
    }

    @Test  public void areEquals() {
        assertEquals(error.editText, editText);
        assertEquals(error.errorMessage, errorMessage);
        assertEquals(error, new ValidationError(editText, errorMessage));
    }

    @Test public void areNotEquals() {
        EditText anotherEditText = mock(EditText.class);
        String anotherErrorMessage = "Required";
        assertNotEquals(error.editText, anotherEditText);
        assertNotEquals(error.errorMessage, anotherErrorMessage);
        assertNotEquals(error, new ValidationError(anotherEditText, anotherErrorMessage));
    }

    @Test public void areNotEqualsWithNullValue() {
        assertNotEquals(error, null);
    }

    @Test public void areNotEqualsDifferentErrorMessage() {
        assertNotEquals(error, new ValidationError(editText, "Another message"));
    }

}
