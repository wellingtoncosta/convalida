package convalida.library.validation.validator;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import convalida.library.validation.Validator;

/**
 * @author Wellington Costa on 21/06/2017.
 */
abstract class AbstractValidator implements Validator {

    private TextInputLayout layout;
    EditText editText;
    private String errorMessage;
    private ValidatorState validatorState;

    AbstractValidator(TextInputLayout layout, String errorMessage) {
        this.layout = layout;
        this.editText = layout.getEditText();
        this.errorMessage = errorMessage;
        this.validatorState = new ValidatorState();

        addTextChangeListener();
    }

    AbstractValidator(EditText editText, String errorMessage) {
        this.editText = editText;
        this.errorMessage = errorMessage;
        this.validatorState = new ValidatorState();

        addTextChangeListener();
    }

    abstract void addTextChangeListener();

    abstract boolean isNotValid(String value);

    abstract void executeValidation(String value);

    void setError() {
        if (layout != null) {
            layout.setErrorEnabled(true);
            layout.setError(errorMessage);
        } else {
            editText.setError(errorMessage);
        }

        validatorState.setError(true);
    }

    void clearError() {
        if (layout != null) {
            layout.setErrorEnabled(false);
            layout.setError(null);
        } else {
            editText.setError(null);
        }

        validatorState.setError(false);
    }

    /**
     *
     * @return true if is valid or false if is not valid
     *
     */
    @Override
    public boolean validate() {
        executeValidation(editText.getText().toString());
        return !validatorState.hasError();
    }

    @Override
    public void clear() {
        clearError();
    }


    private static class ValidatorState {

        private boolean error;

        ValidatorState() {
            this.error = false;
        }

        void setError(boolean error) {
            this.error = error;
        }

        boolean hasError() {
            return error;
        }
    }

}
