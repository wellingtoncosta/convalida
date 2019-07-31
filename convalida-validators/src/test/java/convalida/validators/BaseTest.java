package convalida.validators;

import android.text.Editable;
import android.widget.EditText;
import org.junit.Before;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 16/11/2017.
 */
public class BaseTest {

    protected final String EMPTY_VALUE = "";

    protected EditText mockEditText;

    protected final String errorMessage = "Field invalid";

    @Before public void setUp() {
        mockEditText = mock(EditText.class);
        when(mockEditText.getText()).thenReturn(mock(Editable.class));
    }

}
