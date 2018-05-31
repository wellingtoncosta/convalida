package convalida.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Element;

import convalida.annotations.CpfValidation;
import convalida.annotations.EmailValidation;
import convalida.annotations.LengthValidation;
import convalida.annotations.PasswordValidation;
import convalida.annotations.PatternValidation;
import convalida.compiler.internal.ValidationClass;
import convalida.compiler.internal.ValidationField;

import static convalida.compiler.Constants.ABSTRACT_VALIDATOR;
import static convalida.compiler.Constants.BETWEEN_ANNOTATION;
import static convalida.compiler.Constants.BETWEEN_END_ANNOTATION;
import static convalida.compiler.Constants.BETWEEN_VALIDATOR;
import static convalida.compiler.Constants.BUTTON;
import static convalida.compiler.Constants.CONFIRM_EMAIL_VALIDATION;
import static convalida.compiler.Constants.CONFIRM_EMAIL_VALIDATOR;
import static convalida.compiler.Constants.CONFIRM_PASSWORD_ANNOTATION;
import static convalida.compiler.Constants.CONFIRM_PASSWORD_VALIDATOR;
import static convalida.compiler.Constants.CONVALIDA_DATABINDING_R;
import static convalida.compiler.Constants.CPF_ANNOTATION;
import static convalida.compiler.Constants.CPF_VALIDATOR;
import static convalida.compiler.Constants.EMAIL_ANNOTATION;
import static convalida.compiler.Constants.EMAIL_VALIDATOR;
import static convalida.compiler.Constants.LENGTH_ANNOTATION;
import static convalida.compiler.Constants.LENGTH_VALIDATOR;
import static convalida.compiler.Constants.LIST;
import static convalida.compiler.Constants.NON_NULL;
import static convalida.compiler.Constants.ONLY_NUMBER_ANNOTATION;
import static convalida.compiler.Constants.ONLY_NUMBER_VALIDATOR;
import static convalida.compiler.Constants.OVERRIDE;
import static convalida.compiler.Constants.PASSWORD_ANNOTATION;
import static convalida.compiler.Constants.PASSWORD_VALIDATOR;
import static convalida.compiler.Constants.PATTERN_ANNOTATION;
import static convalida.compiler.Constants.PATTERN_VALIDATOR;
import static convalida.compiler.Constants.REQUIRED_ANNOTATION;
import static convalida.compiler.Constants.REQUIRED_VALIDATOR;
import static convalida.compiler.Constants.UI_THREAD;
import static convalida.compiler.Constants.VALIDATOR_SET;
import static convalida.compiler.Constants.VIEW;
import static convalida.compiler.Constants.VIEWGROUP;
import static convalida.compiler.Constants.VIEW_DATA_BINDING;
import static convalida.compiler.Constants.VIEW_ONCLICK_LISTENER;
import static convalida.compiler.Constants.VIEW_TAG_UTILS;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * @author Wellington Costa on 19/06/2017.
 */
class JavaFiler {

    static JavaFile cookJava(ValidationClass validationClass) {
        TypeSpec.Builder validationClassBuilder = TypeSpec.classBuilder(validationClass.className)
                .addModifiers(PUBLIC)
                .addField(VALIDATOR_SET, "validatorSet", PRIVATE)
                .addMethod(createConstructor(validationClass))
                .addMethod(createDatabindingConstructor(validationClass))
                .addMethod(createValidateOnClickListener(validationClass))
                .addMethod(createClearValidationsOnClickListener(validationClass))
                .addMethod(createInitMethod(validationClass))
                .addMethod(createInitDatabindingMethod(validationClass));

        return JavaFile.builder(validationClass.packageName, validationClassBuilder.build())
                .addFileComment("Generated code from Convalida. Do not modify!")
                .build();
    }

    private static MethodSpec createConstructor(ValidationClass validationClass) {
        return MethodSpec.constructorBuilder()
                .addModifiers(PRIVATE)
                .addAnnotation(UI_THREAD)
                .addParameter(ParameterSpec
                        .builder(validationClass.typeName, "target")
                        .addModifiers(FINAL)
                        .addAnnotation(NON_NULL)
                        .build()
                )
                .addStatement("validatorSet = new $T()", VALIDATOR_SET)
                .addCode(createValidationsCodeBlock(validationClass))
                .addCode(createValidateOnClickCodeBlock(validationClass))
                .addCode(createClearValidationsOnClickCodeBlock(validationClass))
                .build();
    }

