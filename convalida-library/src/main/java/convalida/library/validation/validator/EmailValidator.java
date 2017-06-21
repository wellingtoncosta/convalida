package convalida.library.validation.validator;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.EditText;

import java.util.regex.Pattern;

import convalida.library.validation.Validator;

/**
 * @author Wellington Costa on 21/06/2017.
 */
public class EmailValidator extends AbstractValidator implements Validator {

    public EmailValidator(TextInputLayout layout, String errorMessage) {
        super(layout, errorMessage);
    }

    public EmailValidator(EditText editText, String errorMessage) {
        super(editText, errorMessage);
    }

    @Override
    boolean isNotValid(String value) {
        return !Patterns.EMAIL_ADDRESS.matcher(value).matches();
    }

    @Override
    void executeValidation(String value) {
        if (isNotValid(value)) {
            setError();
        } else {
            clearError();
        }
    }

    @Override
    void addTextChangeListener() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                executeValidation(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    @Override
    public boolean validate() {
        executeValidation(editText.getText().toString());
        return validatorState.hasError();
    }

    @Override
    public void clear() {
        clearError();
    }

}
