package convalida.annotations;

import android.support.annotation.StringRes;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * @author Wellington Costa on 26/06/2017.
 */
@Target(FIELD)
@Retention(CLASS)
public @interface NumericOnlyValidation {

    @StringRes int value();

}
