package convalida.annotations;

import androidx.annotation.StringRes;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * @author Wellington Costa on 30/07/2019.
 */
@Target(FIELD)
@Retention(SOURCE)
public @interface Ipv6 {

    @StringRes int errorMessageResId() default -1;

    String errorMessage() default "";

    boolean autoDismiss() default true;

    boolean required() default true;

}
