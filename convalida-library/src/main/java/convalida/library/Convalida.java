package convalida.library;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author  Wellington Costa on 18/06/2017.
 */
public class Convalida {

    private static final String TAG = "Convalida";
    private static boolean debug = false;

    public static void setDebug(boolean debug) {
        Convalida.debug = debug;
    }

    @UiThread
    public static ConvalidaValidator initialize(@NonNull Object target) {
        Class<?> targetClass = target.getClass();

        if (debug) Log.d(TAG, "Looking up validation for " + targetClass.getName());
        Constructor<? extends ConvalidaValidator> constructor = findConstructor(targetClass);

        if (constructor == null) {
            return ConvalidaValidator.EMPTY;
        }

        //noinspection TryWithIdenticalCatches Resolves to API 19+ only type.
        try {
            return constructor.newInstance(target);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new RuntimeException("Unable to create validation instance.", cause);
        }
    }

    private static Constructor<? extends ConvalidaValidator> findConstructor(Class<?> clazz) {
        String clsName = clazz.getName();

        if (clsName.startsWith("android.") || clsName.startsWith("java.")) {
            if (debug) Log.d(TAG, "Reached framework class. Abandoning constructor search.");
            return null;
        }

        try {
            Class<?> validationClass = clazz.getClassLoader().loadClass(clsName + "_Validation");
            if (debug) Log.d(TAG, "Loaded validation class and constructor.");
            //noinspection unchecked
            return (Constructor<? extends ConvalidaValidator>) validationClass.getConstructor(clazz);
        } catch (ClassNotFoundException e) {
            if (debug) Log.d(TAG, "Constructor not found. Trying superclass... " + clazz.getSuperclass().getName());
            return findConstructor(clazz.getSuperclass());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find validation constructor for " + clsName, e);
        }
    }

}