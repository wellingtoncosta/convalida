package convalida.validators;

import android.widget.EditText;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import convalida.validators.error.ValidationError;
import convalida.validators.error.ValidationErrorSet;
import convalida.validators.util.ExecuteValidationListener;

import static convalida.validators.util.EditTexts.addOnTextChangedListener;

/**
 * @author Wellington Costa on 21/06/2017.
 */
public final class ValidatorSet {

    final Map<EditText, List<AbstractValidator>> map;
    private final List<Boolean> validationResults;
    public final ValidationErrorSet errors;
    private boolean valid;

    public ValidatorSet() {
        this.map = new HashMap<>();
        this.validationResults = new ArrayList<>();
        this.errors = new ValidationErrorSet();
        this.valid = true;
    }

    public void addValidator(@NonNull AbstractValidator validator) {
        createValidatorsListIfNotExists(validator);

        addValidatorToMap(validator);

        addTextChangeListener(validator.autoDismiss, validator.editText);
    }

    public void addValidators(@NonNull List<AbstractValidator> validators) {
        for(AbstractValidator validator : validators) {
            addValidator(validator);
        }
    }

    private void createValidatorsListIfNotExists(@NonNull AbstractValidator validator) {
        if(!this.map.containsKey(validator.editText)) {
            this.map.put(validator.editText, new ArrayList<AbstractValidator>());
        }
    }

    private void addValidatorToMap(@NonNull AbstractValidator validator) {
        List<AbstractValidator> validators = this.map.get(validator.editText);

        if (validators != null) {
            validators.add(validator);
        }
    }

    private void addTextChangeListener(boolean autoDismiss, final EditText editText) {
        if(autoDismiss) {
            final Map<EditText, List<AbstractValidator>> map = this.map;

            addOnTextChangedListener(editText, new ExecuteValidationListener() {
                @Override public void execute(String value) {
                    List<AbstractValidator> validators = map.get(editText);

                    if(validators != null) {
                        for(AbstractValidator validator : validators) {
                            boolean isValid = validator.validate(value);
                            if(!isValid) break;
                        }
                    }
                }
            });
        }
    }

    public boolean isValid() {
        executeValidators();
        return valid;
    }

    private void executeValidators() {
        errors.items.clear();

        validationResults.clear();

        for(EditText editText : map.keySet()) {
            List<AbstractValidator> validators = map.get(editText);

            if(validators != null) {
                for(AbstractValidator validator : validators) {
                    boolean isValid = validator.validate();

                    validationResults.add(isValid);

                    if(!isValid) {
                        addValidationError(validator);

                        break;
                    }
                }
            }
        }

        checkValidationResults();
    }

    private void addValidationError(AbstractValidator validator) {
        errors.items.add(new ValidationError(
                validator.editText,
                validator.errorMessage
        ));
    }

    private void checkValidationResults() {
        int totalInvalidFields = 0;

        for (Boolean validationResult : validationResults) {
            if (!validationResult)
                totalInvalidFields ++;
        }

        valid = totalInvalidFields == 0;
    }

    public void clearValidators() {
        errors.items.clear();

        for (List<AbstractValidator> validators : map.values()) {
            for(AbstractValidator validator : validators) {
                validator.clear();
            }
        }
    }

}