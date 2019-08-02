package convalida.compiler;

import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import convalida.annotations.Between;
import convalida.annotations.ClearValidationsOnClick;
import convalida.annotations.Cnpj;
import convalida.annotations.ConfirmEmail;
import convalida.annotations.ConfirmPassword;
import convalida.annotations.Cpf;
import convalida.annotations.CreditCard;
import convalida.annotations.Email;
import convalida.annotations.FutureDate;
import convalida.annotations.Ipv4;
import convalida.annotations.Ipv6;
import convalida.annotations.Isbn;
import convalida.annotations.Length;
import convalida.annotations.NumericLimit;
import convalida.annotations.OnValidationError;
import convalida.annotations.OnValidationSuccess;
import convalida.annotations.OnlyNumber;
import convalida.annotations.Password;
import convalida.annotations.PastDate;
import convalida.annotations.Pattern;
import convalida.annotations.Required;
import convalida.annotations.Url;
import convalida.annotations.ValidateOnClick;
import convalida.compiler.internal.AndroidResourceSanner;
import convalida.compiler.model.ValidationClass;
import convalida.compiler.model.ValidationField;
import convalida.compiler.util.JavaFiler;
import convalida.compiler.util.Messager;

import static convalida.compiler.internal.AndroidResourceSanner.scanForRClasses;
import static convalida.compiler.parser.AnnotationParsers.processBetweenValidation;
import static convalida.compiler.parser.AnnotationParsers.processClearValidationsOnClickAction;
import static convalida.compiler.parser.AnnotationParsers.processCnpjValidation;
import static convalida.compiler.parser.AnnotationParsers.processConfirmEmailValidation;
import static convalida.compiler.parser.AnnotationParsers.processConfirmPasswordValidation;
import static convalida.compiler.parser.AnnotationParsers.processCpfValidation;
import static convalida.compiler.parser.AnnotationParsers.processCreditCardValidation;
import static convalida.compiler.parser.AnnotationParsers.processEmailValidation;
import static convalida.compiler.parser.AnnotationParsers.processFutureDateValidation;
import static convalida.compiler.parser.AnnotationParsers.processIpv4Validation;
import static convalida.compiler.parser.AnnotationParsers.processIpv6Validation;
import static convalida.compiler.parser.AnnotationParsers.processIsbnValidation;
import static convalida.compiler.parser.AnnotationParsers.processLengthValidation;
import static convalida.compiler.parser.AnnotationParsers.processNumericLimitValidation;
import static convalida.compiler.parser.AnnotationParsers.processOnValidationErrorResult;
import static convalida.compiler.parser.AnnotationParsers.processOnValidationSuccessResult;
import static convalida.compiler.parser.AnnotationParsers.processOnlyNumberValidation;
import static convalida.compiler.parser.AnnotationParsers.processPasswordValidation;
import static convalida.compiler.parser.AnnotationParsers.processPastDateValidation;
import static convalida.compiler.parser.AnnotationParsers.processPatternValidation;
import static convalida.compiler.parser.AnnotationParsers.processRequiredValidation;
import static convalida.compiler.parser.AnnotationParsers.processUrlValidation;
import static convalida.compiler.parser.AnnotationParsers.processValidateOnClickAction;
import static convalida.compiler.util.Messager.error;

/**
 * @author Wellington Costa on 13/06/2017.
 */
public class ConvalidaProcessor extends AbstractProcessor {

    private Elements elementUtils;

    private Filer filer;

    @Override public synchronized void init(ProcessingEnvironment processingEnvironment) {

        super.init(processingEnvironment);

        this.elementUtils = processingEnvironment.getElementUtils();

        this.filer = processingEnvironment.getFiler();

        AndroidResourceSanner.init(processingEnvironment,getSupportedAnnotations());

        Messager.init(processingEnvironment);

    }

    @Override public Set<String> getSupportedAnnotationTypes() {
        Set<Class<? extends Annotation>> annotations = getSupportedAnnotations();

        Set<String> names = new LinkedHashSet<>();

        for(Class<? extends Annotation> annotation : annotations) {
            names.add(annotation.getCanonicalName());
        }

        return names;
    }

