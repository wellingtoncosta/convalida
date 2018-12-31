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

import convalida.annotations.*;
import convalida.annotations.Between;
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
import static convalida.compiler.Constants.NUMBER_LIMIT_ANNOTATION;
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
        NUMBER_LIMIT_ANNOTATION,
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

    @Override public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        this.elementUtils = processingEnvironment.getElementUtils();
        this.typeUtils = processingEnvironment.getTypeUtils();
        this.filer = processingEnvironment.getFiler();

        Messager.init(processingEnvironment.getMessager());

        try {
            trees = Trees.instance(processingEnv);
        } catch (IllegalArgumentException ignored) { }
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();

        annotations.add(Required.class);
        annotations.add(Email.class);
        annotations.add(ConfirmEmail.class);
        annotations.add(Pattern.class);
        annotations.add(Length.class);
        annotations.add(OnlyNumber.class);
        annotations.add(Password.class);
        annotations.add(ConfirmPassword.class);
        annotations.add(Cpf.class);
        annotations.add(Between.Start.class);
        annotations.add(Between.End.class);
        annotations.add(CreditCard.class);
        annotations.add(NumberLimit.class);
        annotations.add(ValidateOnClick.class);
        annotations.add(ClearValidationsOnClick.class);
        annotations.add(OnValidationSuccess.class);
        annotations.add(OnValidationError.class);

        return annotations;
    }

    @Override public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment env) {
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

        // Process each @Required element
        for (Element element : env.getElementsAnnotatedWith(Required.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseRequiredValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, Required.class, e);
            }
        }

        // Process each @Email element
        for (Element element : env.getElementsAnnotatedWith(Email.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseEmailValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, Email.class, e);
            }
        }

        // Process each @ConfirmEmail element
        for (Element element : env.getElementsAnnotatedWith(ConfirmEmail.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseConfirmEmailValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, ConfirmEmail.class, e);
            }
        }

        // Process each @Pattern element.
        for (Element element : env.getElementsAnnotatedWith(Pattern.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parsePatternValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, Pattern.class, e);
            }
        }

        // Process each @Length element.
        for (Element element : env.getElementsAnnotatedWith(Length.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseLengthValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, Length.class, e);
            }
        }

        // Process each @OnlyNumber element.
        for (Element element : env.getElementsAnnotatedWith(OnlyNumber.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseOnlyNumberValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, OnlyNumber.class, e);
            }
        }

        // Process each @Password element.
        for (Element element : env.getElementsAnnotatedWith(Password.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parsePasswordValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, Password.class, e);
            }
        }

        // Process each @ConfirmPassword element.
        for (Element element : env.getElementsAnnotatedWith(ConfirmPassword.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseConfirmPasswordValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, ConfirmPassword.class, e);
            }
        }

        // Process each @Cpf element
        for (Element element : env.getElementsAnnotatedWith(Cpf.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseCpfValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, Cpf.class, e);
            }
        }

        // Process each @Between element
        for (Element element : env.getElementsAnnotatedWith(Between.Start.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseBetweenValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, Between.Start.class, e);
            }
        }

        // Process each @CreditCard element
        for (Element element : env.getElementsAnnotatedWith(CreditCard.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseCreditCardValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, CreditCard.class, e);
            }
        }

        // Process each @NumberLimit element
        for (Element element : env.getElementsAnnotatedWith(NumberLimit.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                parseNumberLimitValidation(element, parents, validationFields);
            } catch (Exception e) {
                logParsingError(element, NumberLimit.class, e);
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

    private void parseValidateOnClick(
            Element element,
            Set<Element> parents,
            List<Element> validationActions
    ) {
        Element parent = element.getEnclosingElement();
        boolean hasError =
                isInaccessible(ValidateOnClick.class, element) ||
                hasMoreThanOneMethodsAnnotatedWith(parent, ValidateOnClick.class);

        if(hasError) return;

        parents.add(parent);
        validationActions.add(element);
    }

    private void parseClearValidationsOnClick(
            Element element,
            Set<Element> parents,
            List<Element> validationActions
    ) {
        Element parent = element.getEnclosingElement();
        boolean hasError =
                isInaccessible(ClearValidationsOnClick.class, element) ||
                hasMoreThanOneMethodsAnnotatedWith(parent, ClearValidationsOnClick.class);

        if(hasError) return;

        parents.add(parent);
        validationActions.add(element);
    }

    private void parseOnValidationSuccess(
            Element element,
            Set<Element> parents,
            List<Element> validationActions
    ) {
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

    private void parseOnValidationError(
            Element element,
            Set<Element> parents,
            List<Element> validationActions
    ) {
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

    private void parseRequiredValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> validationFields
    ) {
        boolean hasError =
                isInvalid(Required.class, element) ||
                isInaccessible(Required.class, element);

        if (hasError) {
            return;
        }

        int errorMessageResourceId = element.getAnnotation(Required.class).errorMessageResId();
        boolean autoDismiss = element.getAnnotation(Required.class).autoDismiss();
        QualifiedId qualifiedId = elementToQualifiedId(element, errorMessageResourceId);

        parents.add(element.getEnclosingElement());
        validationFields.add(new ValidationField(
                element,
                Required.class,
                getId(qualifiedId),
                autoDismiss
        ));
    }

    private void parseEmailValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> validationFields
    ) {
        boolean hasError =
                isInvalid(Email.class, element) ||
                isInaccessible(Email.class, element);

        if (hasError) {
            return;
        }

        int errorMessageResourceId = element.getAnnotation(Email.class).errorMessageResId();
        boolean autoDismiss = element.getAnnotation(Email.class).autoDismiss();
        QualifiedId qualifiedId = elementToQualifiedId(element, errorMessageResourceId);

        parents.add(element.getEnclosingElement());
        validationFields.add(new ValidationField(
                element,
                Email.class,
                getId(qualifiedId),
                autoDismiss
        ));
    }

    private void parseConfirmEmailValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> validationFields
    ) {
        boolean hasError = isInvalid(ConfirmEmail.class, element) ||
                isInaccessible(ConfirmEmail.class, element) ||
                confirmValidationElementsHasError(Email.class, ConfirmEmail.class, element);

        if (hasError) return;

        int errorMessageResourceId = element.getAnnotation(ConfirmEmail.class).errorMessageResId();
        boolean autoDismiss = element.getAnnotation(ConfirmEmail.class).autoDismiss();
        QualifiedId qualifiedId = elementToQualifiedId(element, errorMessageResourceId);

        parents.add(element.getEnclosingElement());
        validationFields.add(new ValidationField(
                element,
                ConfirmEmail.class,
                getId(qualifiedId),
                autoDismiss
        ));
    }

    private void parsePatternValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> validationFields
    ) {
        boolean hasError =
                isInvalid(Pattern.class, element) ||
                isInaccessible(Pattern.class, element);

        if (hasError) {
            return;
        }

        int errorMessageResourceId = element.getAnnotation(Pattern.class).errorMessageResId();
        boolean autoDismiss = element.getAnnotation(Pattern.class).autoDismiss();
        QualifiedId qualifiedId = elementToQualifiedId(element, errorMessageResourceId);

        parents.add(element.getEnclosingElement());
        validationFields.add(new ValidationField(
                element,
                Pattern.class,
                getId(qualifiedId),
                autoDismiss
        ));
    }

    private void parseLengthValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> validationFields
    ) {
        int minLength = element.getAnnotation(Length.class).min();
        int maxLength = element.getAnnotation(Length.class).max();

        if (minLength == 0 && maxLength == 0) {
            error(element, "The min length and max length must be greater than zero.");
        }

        if (maxLength > 0 && maxLength < minLength) {
            error(element, "The max lentgh must be greater than min lentgh.");
        }

        try {
            boolean hasError = isInvalid(Length.class, element) || isInaccessible(Length.class, element);

            if (hasError) {
                return;
            }

            int errorMessageResourceId = element.getAnnotation(Length.class).errorMessageResId();
            boolean autoDismiss = element.getAnnotation(Length.class).autoDismiss();
            QualifiedId qualifiedId = elementToQualifiedId(element, errorMessageResourceId);

            parents.add(element.getEnclosingElement());
            validationFields.add(new ValidationField(
                    element,
                    Length.class,
                    getId(qualifiedId),
                    autoDismiss
            ));
        } catch (Exception e) {
            logParsingError(element, Length.class, e);
        }
    }

    private void parseOnlyNumberValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> validationFields
    ) {
        boolean hasError =
                isInvalid(OnlyNumber.class, element) ||
                isInaccessible(OnlyNumber.class, element);

        if (hasError) {
            return;
        }

        int errorMessageResourceId = element.getAnnotation(OnlyNumber.class).errorMessageResId();
        boolean autoDismiss = element.getAnnotation(OnlyNumber.class).autoDismiss();
        QualifiedId qualifiedId = elementToQualifiedId(element, errorMessageResourceId);

        parents.add(element.getEnclosingElement());
        validationFields.add(new ValidationField(
                element,
                OnlyNumber.class,
                getId(qualifiedId),
                autoDismiss
        ));
    }

    private void parsePasswordValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> validationFields
    ) {
        boolean hasError =
                isInvalid(Password.class, element) ||
                isInaccessible(Password.class, element);

        if (hasError) {
            return;
        }

        List<? extends Element> elementsOfParent = element.getEnclosingElement().getEnclosedElements();
        int elementsAnnotatedWithPasswordValidation = 0;

        for (Element elementOfParent : elementsOfParent) {
            if (elementOfParent.getAnnotation(Password.class) != null) {
                elementsAnnotatedWithPasswordValidation++;
            }
        }

        if (elementsAnnotatedWithPasswordValidation > 1) {
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            error(
                    element.getEnclosingElement(),
                    "%s must have only one element annotated with @Password.",
                    enclosingElement.getQualifiedName()
            );
        }

        int errorMessageResourceId = element.getAnnotation(Password.class).errorMessageResId();
        boolean autoDismiss = element.getAnnotation(Password.class).autoDismiss();
        QualifiedId qualifiedId = elementToQualifiedId(element, errorMessageResourceId);

        parents.add(element.getEnclosingElement());
        validationFields.add(new ValidationField(
                element,
                Password.class,
                getId(qualifiedId),
                autoDismiss
        ));
    }

    private void parseConfirmPasswordValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> validationFields
    ) {
        boolean hasError = isInvalid(ConfirmPassword.class, element) ||
                isInaccessible(ConfirmPassword.class, element) ||
                confirmValidationElementsHasError(Password.class, ConfirmPassword.class, element);

        if (hasError) return;

        int errorMessageResourceId = element.getAnnotation(ConfirmPassword.class).errorMessageResId();
        boolean autoDismiss = element.getAnnotation(ConfirmPassword.class).autoDismiss();
        QualifiedId qualifiedId = elementToQualifiedId(element, errorMessageResourceId);

        parents.add(element.getEnclosingElement());
        validationFields.add(new ValidationField(
                element,
                ConfirmPassword.class,
                getId(qualifiedId),
                autoDismiss
        ));
    }

    private void parseCpfValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> validationFields
    ) {
        boolean hasError =
                isInvalid(Cpf.class, element) ||
                        isInaccessible(Cpf.class, element);

        if (hasError) {
            return;
        }

        int errorMessageResourceId = element.getAnnotation(Cpf.class).errorMessageResId();
        boolean autoDismiss = element.getAnnotation(Cpf.class).autoDismiss();
        QualifiedId qualifiedId = elementToQualifiedId(element, errorMessageResourceId);

        parents.add(element.getEnclosingElement());
        validationFields.add(new ValidationField(
                element,
                Cpf.class,
                getId(qualifiedId),
                autoDismiss
        ));
    }


    private void parseBetweenValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> validationFields
    ) {
        boolean hasError = (
                    isInvalid(Between.Start.class, element) ||
                    isInaccessible(Between.Start.class, element)
                ) || (
                    isInvalid(Between.End.class, element) ||
                    isInaccessible(Between.End.class, element)
                );

        if (hasError) {
            return;
        }

        List<? extends Element> elementsOfParent = element.getEnclosingElement().getEnclosedElements();
        int key = element.getAnnotation(Between.Start.class).key();
        Element endElement = null;

        for (Element elementOfParent : elementsOfParent) {
            if (elementOfParent.getAnnotation(Between.End.class) != null
                    && elementOfParent.getAnnotation(Between.End.class).key() == key) {
                endElement = elementOfParent;
            }
        }

        if(endElement == null) {
            error(
                    element.getEnclosingElement(),
                    "The class %s has one element annotated with @%s with key %s but it requires an element annotated with @%s and with same key.",
                    element.getEnclosingElement().getSimpleName(),
                    Between.Start.class.getSimpleName(),
                    key,
                    Between.End.class.getSimpleName()
            );
            return;
        }

        int startErrorMessage = element.getAnnotation(Between.Start.class).errorMessageResId();
        boolean startAutoDismiss = element.getAnnotation(Between.Start.class).autoDismiss();
        QualifiedId startQualifiedId = elementToQualifiedId(element, startErrorMessage);

        parents.add(element.getEnclosingElement());
        validationFields.add(new ValidationField(
                element,
                Between.Start.class,
                getId(startQualifiedId),
                startAutoDismiss
        ));

        int endErrorMessage = endElement.getAnnotation(Between.End.class).errorMessageResId();
        boolean endAutoDismiss = endElement.getAnnotation(Between.End.class).autoDismiss();
        QualifiedId endQualifiedId = elementToQualifiedId(endElement, endErrorMessage);

        parents.add(endElement.getEnclosingElement());
        validationFields.add(new ValidationField(
                endElement,
                Between.End.class,
                getId(endQualifiedId),
                endAutoDismiss
        ));
    }

    private void parseCreditCardValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> validationFields
    ) {
        boolean hasError =
                isInvalid(CreditCard.class, element) ||
                        isInaccessible(CreditCard.class, element);

        if (hasError) {
            return;
        }

        int errorMessageResourceId = element.getAnnotation(CreditCard.class).errorMessageResId();
        boolean autoDismiss = element.getAnnotation(CreditCard.class).autoDismiss();
        QualifiedId qualifiedId = elementToQualifiedId(element, errorMessageResourceId);

        parents.add(element.getEnclosingElement());
        validationFields.add(new ValidationField(
                element,
                CreditCard.class,
                getId(qualifiedId),
                autoDismiss
        ));
    }

    private void parseNumberLimitValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> validationFields
    ) {
        boolean hasError =
                isInvalid(NumberLimit.class, element) ||
                        isInaccessible(NumberLimit.class, element);

        if (hasError) {
            return;
        }

        int errorMessageResourceId = element.getAnnotation(NumberLimit.class).errorMessageResId();
        boolean autoDismiss = element.getAnnotation(NumberLimit.class).autoDismiss();
        QualifiedId qualifiedId = elementToQualifiedId(element, errorMessageResourceId);

        parents.add(element.getEnclosingElement());
        validationFields.add(new ValidationField(
                element,
                NumberLimit.class,
                getId(qualifiedId),
                autoDismiss
        ));
    }


    private static AnnotationMirror getMirror(
            Element element,
            Class<? extends Annotation> annotation
    ) {
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
