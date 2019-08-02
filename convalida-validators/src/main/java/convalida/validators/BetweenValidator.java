package convalida.validators;

import android.widget.EditText;

import java.math.BigDecimal;

import convalida.validators.util.EditTexts;
import convalida.validators.util.ExecuteValidationListener;

/**
 * @author Wellington Costa on 25/04/18
 */
public class BetweenValidator extends AbstractValidator {

    private EditText endEditText;
    private String endErrorMessage;
    private boolean endFieldHasError = false;

    public BetweenValidator(
            final EditText startEditText,
            final EditText limitEditText,
            final String startErrorMessage,
            final String limitErrorMessage,
            boolean startAutoDismiss,
            final boolean limitAutoDismiss
    ) {
        super(startEditText, startErrorMessage, startAutoDismiss);
        this.endEditText = limitEditText;
        this.endErrorMessage = limitErrorMessage;

        if(limitAutoDismiss) {
            EditTexts.addOnTextChangedListener(limitEditText, new ExecuteValidationListener() {
                @Override
                public void execute(String value) {
                    applyValidationToEndField();
                }
            });
        }
    }

    private boolean endFieldIsValid() {
        String startValue = editText.getText().toString();
        String endValue = endEditText.getText().toString();
        try {
            BigDecimal startValueBigDecimal = new BigDecimal(startValue);
            BigDecimal endValueBigDecimal = new BigDecimal(endValue);
            return startValueBigDecimal.compareTo(endValueBigDecimal) <= 0;
        } catch (NumberFormatException e) {
            return startValue.compareTo(endValue) <= 0;
        }
    }

    private void applyValidationToEndField() {
        endFieldHasError = !endFieldIsValid();
        if(endFieldIsValid()) {
            EditTexts.removeError(editText);
            EditTexts.removeError(endEditText);
        } else {
            EditTexts.setError(editText, errorMessage);
            EditTexts.setError(endEditText, endErrorMessage);
        }
    }

    @Override
    public boolean isValid(String value) {
        applyValidationToEndField();
        String endFieldValue = endEditText.getText().toString();
        return !(value.isEmpty()
                || !value.equals(endFieldValue)
                && (endFieldValue.isEmpty()
                || endFieldHasError));
    }
}
