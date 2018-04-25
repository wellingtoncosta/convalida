package convalida.validators;

import android.text.Editable;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author wellingtoncosta on 25/04/18
 */
public class BetweenValidatorTest {

    private EditText startEditText;
    private EditText endEditText;

    private static String startErrorMessage = "Value cannot greater then end field value";
    private static String endErrorMessage = "Value cannot less then start field value";

    @Before public void setUp() {
        startEditText = mock(EditText.class);
        endEditText = mock(EditText.class);
        when(startEditText.getText()).thenReturn(mock(Editable.class));
        when(endEditText.getText()).thenReturn(mock(Editable.class));
    }

    @Test public void emptyValues() {
        BetweenValidator validator = new BetweenValidator(
                startEditText,
                endEditText,
                startErrorMessage,
                endErrorMessage,
                true,
                true);

        when(startEditText.getText().toString()).thenReturn("");
        when(endEditText.getText().toString()).thenReturn("");

        assertEquals(validator.validate(), false);
    }

    @Test public void startValueGreaterThanEndValue_Numeric() {
        BetweenValidator validator = new BetweenValidator(
                startEditText,
                endEditText,
                startErrorMessage,
                endErrorMessage,
                false,
                false);

        when(startEditText.getText().toString()).thenReturn("2000");
        when(endEditText.getText().toString()).thenReturn("1000");

        assertEquals(validator.validate(), false);
    }

    @Test public void startValueGreaterThanEndValue_Text() {
        BetweenValidator validator = new BetweenValidator(
                startEditText,
                endEditText,
                startErrorMessage,
                endErrorMessage,
                false,
                false);

        when(startEditText.getText().toString()).thenReturn("zzzz");
        when(endEditText.getText().toString()).thenReturn("aaaa");

        assertEquals(validator.validate(), false);
    }

    @Test public void startValueEqualsToEndValue_Numeric() {
        BetweenValidator validator = new BetweenValidator(
                startEditText,
                endEditText,
                startErrorMessage,
                endErrorMessage,
                true,
                true);

        when(startEditText.getText().toString()).thenReturn("2222");
        when(endEditText.getText().toString()).thenReturn("2222");

        assertEquals(validator.validate(), true);
    }

    @Test public void startValueEqualsToEndValue_Text() {
        BetweenValidator validator = new BetweenValidator(
                startEditText,
                endEditText,
                startErrorMessage,
                endErrorMessage,
                true,
                true);

        when(startEditText.getText().toString()).thenReturn("xxxx");
        when(endEditText.getText().toString()).thenReturn("xxxx");

        assertEquals(validator.validate(), true);
    }

    @Test public void startValueLessThanEndValue_Numeric() {
        BetweenValidator validator = new BetweenValidator(
                startEditText,
                endEditText,
                startErrorMessage,
                endErrorMessage,
                true,
                true);

        when(startEditText.getText().toString()).thenReturn("1111");
        when(endEditText.getText().toString()).thenReturn("2222");

        assertEquals(validator.validate(), true);
    }

    @Test public void startValueLessThanEndValue_Text() {
        BetweenValidator validator = new BetweenValidator(
                startEditText,
                endEditText,
                startErrorMessage,
                endErrorMessage,
                true,
                true);

        when(startEditText.getText().toString()).thenReturn("xxxx");
        when(endEditText.getText().toString()).thenReturn("zzzz");

        assertEquals(validator.validate(), true);
    }

}
