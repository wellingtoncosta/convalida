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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

import convalida.annotations.ClearValidationsOnClick;
import convalida.annotations.ConfirmEmailValidation;
import convalida.annotations.ConfirmPasswordValidation;
import convalida.annotations.EmailValidation;
import convalida.annotations.LengthValidation;
import convalida.annotations.NotEmptyValidation;
import convalida.annotations.OnValidationError;
import convalida.annotations.OnValidationSuccess;
import convalida.annotations.OnlyNumberValidation;
import convalida.annotations.PasswordValidation;
import convalida.annotations.PatternValidation;
import convalida.annotations.ValidateOnClick;
import convalida.compiler.internal.Id;
import convalida.compiler.internal.QualifiedId;
import convalida.compiler.internal.ValidationClass;
import convalida.compiler.internal.ValidationField;
import convalida.compiler.internal.scanners.IdScanner;
import convalida.compiler.internal.scanners.RClassScanner;

import static convalida.compiler.Constants.CONFIRM_EMAIL_VALIDATION;
import static convalida.compiler.Constants.CONFIRM_PASSWORD_ANNOTATION;
import static convalida.compiler.Constants.EMAIL_ANNOTATION;
import static convalida.compiler.Constants.LENGTH_ANNOTATION;
import static convalida.compiler.Constants.NOT_EMPTY_ANNOTATION;
import static convalida.compiler.Constants.ONLY_NUMBER_ANNOTATION;
import static convalida.compiler.Constants.PASSWORD_ANNOTATION;
import static convalida.compiler.Constants.PATTERN_ANNOTATION;
import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.FIELD;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * @author Wellington Costa on 13/06/2017.
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
        NOT_EMPTY_ANNOTATION,
        EMAIL_ANNOTATION,
        CONFIRM_EMAIL_VALIDATION,
        PATTERN_ANNOTATION,
        LENGTH_ANNOTATION,
        ONLY_NUMBER_ANNOTATION,
        PASSWORD_ANNOTATION,
        CONFIRM_PASSWORD_ANNOTATION
})
public class ConvalidaProcessor extends AbstractProcessor {

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

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();

