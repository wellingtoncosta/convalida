package convalida.compiler.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.Objects;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * @author wellingtoncosta on 02/04/18
 */
public class Messager {

    // Can not be instantiated
    private Messager() { }

    private static javax.annotation.processing.Messager messager;

    private static boolean initialized = false;

    public static void init(ProcessingEnvironment processingEnvironment) {
        if (initialized) {
            throw new IllegalStateException("Cannot call init more than once.");
        } else {
            Objects.requireNonNull(
                    processingEnvironment,
                    "processingEnvironment parameter cannot be null."
            );

            initialized = true;

            messager = processingEnvironment.getMessager();
        }
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

        if(Messager.messager == null) throw new IllegalStateException(
                "javax.annotation.processing.Messager messager property is not initialized."
        );

        messager.printMessage(Diagnostic.Kind.ERROR, message, element);
    }

}