    private static MethodSpec createDatabindingConstructor(ValidationClass validationClass) {
        return MethodSpec.constructorBuilder()
                .addModifiers(PRIVATE)
                .addAnnotation(UI_THREAD)
                .addParameter(ParameterSpec
                        .builder(validationClass.typeName, "target")
                        .addModifiers(FINAL)
                        .addAnnotation(NON_NULL)
                        .build()
                )
                .addParameter(ParameterSpec
                        .builder(VIEW_DATA_BINDING, "binding")
                        .addModifiers(FINAL)
                        .addAnnotation(NON_NULL)
                        .build()
                )
                .addCode("if (binding.hasPendingBindings()) {")
                .addCode("\n")
                .addCode(CodeBlock.builder().indent().build())
                .addCode("binding.executePendingBindings();")
                .addCode("\n")
                .addCode(CodeBlock.builder().unindent().build())
                .addCode("}")
                .addCode("\n")
                .addCode("\n")
                .addStatement("validatorSet = new $T()", VALIDATOR_SET)
                .addCode(
                        "$T<$T> views = $T.getViewsByTag(($T) binding.getRoot(), $T.id.validation_type);",
                        LIST,
                        VIEW,
                        VIEW_TAG_UTILS,
                        VIEWGROUP,
                        CONVALIDA_DATABINDING_R
                )
                .addCode("\n")
                .addCode(
                        "$T<$T> buttons = $T.getViewsByTag(($T) binding.getRoot(), $T.id.validation_action);",
                        LIST,
                        VIEW,
                        VIEW_TAG_UTILS,
                        VIEWGROUP,
                        CONVALIDA_DATABINDING_R
                )
                .addCode("\n")
                .addCode("$T validateButton = null;", BUTTON)
                .addCode("\n")
                .addCode("$T clearValidationsButton = null;", BUTTON)
                .addCode("\n")
                .addCode("\n")
                .addCode("for (View view : views) {")
                .addCode("\n")
                .addCode(CodeBlock.builder().indent().build())
                .addCode(
                        "validatorSet.addValidator(($T) view.getTag($T.id.validation_type));",
                        ABSTRACT_VALIDATOR,
                        CONVALIDA_DATABINDING_R
                )
                .addCode(CodeBlock.builder().unindent().build())
                .addCode("\n")
                .addCode("}")
                .addCode("\n")
                .addCode("\n")
                .addCode("for (View button : buttons) {")
                .addCode("\n")
                .addCode(CodeBlock.builder().indent().build())
                .addCode(
                        "if (button.getTag($T.id.validation_action).equals($T.id.validate) && validateButton == null) {",
                        CONVALIDA_DATABINDING_R,
                        CONVALIDA_DATABINDING_R
                )
                .addCode("\n")
                .addCode(CodeBlock.builder().indent().build())
                .addCode("validateButton = ($T) button;", BUTTON)
                .addCode(CodeBlock.builder().unindent().build())
                .addCode("\n")
                .addCode("}")
                .addCode("\n")
                .addCode("\n")
                .addCode(
                        "if (button.getTag($T.id.validation_action).equals($T.id.clear) && clearValidationsButton == null) {",
                        CONVALIDA_DATABINDING_R,
                        CONVALIDA_DATABINDING_R
                )
                .addCode("\n")
                .addCode(CodeBlock.builder().indent().build())
                .addCode("clearValidationsButton = ($T) button;", BUTTON)
                .addCode(CodeBlock.builder().unindent().build())
                .addCode("\n")
                .addCode("}")
                .addCode("\n")
                .addCode("\n")
                .addCode("if (validateButton != null && clearValidationsButton != null) {")
                .addCode("\n")
                .addCode(CodeBlock.builder().indent().build())
                .addCode("break;")
                .addCode(CodeBlock.builder().unindent().build())
                .addCode("\n")
                .addCode("}")
                .addCode("\n")
                .addCode(CodeBlock.builder().unindent().build())
                .addCode("}")
                .addCode("\n")
                .addCode("\n")
                .addCode("if (validateButton != null) {")
                .addCode("\n")
                .addCode(CodeBlock.builder().indent().build())
                .addCode("validateOnClickListener(validateButton, target);")
                .addCode(CodeBlock.builder().unindent().build())
                .addCode("\n")
                .addCode("}")
                .addCode("\n")
                .addCode("\n")
                .addCode("if (clearValidationsButton != null) {")
                .addCode("\n")
                .addCode(CodeBlock.builder().indent().build())
                .addCode("clearValidationsOnClickListener(clearValidationsButton, target);")
                .addCode(CodeBlock.builder().unindent().build())
                .addCode("\n")
                .addCode("}")
                .addCode("\n")
                .build();
    }

