package convalida.validators;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Wellington Costa on 21/06/2017.
 */
public final class ValidatorSet {

    private List<AbstractValidator> validators;
    private boolean isValid;

    public ValidatorSet() {
        this.validators = new LinkedList<>();
        this.isValid = true;
    }

    public void addValidator(AbstractValidator validator) {
        this.validators.add(validator);
    }

    public boolean isValid() {
        executeValidators();
        return isValid;
    }

    private void executeValidators() {
        List<Boolean> validationResults = new ArrayList<>();

        for(AbstractValidator validator : validators) {
            validationResults.add(validator.validate());
        }

        for (Boolean validationResult : validationResults) {
            isValid = validationResult;
            if (!validationResult) {
                break;
            }
        }
    }

    public void clearValidators() {
        for (AbstractValidator validator : validators) {
            validator.clear();
        }
    }

    public List<AbstractValidator> getValidators() {
        return validators;
    }

    public int getValidatorsCount() {
        return validators.size();
    }

}