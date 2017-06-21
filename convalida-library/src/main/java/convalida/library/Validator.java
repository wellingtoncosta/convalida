package convalida.library;

import android.support.annotation.UiThread;

/**
 * @author Wellington Costa on 19/06/2017.
 */
public interface Validator {

    @UiThread
    void validate();

    @UiThread
    void clear();


    Validator EMPTY = new Validator() {

        @Override
        public void validate() { }

        @Override
        public void clear() { }

    };

}
