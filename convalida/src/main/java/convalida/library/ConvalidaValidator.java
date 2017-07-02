package convalida.library;

import android.support.annotation.UiThread;

/**
 * @author Wellington Costa on 19/06/2017.
 */
public interface ConvalidaValidator {

    @UiThread
    boolean validateFields();

    @UiThread
    void clearValidations();


    ConvalidaValidator EMPTY = new ConvalidaValidator() {

        @Override
        public boolean validateFields() { return true; }

        @Override
        public void clearValidations() { }

    };

}
