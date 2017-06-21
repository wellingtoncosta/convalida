package convalida.compiler;

import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;

import convalida.annotations.EmailValidation;
import convalida.annotations.NotEmptyValidation;
import convalida.annotations.PasswordValidation;
import convalida.annotations.PatternValidation;

import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.FIELD;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * @author Wellington Costa on 13/06/2017.
 */
@AutoService(Processor.class)
public class ConvalidaProcessor extends AbstractProcessor {

    private static final String TEXT_INPUT_LAYOUT_TYPE = "android.support.design.widget.TextInputLayout";
    private static final String EDIT_TEXT_TYPE = "android.widget.EditText";

    private Elements elements;
    private Messager messager;
    private Filer filer;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        this.elements = processingEnvironment.getElementUtils();
        this.messager = processingEnvironment.getMessager();
        this.filer = processingEnvironment.getFiler();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supportedAnnotations = new HashSet<>();

        supportedAnnotations.add(NotEmptyValidation.class.getCanonicalName());
        supportedAnnotations.add(EmailValidation.class.getCanonicalName());
        supportedAnnotations.add(PasswordValidation.class.getCanonicalName());
        supportedAnnotations.add(PatternValidation.class.getCanonicalName());

        return supportedAnnotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment env) {
        Map<TargetInfo, Set<FieldValidation>> map = findAndParseTargets(env);

        for (Map.Entry<TargetInfo, Set<FieldValidation>> entry : map.entrySet()) {
            TargetInfo targetInfo = entry.getKey();
            TypeElement typeElement = targetInfo.getTypeElement();
            Set<FieldValidation> fields = entry.getValue();

            try {
                JavaFile javaFile = JavaFiler.cookJava(targetInfo, fields);
                javaFile.writeTo(this.filer);
            } catch (IOException e) {
                error(typeElement, "Unable to write validation for type %s: %s", typeElement, e.getMessage());
            }
        }

        return false;
    }

    private Map<TargetInfo, Set<FieldValidation>> findAndParseTargets(RoundEnvironment env) {
        Map<TargetInfo, Set<FieldValidation>> map = new LinkedHashMap<>();
        Set<TargetInfo> targetElements = new LinkedHashSet<>();
        Set<FieldValidation> elements = new LinkedHashSet<>();

        // Process each @NotEmptyValidation element.
        for (Element element : env.getElementsAnnotatedWith(NotEmptyValidation.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseNotEmptyValidation(element, targetElements, elements);
            } catch (Exception e) {
                logParsingError(element, NotEmptyValidation.class, e);
            }
        }

        // Process each @EmailValidation element.
        for (Element element : env.getElementsAnnotatedWith(EmailValidation.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseEmailValidation(element, targetElements, elements);
            } catch (Exception e) {
                logParsingError(element, EmailValidation.class, e);
            }
        }

        // Process each @PasswordValidation element.
        for (Element element : env.getElementsAnnotatedWith(PasswordValidation.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parsePasswordValidation(element, targetElements, elements);
            } catch (Exception e) {
                logParsingError(element, PasswordValidation.class, e);
            }
        }

        // Process each @PatternValidation element.
        for (Element element : env.getElementsAnnotatedWith(PatternValidation.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parsePatternValidation(element, targetElements, elements);
            } catch (Exception e) {
                logParsingError(element, PatternValidation.class, e);
            }
        }

        TargetInfo targetElement = findTargetElement(targetElements);

        if (targetElement != null && elements.size() > 0) {
            map.put(targetElement, elements);
        }

        return map;
    }

    private void parseNotEmptyValidation(Element element, Set<TargetInfo> targetElements, Set<FieldValidation> fields) {
        boolean hasError = isInvalid(NotEmptyValidation.class, element) || isInaccessible(NotEmptyValidation.class, element);

        if (hasError) {
            return;
        }

        targetElements.add(new TargetInfo(element.getEnclosingElement(), this.elements));
        fields.add(new FieldValidation(element));

        note(element, "Processing @NotEmptyValidation element");
    }

    private void parseEmailValidation(Element element, Set<TargetInfo> targetElements, Set<FieldValidation> fields) {
        boolean hasError = isInvalid(EmailValidation.class, element) || isInaccessible(EmailValidation.class, element);

        if (hasError) {
            return;
        }

        targetElements.add(new TargetInfo(element.getEnclosingElement(), this.elements));
        fields.add(new FieldValidation(element));

        note(element, "Processing @EmailValidation element");
    }

    private void parsePasswordValidation(Element element, Set<TargetInfo> targetElements, Set<FieldValidation> fields) {
        boolean hasError = isInvalid(PasswordValidation.class, element) || isInaccessible(PasswordValidation.class, element);

        if (hasError) {
            return;
        }

        targetElements.add(new TargetInfo(element.getEnclosingElement(), this.elements));
        fields.add(new FieldValidation(element));

        note(element, "Processing @PasswordValidation element");
    }

    private void parsePatternValidation(Element element, Set<TargetInfo> targetElements, Set<FieldValidation> fields) {
        boolean hasError = isInvalid(PatternValidation.class, element) || isInaccessible(PatternValidation.class, element);

        if (hasError) {
            return;
        }

        targetElements.add(new TargetInfo(element.getEnclosingElement(), this.elements));
        fields.add(new FieldValidation(element));

        note(element, "Processing @PatternValidation element");
    }

    private boolean isInvalid(Class<? extends Annotation> annotationClass, Element element) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        String elementType = element.asType().toString();
        boolean hasError = false;

        // Verify element kind
        if (!element.getKind().equals(FIELD)) {
            error(
                    element,
                    "@%s must only be aplied in fields. (%s.%s)",
                    annotationClass.getSimpleName(),
                    enclosingElement.getQualifiedName(),
                    element.getSimpleName()
            );

            hasError = true;
        }

        // Verify element type
        if (!TEXT_INPUT_LAYOUT_TYPE.equals(elementType) && !EDIT_TEXT_TYPE.equals(elementType)) {
            error(
                    element,
                    "@%s must only be aplied in fields of the type TextInputLaytout or EditText. (%s.%s)",
                    annotationClass.getSimpleName(),
                    enclosingElement.getQualifiedName(),
                    element.getSimpleName()
            );

            hasError = true;
        }

        return hasError;
    }

    private boolean isInaccessible(Class<? extends Annotation> annotationClass, Element element) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        boolean hasError = false;

        // Verify element kind
        if (!element.getKind().equals(FIELD)) {
            error(
                    element,
                    "@%s must only be aplied in fields. (%s.%s)",
                    annotationClass.getSimpleName(),
                    enclosingElement.getQualifiedName(),
                    element.getSimpleName()
            );

            hasError = true;
        }

        // Verify element modifiers
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(PRIVATE) || modifiers.contains(STATIC)) {
            error(
                    element,
                    "@%s must not be aplied in private or static fields. (%s.%s)",
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

    private TargetInfo findTargetElement(Set<TargetInfo> targetElements) {
        return targetElements.iterator().hasNext() ? targetElements.iterator().next() : null;
    }

    private void logParsingError(Element element, Class<? extends Annotation> annotation, Exception e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        error(element, "Unable to parse @%s validation.\n\n%s", annotation.getSimpleName(), stackTrace);
    }

    private void error(Element element, String message, Object... args) {
        printMessage(Kind.ERROR, element, message, args);
    }

    private void note(Element element, String message, Object... args) {
        printMessage(Kind.NOTE, element, message, args);
    }

    private void printMessage(Kind kind, Element element, String message, Object[] args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }

        this.messager.printMessage(kind, message, element);
    }

}
