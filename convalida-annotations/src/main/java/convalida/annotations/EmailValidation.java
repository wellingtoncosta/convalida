package convalida.annotations;

import android.support.annotation.StringRes;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * @author Wellington Costa on 05/06/17.
 */
@Target(FIELD)
@Retention(SOURCE)
public @interface EmailValidation {

    @StringRes int errorMessage();

    boolean autoDismiss() default true;

}
