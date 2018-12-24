package convalida.compiler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;

import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * @author wellingtoncosta on 02/04/18
 */
class Messager {

    // Can not be instantiated
    private Messager() { }

    private static javax.annotation.processing.Messager messager;

    static void init(javax.annotation.processing.Messager messager) {
        Messager.messager = messager;
    }

    static void logParsingError(Element element, Class<? extends Annotation> annotation, Exception e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        error(element, "Unable to parse @%s validation.\n\n%s", annotation.getSimpleName(), stackTrace);
    }

    static void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }

        messager.printMessage(Diagnostic.Kind.ERROR, message, element);
    }

}
