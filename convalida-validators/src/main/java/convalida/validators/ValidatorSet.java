package convalida.validators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Wellington Costa on 21/06/2017.
 */
public class ValidatorSet {

    private Set<Validator> validators;
    private boolean isValid;

    public ValidatorSet() {
        this.validators = new HashSet<>();
        this.isValid = true;
    }

    public void addValidator(Validator validator) {
        this.validators.add(validator);
    }

    public boolean isValid() {
        executeValidators();
        return isValid;
    }

    private void executeValidators() {
        List<Boolean> validationResults = new ArrayList<>();

        for (Validator validator : validators) {
            validationResults.add(validator.validate());
        }

        for(Boolean validationResult : validationResults) {
            isValid = validationResult;
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

    int getValidatorsSize() {
        return validators.size();
    }

}