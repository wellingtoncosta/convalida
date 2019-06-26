package convalida.validators;

import android.widget.EditText;

import java.math.BigDecimal;

/**
 * @author Wellington Costa on 04/06/18
 */
public class NumericLimitValidator extends AbstractValidator {

    private BigDecimal minValue;
    private BigDecimal maxValue;
    private boolean required;

    public NumericLimitValidator(
            EditText editText,
            String errorMessage,
            boolean autoDismiss,
            String min,
            String max,
            boolean required
    ) {
        super(editText, errorMessage, autoDismiss);
        this.minValue = new BigDecimal(min);
        this.maxValue = new BigDecimal(max);
        this.required = required;
    }

    @Override public boolean isValid(String value) {
        if(required && value.isEmpty()) {
            return false;
        } else {
            if(value.isEmpty()) return true;
            try {
                BigDecimal bigDecimal = new BigDecimal(value);
                return bigDecimal.compareTo(minValue) > -1
                        && bigDecimal.compareTo(maxValue) < 1;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }
}
