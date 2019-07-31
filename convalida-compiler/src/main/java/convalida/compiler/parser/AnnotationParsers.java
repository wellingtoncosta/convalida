package convalida.compiler.parser;

import com.google.auto.common.SuperficialValidation;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

import convalida.annotations.Between;
import convalida.annotations.ClearValidationsOnClick;
import convalida.annotations.Cnpj;
import convalida.annotations.ConfirmEmail;
import convalida.annotations.ConfirmPassword;
import convalida.annotations.Cpf;
import convalida.annotations.CreditCard;
import convalida.annotations.Email;
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
import convalida.compiler.internal.QualifiedId;
import convalida.compiler.model.CommonValidationParameters;
import convalida.compiler.model.ValidationField;

import static convalida.compiler.internal.AndroidResourceSanner.elementToQualifiedId;
import static convalida.compiler.internal.AndroidResourceSanner.getId;
import static convalida.compiler.util.Constants.VALIDATION_ERROR;
import static convalida.compiler.util.Messager.error;
import static convalida.compiler.util.Messager.logParsingError;
import static convalida.compiler.util.Preconditions.confirmValidationElementsHasError;
import static convalida.compiler.util.Preconditions.hasMoreThanOneMethodsAnnotatedWith;
import static convalida.compiler.util.Preconditions.isInaccessible;
import static convalida.compiler.util.Preconditions.isInvalid;
import static convalida.compiler.util.Preconditions.methodHasZeroOrOneParameterOfType;
import static convalida.compiler.util.Preconditions.methodHasParams;

/**
 * @author Wellington Costa on 31/07/2019.
 */
public class AnnotationParsers {

    // Can not be instantiated
    private AnnotationParsers() { }

    public static void processRequiredValidation(
            RoundEnvironment env,
            Set<Element> parents,
            List<ValidationField> classes
    ) {
        parseGenericValidation(Required.class, env, parents, classes,
                new ValidationCallBack() {
                    @Override public void execute(
                            Element element,
                            Set<Element> parents,
                            List<ValidationField> fields
                    ) { parseRequiredValidation(element, parents, fields); }
                }
        );
    }

    public static void processEmailValidation(
            RoundEnvironment env,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        parseGenericValidation(Email.class, env, parents, fields,
                new ValidationCallBack() {
                    @Override public void execute(
                            Element element,
                            Set<Element> parents,
                            List<ValidationField> fields
                    ) { parseEmailValidation(element, parents, fields); }
                }
        );
    }

    public static void processConfirmEmailValidation(
            RoundEnvironment env,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        parseGenericValidation(ConfirmEmail.class, env, parents, fields,
                new ValidationCallBack() {
                    @Override public void execute(
                            Element element,
                            Set<Element> parents,
                            List<ValidationField> fields
                    ) { parseConfirmEmailValidation(element, parents, fields); }
                }
        );
    }

    public static void processPatternValidation(
            RoundEnvironment env,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        parseGenericValidation(Pattern.class, env, parents, fields,
                new ValidationCallBack() {
                    @Override public void execute(
                            Element element,
                            Set<Element> parents,
                            List<ValidationField> fields
                    ) { parsePatternValidation(element, parents, fields); }
                }
        );
    }

    public static void processLengthValidation(
            RoundEnvironment env,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        parseGenericValidation(Length.class, env, parents, fields,
                new ValidationCallBack() {
                    @Override public void execute(
                            Element element,
                            Set<Element> parents,
                            List<ValidationField> fields
                    ) { parseLengthValidation(element, parents, fields); }
                }
        );
    }

    public static void processOnlyNumberValidation(
            RoundEnvironment env,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        parseGenericValidation(OnlyNumber.class, env, parents, fields,
                new ValidationCallBack() {
                    @Override public void execute(
                            Element element,
                            Set<Element> parents,
                            List<ValidationField> fields
                    ) { parseOnlyNumberValidation(element, parents, fields); }
                }
        );
    }

    public static void processPasswordValidation(
            RoundEnvironment env,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        parseGenericValidation(Password.class, env, parents, fields,
                new ValidationCallBack() {
                    @Override public void execute(
                            Element element,
                            Set<Element> parents,
                            List<ValidationField> fields
                    ) { parsePasswordValidation(element, parents, fields); }
                }
        );
    }

