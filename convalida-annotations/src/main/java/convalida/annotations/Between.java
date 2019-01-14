package convalida.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import androidx.annotation.StringRes;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * @author Wellington Costa on 25/04/18.
 */
public @interface Between {

    @Target(FIELD)
    @Retention(SOURCE)
    @interface Start {

        int key();

        @StringRes int errorMessageResId() default -1;

        String errorMessage() default "";

        boolean autoDismiss() default true;

    }

    @Target(FIELD)
    @Retention(SOURCE)
    @interface End {

        int key();

        @StringRes int errorMessageResId() default -1;

        String errorMessage() default "";

        boolean autoDismiss() default true;

    }

}
