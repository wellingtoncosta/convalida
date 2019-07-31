package convalida.compiler.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;

import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * @author wellingtoncosta on 02/04/18
 */
public class Messager {

    // Can not be instantiated
    private Messager() { }

    private static javax.annotation.processing.Messager messager;

    public static void init(javax.annotation.processing.Messager messager) {
        Messager.messager = messager;
    }

    public static void logParsingError(Element element, Class<? extends Annotation> annotation, Exception e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        error(element, "Unable to parse @%s validation.\n\n%s", annotation.getSimpleName(), stackTrace);
    }

    public static void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }

        messager.printMessage(Diagnostic.Kind.ERROR, message, element);
    }

}
