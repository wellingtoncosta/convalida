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

import convalida.annotations.ConfirmPasswordValidation;
import convalida.annotations.EmailValidation;
import convalida.annotations.LengthValidation;
import convalida.annotations.NotEmptyValidation;
import convalida.annotations.OnlyNumberValidation;
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
        supportedAnnotations.add(PatternValidation.class.getCanonicalName());
        supportedAnnotations.add(LengthValidation.class.getCanonicalName());
        supportedAnnotations.add(OnlyNumberValidation.class.getCanonicalName());
        supportedAnnotations.add(PasswordValidation.class.getCanonicalName());
        supportedAnnotations.add(ConfirmPasswordValidation.class.getCanonicalName());

        return supportedAnnotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment env) {
        Map<TargetInfo, Set<FieldInfo>> validationsMap = findAndParseValidations(env);

        for (Map.Entry<TargetInfo, Set<FieldInfo>> entry : validationsMap.entrySet()) {
            TargetInfo targetInfo = entry.getKey();
            TypeElement typeElement = targetInfo.getTypeElement();
            Set<FieldInfo> fields = entry.getValue();

            try {
                JavaFile javaFile = JavaFiler.cookJava(targetInfo, fields);
                javaFile.writeTo(this.filer);
            } catch (IOException e) {
                error(typeElement, "Unable to write validation for type %s: %s", typeElement, e.getMessage());
            }
        }

        return false;
    }

    private Map<TargetInfo, Set<FieldInfo>> findAndParseValidations(RoundEnvironment env) {
        Map<TargetInfo, Set<FieldInfo>> map = new LinkedHashMap<>();
        Set<TargetInfo> targetInfos = new LinkedHashSet<>();
        Set<FieldInfo> fieldInfos = new LinkedHashSet<>();

        // Process each @NotEmptyValidation element.
        processValidations(env, NotEmptyValidation.class, targetInfos, fieldInfos);

        // Process each @EmailValidation element.
        processValidations(env, EmailValidation.class, targetInfos, fieldInfos);

        // Process each @PatternValidation element.
        processValidations(env, PatternValidation.class, targetInfos, fieldInfos);

        // Process each @LengthValidation element.
        processLengthValidations(env, targetInfos, fieldInfos);

        // Process each @OnlyNumberValidation element.
        processValidations(env, OnlyNumberValidation.class, targetInfos, fieldInfos);

        // Process each @PasswordValidation element.
        processPasswordValidations(env, targetInfos, fieldInfos);

        // Process each @ConfirmPasswordValidation element.
        processConfirmPasswordValidations(env, targetInfos, fieldInfos);


        TargetInfo targetInfo = findTargetInfoElement(targetInfos);

        if (targetInfo != null && fieldInfos.size() > 0) {
            map.put(targetInfo, fieldInfos);
        }

        return map;
    }

    private void processValidations(
            RoundEnvironment env,
            Class<? extends Annotation> annotation,
            Set<TargetInfo> targetInfos,
            Set<FieldInfo> fieldInfos) {

        for (Element element : env.getElementsAnnotatedWith(annotation)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseValidation(element, annotation, targetInfos, fieldInfos);
            } catch (Exception e) {
                logParsingError(element, annotation, e);
            }
        }
    }

    private void processLengthValidations(
            RoundEnvironment env,
            Set<TargetInfo> targetInfos,
            Set<FieldInfo> fieldInfos) {

        Set<? extends Element> lengthElements = env.getElementsAnnotatedWith(LengthValidation.class);

        for (Element element : lengthElements) {
            if (!SuperficialValidation.validateElement(element)) continue;

            int minLength = element.getAnnotation(LengthValidation.class).min();
            int maxLength = element.getAnnotation(LengthValidation.class).max();

            if (minLength == 0 && maxLength == 0) {
                error(element, "The min length and max length must be greater than zero.");
            }

            if (maxLength > 0 && maxLength < minLength) {
                error(element, "The max lentgh must be greater than min lentgh.");
            }

            try {
                parseValidation(element, LengthValidation.class, targetInfos, fieldInfos);
            } catch (Exception e) {
                logParsingError(element, LengthValidation.class, e);
            }
        }
    }

    private void processPasswordValidations(
            RoundEnvironment env,
            Set<TargetInfo> targetInfos,
            Set<FieldInfo> fieldInfos) {

        Set<? extends Element> passwordElements = env.getElementsAnnotatedWith(PasswordValidation.class);

        if (passwordElements.size() > 1) {
            TypeElement enclosingElement = (TypeElement) passwordElements.iterator().next().getEnclosingElement();
            error(
                    passwordElements.iterator().next(),
                    "%s must have only one element annotated with @PasswordValidation.",
                    enclosingElement.getQualifiedName()
            );
        }

        processValidations(env, PasswordValidation.class, targetInfos, fieldInfos);
    }

    private void processConfirmPasswordValidations(
            RoundEnvironment env,
            Set<TargetInfo> targetInfos,
            Set<FieldInfo> fieldInfos) {

        Set<? extends Element> passwordElements = env.getElementsAnnotatedWith(PasswordValidation.class);
        Set<? extends Element> confirmPasswordElements = env.getElementsAnnotatedWith(ConfirmPasswordValidation.class);

        if (confirmPasswordElements.size() > 0 && passwordElements.isEmpty()) {
            TypeElement enclosingElement = (TypeElement) confirmPasswordElements.iterator().next().getEnclosingElement();
            error(
                    confirmPasswordElements.iterator().next(),
                    "%s must have at least one element annotated with @PasswordValidation.",
                    enclosingElement.getSimpleName()
            );
        }

        if (confirmPasswordElements.size() > 1) {
            TypeElement enclosingElement = (TypeElement) confirmPasswordElements.iterator().next().getEnclosingElement();
            error(
                    confirmPasswordElements.iterator().next(),
                    "%s must have only one element annotated with @ConfirmPasswordValidation.",
                    enclosingElement.getSimpleName()
            );
        }

        processValidations(env, ConfirmPasswordValidation.class, targetInfos, fieldInfos);
    }

    private void parseValidation(
            Element element,
            Class<? extends  Annotation> annotationClass,
            Set<TargetInfo> targetInfos,
            Set<FieldInfo> fieldInfos) {

        boolean hasError = isInvalid(annotationClass, element) || isInaccessible(annotationClass, element);

        if (hasError) {
            return;
        }

        targetInfos.add(new TargetInfo(element.getEnclosingElement(), this.elements));
        fieldInfos.add(new FieldInfo(element, annotationClass.getCanonicalName()));
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

    private TargetInfo findTargetInfoElement(Set<TargetInfo> targetInfos) {
        return targetInfos.iterator().hasNext() ? targetInfos.iterator().next() : null;
    }

    private void logParsingError(Element element, Class<? extends Annotation> annotation, Exception e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        error(element, "Unable to parse @%s validation.\n\n%s", annotation.getSimpleName(), stackTrace);
    }

    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }

        this.messager.printMessage(Kind.ERROR, message, element);
    }

}