    private static CodeBlock createValidationsCodeBlock(final ValidationClass validationClass) {
        CodeBlock.Builder builder = CodeBlock.builder();

        for(ValidationField field : validationClass.fields) {
            switch (field.annotationClassName) {
                case REQUIRED_ANNOTATION:
                    builder.add(createRequiredValidationCodeBlock(field));
                    break;
                case EMAIL_ANNOTATION:
                    builder.add(createEmailValidationCodeBlock(field));
                    break;
                case CONFIRM_EMAIL_VALIDATION:
                    for(ValidationField validationField : validationClass.fields) {
                        if(validationField.annotationClassName.equals(EMAIL_ANNOTATION)) {
                            builder.add(createConfirmEmailValidationCodeBlock(validationField, field));
                            break;
                        }
                    }
                    break;
                case PATTERN_ANNOTATION:
                    builder.add(createPatternValidationCodeBlock(field));
                    break;
                case LENGTH_ANNOTATION:
                    builder.add(createLengthValidationCodeBlock(field));
                    break;
                case ONLY_NUMBER_ANNOTATION:
                    builder.add(createOnlyNumberValidationCodeBlock(field));
                    break;
                case PASSWORD_ANNOTATION:
                    builder.add(createPasswordValidationCodeBlock(field));
                    break;
                case CONFIRM_PASSWORD_ANNOTATION:
                    for(ValidationField validationField : validationClass.fields) {
                        if(validationField.annotationClassName.equals(PASSWORD_ANNOTATION)) {
                            builder.add(createConfirmPasswordValidationCodeBlock(validationField, field));
                            break;
                        }
                    }
                    break;
                case CPF_ANNOTATION:
                    builder.add(createCpfValidationCodeBlock(field));
                    break;
                case BETWEEN_ANNOTATION:
                    builder.add(createBetweenValidationCodeBlock(validationClass, field));
                    break;
            }
        }
        return builder.build();
    }

    private static CodeBlock createRequiredValidationCodeBlock(ValidationField field) {
        return CodeBlock.builder()
                .addStatement(
                        "validatorSet.addValidator(new $T(target.$N, target.getString($L), $L))",
                        REQUIRED_VALIDATOR,
                        field.name,
                        field.id.code,
                        field.autoDismiss
                )
                .build();
    }

    private static CodeBlock createEmailValidationCodeBlock(ValidationField field) {
        return CodeBlock.builder()
                .addStatement(
                        "validatorSet.addValidator(new $T(target.$N, target.getString($L), $L, $L))",
                        EMAIL_VALIDATOR,
                        field.name,
                        field.id.code,
                        field.autoDismiss,
                        field.element.getAnnotation(EmailValidation.class).required()
                )
                .build();
    }

    private static CodeBlock createConfirmEmailValidationCodeBlock(
            ValidationField emailField,
            ValidationField confirmEmailField) {
        return CodeBlock.builder()
                .addStatement(
                        "validatorSet.addValidator(new $T(target.$N, target.$N, target.getString($L), $L))",
                        CONFIRM_EMAIL_VALIDATOR,
                        emailField.name,
                        confirmEmailField.name,
                        confirmEmailField.id.code,
                        confirmEmailField.autoDismiss
                )
                .build();
    }

    private static CodeBlock createPatternValidationCodeBlock(ValidationField field) {
        return CodeBlock.builder()
                .addStatement(
                        "validatorSet.addValidator(new $T(target.$N, target.getString($L), $S, $L))",
                        PATTERN_VALIDATOR,
                        field.name,
                        field.id.code,
                        field.element.getAnnotation(PatternValidation.class).pattern(),
                        field.autoDismiss
                )
                .build();
    }

