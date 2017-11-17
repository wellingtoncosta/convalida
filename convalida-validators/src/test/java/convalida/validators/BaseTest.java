package convalida.validators;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.widget.EditText;

import org.junit.Before;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 16/11/2017.
 */
public class BaseTest {

    TextInputLayout mockTextInputLayout;
    EditText mockEditText;
    String errorMessage = "Field invalid";

    @Before
    public void setUp() {
        mockTextInputLayout = mock(TextInputLayout.class);
        mockEditText = mock(EditText.class);

        Editable mockEditable = mock(Editable.class);

        when(mockEditText.getText()).thenReturn(mockEditable);
        when(mockTextInputLayout.getEditText()).thenReturn(mockEditText);
    }

}
