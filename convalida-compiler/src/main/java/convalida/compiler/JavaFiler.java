package convalida.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Element;

import convalida.annotations.LengthValidation;
import convalida.annotations.PasswordValidation;
import convalida.annotations.PatternValidation;
import convalida.compiler.internal.ValidationClass;
import convalida.compiler.internal.ValidationField;

import static convalida.compiler.Constants.CONFIRM_EMAIL_VALIDATION;
import static convalida.compiler.Constants.CONFIRM_EMAIL_VALIDATOR;
import static convalida.compiler.Constants.CONFIRM_PASSWORD_ANNOTATION;
import static convalida.compiler.Constants.CONFIRM_PASSWORD_VALIDATOR;
import static convalida.compiler.Constants.EMAIL_ANNOTATION;
import static convalida.compiler.Constants.EMAIL_VALIDATOR;
import static convalida.compiler.Constants.LENGTH_ANNOTATION;
import static convalida.compiler.Constants.LENGTH_VALIDATOR;
import static convalida.compiler.Constants.NON_NULL;
import static convalida.compiler.Constants.NOT_EMPTY_ANNOTATION;
import static convalida.compiler.Constants.NOT_EMPTY_VALIDATOR;
import static convalida.compiler.Constants.ONLY_NUMBER_ANNOTATION;
import static convalida.compiler.Constants.ONLY_NUMBER_VALIDATOR;
import static convalida.compiler.Constants.OVERRIDE;
import static convalida.compiler.Constants.PASSWORD_ANNOTATION;
import static convalida.compiler.Constants.PASSWORD_VALIDATOR;
import static convalida.compiler.Constants.PATTERN_ANNOTATION;
import static convalida.compiler.Constants.PATTERN_VALIDATOR;
import static convalida.compiler.Constants.UI_THREAD;
import static convalida.compiler.Constants.VALIDATOR_SET;
import static convalida.compiler.Constants.VIEW;
import static convalida.compiler.Constants.VIEW_ONCLICK_LISTENER;
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
                .addMethod(createValidateOnClickMethod(validationClass))
                .addMethod(createClearValidationsOnClickMethod(validationClass))
                .addMethod(createInitMethod(validationClass));

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
                        .addAnnotation(NON_NULL)
                        .build()
                )
                .addStatement("validatorSet = new $T()", VALIDATOR_SET)
                .addCode("\n")
                .addCode(createValidationsCodeBlock(validationClass))
                .build();
    }

    private static CodeBlock createValidationsCodeBlock(final ValidationClass validationClass) {
        CodeBlock.Builder builder = CodeBlock.builder();

        for(ValidationField field : validationClass.fields) {
            switch (field.annotationClassName) {
                case NOT_EMPTY_ANNOTATION:
                    builder.add(createNotEmptyValidationCodeBlock(field));
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
            }
        }

        builder.add(createValidateOnClickCodeBlock(validationClass));
        builder.add(createClearValidationsOnClickCodeBlock(validationClass));
        return builder.build();
    }

    private static CodeBlock createNotEmptyValidationCodeBlock(ValidationField field) {
        return CodeBlock.builder()
                .addStatement(
                        "validatorSet.addValidator(new $T(target.$N, target.getString($L), $L))",
                        NOT_EMPTY_VALIDATOR,
                        field.name,
                        field.id.code,
                        field.autoDismiss
                )
                .build();
    }

    private static CodeBlock createEmailValidationCodeBlock(ValidationField field) {
        return CodeBlock.builder()
                .addStatement(
                        "validatorSet.addValidator(new $T(target.$N, target.getString($L), $L))",
                        EMAIL_VALIDATOR,
                        field.name,
                        field.id.code,
                        field.autoDismiss
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

    private static CodeBlock createValidateOnClickCodeBlock(ValidationClass validationClass) {
        Element button = validationClass.getValidateButton();
        return CodeBlock.builder()
                .add("\n")
                .addStatement(
                        "target.$N.setOnClickListener(validateOnClickListener(target))",
                        button.getSimpleName().toString()
                )
                .build();
    }

    private static MethodSpec createValidateOnClickMethod(ValidationClass validationClass) {
        MethodSpec.Builder validateOnClickMethod = MethodSpec.methodBuilder("validateOnClickListener")
                .addAnnotation(UI_THREAD)
                .addModifiers(PRIVATE)
                .addParameter(ParameterSpec.builder(
                        validationClass.typeName,
                        "target",
                        FINAL
                    ).build()
                )
                .addCode(
                        "return new $T() {",
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

        if(validationClass.getOnValidationErrorMethod() != null) {
            validateOnClickMethod
                    .addCode(" else {")
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

        validateOnClickMethod.addCode("\n")
                .addCode(CodeBlock.builder().unindent().build())
                .addCode("}")
                .addCode("\n")
                .addCode(CodeBlock.builder().unindent().build())
                .addCode("};")
                .addCode("\n")
                .returns(VIEW_ONCLICK_LISTENER);

        return validateOnClickMethod.build();
    }

    private static CodeBlock createClearValidationsOnClickCodeBlock(ValidationClass validationClass) {
        Element clearValidationsButton = validationClass.getClearValidationsButton();

        if (clearValidationsButton != null) {
            return CodeBlock.builder()
                    .addStatement(
                            "target.$N.setOnClickListener(clearValidationsOnClickListener(target))",
                            clearValidationsButton.getSimpleName().toString()
                    )
                    .build();
        }

        return CodeBlock.of("");
    }

    private static MethodSpec createClearValidationsOnClickMethod(ValidationClass validationClass) {
        return MethodSpec.methodBuilder("clearValidationsOnClickListener")
                .addAnnotation(UI_THREAD)
                .addModifiers(PRIVATE)
                .addParameter(ParameterSpec.builder(
                        validationClass.typeName,
                        "target",
                        FINAL
                    ).build()
                )
                .addCode("return new $T() {", VIEW_ONCLICK_LISTENER)
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
                .addCode("};")
                .addCode("\n")
                .returns(VIEW_ONCLICK_LISTENER)
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

}