    public static void processConfirmPasswordValidation(
            RoundEnvironment env,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        parseGenericValidation(ConfirmPassword.class, env, parents, fields,
                new ValidationCallBack() {
                    @Override public void execute(
                            Element element,
                            Set<Element> parents,
                            List<ValidationField> fields
                    ) { parseConfirmPasswordValidation(element, parents, fields); }
                }
        );
    }

    public static void processCpfValidation(
            RoundEnvironment env,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        parseGenericValidation(Cpf.class, env, parents, fields,
                new ValidationCallBack() {
                    @Override public void execute(
                            Element element,
                            Set<Element> parents,
                            List<ValidationField> fields
                    ) { parseCpfValidation(element, parents, fields); }
                }
        );
    }

    public static void processCnpjValidation(
            RoundEnvironment env,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        parseGenericValidation(Cnpj.class, env, parents, fields,
                new ValidationCallBack() {
                    @Override public void execute(
                            Element element,
                            Set<Element> parents,
                            List<ValidationField> fields
                    ) { parseCnpjValidation(element, parents, fields); }
                }
        );
    }

    public static void processIsbnValidation(
            RoundEnvironment env,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        parseGenericValidation(Isbn.class, env, parents, fields,
                new ValidationCallBack() {
                    @Override public void execute(
                            Element element,
                            Set<Element> parents,
                            List<ValidationField> fields
                    ) { parseIsbnValidation(element, parents, fields); }
                }
        );
    }

    public static void processBetweenValidation(
            RoundEnvironment env,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        parseGenericValidation(Between.Start.class, env, parents, fields,
                new ValidationCallBack() {
                    @Override public void execute(
                            Element element,
                            Set<Element> parents,
                            List<ValidationField> fields
                    ) { parseBetweenValidation(element, parents, fields); }
                }
        );
    }

    public static void processCreditCardValidation(
            RoundEnvironment env,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        parseGenericValidation(CreditCard.class, env, parents, fields,
                new ValidationCallBack() {
                    @Override public void execute(
                            Element element,
                            Set<Element> parents,
                            List<ValidationField> fields
                    ) { parseCreditCardValidation(element, parents, fields); }
                }
        );
    }

    public static void processNumericLimitValidation(
            RoundEnvironment env,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        parseGenericValidation(NumericLimit.class, env, parents, fields,
                new ValidationCallBack() {
                    @Override public void execute(
                            Element element,
                            Set<Element> parents,
                            List<ValidationField> fields
                    ) { parseNumericLimitValidation(element, parents, fields); }
                }
        );
    }

    public static void processIpv4Validation(
            RoundEnvironment env,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        parseGenericValidation(Ipv4.class, env, parents, fields,
                new ValidationCallBack() {
                    @Override public void execute(
                            Element element,
                            Set<Element> parents,
                            List<ValidationField> fields
                    ) { parseIpv4Validation(element, parents, fields); }
                }
        );
    }

    public static void processIpv6Validation(
            RoundEnvironment env,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        parseGenericValidation(Ipv6.class, env, parents, fields,
                new ValidationCallBack() {
                    @Override public void execute(
                            Element element,
                            Set<Element> parents,
                            List<ValidationField> fields
                    ) { parseIpv6Validation(element, parents, fields); }
                }
        );
    }

    public static void processUrlValidation(
            RoundEnvironment env,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        parseGenericValidation(Url.class, env, parents, fields,
                new ValidationCallBack() {
                    @Override public void execute(
                            Element element,
                            Set<Element> parents,
                            List<ValidationField> fields
                    ) { parseUrlValidation(element, parents, fields); }
                }
        );
    }

    public static void processPastDateValidation(
            RoundEnvironment env,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        parseGenericValidation(PastDate.class, env, parents, fields,
                new ValidationCallBack() {
                    @Override public void execute(
                            Element element,
                            Set<Element> parents,
                            List<ValidationField> fields
                    ) { parsePastDateValidation(element, parents, fields); }
                }
        );
    }

    public static void processValidateOnClickAction(
            RoundEnvironment env,
            Set<Element> parents,
            List<Element> elements
    ) {
        parseGenericElement(ValidateOnClick.class, env, parents, elements,
                new ElementCallBack() {
                    @Override public void execute(
                            Element element,
                            Set<Element> parents,
                            List<Element> elements
                    ) { parseValidateOnClickAction(element, parents, elements); }
                }
        );
    }

