package convalida.compiler;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static convalida.compiler.Constants.EDIT_TEXT_TYPE;
import static convalida.compiler.Messager.error;
import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.FIELD;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * @author wellingtoncosta on 02/04/18
 */
class Preconditions {

    // Can not be instantiated
    private Preconditions() { }

    static boolean methodHasParams(ExecutableElement method, Class<? extends Annotation> annotationClass) {
        boolean hasParams = false;

        if (method.getParameters().size() > 0) {
            hasParams = true;
            error(method, "Method annotated with @%s can not have parameters.",annotationClass.getSimpleName());
        }

        return hasParams;
    }

    static boolean hasMoreThanOneMethodsAnnotatedWith(
            Element parent,
            Class<? extends Annotation> annotationClass) {
        boolean hasMoreThanOneElement = false;
        List<Element> elements = new ArrayList<>();

        for(Element e : parent.getEnclosedElements()) {
            if(e.getAnnotation(annotationClass) != null) {
                elements.add(e);
            }
        }

        if (elements.size() > 1) {
            hasMoreThanOneElement = true;
            error(
                    parent,
                    "The class %s must have only one element annotated with @%s.",
                    parent.getSimpleName(),
                    annotationClass.getSimpleName()
            );
        }

        return hasMoreThanOneElement;
    }

    static boolean hasNoMethodAnnotatedWith(
            Element parent,
            Class<? extends Annotation> annotationClass) {
        boolean hasNoElements = false;
        List<Element> elements = new ArrayList<>();

        for(Element e : parent.getEnclosedElements()) {
            if(e.getAnnotation(annotationClass) != null) {
                elements.add(e);
            }
        }

        if (elements.size() == 0) {
            hasNoElements = true;
            error(
                    parent,
                    "The class %s must have one method annotated with @%s.",
                    parent.getSimpleName(),
                    annotationClass.getSimpleName()
            );
        }

        return hasNoElements;
    }

    static boolean confirmValidationElementsHasError(
            Class<? extends Annotation> primaryAnnotation,
            Class<? extends Annotation> confirmAnnotation,
            Element element) {
        boolean hasError = false;

        String primaryAnnotationClassName = primaryAnnotation.getSimpleName();
        String confirmAnnotationClassName = confirmAnnotation.getSimpleName();

        int elementsAnnotatedWithPrimaryValidation = 0;
        int elementsAnnotatedWithConfirmValidation = 0;

        List<? extends Element> elementsOfParent = element.getEnclosingElement().getEnclosedElements();

        for(int i = 0; i < elementsOfParent.size(); i++) {
            if(elementsOfParent.get(i).getAnnotation(primaryAnnotation) != null) {
                elementsAnnotatedWithPrimaryValidation ++;
            }

            if(elementsOfParent.get(i).getAnnotation(confirmAnnotation) != null) {
                elementsAnnotatedWithConfirmValidation ++;
            }
        }

        if (elementsAnnotatedWithPrimaryValidation == 0 && elementsAnnotatedWithConfirmValidation > 0) {
            hasError = true;
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            error(
                    element.getEnclosingElement(),
                    "%s must have at least one element annotated with @%s.",
                    enclosingElement.getSimpleName(),
                    primaryAnnotationClassName
            );
        }

        if (elementsAnnotatedWithConfirmValidation > 1) {
            hasError = true;
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            error(
                    element.getEnclosingElement(),
                    "%s must have only one element annotated with @%s.",
                    enclosingElement.getSimpleName(),
                    confirmAnnotationClassName
            );
        }

        return hasError;
    }

    static boolean isInvalid(Class<? extends Annotation> annotationClass, Element element) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        String elementType = element.asType().toString();
        boolean hasError = false;

        // Verify element kind
        if (!element.getKind().equals(FIELD)) {
            error(
                    element,
                    "@%s must only be applied in fields. (%s.%s)",
                    annotationClass.getSimpleName(),
                    enclosingElement.getQualifiedName(),
                    element.getSimpleName()
            );

            hasError = true;
        }

        // Verify element type
        if (!EDIT_TEXT_TYPE.equals(elementType)) { // improve this check
            error(
                    element,
                    "@%s must only be applied in fields of the type TextInputLaytout or EditText. (%s.%s)",
                    annotationClass.getSimpleName(),
                    enclosingElement.getQualifiedName(),
                    element.getSimpleName()
            );

            hasError = true;
        }

        return hasError;
    }

    static boolean isInaccessible(Class<? extends Annotation> annotationClass, Element element) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        boolean hasError = false;

        // Verify element modifiers
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(PRIVATE) || modifiers.contains(STATIC)) {
            error(
                    element,
                    "@%s must not be applied in private or static fields. (%s.%s)",
                    annotationClass.getSimpleName(),
                    enclosingElement.getQualifiedName(),
                    element.getSimpleName()
            );

            hasError = true;
        }

        // Verify containing type
        if (enclosingElement.getKind() != CLASS) {
            error(
                    enclosingElement,
                    "@%s fields may only be contained in classes. (%s.%s)",
                    annotationClass.getSimpleName(),
                    enclosingElement.getQualifiedName(),
                    element.getSimpleName()
            );

            hasError = true;
        }

        // Verify containing class visibility is not private
        if (enclosingElement.getModifiers().contains(PRIVATE)) {
            error(
                    enclosingElement,
                    "@%s fields may not be contained in private classes. (%s.%s)",
                    annotationClass.getSimpleName(),
                    enclosingElement.getQualifiedName(),
                    element.getSimpleName()
            );

            hasError = true;
        }

        return hasError;
    }

}
