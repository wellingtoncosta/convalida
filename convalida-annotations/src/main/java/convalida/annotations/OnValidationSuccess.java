package convalida.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * @author Wellington Costa on 19/01/2018.
 */
@Target(METHOD)
@Retention(SOURCE)
public @interface OnValidationSuccess { }