    private static CodeBlock createLengthValidationCodeBlock(ValidationField field) {
        return CodeBlock.builder()
                .addStatement(
                        "validatorSet.addValidator(new $T(target.$N, $L, $L, target.getString($L), $L))",
                        LENGTH_VALIDATOR,
                        field.name,
                        field.element.getAnnotation(LengthValidation.class).min(),
                        field.element.getAnnotation(LengthValidation.class).max(),
                        field.id.code,
                        field.autoDismiss
                )
                .build();
    }

    private static CodeBlock createOnlyNumberValidationCodeBlock(ValidationField field) {
        return CodeBlock.builder()
                .addStatement(
                        "validatorSet.addValidator(new $T(target.$N, target.getString($L), $L))",
                        ONLY_NUMBER_VALIDATOR,
                        field.name,
                        field.id.code,
                        field.autoDismiss
                )
                .build();
    }

    private static CodeBlock createPasswordValidationCodeBlock(ValidationField field) {
        return CodeBlock.builder()
                .addStatement(
                        "validatorSet.addValidator(new $T(target.$N, $L, $S, target.getString($L), $L))",
                        PASSWORD_VALIDATOR,
                        field.name,
                        field.element.getAnnotation(PasswordValidation.class).min(),
                        field.element.getAnnotation(PasswordValidation.class).pattern(),
                        field.id.code,
                        field.autoDismiss
                )
                .build();
    }

    private static CodeBlock createConfirmPasswordValidationCodeBlock(
            ValidationField passwordField,
            ValidationField confirmPasswordField) {
        return CodeBlock.builder()
                .addStatement(
                        "validatorSet.addValidator(new $T(target.$N, target.$N, target.getString($L), $L))",
                        CONFIRM_PASSWORD_VALIDATOR,
                        passwordField.name,
                        confirmPasswordField.name,
                        confirmPasswordField.id.code,
                        confirmPasswordField.autoDismiss
                )
                .build();
    }

    private static CodeBlock createCpfValidationCodeBlock(ValidationField field) {
        return CodeBlock.builder()
                .addStatement(
                        "validatorSet.addValidator(new $T(target.$N, target.getString($L), $L, $L))",
                        CPF_VALIDATOR,
                        field.name,
                        field.id.code,
                        field.autoDismiss,
                        field.element.getAnnotation(CpfValidation.class).required()
                )
                .build();
    }

    private static CodeBlock createBetweenValidationCodeBlock(ValidationClass validationClass, ValidationField startField) {
        ValidationField endField = null;

        for(ValidationField field : validationClass.fields) {
            if(field.annotationClassName.equals(BETWEEN_END_ANNOTATION)) {
                endField = field;
                break;
            }
        }

        if(endField != null) {
            return CodeBlock.builder()
                    .addStatement(
                            "validatorSet.addValidator(new $T(target.$N, target.$N, target.getString($L), target.getString($L), $L, $L))",
                            BETWEEN_VALIDATOR,
                            startField.name,
                            endField.name,
                            startField.id.code,
                            endField.id.code,
                            startField.autoDismiss,
                            endField.autoDismiss
                    )
                    .build();
        } else return CodeBlock.builder().build();
    }

    private static CodeBlock createValidateOnClickCodeBlock(ValidationClass validationClass) {
        Element button = validationClass.getValidateButton();
        if(button != null) {
            return CodeBlock.builder()
                    .add("\n")
                    .add(
                            "validateOnClickListener(target.$N, target);",
                            button.getSimpleName().toString()
                    )
                    .build();
        }
        return CodeBlock.of("");
    }

    private static CodeBlock createClearValidationsOnClickCodeBlock(ValidationClass validationClass) {
        Element button = validationClass.getClearValidationsButton();
        if (button != null) {
            return CodeBlock.builder()
                    .add("\n")
                    .add(
                            "clearValidationsOnClickListener(target.$N, target);",
                            button.getSimpleName().toString()
                    )
                    .add("\n")
                    .build();
        }
        return CodeBlock.of("");
    }

