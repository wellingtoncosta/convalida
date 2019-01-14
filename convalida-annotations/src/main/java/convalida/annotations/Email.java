package convalida.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import androidx.annotation.StringRes;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * @author Wellington Costa on 05/06/17.
 */
@Target(FIELD)
@Retention(SOURCE)
public @interface Email {

    @StringRes int errorMessageResId() default -1;

    String errorMessage() default "";

    boolean autoDismiss() default true;

    boolean required() default true;

}
