package convalida.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import androidx.annotation.StringRes;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * @author Wellington Costa on 27/06/2017.
 */
@Target(FIELD)
@Retention(SOURCE)
public @interface ConfirmPassword {

    @StringRes int errorMessageResId() default -1;

    String errorMessage() default "";

    boolean autoDismiss() default true;

}
