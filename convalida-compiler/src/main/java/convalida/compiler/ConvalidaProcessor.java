package convalida.compiler;

import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree;

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
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

import convalida.annotations.ConfirmPasswordValidation;
import convalida.annotations.EmailValidation;
import convalida.annotations.LengthValidation;
import convalida.annotations.NotEmptyValidation;
import convalida.annotations.OnlyNumberValidation;
import convalida.annotations.PasswordValidation;
import convalida.annotations.PatternValidation;
import convalida.compiler.internal.FieldValidationInfo;
import convalida.compiler.internal.Id;
import convalida.compiler.internal.QualifiedId;
import convalida.compiler.internal.TargetClassInfo;
import convalida.compiler.internal.scanners.IdScanner;
import convalida.compiler.internal.scanners.RClassScanner;

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

    private Elements elementUtils;
    private Types typeUtils;
    private Messager messager;
    private Filer filer;
    private Trees trees;

    private final Map<QualifiedId, Id> symbols = new LinkedHashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        this.elementUtils = processingEnvironment.getElementUtils();
        this.typeUtils = processingEnvironment.getTypeUtils();
        this.messager = processingEnvironment.getMessager();
        this.filer = processingEnvironment.getFiler();

        try {
            trees = Trees.instance(processingEnv);
        } catch (IllegalArgumentException ignored) { }
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

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();

        annotations.add(NotEmptyValidation.class);
        annotations.add(EmailValidation.class);
        annotations.add(PatternValidation.class);
        annotations.add(LengthValidation.class);
        annotations.add(OnlyNumberValidation.class);
        annotations.add(PasswordValidation.class);
        annotations.add(ConfirmPasswordValidation.class);

        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment env) {
        Map<TargetClassInfo, Set<FieldValidationInfo>> validationsMap = findAndParseValidations(env);

        for (Map.Entry<TargetClassInfo, Set<FieldValidationInfo>> entry : validationsMap.entrySet()) {
            TargetClassInfo targetClassInfo = entry.getKey();
            TypeElement typeElement = targetClassInfo.getTypeElement();
            Set<FieldValidationInfo> fields = entry.getValue();

            try {
                JavaFile javaFile = JavaFiler.cookJava(targetClassInfo, fields);
                javaFile.writeTo(this.filer);
            } catch (IOException e) {
                error(typeElement, "Unable to write validation for type %s: %s", typeElement, e.getMessage());
            }
        }

        return false;
    }

    private Map<TargetClassInfo, Set<FieldValidationInfo>> findAndParseValidations(RoundEnvironment env) {
        Map<TargetClassInfo, Set<FieldValidationInfo>> map = new LinkedHashMap<>();
        Set<TargetClassInfo> targetClassInfos = new LinkedHashSet<>();
        Set<FieldValidationInfo> fieldValidationInfos = new LinkedHashSet<>();

        scanForRClasses(env);

        // Process each @NotEmptyValidation element
        for (Element element : env.getElementsAnnotatedWith(NotEmptyValidation.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseNotEmptyValidation(element, targetClassInfos, fieldValidationInfos);
            } catch (Exception e) {
                logParsingError(element, NotEmptyValidation.class, e);
            }
        }


        // Process each @EmailValidation element.
        for (Element element : env.getElementsAnnotatedWith(EmailValidation.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseEmailValidation(element, targetClassInfos, fieldValidationInfos);
            } catch (Exception e) {
                logParsingError(element, EmailValidation.class, e);
            }
        }

        // Process each @PatternValidation element.
        for (Element element : env.getElementsAnnotatedWith(PatternValidation.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parsePatternValidation(element, targetClassInfos, fieldValidationInfos);
            } catch (Exception e) {
                logParsingError(element, PatternValidation.class, e);
            }
        }

        // Process each @LengthValidation element.
        for (Element element : env.getElementsAnnotatedWith(LengthValidation.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseLengthValidation(element, targetClassInfos, fieldValidationInfos);
            } catch (Exception e) {
                logParsingError(element, LengthValidation.class, e);
            }
        }

        // Process each @OnlyNumberValidation element.
        for (Element element : env.getElementsAnnotatedWith(OnlyNumberValidation.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseOnlyNumberValidation(element, targetClassInfos, fieldValidationInfos);
            } catch (Exception e) {
                logParsingError(element, OnlyNumberValidation.class, e);
            }
        }

        // Process each @PasswordValidation element.
        processPasswordValidations(env, targetClassInfos, fieldValidationInfos);

        // Process each @ConfirmPasswordValidation element.
        processConfirmPasswordValidations(env, targetClassInfos, fieldValidationInfos);

        TargetClassInfo targetClassInfo = findTargetInfoElement(targetClassInfos);

        if (targetClassInfo != null && fieldValidationInfos.size() > 0) {
            map.put(targetClassInfo, fieldValidationInfos);
        }

        return map;
    }

    private void parseNotEmptyValidation(
            Element element,
            Set<TargetClassInfo> targetClassInfos,
            Set<FieldValidationInfo> fieldValidationInfos) {

        boolean hasError = isInvalid(NotEmptyValidation.class, element) || isInaccessible(NotEmptyValidation.class, element);

        if (hasError) {
            return;
        }

        int id = element.getAnnotation(NotEmptyValidation.class).value();
        QualifiedId qualifiedId = elementToQualifiedId(element, id);

        targetClassInfos.add(new TargetClassInfo(element.getEnclosingElement(), this.elementUtils));
        fieldValidationInfos.add(new FieldValidationInfo(element, NotEmptyValidation.class.getCanonicalName(), getId(qualifiedId)));
    }

    private void parseEmailValidation(
            Element element,
            Set<TargetClassInfo> targetClassInfos,
            Set<FieldValidationInfo> fieldValidationInfos) {

        boolean hasError = isInvalid(EmailValidation.class, element) || isInaccessible(EmailValidation.class, element);

        if (hasError) {
            return;
        }

        int id = element.getAnnotation(EmailValidation.class).value();
        QualifiedId qualifiedId = elementToQualifiedId(element, id);

        targetClassInfos.add(new TargetClassInfo(element.getEnclosingElement(), this.elementUtils));
        fieldValidationInfos.add(new FieldValidationInfo(element, EmailValidation.class.getCanonicalName(), getId(qualifiedId)));
    }

    private void parsePatternValidation(
            Element element,
            Set<TargetClassInfo> targetClassInfos,
            Set<FieldValidationInfo> fieldValidationInfos) {

        boolean hasError = isInvalid(PatternValidation.class, element) || isInaccessible(PatternValidation.class, element);

        if (hasError) {
            return;
        }

        int errorMessage = element.getAnnotation(PatternValidation.class).errorMessage();
        QualifiedId qualifiedId = elementToQualifiedId(element, errorMessage);

        targetClassInfos.add(new TargetClassInfo(element.getEnclosingElement(), this.elementUtils));
        fieldValidationInfos.add(new FieldValidationInfo(element, PatternValidation.class.getCanonicalName(), getId(qualifiedId)));
    }

    private void parseLengthValidation(
            Element element,
            Set<TargetClassInfo> targetClassInfos,
            Set<FieldValidationInfo> fieldValidationInfos) {

        int minLength = element.getAnnotation(LengthValidation.class).min();
        int maxLength = element.getAnnotation(LengthValidation.class).max();

        if (minLength == 0 && maxLength == 0) {
            error(element, "The min length and max length must be greater than zero.");
        }

        if (maxLength > 0 && maxLength < minLength) {
            error(element, "The max lentgh must be greater than min lentgh.");
        }

        try {
            boolean hasError = isInvalid(LengthValidation.class, element) || isInaccessible(LengthValidation.class, element);

            if (hasError) {
                return;
            }

            int errorMessage = element.getAnnotation(LengthValidation.class).errorMessage();
            QualifiedId qualifiedId = elementToQualifiedId(element, errorMessage);

            targetClassInfos.add(new TargetClassInfo(element.getEnclosingElement(), this.elementUtils));
            fieldValidationInfos.add(new FieldValidationInfo(element, LengthValidation.class.getCanonicalName(), getId(qualifiedId)));
        } catch (Exception e) {
            logParsingError(element, LengthValidation.class, e);
        }
    }

    private void parseOnlyNumberValidation(
            Element element,
            Set<TargetClassInfo> targetClassInfos,
            Set<FieldValidationInfo> fieldValidationInfos) {

        boolean hasError = isInvalid(OnlyNumberValidation.class, element) || isInaccessible(OnlyNumberValidation.class, element);

        if (hasError) {
            return;
        }

        int id = element.getAnnotation(OnlyNumberValidation.class).value();
        QualifiedId qualifiedId = elementToQualifiedId(element, id);

        targetClassInfos.add(new TargetClassInfo(element.getEnclosingElement(), this.elementUtils));
        fieldValidationInfos.add(new FieldValidationInfo(element, OnlyNumberValidation.class.getCanonicalName(), getId(qualifiedId)));
    }

    private void processPasswordValidations(
            RoundEnvironment env,
            Set<TargetClassInfo> targetClassInfos,
            Set<FieldValidationInfo> fieldValidationInfos) {

        Set<? extends Element> passwordElements = env.getElementsAnnotatedWith(PasswordValidation.class);

        if (passwordElements.size() > 1) {
            TypeElement enclosingElement = (TypeElement) passwordElements.iterator().next().getEnclosingElement();
            error(
                    passwordElements.iterator().next(),
                    "%s must have only one element annotated with @PasswordValidation.",
                    enclosingElement.getQualifiedName()
            );
        }

        // Has no errors
        for (Element element : env.getElementsAnnotatedWith(PasswordValidation.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parsePasswordValidation(element, targetClassInfos, fieldValidationInfos);
            } catch (Exception e) {
                logParsingError(element, PasswordValidation.class, e);
            }
        }
    }

    private void parsePasswordValidation(
            Element element,
            Set<TargetClassInfo> targetClassInfos,
            Set<FieldValidationInfo> fieldValidationInfos) {

        boolean hasError = isInvalid(PasswordValidation.class, element) || isInaccessible(PasswordValidation.class, element);

        if (hasError) {
            return;
        }

        int errorMessage = element.getAnnotation(PasswordValidation.class).errorMessage();
        QualifiedId qualifiedId = elementToQualifiedId(element, errorMessage);

        targetClassInfos.add(new TargetClassInfo(element.getEnclosingElement(), this.elementUtils));
        fieldValidationInfos.add(new FieldValidationInfo(element, PasswordValidation.class.getCanonicalName(), getId(qualifiedId)));
    }

    private void processConfirmPasswordValidations(
            RoundEnvironment env,
            Set<TargetClassInfo> targetClassInfos,
            Set<FieldValidationInfo> fieldValidationInfos) {

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

        // Has no errors
        for (Element element : env.getElementsAnnotatedWith(ConfirmPasswordValidation.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseConfirmPasswordValidation(element, targetClassInfos, fieldValidationInfos);
            } catch (Exception e) {
                logParsingError(element, ConfirmPasswordValidation.class, e);
            }
        }
    }

    private void parseConfirmPasswordValidation(
            Element element,
            Set<TargetClassInfo> targetClassInfos,
            Set<FieldValidationInfo> fieldValidationInfos) {

        boolean hasError = isInvalid(ConfirmPasswordValidation.class, element) || isInaccessible(ConfirmPasswordValidation.class, element);

        if (hasError) {
            return;
        }

        int id = element.getAnnotation(ConfirmPasswordValidation.class).value();
        QualifiedId qualifiedId = elementToQualifiedId(element, id);

        targetClassInfos.add(new TargetClassInfo(element.getEnclosingElement(), this.elementUtils));
        fieldValidationInfos.add(new FieldValidationInfo(element, ConfirmPasswordValidation.class.getCanonicalName(), getId(qualifiedId)));
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

    private TargetClassInfo findTargetInfoElement(Set<TargetClassInfo> targetClassInfos) {
        return targetClassInfos.iterator().hasNext() ? targetClassInfos.iterator().next() : null;
    }

    private static AnnotationMirror getMirror(Element element, Class<? extends Annotation> annotation) {
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            if (annotationMirror.getAnnotationType().toString().equals(annotation.getCanonicalName())) {
                return annotationMirror;
            }
        }
        return null;
    }

    private QualifiedId elementToQualifiedId(Element element, int id) {
        return new QualifiedId(elementUtils.getPackageOf(element).getQualifiedName().toString(), id);
    }

    private Id getId(QualifiedId qualifiedId) {
        if (symbols.get(qualifiedId) == null) {
            symbols.put(qualifiedId, new Id(qualifiedId.id));
        }
        return symbols.get(qualifiedId);
    }

    private void scanForRClasses(RoundEnvironment env) {
        if (trees == null) return;

        RClassScanner scanner = new RClassScanner();

        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            for (Element element : env.getElementsAnnotatedWith(annotation)) {
                JCTree tree = (JCTree) trees.getTree(element, getMirror(element, annotation));
                if (tree != null) { // tree can be null if the references are compiled types and not source
                    String respectivePackageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
                    scanner.setCurrentPackageName(respectivePackageName);
                    tree.accept(scanner);
                }
            }
        }

        for (Map.Entry<String, Set<String>> packageNameToRClassSet : scanner.getRClasses().entrySet()) {
            String respectivePackageName = packageNameToRClassSet.getKey();
            for (String rClass : packageNameToRClassSet.getValue()) {
                parseRClass(respectivePackageName, rClass);
            }
        }
    }

    private void parseRClass(String respectivePackageName, String rClass) {
        Element element;

        try {
            element = elementUtils.getTypeElement(rClass);
        } catch (MirroredTypeException mte) {
            element = typeUtils.asElement(mte.getTypeMirror());
        }

        JCTree tree = (JCTree) trees.getTree(element);
        if (tree != null) {
            IdScanner idScanner = new IdScanner(symbols, elementUtils.getPackageOf(element)
                    .getQualifiedName().toString(), respectivePackageName);
            tree.accept(idScanner);
        } else {
            parseCompiledR(respectivePackageName, (TypeElement) element);
        }
    }

    private void parseCompiledR(String respectivePackageName, TypeElement rClass) {
        for (Element element : rClass.getEnclosedElements()) {
            String innerClassName = element.getSimpleName().toString();
            if (innerClassName.equals("string")) {
                for (Element enclosedElement : element.getEnclosedElements()) {
                    if (enclosedElement instanceof VariableElement) {
                        VariableElement variableElement = (VariableElement) enclosedElement;
                        Object value = variableElement.getConstantValue();

                        if (value instanceof Integer) {
                            int id = (Integer) value;
                            ClassName rClassName = ClassName.get(elementUtils.getPackageOf(variableElement).toString(), "R", innerClassName);
                            String resourceName = variableElement.getSimpleName().toString();
                            QualifiedId qualifiedId = new QualifiedId(respectivePackageName, id);
                            symbols.put(qualifiedId, new Id(id, rClassName, resourceName));
                        }
                    }
                }
            }
        }
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