        annotations.add(NotEmptyValidation.class);
        annotations.add(EmailValidation.class);
        annotations.add(ConfirmEmailValidation.class);
        annotations.add(PatternValidation.class);
        annotations.add(LengthValidation.class);
        annotations.add(OnlyNumberValidation.class);
        annotations.add(PasswordValidation.class);
        annotations.add(ConfirmPasswordValidation.class);

        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment env) {
        List<ValidationClass> validationClasses = findAndParseValidations(env);

        // Generate validation classes source code
        for (ValidationClass validationClass : validationClasses) {
            try {
                JavaFile javaFile = JavaFiler.cookJava(validationClass);
                javaFile.writeTo(this.filer);
            } catch (IOException e) {
                error(validationClass.typeElement, "Unable to write validation for type %s: %s", validationClass.typeElement, e.getMessage());
            }
        }

        return false;
    }

    private List<ValidationClass> findAndParseValidations(RoundEnvironment env) {
        Set<Element> parents = new HashSet<>();
        List<ValidationField> validationFields = new ArrayList<>();
        List<ValidationClass> validationClasses = new ArrayList<>();

        scanForRClasses(env);

        // Process each @NotEmptyValidation element
        for (Element element : env.getElementsAnnotatedWith(NotEmptyValidation.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseNotEmptyValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, NotEmptyValidation.class, e);
            }
        }

        // Process each @EmailValidation element
        for (Element element : env.getElementsAnnotatedWith(EmailValidation.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseEmailValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, EmailValidation.class, e);
            }
        }

        // Process each @ConfirmEmailValidation element
        for (Element element : env.getElementsAnnotatedWith(ConfirmEmailValidation.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseConfirmEmailValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, ConfirmEmailValidation.class, e);
            }
        }

        // Process each @PatternValidation element.
        for (Element element : env.getElementsAnnotatedWith(PatternValidation.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parsePatternValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, PatternValidation.class, e);
            }
        }

        // Process each @LengthValidation element.
        for (Element element : env.getElementsAnnotatedWith(LengthValidation.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseLengthValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, LengthValidation.class, e);
            }
        }

        // Process each @OnlyNumberValidation element.
        for (Element element : env.getElementsAnnotatedWith(OnlyNumberValidation.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseOnlyNumberValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, OnlyNumberValidation.class, e);
            }
        }

        // Process each @PasswordValidation element.
        for (Element element : env.getElementsAnnotatedWith(PasswordValidation.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parsePasswordValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, PasswordValidation.class, e);
            }
        }

        // Process each @ConfirmPasswordValidation element.
        for (Element element : env.getElementsAnnotatedWith(ConfirmPasswordValidation.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseConfirmPasswordValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, ConfirmPasswordValidation.class, e);
            }
        }


        // Assemble the validation classes and fields
        for (Element parent : parents) {
            ValidationClass validationClass = new ValidationClass(parent, this.elementUtils);

            for (ValidationField validationField : validationFields) {
                Element element = validationField.element;
                if (element.getEnclosingElement().equals(parent)) {
                    validationClass.addField(validationField);
                }
            }

            parseValidateOnClick(parent, validationClass);
            parseClearValidationsOnClick(parent, validationClass);

            validationClasses.add(validationClass);
        }

        return validationClasses;
    }

    private void parseValidateOnClick(Element parent, ValidationClass validationClass) {
        List<Element> elements = parent.getEnclosedElements()
                .stream()
                .filter(element -> element.getAnnotation(ValidateOnClick.class) != null)
                .collect(Collectors.toList());

        if (elements.size() > 1) {
            error(
                    parent,
                    "The class %s must have only one element annotated with @ValidateOnClick annotation.",
                    parent.getSimpleName()
            );
            return;
        }

        if (elements.size() == 1) {
            boolean isInaccessible = isInaccessible(ValidateOnClick.class, elements.get(0));
            if (isInaccessible) return;

            validationClass.setValidateButton(elements.get(0));
            parseOnValidationSuccess(parent, validationClass);
            parseOnValidationError(parent, validationClass);
        }
    }

    private boolean validationMethodHasNoParams(Class<? extends Annotation> annotationClass, ExecutableElement method) {
        boolean hasNoParams = true;

        if (method.getParameters().size() > 0) {
            hasNoParams = false;
            error(method, "Method annotated with @%s can not have parameters.",annotationClass.getSimpleName());
        }

        return hasNoParams;
    }

    private void parseOnValidationSuccess(Element parent, ValidationClass validationClass) {
        List<Element> elements = parent.getEnclosedElements()
                .stream()
                .filter(element -> element.getAnnotation(OnValidationSuccess.class) != null)
                .collect(Collectors.toList());

        if (elements.size() == 0) {
            error(
                    parent,
                    "The class %s have an element annotated with @ValidateOnClick annotation and it requires a method annotated with @OnValidationSuccess annotation.",
                    parent.getSimpleName()
            );
            return;
        }

        if (elements.size() > 1) {
            error(
                    parent,
                    "The class %s must have only one element annotated with @OnValidationSuccess annotation.",
                    parent.getSimpleName()
            );
            return;
        }

        if (elements.size() == 1) {
            boolean isInaccessible = isInaccessible(OnValidationSuccess.class, elements.get(0));
            boolean validationMethodHasNoParams = validationMethodHasNoParams(OnValidationSuccess.class, ((ExecutableElement) elements.get(0)));
            if (isInaccessible && !validationMethodHasNoParams) return;

            validationClass.setOnValidationSuccessMethod(elements.get(0));
        }
    }

    private void parseOnValidationError(Element parent, ValidationClass validationClass) {
        List<Element> elements = parent.getEnclosedElements()
                .stream()
                .filter(element -> element.getAnnotation(OnValidationError.class) != null)
                .collect(Collectors.toList());

        if (elements.size() > 1) {
            error(
                    parent,
                    "The class %s must have only one element annotated with @OnValidationError annotation.",
                    parent.getSimpleName()
            );
            return;
        }

        if (elements.size() == 1) {
            boolean isInaccessible = isInaccessible(OnValidationError.class, elements.get(0));
            boolean validationMethodHasNoParams = validationMethodHasNoParams(OnValidationError.class, ((ExecutableElement) elements.get(0)));
            if (isInaccessible && !validationMethodHasNoParams) return;

            validationClass.setOnValidationErrorMethod(elements.get(0));
        }
    }

    private void parseClearValidationsOnClick(Element parent, ValidationClass validationClass) {
        List<Element> elements = parent.getEnclosedElements()
                .stream()
                .filter(element -> element.getAnnotation(ClearValidationsOnClick.class) != null)
                .collect(Collectors.toList());

        if (elements.size() > 1) {
            error(
                    parent,
                    "The class %s must have only one element annotated with @ClearValidationsOnClick annotation.",
                    parent.getSimpleName()
            );
            return;
        }

        if (elements.size() == 1) {
            validationClass.setClearValidationsButton(elements.get(0));
        }
    }

    private void parseNotEmptyValidation(Element element, Set<Element> parents, List<ValidationField> validationFields) {
        boolean hasError = isInvalid(NotEmptyValidation.class, element) || isInaccessible(NotEmptyValidation.class, element);

        if (hasError) {
            return;
        }

        int errorMessageResourceId = element.getAnnotation(NotEmptyValidation.class).errorMessage();
        boolean autoDismiss = element.getAnnotation(NotEmptyValidation.class).autoDismiss();
        QualifiedId qualifiedId = elementToQualifiedId(element, errorMessageResourceId);

        parents.add(element.getEnclosingElement());
        validationFields.add(new ValidationField(
                element,
                NotEmptyValidation.class,
                getId(qualifiedId),
                autoDismiss
        ));
    }

    private void parseEmailValidation(Element element, Set<Element> parents, List<ValidationField> validationFields) {
        boolean hasError = isInvalid(EmailValidation.class, element) || isInaccessible(EmailValidation.class, element);

        if (hasError) {
            return;
        }

        int errorMessageResourceId = element.getAnnotation(EmailValidation.class).errorMessage();
        boolean autoDismiss = element.getAnnotation(EmailValidation.class).autoDismiss();
        QualifiedId qualifiedId = elementToQualifiedId(element, errorMessageResourceId);

        parents.add(element.getEnclosingElement());
        validationFields.add(new ValidationField(
                element,
                EmailValidation.class,
                getId(qualifiedId),
                autoDismiss
        ));
    }

    private void parseConfirmEmailValidation(Element element, Set<Element> parents, List<ValidationField> validationFields) {
        boolean hasError = isInvalid(ConfirmEmailValidation.class, element) ||
                isInaccessible(ConfirmEmailValidation.class, element) ||
                !validateConfirmValidationElements(EmailValidation.class, ConfirmEmailValidation.class, element);

        if (hasError) return;

        int errorMessageResourceId = element.getAnnotation(ConfirmEmailValidation.class).errorMessage();
        boolean autoDismiss = element.getAnnotation(ConfirmEmailValidation.class).autoDismiss();
        QualifiedId qualifiedId = elementToQualifiedId(element, errorMessageResourceId);

        parents.add(element.getEnclosingElement());
        validationFields.add(new ValidationField(
                element,
                ConfirmEmailValidation.class,
                getId(qualifiedId),
                autoDismiss
        ));
    }

    private void parsePatternValidation(Element element, Set<Element> parents, List<ValidationField> validationFields) {
        boolean hasError = isInvalid(PatternValidation.class, element) || isInaccessible(PatternValidation.class, element);

        if (hasError) {
            return;
        }

        int errorMessageResourceId = element.getAnnotation(PatternValidation.class).errorMessage();
        boolean autoDismiss = element.getAnnotation(PatternValidation.class).autoDismiss();
        QualifiedId qualifiedId = elementToQualifiedId(element, errorMessageResourceId);

        parents.add(element.getEnclosingElement());
        validationFields.add(new ValidationField(
                element,
                PatternValidation.class,
                getId(qualifiedId),
                autoDismiss
        ));
    }

    private void parseLengthValidation(Element element, Set<Element> parents, List<ValidationField> validationFields) {
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

            int errorMessageResourceId = element.getAnnotation(LengthValidation.class).errorMessage();
            boolean autoDismiss = element.getAnnotation(LengthValidation.class).autoDismiss();
            QualifiedId qualifiedId = elementToQualifiedId(element, errorMessageResourceId);

            parents.add(element.getEnclosingElement());
            validationFields.add(new ValidationField(
                    element,
                    LengthValidation.class,
                    getId(qualifiedId),
                    autoDismiss
            ));
        } catch (Exception e) {
            logParsingError(element, LengthValidation.class, e);
        }
    }

    private void parseOnlyNumberValidation(Element element, Set<Element> parents, List<ValidationField> validationFields) {
        boolean hasError = isInvalid(OnlyNumberValidation.class, element) || isInaccessible(OnlyNumberValidation.class, element);

        if (hasError) {
            return;
        }

        int errorMessageResourceId = element.getAnnotation(OnlyNumberValidation.class).errorMessage();
        boolean autoDismiss = element.getAnnotation(OnlyNumberValidation.class).autoDismiss();
        QualifiedId qualifiedId = elementToQualifiedId(element, errorMessageResourceId);

        parents.add(element.getEnclosingElement());
        validationFields.add(new ValidationField(
                element,
                OnlyNumberValidation.class,
                getId(qualifiedId),
                autoDismiss
        ));
    }

    private void parsePasswordValidation(Element element, Set<Element> parents, List<ValidationField> validationFields) {
        boolean hasError = isInvalid(PasswordValidation.class, element) || isInaccessible(PasswordValidation.class, element);

        if (hasError) {
            return;
        }

        List<? extends Element> elementsOfParent = element.getEnclosingElement().getEnclosedElements();
        int elementsAnnotatedWithPasswordValidation = 0;

        for (Element elementOfParent : elementsOfParent) {
            if (elementOfParent.getAnnotation(PasswordValidation.class) != null) {
                elementsAnnotatedWithPasswordValidation++;
            }
        }

        if (elementsAnnotatedWithPasswordValidation > 1) {
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            error(
                    element.getEnclosingElement(),
                    "%s must have only one element annotated with @PasswordValidation.",
                    enclosingElement.getQualifiedName()
            );
        }

        int errorMessageResourceId = element.getAnnotation(PasswordValidation.class).errorMessage();
        boolean autoDismiss = element.getAnnotation(PasswordValidation.class).autoDismiss();
        QualifiedId qualifiedId = elementToQualifiedId(element, errorMessageResourceId);

        parents.add(element.getEnclosingElement());
        validationFields.add(new ValidationField(
                element,
                PasswordValidation.class,
                getId(qualifiedId),
                autoDismiss
        ));
    }

    private void parseConfirmPasswordValidation(Element element, Set<Element> parents, List<ValidationField> validationFields) {
        boolean hasError = isInvalid(ConfirmPasswordValidation.class, element) ||
                isInaccessible(ConfirmPasswordValidation.class, element) ||
                !validateConfirmValidationElements(PasswordValidation.class, ConfirmPasswordValidation.class, element);

        if (hasError) return;

        int errorMessageResourceId = element.getAnnotation(ConfirmPasswordValidation.class).errorMessage();
        boolean autoDismiss = element.getAnnotation(ConfirmPasswordValidation.class).autoDismiss();
        QualifiedId qualifiedId = elementToQualifiedId(element, errorMessageResourceId);

        parents.add(element.getEnclosingElement());
        validationFields.add(new ValidationField(
                element,
                ConfirmPasswordValidation.class,
                getId(qualifiedId),
                autoDismiss
        ));
    }

    private boolean validateConfirmValidationElements(
            Class<? extends Annotation> primaryAnnotation,
            Class<? extends Annotation> confirmAnnotation,
            Element element
    ) {
        boolean isValid = true;

        String primaryAnnotationClassName = primaryAnnotation.getSimpleName();
        String confirmAnnotationClassName = confirmAnnotation.getSimpleName();

        int elementsAnnotatedWithPrimaryValidation;
        int elementsAnnotatedWithConfirmValidation;

        List<? extends Element> elementsOfParent = element.getEnclosingElement().getEnclosedElements();

        elementsAnnotatedWithPrimaryValidation = (int) elementsOfParent.stream()
                .filter(elementOfParent -> elementOfParent.getAnnotation(primaryAnnotation) != null)
                .count();

        elementsAnnotatedWithConfirmValidation = (int) elementsOfParent.stream()
                .filter(elementOfParent -> elementOfParent.getAnnotation(confirmAnnotation) != null)
                .count();

        if (elementsAnnotatedWithPrimaryValidation == 0 && elementsAnnotatedWithConfirmValidation > 0) {
            isValid = false;
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            error(
                    element.getEnclosingElement(),
                    "%s must have at least one element annotated with @%s.",
                    enclosingElement.getSimpleName(),
                    primaryAnnotationClassName
            );
        }

        if (elementsAnnotatedWithConfirmValidation > 1) {
            isValid = false;
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            error(
                    element.getEnclosingElement(),
                    "%s must have only one element annotated with @%s.",
                    enclosingElement.getSimpleName(),
                    confirmAnnotationClassName
            );
        }

        return isValid;
    }

    private boolean isInvalid(Class<? extends Annotation> annotationClass, Element element) {
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

    private boolean isInaccessible(Class<? extends Annotation> annotationClass, Element element) {
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
        symbols.computeIfAbsent(qualifiedId, i -> new Id(i.id));
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