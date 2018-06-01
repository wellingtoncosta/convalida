package convalida.compiler;

import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import convalida.annotations.BetweenValidation;
import convalida.annotations.ClearValidationsOnClick;
import convalida.annotations.ConfirmEmailValidation;
import convalida.annotations.ConfirmPasswordValidation;
import convalida.annotations.CpfValidation;
import convalida.annotations.CreditCardValidation;
import convalida.annotations.EmailValidation;
import convalida.annotations.LengthValidation;
import convalida.annotations.OnValidationError;
import convalida.annotations.OnValidationSuccess;
import convalida.annotations.OnlyNumberValidation;
import convalida.annotations.PasswordValidation;
import convalida.annotations.PatternValidation;
import convalida.annotations.RequiredValidation;
import convalida.annotations.ValidateOnClick;
import convalida.compiler.internal.Id;
import convalida.compiler.internal.QualifiedId;
import convalida.compiler.internal.ValidationClass;
import convalida.compiler.internal.ValidationField;
import convalida.compiler.internal.scanners.IdScanner;
import convalida.compiler.internal.scanners.RClassScanner;

import static convalida.compiler.Constants.BETWEEN_END_ANNOTATION;
import static convalida.compiler.Constants.BETWEEN_START_ANNOTATION;
import static convalida.compiler.Constants.CLEAR_VALIDATIONS_ON_CLICK_ANNOTATION;
import static convalida.compiler.Constants.CONFIRM_EMAIL_VALIDATION;
import static convalida.compiler.Constants.CONFIRM_PASSWORD_ANNOTATION;
import static convalida.compiler.Constants.CPF_ANNOTATION;
import static convalida.compiler.Constants.CREDIT_CARD_ANNOTATION;
import static convalida.compiler.Constants.EMAIL_ANNOTATION;
import static convalida.compiler.Constants.LENGTH_ANNOTATION;
import static convalida.compiler.Constants.ONLY_NUMBER_ANNOTATION;
import static convalida.compiler.Constants.ON_VALIDATION_ERROR_ANNOTATION;
import static convalida.compiler.Constants.ON_VALIDATION_SUCCESS_ANNOTATION;
import static convalida.compiler.Constants.PASSWORD_ANNOTATION;
import static convalida.compiler.Constants.PATTERN_ANNOTATION;
import static convalida.compiler.Constants.REQUIRED_ANNOTATION;
import static convalida.compiler.Constants.VALIDATE_ON_CLICK_ANNOTATION;
import static convalida.compiler.Messager.error;
import static convalida.compiler.Messager.logParsingError;
import static convalida.compiler.Preconditions.confirmValidationElementsHasError;
import static convalida.compiler.Preconditions.hasMoreThanOneMethodsAnnotatedWith;
import static convalida.compiler.Preconditions.hasNoMethodAnnotatedWith;
import static convalida.compiler.Preconditions.isInaccessible;
import static convalida.compiler.Preconditions.isInvalid;
import static convalida.compiler.Preconditions.methodHasParams;

