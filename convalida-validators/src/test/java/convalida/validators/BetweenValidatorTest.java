package convalida.validators;

import android.text.Editable;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author wellingtoncosta on 25/04/18
 */
public class BetweenValidatorTest {

    private EditText startEditText;
    private EditText limitEditText;

    private static String startErrorMessage = "Value cannot greater then limit field value";
    private static String limitErrorMessage = "Value cannot less then start field value";

    @Before public void setUp() {
        startEditText = mock(EditText.class);
        limitEditText = mock(EditText.class);
        when(startEditText.getText()).thenReturn(mock(Editable.class));
        when(limitEditText.getText()).thenReturn(mock(Editable.class));
    }

    @Test public void emptyValues() {
        BetweenValidator validator = new BetweenValidator(
                startEditText,
                limitEditText,
                startErrorMessage,
                limitErrorMessage,
                true,
                true);

        when(startEditText.getText().toString()).thenReturn("");
        when(limitEditText.getText().toString()).thenReturn("");

        assertFalse(validator.validate());
    }

    @Test public void startFieldWithValueAndLimitFieldWithEmptyValue() {
        BetweenValidator validator = new BetweenValidator(
                startEditText,
                limitEditText,
                startErrorMessage,
                limitErrorMessage,
                true,
                true);

        when(startEditText.getText().toString()).thenReturn("123");
        when(limitEditText.getText().toString()).thenReturn("");

        assertFalse(validator.validate());
    }

    @Test public void startValueGreaterThanLimitValue_Numeric() {
        BetweenValidator validator = new BetweenValidator(
                startEditText,
                limitEditText,
                startErrorMessage,
                limitErrorMessage,
                false,
                false);

        when(startEditText.getText().toString()).thenReturn("2000");
        when(limitEditText.getText().toString()).thenReturn("1000");

        assertFalse(validator.validate());
    }

    @Test public void startValueGreaterThanLimitValue_Text() {
        BetweenValidator validator = new BetweenValidator(
                startEditText,
                limitEditText,
                startErrorMessage,
                limitErrorMessage,
                false,
                false);

        when(startEditText.getText().toString()).thenReturn("zzzz");
        when(limitEditText.getText().toString()).thenReturn("aaaa");

        assertFalse(validator.validate());
    }

    @Test public void startValueEqualsToLimitValue_Numeric() {
        BetweenValidator validator = new BetweenValidator(
                startEditText,
                limitEditText,
                startErrorMessage,
                limitErrorMessage,
                true,
                true);

        when(startEditText.getText().toString()).thenReturn("2222");
        when(limitEditText.getText().toString()).thenReturn("2222");

        assertTrue(validator.validate());
    }

    @Test public void startValueEqualsToLimitValue_Text() {
        BetweenValidator validator = new BetweenValidator(
                startEditText,
                limitEditText,
                startErrorMessage,
                limitErrorMessage,
                true,
                true);

        when(startEditText.getText().toString()).thenReturn("xxxx");
        when(limitEditText.getText().toString()).thenReturn("xxxx");

        assertTrue(validator.validate());
    }

    @Test public void startValueLessThanLimitValue_Numeric() {
        BetweenValidator validator = new BetweenValidator(
                startEditText,
                limitEditText,
                startErrorMessage,
                limitErrorMessage,
                true,
                true);

        when(startEditText.getText().toString()).thenReturn("1111");
        when(limitEditText.getText().toString()).thenReturn("2222");

        assertTrue(validator.validate());
    }

    @Test public void startValueLessThanLimitValue_Text() {
        BetweenValidator validator = new BetweenValidator(
                startEditText,
                limitEditText,
                startErrorMessage,
                limitErrorMessage,
                true,
                true);

        when(startEditText.getText().toString()).thenReturn("xxxx");
        when(limitEditText.getText().toString()).thenReturn("zzzz");

        assertTrue(validator.validate());
    }

}
