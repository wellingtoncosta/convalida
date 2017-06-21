package convalida.library.validation;

import java.util.HashSet;
import java.util.Set;

/**
 * @author  Wellington Costa on 21/06/2017.
 */
public class ValidationSet {

    private Set<Validator> validators;
    private boolean valid;

    public ValidationSet() {
        this.validators = new HashSet<>();
        this.valid = false;
    }

    public void addValidator(Validator validator) {
        this.validators.add(validator);
    }

    public boolean isValid() {
        executeValidators();
        return valid;
    }

    private void executeValidators() {
        for (Validator validator : validators) {
            valid |= validator.validate();
        }
    }

    public void clearValidators() {
        for (Validator validator : validators) {
            validator.clear();
        }
    }

}
