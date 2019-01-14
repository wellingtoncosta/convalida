package convalida.validators.error;

import android.widget.EditText;
import androidx.annotation.Nullable;

/**
 * @author Wellington Costa on 31/12/18
 */
public class ValidationError {

    public final EditText editText;
    public final String errorMessage;

    public ValidationError(EditText editText, String errorMessage) {
        this.editText = editText;
        this.errorMessage = errorMessage;
    }

    @Override public boolean equals(@Nullable Object obj) {
        if(obj == null) {
            return false;
        }

        ValidationError another = (ValidationError) obj;

        if(another.editText.equals(this.editText) &&
                another.errorMessage.equals(this.errorMessage)) {
            return true;
        }

        return super.equals(obj);
    }
}
