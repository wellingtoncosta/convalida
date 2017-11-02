package convalida.library;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import convalida.validators.Validator;

/**
 * @author Wellington Costa on 21/06/2017.
 */
public class ValidationSet {

    private Set<Validator> validators;
    private boolean valid;

    public ValidationSet() {
        this.validators = new HashSet<>();
        this.valid = true;
    }

    public void addValidator(Validator validator) {
        this.validators.add(validator);
    }

    public boolean isValid() {
        executeValidators();
        return valid;
    }

    private void executeValidators() {
        List<Boolean> validationResults = new ArrayList<>();

        for (Validator validator : validators) {
            validationResults.add(validator.validate());
        }

        for(Boolean validationResult : validationResults) {
            valid = validationResult;
            if (!validationResult) {
                break;
            }
        }
    }

    public void clearValidators() {
        for (Validator validator : validators) {
            validator.clear();
        }
    }

}