    @Override public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        return new LinkedHashSet<>(
                Arrays.asList(
                        Required.class,
                        Email.class,
                        ConfirmEmail.class,
                        Pattern.class,
                        Length.class,
                        OnlyNumber.class,
                        Password.class,
                        ConfirmPassword.class,
                        Cpf.class,
                        Cnpj.class,
                        Isbn.class,
                        Between.Start.class,
                        Between.Limit.class,
                        CreditCard.class,
                        NumericLimit.class,
                        Ipv4.class,
                        Ipv6.class,
                        Url.class,
                        PastDate.class,
                        FutureDate.class,
                        ValidateOnClick.class,
                        ClearValidationsOnClick.class,
                        OnValidationSuccess.class,
                        OnValidationError.class
                )
        );
    }

    @Override public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment env) {
        List<ValidationClass> validationClasses = scanAndProcessValidations(env);

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

    private List<ValidationClass> scanAndProcessValidations(RoundEnvironment env) {

        Set<Element> parents = new HashSet<>();

        List<ValidationClass> classes = new ArrayList<>();

        List<ValidationField> fields = new ArrayList<>();

        List<Element> actions = new ArrayList<>();

        List<Element> results = new ArrayList<>();

        scanForRClasses(env);

        processRequiredValidation(env, parents, fields);

        processEmailValidation(env, parents, fields);

        processConfirmEmailValidation(env, parents, fields);

        processPatternValidation(env, parents, fields);

        processLengthValidation(env, parents, fields);

        processOnlyNumberValidation(env, parents, fields);

        processPasswordValidation(env, parents, fields);

        processConfirmPasswordValidation(env, parents, fields);

        processCpfValidation(env, parents, fields);

        processCnpjValidation(env, parents, fields);

        processIsbnValidation(env, parents, fields);

        processBetweenValidation(env, parents, fields);

        processCreditCardValidation(env, parents, fields);

        processNumericLimitValidation(env, parents, fields);

        processIpv4Validation(env, parents, fields);

        processIpv6Validation(env, parents, fields);

        processUrlValidation(env, parents, fields);

        processPastDateValidation(env, parents, fields);

        processFutureDateValidation(env, parents, fields);

        processValidateOnClickAction(env, parents, actions);

        processClearValidationsOnClickAction(env, parents, actions);

        processOnValidationSuccessResult(env, parents, results);

        processOnValidationErrorResult(env, parents, results);

        processAll(elementUtils, parents, classes, fields, actions, results);

        return classes;
    }

    private static void processAll(
            Elements elementUtils,
            Set<Element> parents,
            List<ValidationClass> classes,
            List<ValidationField> fields,
            List<Element> actions,
            List<Element> results
    ) {
        for (Element parent : parents) {

            ValidationClass validationClass = new ValidationClass(parent, elementUtils);

            processAllFields(parent, validationClass, fields);

            processAllActions(parent, validationClass, actions);

            processAllResults(parent, validationClass, results);

            classes.add(validationClass);

        }
    }

    private static void processAllFields(
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

    private static void processAllActions(
            Element parent,
            ValidationClass validationClass,
            List<Element> validationActions
    ) {
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

    private static void processAllResults(
            Element parent,
            ValidationClass validationClass,
            List<Element> validationResults
    ) {
        for(Element validationResult : validationResults) {
            if(validationResult.getEnclosingElement().equals(parent)) {
                if(validationResult.getAnnotation(OnValidationSuccess.class) != null) {
                    validationClass.setOnValidationSuccessMethod(validationResult);
                }
                if(validationResult.getAnnotation(OnValidationError.class) != null) {
                    validationClass.setOnValidationErrorMethod(validationResult);
                }

                if(validationClass.getOnValidationSuccessMethod() != null &&
                        validationClass.getOnValidationErrorMethod() != null) {
                    break;
                }
            }
        }
    }

}
