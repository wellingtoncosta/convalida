package convalida.library;

import android.content.Context;
import android.util.Log;

/**
 * @author  Wellington Costa on 18/06/2017.
 */
public class Convalida {

    public static void init(Context context) {
        Log.i("Convalida", "init!");
    }

    public static boolean validate() {
        Log.i("Convalida", "validate!");
        return true;
    }

}