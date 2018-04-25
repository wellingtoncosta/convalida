package convalida.validators;

import android.widget.EditText;

import java.math.BigDecimal;

import convalida.validators.util.EditTextUtils;
import convalida.validators.util.ExecuteValidationListener;

/**
 * @author Wellington Costa on 25/04/18
 */
public class BetweenValidator extends AbstractValidator {

    private EditText endEditText;

    public BetweenValidator(
            final EditText startEditText,
            final EditText endEditText,
            final String startErrorMessage,
            final String endErrorMessage,
            boolean startAutoDismiss,
            final boolean endAutoDismiss
    ) {
        super(startEditText, startErrorMessage, startAutoDismiss);
        this.endEditText = endEditText;

        if(endAutoDismiss) {
            EditTextUtils.addOnTextChangedListener(endEditText, new ExecuteValidationListener() {
                @Override
                public void execute(String value) {
                    boolean endFieldIsValid = endFieldIsValid();
                    if(endFieldIsValid) {
                        EditTextUtils.setError(startEditText, null);
                        EditTextUtils.setError(endEditText, null);
                    } else {
                        EditTextUtils.setError(startEditText, startErrorMessage);
                        EditTextUtils.setError(endEditText, endErrorMessage);
                    }
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

    @Override
    public boolean isNotValid(String value) {
        String endFieldValue = endEditText.getText().toString();
        return value.isEmpty()
                || !value.equals(endFieldValue)
                && (endFieldValue.isEmpty()
                || !endFieldIsValid());
    }
}
