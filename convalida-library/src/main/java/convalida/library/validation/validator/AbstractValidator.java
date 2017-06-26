package convalida.library.validation.validator;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import convalida.library.validation.Validator;

/**
 * @author Wellington Costa on 21/06/2017.
 */
abstract class AbstractValidator implements Validator {

    private TextInputLayout layout;
    private EditText editText;
    private String errorMessage;
    private boolean hasError;

    AbstractValidator(TextInputLayout layout, String errorMessage) {
        this.layout = layout;
        this.editText = layout.getEditText();
        this.errorMessage = errorMessage;
        this.hasError = false;

        addTextChangeListener();
    }

    AbstractValidator(EditText editText, String errorMessage) {
        this.editText = editText;
        this.errorMessage = errorMessage;
        this.hasError = false;

        addTextChangeListener();
    }

    abstract boolean isNotValid(String value);

    abstract void executeValidation(String value);

    private void addTextChangeListener() {
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

    void setError() {
        if (layout != null) {
            layout.setErrorEnabled(true);
            layout.setError(errorMessage);
        } else {
            editText.setError(errorMessage);
        }

        hasError = true;
    }

    void clearError() {
        if (layout != null) {
            layout.setErrorEnabled(false);
            layout.setError(null);
        } else {
            editText.setError(null);
        }

        hasError = false;
    }

    @Override
    public boolean validate() {
        executeValidation(editText.getText().toString());
        return !hasError;
    }

    @Override
    public void clear() {
        clearError();
    }

}