package convalida.validators;

import android.widget.EditText;
import java.util.regex.Pattern;

/**
 * @author Wellington Costa on 29/06/2017.
 */
public class OnlyNumberValidator extends AbstractValidator{

    public OnlyNumberValidator(
            EditText editText,
            String errorMessage,
            boolean autoDismiss) {
        super(editText, errorMessage, autoDismiss);
    }

    @Override public boolean isNotValid(String value) {
        return !Pattern.compile("^\\d+$").matcher(value).matches();
    }

}