    public static void processClearValidationsOnClickAction(
            RoundEnvironment env,
            Set<Element> parents,
            List<Element> elements
    ) {
        parseGenericElement(ClearValidationsOnClick.class, env, parents, elements,
                new ElementCallBack() {
                    @Override public void execute(
                            Element element,
                            Set<Element> parents,
                            List<Element> elements
                    ) { parseClearValidationsOnClickAction(element, parents, elements); }
                }
        );
    }

    public static void processOnValidationSuccessResult(
            RoundEnvironment env,
            Set<Element> parents,
            List<Element> elements
    ) {
        parseGenericElement(OnValidationSuccess.class, env, parents, elements,
                new ElementCallBack() {
                    @Override public void execute(
                            Element element,
                            Set<Element> parents,
                            List<Element> elements
                    ) { parseOnValidationSuccess(element, parents, elements); }
                }
        );
    }

    public static void processOnValidationErrorResult(
            RoundEnvironment env,
            Set<Element> parents,
            List<Element> elements
    ) {
        parseGenericElement(OnValidationError.class, env, parents, elements,
                new ElementCallBack() {
                    @Override public void execute(
                            Element element,
                            Set<Element> parents,
                            List<Element> elements
                    ) { parseOnValidationError(element, parents, elements); }
                }
        );
    }

