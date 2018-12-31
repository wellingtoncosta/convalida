package convalida.validators;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Wellington Costa on 22/01/2018.
 */
public class ValidatorSetTest extends BaseTest {

    private EditText mockEditText_2;
    private EditText mockEditText_3;
    private ValidatorSet validatorSet;

    @Before public void setupValidator() {
        mockEditText_2 = mock(EditText.class);
        when(mockEditText_2.getText()).thenReturn(mock(Editable.class));
        mockEditText_3 = mock(EditText.class);
        when(mockEditText_3.getText()).thenReturn(mock(Editable.class));
        validatorSet = new ValidatorSet();
    }

    @Test public void addOneValidator() {
        validatorSet.addValidator(new RequiredValidator(mockEditText, errorMessage, true));
        assertEquals(validatorSet.getValidatorsCount(), 1);
    }

    @Test public void addTwoValidators() {
        validatorSet.addValidator(new RequiredValidator(mockEditText, errorMessage, true));
        validatorSet.addValidator(new EmailValidator(mockEditText_2, errorMessage, true, true));
        assertEquals(validatorSet.getValidatorsCount(), 2);
    }

    @Test public void addThreeValidators() {
        validatorSet.addValidator(new RequiredValidator(mockEditText, errorMessage, true));
        validatorSet.addValidator(new EmailValidator(mockEditText_2, errorMessage, true, true));
        validatorSet.addValidator(new LengthValidator(mockEditText_3,errorMessage, 0, 5, true, true));
        assertEquals(validatorSet.getValidatorsCount(), 3);
    }

    @Test public void executeValidationsWithSuccess() {
        validatorSet.addValidator(new RequiredValidator(mockEditText, errorMessage, true));
        validatorSet.addValidator(new RequiredValidator(mockEditText_2, errorMessage, true));
        validatorSet.addValidator(new RequiredValidator(mockEditText_3, errorMessage, true));

        when(mockEditText.getVisibility()).thenReturn(View.GONE);
        when(mockEditText.getText().toString()).thenReturn("");

        when(mockEditText_2.getVisibility()).thenReturn(View.INVISIBLE);
        when(mockEditText_2.getText().toString()).thenReturn("");

        when(mockEditText_3.getVisibility()).thenReturn(View.VISIBLE);
        when(mockEditText_3.getText().toString()).thenReturn("test");
        assertTrue(validatorSet.isValid());
    }

    @Test public void executeValidationsWithError() {
        validatorSet.addValidator(new RequiredValidator(mockEditText, errorMessage, true));
        validatorSet.addValidator(new RequiredValidator(mockEditText_2, errorMessage, true));
        validatorSet.addValidator(new RequiredValidator(mockEditText_3, errorMessage, true));

        when(mockEditText.getVisibility()).thenReturn(View.GONE);
        when(mockEditText.getText().toString()).thenReturn("");

        when(mockEditText_2.getVisibility()).thenReturn(View.INVISIBLE);
        when(mockEditText_2.getText().toString()).thenReturn("");

        when(mockEditText_3.getVisibility()).thenReturn(View.VISIBLE);
        when(mockEditText_3.getText().toString()).thenReturn("");

        assertFalse(validatorSet.isValid());
    }

    @Test public void clearValidations() {
        validatorSet.addValidator(new RequiredValidator(mockEditText, errorMessage, true));
        validatorSet.clearValidators();
        assertNull(mockEditText.getError());
    }

}