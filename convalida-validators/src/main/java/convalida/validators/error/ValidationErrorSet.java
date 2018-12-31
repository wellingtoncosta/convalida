package convalida.validators.error;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Wellington Costa on 31/12/18
 */
public class ValidationErrorSet {

    public final Set<ValidationError> items;

    public ValidationErrorSet() {
        this.items = new HashSet<>();
    }

}
