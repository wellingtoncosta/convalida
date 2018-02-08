package convalida.annotations;

import android.support.annotation.StringRes;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * @author  Wellington Costa on 18/06/2017.
 */
@Target(FIELD)
@Retention(SOURCE)
public @interface PatternValidation {

    String pattern();

    @StringRes int errorMessage();

    boolean autoDismiss() default true;

}
