package convalida.library;

import android.app.Activity;
import android.support.annotation.UiThread;
import android.util.Log;

/**
 * @author  Wellington Costa on 18/06/2017.
 */
public class Convalida {

    @UiThread
    public static void init(Activity target) {
        Log.i("Convalida", "init!");
    }

}