    private static void parseGenericValidation(
            Class<? extends Annotation> annotation,
            RoundEnvironment env,
            Set<Element> parents,
            List<ValidationField> fields,
            ValidationCallBack validationCallBack
    ) {
        for (Element element : env.getElementsAnnotatedWith(annotation)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                validationCallBack.execute(element, parents, fields);
            } catch (Exception e) {
                logParsingError(element, annotation, e);
            }
        }
    }

    private static void parseGenericElement(
            Class<? extends Annotation> annotation,
            RoundEnvironment env,
            Set<Element> parents,
            List<Element> action,
            ElementCallBack elementCallBack
    ) {
        for (Element element : env.getElementsAnnotatedWith(annotation)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            try {
                elementCallBack.execute(element, parents, action);
            } catch (Exception e) {
                logParsingError(element, annotation, e);
            }
        }
    }

    private static void parseRequiredValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        CommonValidationParameters parameters = new CommonValidationParameters(
                Required.class,
                element,
                element.getAnnotation(Required.class).errorMessageResId(),
                element.getAnnotation(Required.class).autoDismiss()
        );

        parseGenericValidation(parameters, parents, fields);
    }

    private static void parseEmailValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        CommonValidationParameters parameters = new CommonValidationParameters(
                Email.class,
                element,
                element.getAnnotation(Email.class).errorMessageResId(),
                element.getAnnotation(Email.class).autoDismiss()
        );

        parseGenericValidation(parameters, parents, fields);
    }

    private static void parseConfirmEmailValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        if (confirmValidationElementsHasError(Email.class, ConfirmEmail.class, element))
            return;

        CommonValidationParameters parameters = new CommonValidationParameters(
                ConfirmEmail.class,
                element,
                element.getAnnotation(ConfirmEmail.class).errorMessageResId(),
                element.getAnnotation(ConfirmEmail.class).autoDismiss()
        );

        parseGenericValidation(parameters, parents, fields);
    }

    private static void parsePatternValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        CommonValidationParameters parameters = new CommonValidationParameters(
                Pattern.class,
                element,
                element.getAnnotation(Pattern.class).errorMessageResId(),
                element.getAnnotation(Pattern.class).autoDismiss()
        );

        parseGenericValidation(parameters, parents, fields);
    }

    private static void parseLengthValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        CommonValidationParameters parameters = new CommonValidationParameters(
                Length.class,
                element,
                element.getAnnotation(Length.class).errorMessageResId(),
                element.getAnnotation(Length.class).autoDismiss()
        );

        parseGenericValidation(parameters, parents, fields);
    }

    private static void parseOnlyNumberValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        CommonValidationParameters parameters = new CommonValidationParameters(
                OnlyNumber.class,
                element,
                element.getAnnotation(OnlyNumber.class).errorMessageResId(),
                element.getAnnotation(OnlyNumber.class).autoDismiss()
        );

        parseGenericValidation(parameters, parents, fields);
    }

    private static void parsePasswordValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        CommonValidationParameters parameters = new CommonValidationParameters(
                Password.class,
                element,
                element.getAnnotation(Password.class).errorMessageResId(),
                element.getAnnotation(Password.class).autoDismiss()
        );

        parseGenericValidation(parameters, parents, fields);
    }

    private static void parseConfirmPasswordValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        if (confirmValidationElementsHasError(Password.class, ConfirmPassword.class, element))
            return;

        CommonValidationParameters parameters = new CommonValidationParameters(
                ConfirmPassword.class,
                element,
                element.getAnnotation(ConfirmPassword.class).errorMessageResId(),
                element.getAnnotation(ConfirmPassword.class).autoDismiss()
        );

        parseGenericValidation(parameters, parents, fields);
    }

    private static void parseCpfValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        CommonValidationParameters parameters = new CommonValidationParameters(
                Cpf.class,
                element,
                element.getAnnotation(Cpf.class).errorMessageResId(),
                element.getAnnotation(Cpf.class).autoDismiss()
        );

        parseGenericValidation(parameters, parents, fields);
    }

    private static void parseCnpjValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        CommonValidationParameters parameters = new CommonValidationParameters(
                Cnpj.class,
                element,
                element.getAnnotation(Cnpj.class).errorMessageResId(),
                element.getAnnotation(Cnpj.class).autoDismiss()
        );

        parseGenericValidation(parameters, parents, fields);
    }

    private static void parseIsbnValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        CommonValidationParameters parameters = new CommonValidationParameters(
                Isbn.class,
                element,
                element.getAnnotation(Isbn.class).errorMessageResId(),
                element.getAnnotation(Isbn.class).autoDismiss()
        );

        parseGenericValidation(parameters, parents, fields);
    }

    private static void parseBetweenValidation(
            Element startElement,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        boolean hasError = (
                isInvalid(Between.Start.class, startElement) ||
                        isInaccessible(Between.Start.class, startElement)
        ) || (
                isInvalid(Between.Limit.class, startElement) ||
                        isInaccessible(Between.Limit.class, startElement)
        );

        if (hasError) {
            return;
        }

        Element limitElement = validateBetweenStartAnnotation(startElement);

        if (limitElement == null) return;

        parseBetweenStartValidation(startElement, parents, fields);

        parseBetweenLimitValidation(limitElement, parents, fields);
    }

    private static Element validateBetweenStartAnnotation(Element startElement) {
        List<? extends Element> elementsOfParent = startElement.getEnclosingElement().getEnclosedElements();

        int key = startElement.getAnnotation(Between.Start.class).key();

        Element limitElement = null;

        for (Element elementOfParent : elementsOfParent) {
            if (elementOfParent.getAnnotation(Between.Limit.class) != null
                    && elementOfParent.getAnnotation(Between.Limit.class).key() == key) {

                limitElement = elementOfParent;

                break;
            }
        }

        if(limitElement == null) {
            error(
                    startElement.getEnclosingElement(),
                    "The class %s has one element annotated with @%s with key %s but it requires an element annotated with @%s and with same key.",
                    startElement.getEnclosingElement().getSimpleName(),
                    Between.Start.class.getSimpleName(),
                    key,
                    Between.Limit.class.getSimpleName()
            );
        }

        return limitElement;
    }

    private static void parseBetweenStartValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        CommonValidationParameters parameters = new CommonValidationParameters(
                Between.Start.class,
                element,
                element.getAnnotation(Between.Start.class).errorMessageResId(),
                element.getAnnotation(Between.Start.class).autoDismiss()
        );

        parseGenericValidation(parameters, parents, fields);
    }

    private static void parseBetweenLimitValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        CommonValidationParameters parameters = new CommonValidationParameters(
                Between.Limit.class,
                element,
                element.getAnnotation(Between.Limit.class).errorMessageResId(),
                element.getAnnotation(Between.Limit.class).autoDismiss()
        );

        parseGenericValidation(parameters, parents, fields);
    }

    private static void parseCreditCardValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        CommonValidationParameters parameters = new CommonValidationParameters(
                CreditCard.class,
                element,
                element.getAnnotation(CreditCard.class).errorMessageResId(),
                element.getAnnotation(CreditCard.class).autoDismiss()
        );

        parseGenericValidation(parameters, parents, fields);
    }

    private static void parseNumericLimitValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        CommonValidationParameters parameters = new CommonValidationParameters(
                NumericLimit.class,
                element,
                element.getAnnotation(NumericLimit.class).errorMessageResId(),
                element.getAnnotation(NumericLimit.class).autoDismiss()
        );

        parseGenericValidation(parameters, parents, fields);
    }

    private static void parseIpv4Validation(
            Element element,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        CommonValidationParameters parameters = new CommonValidationParameters(
                Ipv4.class,
                element,
                element.getAnnotation(Ipv4.class).errorMessageResId(),
                element.getAnnotation(Ipv4.class).autoDismiss()
        );

        parseGenericValidation(parameters, parents, fields);
    }

    private static void parseIpv6Validation(
            Element element,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        CommonValidationParameters parameters = new CommonValidationParameters(
                Ipv6.class,
                element,
                element.getAnnotation(Ipv6.class).errorMessageResId(),
                element.getAnnotation(Ipv6.class).autoDismiss()
        );

        parseGenericValidation(parameters, parents, fields);
    }

    private static void parseUrlValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        CommonValidationParameters parameters = new CommonValidationParameters(
                Url.class,
                element,
                element.getAnnotation(Url.class).errorMessageResId(),
                element.getAnnotation(Url.class).autoDismiss()
        );

        parseGenericValidation(parameters, parents, fields);
    }

    private static void parsePastDateValidation(
            Element element,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        CommonValidationParameters parameters = new CommonValidationParameters(
                PastDate.class,
                element,
                element.getAnnotation(PastDate.class).errorMessageResId(),
                element.getAnnotation(PastDate.class).autoDismiss()
        );

        parseGenericValidation(parameters, parents, fields);
    }

    private static void parseValidateOnClickAction(
            Element element,
            Set<Element> parents,
            List<Element> actions
    ) {
        parseGenericElement(ValidateOnClick.class, element, parents, actions);
    }

    private static void parseClearValidationsOnClickAction(
            Element element,
            Set<Element> parents,
            List<Element> actions
    ) {
        parseGenericElement(ClearValidationsOnClick.class, element, parents, actions);
    }

    private static void parseOnValidationSuccess(
            Element element,
            Set<Element> parents,
            List<Element> actions
    ) {
        parseGenericResult(OnValidationSuccess.class, element, parents, actions);
    }

    private static void parseOnValidationError(
            Element element,
            Set<Element> parents,
            List<Element> actions
    ) {
        String validationErrorType = VALIDATION_ERROR.toString();

        ExecutableElement executableElement = (ExecutableElement) element;

        Class<OnValidationError> annotation = OnValidationError.class;

        if(methodHasZeroOrOneParameterOfType(executableElement, annotation, validationErrorType))
            return;

        parseGenericResult(OnValidationError.class, element, parents, actions);
    }

    private static void parseGenericValidation(
            CommonValidationParameters parameters,
            Set<Element> parents,
            List<ValidationField> fields
    ) {
        boolean hasError = isInvalid(parameters.annotation, parameters.element) ||
                        isInaccessible(parameters.annotation, parameters.element);

        if (hasError) return;

        QualifiedId qualifiedId = elementToQualifiedId(parameters.element, parameters.errorMessageResId);

        parents.add(parameters.element.getEnclosingElement());

        fields.add(new ValidationField(
                parameters.element,
                parameters.annotation,
                getId(qualifiedId),
                parameters.autoDismiss
        ));
    }

    private static void parseGenericElement(
            Class<? extends Annotation> annotation,
            Element element,
            Set<Element> parents,
            List<Element> actions
    ) {
        Element parent = element.getEnclosingElement();

        boolean hasError = isInaccessible(annotation, element) ||
                        hasMoreThanOneMethodsAnnotatedWith(parent, annotation);

        if(hasError) return;

        parents.add(parent);

        actions.add(element);
    }

    private static void parseGenericResult(
            Class<? extends Annotation> annotation,
            Element element,
            Set<Element> parents,
            List<Element> validationActions
    ) {
        Element parent = element.getEnclosingElement();

        ExecutableElement executableElement = (ExecutableElement)element;

        boolean hasError = isInaccessible(annotation, element) ||
                        methodHasParams(executableElement, annotation) ||
                        hasMoreThanOneMethodsAnnotatedWith(parent, annotation);

        if(hasError) return;

        parents.add(parent);

        validationActions.add(element);
    }

    private interface ValidationCallBack {

        void execute(Element element, Set<Element> parents, List<ValidationField> fields);

    }

    private interface ElementCallBack {

        void execute(Element element, Set<Element> parents, List<Element> elements);

    }

}
