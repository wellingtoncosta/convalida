package convalida.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import androidx.annotation.StringRes;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * @author Wellington Costa on 26/06/2017.
 */
@Target(FIELD)
@Retention(SOURCE)
public @interface Length {

    int min();

    int max() default 0;

    @StringRes int errorMessage();

    boolean autoDismiss() default true;

    boolean required() default true;

}