    private static MethodSpec createValidateOnClickListener(
            ValidationClass validationClass) {
        Element onValidationErrorMethod = validationClass.getOnValidationErrorMethod();
        MethodSpec.Builder method = MethodSpec.methodBuilder("validateOnClickListener")
                .addModifiers(PRIVATE)
                .addAnnotation(UI_THREAD)
                .addParameter(ParameterSpec
                        .builder(BUTTON, "button")
                        .addAnnotation(NON_NULL)
                        .build()
                )
                .addParameter(ParameterSpec
                        .builder(validationClass.typeName, "target")
                        .addModifiers(FINAL)
                        .addAnnotation(NON_NULL)
                        .build()
                )
                .addCode(
                        "button.setOnClickListener(new $T() {",
                        VIEW_ONCLICK_LISTENER
                )
                .addCode("\n")
                .addCode(CodeBlock.builder().indent().build())
                .addCode(
                        "@$T public void onClick($T view) {",
                        OVERRIDE,
                        VIEW
                )
                .addCode("\n")
                .addCode(CodeBlock.builder().indent().build())
                .addCode("if(validatorSet.isValid()) {")
                .addCode("\n")
                .addCode(CodeBlock.builder().indent().build())
                .addCode(
                        "target.$N();",
                        validationClass.getOnValidationSuccessMethod().getSimpleName()
                )
                .addCode("\n")
                .addCode(CodeBlock.builder().unindent().build())
                .addCode("}");

        if(onValidationErrorMethod != null) {
            method.addCode(" else {")
                    .addCode("\n")
                    .addCode(CodeBlock.builder().indent().build())
                    .addCode(
                            "target.$N();",
                            validationClass.getOnValidationErrorMethod().getSimpleName()
                    )
                    .addCode("\n")
                    .addCode(CodeBlock.builder().unindent().build())
                    .addCode("}");
        }

        method.addCode(CodeBlock.builder().unindent().build())
                .addCode("\n")
                .addCode("}")
                .addCode(CodeBlock.builder().unindent().build())
                .addCode("\n")
                .addCode("});")
                .addCode("\n");

        return method.build();
    }

    private static MethodSpec createClearValidationsOnClickListener(
            ValidationClass validationClass) {
        return MethodSpec.methodBuilder("clearValidationsOnClickListener")
                .addModifiers(PRIVATE)
                .addAnnotation(UI_THREAD)
                .addParameter(ParameterSpec
                        .builder(BUTTON, "button")
                        .addAnnotation(NON_NULL)
                        .build()
                )
                .addParameter(ParameterSpec
                        .builder(validationClass.typeName, "target")
                        .addAnnotation(NON_NULL)
                        .build()
                )
                .addCode(
                        "button.setOnClickListener(new $T() {",
                        VIEW_ONCLICK_LISTENER
                )
                .addCode("\n")
                .addCode(CodeBlock.builder().indent().build())
                .addCode(
                        "@$T public void onClick($T view) {",
                        OVERRIDE,
                        VIEW
                )
                .addCode("\n")
                .addCode(CodeBlock.builder().indent().build())
                .addCode("validatorSet.clearValidators();")
                .addCode("\n")
                .addCode(CodeBlock.builder().unindent().build())
                .addCode("}")
                .addCode("\n")
                .addCode(CodeBlock.builder().unindent().build())
                .addCode("});")
                .addCode("\n")
                .build();
    }

    private static MethodSpec createInitMethod(ValidationClass validationClass) {
        ClassName className = ClassName.get(validationClass.packageName, validationClass.className);
        return MethodSpec.methodBuilder("init")
                .addModifiers(PUBLIC, STATIC)
                .addAnnotation(UI_THREAD)
                .addParameter(ParameterSpec
                        .builder(validationClass.typeName, "target")
                        .addAnnotation(NON_NULL)
                        .build()
                )
                .addStatement("new $T(target)", className)
                .build();
    }

    private static MethodSpec createInitDatabindingMethod(ValidationClass validationClass) {
        ClassName className = ClassName.get(validationClass.packageName, validationClass.className);
        return MethodSpec.methodBuilder("init")
                .addModifiers(PUBLIC, STATIC)
                .addAnnotation(UI_THREAD)
                .addParameter(ParameterSpec
                        .builder(validationClass.typeName, "target")
                        .addAnnotation(NON_NULL)
                        .build()
                )
                .addParameter(ParameterSpec
                        .builder(VIEW_DATA_BINDING, "binding")
                        .addAnnotation(NON_NULL)
                        .build()
                )
                .addStatement("new $T(target, binding)", className)
                .build();
    }

}