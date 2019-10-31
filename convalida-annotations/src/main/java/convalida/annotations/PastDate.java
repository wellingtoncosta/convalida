package convalida.annotations;

import androidx.annotation.StringRes;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * @author Wellington Costa on 31/07/2019.
 */
@Target(FIELD)
@Retention(SOURCE)
public @interface PastDate {

    @StringRes int errorMessageResId() default -1;

    String errorMessage() default "";

    String dateFormat();

    String limitDate();

    boolean autoDismiss() default true;

    boolean required() default true;

}
