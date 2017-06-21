package convalida.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * @author  Wellington Costa on 18/06/2017.
 */
@Target(FIELD)
@Retention(CLASS)
public @interface PatternValidation {

    String errorMessage() default "Value does not match";

    String pattern();

}