/**
 * @author Wellington Costa on 13/06/2017.
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({
        REQUIRED_ANNOTATION,
        EMAIL_ANNOTATION,
        CONFIRM_EMAIL_VALIDATION,
        PATTERN_ANNOTATION,
        LENGTH_ANNOTATION,
        ONLY_NUMBER_ANNOTATION,
        PASSWORD_ANNOTATION,
        CONFIRM_PASSWORD_ANNOTATION,
        CPF_ANNOTATION,
        BETWEEN_START_ANNOTATION,
        BETWEEN_END_ANNOTATION,
        CREDIT_CARD_ANNOTATION,
        VALIDATE_ON_CLICK_ANNOTATION,
        CLEAR_VALIDATIONS_ON_CLICK_ANNOTATION,
        ON_VALIDATION_SUCCESS_ANNOTATION,
        ON_VALIDATION_ERROR_ANNOTATION
})
public class ConvalidaProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Types typeUtils;
    private Filer filer;
    private Trees trees;

    private final Map<QualifiedId, Id> symbols = new LinkedHashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        this.elementUtils = processingEnvironment.getElementUtils();
        this.typeUtils = processingEnvironment.getTypeUtils();
        Messager.init(processingEnvironment.getMessager());
        this.filer = processingEnvironment.getFiler();

        try {
            trees = Trees.instance(processingEnv);
        } catch (IllegalArgumentException ignored) { }
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();

        annotations.add(RequiredValidation.class);
        annotations.add(EmailValidation.class);
        annotations.add(ConfirmEmailValidation.class);
        annotations.add(PatternValidation.class);
        annotations.add(LengthValidation.class);
        annotations.add(OnlyNumberValidation.class);
        annotations.add(PasswordValidation.class);
        annotations.add(ConfirmPasswordValidation.class);
        annotations.add(CpfValidation.class);
        annotations.add(BetweenValidation.Start.class);
        annotations.add(BetweenValidation.End.class);
        annotations.add(CreditCardValidation.class);
        annotations.add(ValidateOnClick.class);
        annotations.add(ClearValidationsOnClick.class);
        annotations.add(OnValidationSuccess.class);
        annotations.add(OnValidationError.class);

        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
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
        List<Element> validationActions = new ArrayList<>();
        List<Element> validationResults = new ArrayList<>();
        List<ValidationClass> validationClasses = new ArrayList<>();

        scanForRClasses(env);

        // Process each @RequiredValidation element
        for (Element element : env.getElementsAnnotatedWith(RequiredValidation.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseRequiredValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, RequiredValidation.class, e);
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

        // Process each @CpfValidation element
        for (Element element : env.getElementsAnnotatedWith(CpfValidation.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseCpfValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, CpfValidation.class, e);
            }
        }

        // Process each @BetweenValidation element
        for (Element element : env.getElementsAnnotatedWith(BetweenValidation.Start.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseBetweenValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, BetweenValidation.Start.class, e);
            }
        }

        // Process each @CreditCardValidation element
        for (Element element : env.getElementsAnnotatedWith(CreditCardValidation.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseCreditCardValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, CreditCardValidation.class, e);
            }
        }

        // Process each @ValidateOnClick element.
        for (Element element : env.getElementsAnnotatedWith(ValidateOnClick.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseValidateOnClick(element, parents, validationActions);
            } catch (Exception e) {
                logParsingError(element, ValidateOnClick.class, e);
            }
        }

        // Process each @ClearValidationsOnClick element.
        for (Element element : env.getElementsAnnotatedWith(ClearValidationsOnClick.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseClearValidationsOnClick(element, parents, validationActions);
            } catch (Exception e) {
                logParsingError(element, ClearValidationsOnClick.class, e);
            }
        }

        // Process each @OnValidationSuccess element.
        for (Element element : env.getElementsAnnotatedWith(OnValidationSuccess.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseOnValidationSuccess(element, parents, validationResults);
            } catch (Exception e) {
                logParsingError(element, OnValidationSuccess.class, e);
            }
        }

        // Process each @OnValidationError element.
        for (Element element : env.getElementsAnnotatedWith(OnValidationError.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseOnValidationError(element, parents, validationResults);
            } catch (Exception e) {
                logParsingError(element, OnValidationError.class, e);
            }
        }

        // Assemble the validation classes and fields
        for (Element parent : parents) {
            ValidationClass validationClass = new ValidationClass(parent, this.elementUtils);

            processValidationFields(parent, validationClass, validationFields);

            processValidationActions(parent, validationClass, validationActions);

            processValidationResults(parent, validationClass, validationResults);

            validationClasses.add(validationClass);
        }

        return validationClasses;
    }

    private void processValidationFields(
            Element parent,
            ValidationClass validationClass,
            List<ValidationField> validationFields) {
        for (ValidationField validationField : validationFields) {
            Element element = validationField.element;
            if (element.getEnclosingElement().equals(parent)) {
                validationClass.addField(validationField);
            }
        }
    }

    private void processValidationActions(
            Element parent,
            ValidationClass validationClass,
            List<Element> validationActions) {
        for(Element validationAction : validationActions) {
            if(validationAction.getEnclosingElement().equals(parent)) {
                if(validationAction.getAnnotation(ValidateOnClick.class) != null) {
                    validationClass.setValidateButton(validationAction);
                }
                if(validationAction.getAnnotation(ClearValidationsOnClick.class) != null) {
                    validationClass.setClearValidationsButton(validationAction);
                }
            }
        }
    }

    private void processValidationResults(
            Element parent,
            ValidationClass validationClass,
            List<Element> validationResults) {
        for(Element validationResult : validationResults) {
            if(validationResult.getEnclosingElement().equals(parent)) {
                if(validationResult.getAnnotation(OnValidationSuccess.class) != null) {
                    validationClass.setOnValidationSuccessMethod(validationResult);
                }
                if(validationResult.getAnnotation(OnValidationError.class) != null) {
                    validationClass.setOnValidationErrorMethod(validationResult);
                }
            }
        }
    }

    private void parseValidateOnClick(Element element, Set<Element> parents, List<Element> validationActions) {
        Element parent = element.getEnclosingElement();
        boolean hasError =
                isInaccessible(ValidateOnClick.class, element) ||
                hasMoreThanOneMethodsAnnotatedWith(parent, ValidateOnClick.class);

        if(hasError) return;

        parents.add(parent);
        validationActions.add(element);
    }

    private void parseClearValidationsOnClick(Element element, Set<Element> parents, List<Element> validationActions) {
        Element parent = element.getEnclosingElement();
        boolean hasError =
                isInaccessible(ClearValidationsOnClick.class, element) ||
                hasMoreThanOneMethodsAnnotatedWith(parent, ClearValidationsOnClick.class);

        if(hasError) return;

        parents.add(parent);
        validationActions.add(element);
    }

    private void parseOnValidationSuccess(Element element, Set<Element> parents, List<Element> validationActions) {
        Element parent = element.getEnclosingElement();
        ExecutableElement executableElement = (ExecutableElement)element;
        boolean hasError =
                isInaccessible(OnValidationSuccess.class, element) ||
                methodHasParams(executableElement, OnValidationSuccess.class) ||
                hasMoreThanOneMethodsAnnotatedWith(parent, OnValidationSuccess.class);

        if(hasError) return;

        parents.add(parent);
        validationActions.add(element);
    }

    private void parseOnValidationError(Element element, Set<Element> parents, List<Element> validationActions) {
        Element parent = element.getEnclosingElement();
        ExecutableElement executableElement = (ExecutableElement)element;
        boolean hasError =
                isInaccessible(OnValidationError.class, element) ||
                methodHasParams(executableElement, OnValidationError.class) ||
                hasMoreThanOneMethodsAnnotatedWith(parent, OnValidationError.class) ||
                hasNoMethodAnnotatedWith(parent, OnValidationSuccess.class);

        if(hasError) return;

        parents.add(parent);
        validationActions.add(element);
    }

    private void parseRequiredValidation(Element element, Set<Element> parents, List<ValidationField> validationFields) {
        boolean hasError =
                isInvalid(RequiredValidation.class, element) ||
                isInaccessible(RequiredValidation.class, element);

        if (hasError) {
            return;
        }

        int errorMessageResourceId = element.getAnnotation(RequiredValidation.class).errorMessage();
        boolean autoDismiss = element.getAnnotation(RequiredValidation.class).autoDismiss();
        QualifiedId qualifiedId = elementToQualifiedId(element, errorMessageResourceId);

        parents.add(element.getEnclosingElement());
        validationFields.add(new ValidationField(
                element,
                RequiredValidation.class,
                getId(qualifiedId),
                autoDismiss
        ));
    }

    private void parseEmailValidation(Element element, Set<Element> parents, List<ValidationField> validationFields) {
        boolean hasError =
                isInvalid(EmailValidation.class, element) ||
                isInaccessible(EmailValidation.class, element);

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
                confirmValidationElementsHasError(EmailValidation.class, ConfirmEmailValidation.class, element);

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
        boolean hasError =
                isInvalid(PatternValidation.class, element) ||
                isInaccessible(PatternValidation.class, element);

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
        boolean hasError =
                isInvalid(OnlyNumberValidation.class, element) ||
                isInaccessible(OnlyNumberValidation.class, element);

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
        boolean hasError =
                isInvalid(PasswordValidation.class, element) ||
                isInaccessible(PasswordValidation.class, element);

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
                confirmValidationElementsHasError(PasswordValidation.class, ConfirmPasswordValidation.class, element);

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

    private void parseCpfValidation(Element element, Set<Element> parents, List<ValidationField> validationFields) {
        boolean hasError =
                isInvalid(CpfValidation.class, element) ||
                        isInaccessible(CpfValidation.class, element);

        if (hasError) {
            return;
        }

        int errorMessageResourceId = element.getAnnotation(CpfValidation.class).errorMessage();
        boolean autoDismiss = element.getAnnotation(CpfValidation.class).autoDismiss();
        QualifiedId qualifiedId = elementToQualifiedId(element, errorMessageResourceId);

        parents.add(element.getEnclosingElement());
        validationFields.add(new ValidationField(
                element,
                CpfValidation.class,
                getId(qualifiedId),
                autoDismiss
        ));
    }


    private void parseBetweenValidation(Element element, Set<Element> parents, List<ValidationField> validationFields) {
        boolean hasError = (
                    isInvalid(BetweenValidation.Start.class, element) ||
                    isInaccessible(BetweenValidation.Start.class, element)
                ) || (
                    isInvalid(BetweenValidation.End.class, element) ||
                    isInaccessible(BetweenValidation.End.class, element)
                );

        if (hasError) {
            return;
        }

        List<? extends Element> elementsOfParent = element.getEnclosingElement().getEnclosedElements();
        int key = element.getAnnotation(BetweenValidation.Start.class).key();
        Element endElement = null;

        for (Element elementOfParent : elementsOfParent) {
            if (elementOfParent.getAnnotation(BetweenValidation.End.class) != null
                    && elementOfParent.getAnnotation(BetweenValidation.End.class).key() == key) {
                endElement = elementOfParent;
            }
        }

        if(endElement == null) {
            error(
                    element.getEnclosingElement(),
                    "The class %s has one element annotated with @%s with key %s but it requires an element annotated with @%s and with same key.",
                    element.getEnclosingElement().getSimpleName(),
                    BetweenValidation.Start.class.getSimpleName(),
                    key,
                    BetweenValidation.End.class.getSimpleName()
            );
            return;
        }

        int startErrorMessage = element.getAnnotation(BetweenValidation.Start.class).errorMessage();
        boolean startAutoDismiss = element.getAnnotation(BetweenValidation.Start.class).autoDismiss();
        QualifiedId startQualifiedId = elementToQualifiedId(element, startErrorMessage);

        parents.add(element.getEnclosingElement());
        validationFields.add(new ValidationField(
                element,
                BetweenValidation.Start.class,
                getId(startQualifiedId),
                startAutoDismiss
        ));

        int endErrorMessage = endElement.getAnnotation(BetweenValidation.End.class).errorMessage();
        boolean endAutoDismiss = endElement.getAnnotation(BetweenValidation.End.class).autoDismiss();
        QualifiedId endQualifiedId = elementToQualifiedId(endElement, endErrorMessage);

        parents.add(endElement.getEnclosingElement());
        validationFields.add(new ValidationField(
                endElement,
                BetweenValidation.End.class,
                getId(endQualifiedId),
                endAutoDismiss
        ));
    }

    private void parseCreditCardValidation(Element element, Set<Element> parents, List<ValidationField> validationFields) {
        boolean hasError =
                isInvalid(CreditCardValidation.class, element) ||
                        isInaccessible(CreditCardValidation.class, element);

        if (hasError) {
            return;
        }

        int errorMessageResourceId = element.getAnnotation(CreditCardValidation.class).errorMessage();
        boolean autoDismiss = element.getAnnotation(CreditCardValidation.class).autoDismiss();
        QualifiedId qualifiedId = elementToQualifiedId(element, errorMessageResourceId);

        parents.add(element.getEnclosingElement());
        validationFields.add(new ValidationField(
                element,
                CreditCardValidation.class,
                getId(qualifiedId),
                autoDismiss
        ));
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

}