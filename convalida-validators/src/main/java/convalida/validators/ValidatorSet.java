package convalida.validators;

import convalida.validators.error.ValidationError;
import convalida.validators.error.ValidationErrorSet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Wellington Costa on 21/06/2017.
 */
public final class ValidatorSet {

    public final List<AbstractValidator> validators;
    public final ValidationErrorSet errors;
    private boolean valid;

    public ValidatorSet() {
        this.validators = new LinkedList<>();
        this.errors = new ValidationErrorSet();
        this.valid = true;
    }

    public void addValidator(AbstractValidator validator) {
        this.validators.add(validator);
    }

    public boolean isValid() {
        executeValidators();
        return valid;
    }

    private void executeValidators() {
        errors.items.clear();
        List<Boolean> validationResults = new ArrayList<>();

        for(AbstractValidator validator : validators) {
            boolean valid = validator.validate();

            if(!valid) {
                errors.items.add(new ValidationError(
                        validator.editText,
                        validator.errorMessage
                ));
            }

            validationResults.add(valid);
        }

        for (Boolean validationResult : validationResults) {
            valid = validationResult;
            if (!validationResult) {
                break;
            }
        }
    }

    public void clearValidators() {
        errors.items.clear();
        for (AbstractValidator validator : validators) {
            validator.clear();
        }
    }

}