package convalida.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Set;

import javax.lang.model.element.Modifier;

import convalida.annotations.LengthValidation;
import convalida.annotations.PasswordValidation;
import convalida.annotations.PatternValidation;
import convalida.compiler.internal.FieldValidationInfo;
import convalida.compiler.internal.Id;
import convalida.compiler.internal.TargetClassInfo;

/**
 * @author Wellington Costa on 19/06/2017.
 */
class JavaFiler {

    private static final ClassName UI_THREAD            = ClassName.get("android.support.annotation", "UiThread");
    private static final ClassName OVERRIDE             = ClassName.get("java.lang", "Override");

    private static final ClassName VALIDATOR            = ClassName.get("convalida.library", "ConvalidaValidator");
    private static final ClassName VALIDATION_SET       = ClassName.get("convalida.library.validation", "ValidationSet");

    private static final String NOT_EMPTY_ANNOTATION        = "convalida.annotations.NotEmptyValidation";
    private static final String EMAIL_ANNOTATION            = "convalida.annotations.EmailValidation";
    private static final String PATTERN_ANNOTATION          = "convalida.annotations.PatternValidation";
    private static final String LENGTH_ANNOTATION           = "convalida.annotations.LengthValidation";
    private static final String ONLY_NUMBER_ANNOTATION      = "convalida.annotations.OnlyNumberValidation";
    private static final String PASSWORD_ANNOTATION         = "convalida.annotations.PasswordValidation";
    private static final String CONFIRM_PASSWORD_ANNOTATION = "convalida.annotations.ConfirmPasswordValidation";

    private static final String VALIDATORS_PACKAGE      = "convalida.library.validation.validator";

    private static final ClassName NOT_EMPTY_VALIDATOR          = ClassName.get(VALIDATORS_PACKAGE, "NotEmptyValidator");
    private static final ClassName EMAIL_VALIDATOR              = ClassName.get(VALIDATORS_PACKAGE, "EmailValidator");
    private static final ClassName PATTERN_VALIDATOR            = ClassName.get(VALIDATORS_PACKAGE, "PatternValidator");
    private static final ClassName LENGTH_VALIDATOR             = ClassName.get(VALIDATORS_PACKAGE, "LengthValidator");
    private static final ClassName ONLY_NUMBER_VALIDATOR        = ClassName.get(VALIDATORS_PACKAGE, "OnlyNumberValidator");
    private static final ClassName PASSWORD_VALIDATOR           = ClassName.get(VALIDATORS_PACKAGE, "PasswordValidator");
    private static final ClassName CONFIRM_PASSWORD_VALIDATOR   = ClassName.get(VALIDATORS_PACKAGE, "ConfirmPasswordValidator");


    static JavaFile cookJava(TargetClassInfo targetClassInfo, Set<FieldValidationInfo> fieldValidationInfos) {
        TypeSpec classValidator = TypeSpec.classBuilder(targetClassInfo.getClassName())
                .addSuperinterface(VALIDATOR)
                .addModifiers(Modifier.PUBLIC)
                .addField(VALIDATION_SET, "validationSet", Modifier.PRIVATE)
                .addMethod(createConstructor(targetClassInfo, fieldValidationInfos))
                .addMethod(createValidateMethod())
                .addMethod(createClearValidationMethod())
                .build();

        return JavaFile.builder(targetClassInfo.getPackageName(), classValidator)
                .addFileComment("Generated code from Convalida. Do not modify!")
                .build();
    }

    private static MethodSpec createConstructor(TargetClassInfo targetClassInfo, Set<FieldValidationInfo> fieldValidationInfos) {
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addAnnotation(UI_THREAD)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(targetClassInfo.getTypeName(), "target")
                .addStatement("this.$N = new $T()", "validationSet", VALIDATION_SET);

        for (FieldValidationInfo fieldValidationInfo : fieldValidationInfos) {
            chooseValidationStrategy(constructorBuilder, fieldValidationInfo);
        }

        return constructorBuilder.build();
    }

    private static void chooseValidationStrategy(MethodSpec.Builder constructorBuilder, FieldValidationInfo fieldValidationInfo) {
        String annotationClass = fieldValidationInfo.getAnnotationClass();

        switch (annotationClass) {
            case NOT_EMPTY_ANNOTATION:
                CodeBlock notEmptyValidationCodeBlock = CodeBlock.builder()
                        .add("\n")
                        .add("{")
                        .add("\n")
                        .indent()
                        .add(createElementDeclarationCode(fieldValidationInfo))
                        .add(createErrorMessageDeclarationCode(fieldValidationInfo.getId()))
                        .addStatement(
                                "this.$N.addValidator(new $T($N, $N))",
                                "validationSet",
                                NOT_EMPTY_VALIDATOR,
                                fieldValidationInfo.getName(),
                                "errorMessage"
                        )
                        .unindent()
                        .add("}")
                        .add("\n")
                        .build();

                constructorBuilder.addCode(notEmptyValidationCodeBlock);
                break;
            case EMAIL_ANNOTATION:
                CodeBlock emailValidationCodeBlock = CodeBlock.builder()
                        .add("\n")
                        .add("{")
                        .add("\n")
                        .indent()
                        .add(createElementDeclarationCode(fieldValidationInfo))
                        .add(createErrorMessageDeclarationCode(fieldValidationInfo.getId()))
                        .addStatement(
                                "this.$N.addValidator(new $T($N, $N))",
                                "validationSet",
                                EMAIL_VALIDATOR,
                                fieldValidationInfo.getName(),
                                "errorMessage"
                        )
                        .unindent()
                        .add("}")
                        .add("\n")
                        .build();

                constructorBuilder.addCode(emailValidationCodeBlock);
                break;
            case PATTERN_ANNOTATION:
                CodeBlock patternValidationCodeBlock = CodeBlock.builder()
                        .add("\n")
                        .add("{")
                        .add("\n")
                        .indent()
                        .add(createElementDeclarationCode(fieldValidationInfo))
                        .add(createErrorMessageDeclarationCode(fieldValidationInfo.getId()))
                        .addStatement(
                                "String pattern = $S",
                                fieldValidationInfo.getElement().getAnnotation(PatternValidation.class).pattern()
                        )
                        .addStatement(
                                "this.$N.addValidator(new $T($N, $N, $N))",
                                "validationSet",
                                PATTERN_VALIDATOR,
                                fieldValidationInfo.getName(),
                                "errorMessage",
                                "pattern"
                        )
                        .unindent()
                        .add("}")
                        .add("\n")
                        .build();

                constructorBuilder.addCode(patternValidationCodeBlock);
                break;
            case LENGTH_ANNOTATION:
                int minLength = fieldValidationInfo.getElement().getAnnotation(LengthValidation.class).min();
                int maxLength = fieldValidationInfo.getElement().getAnnotation(LengthValidation.class).max();
                CodeBlock lengthValidationCodeBlock = CodeBlock.builder()
                        .add("\n")
                        .add("{")
                        .add("\n")
                        .indent()
                        .add(createElementDeclarationCode(fieldValidationInfo))
                        .addStatement("int minLength = $L", minLength)
                        .addStatement("int maxLength = $L", maxLength)
                        .add(createErrorMessageDeclarationCode(fieldValidationInfo.getId()))
                        .addStatement(
                                "this.$N.addValidator(new $T($N, $N, $N, $N))",
                                "validationSet",
                                LENGTH_VALIDATOR,
                                fieldValidationInfo.getName(),
                                "minLength",
                                "maxLength",
                                "errorMessage"
                        )
                        .unindent()
                        .add("}")
                        .add("\n")
                        .build();

                constructorBuilder.addCode(lengthValidationCodeBlock);
                break;

            case ONLY_NUMBER_ANNOTATION:
                CodeBlock numericOnlyValidationCodeBlock = CodeBlock.builder()
                        .add("\n")
                        .add("{")
                        .add("\n")
                        .indent()
                        .add(createElementDeclarationCode(fieldValidationInfo))
                        .add(createErrorMessageDeclarationCode(fieldValidationInfo.getId()))
                        .addStatement(
                                "this.$N.addValidator(new $T($N, $N))",
                                "validationSet",
                                ONLY_NUMBER_VALIDATOR,
                                fieldValidationInfo.getName(),
                                "errorMessage"
                        )
                        .unindent()
                        .add("}")
                        .add("\n")
                        .build();

                constructorBuilder.addCode(numericOnlyValidationCodeBlock);
                break;
            case PASSWORD_ANNOTATION:
                int minPasswordLength = fieldValidationInfo.getElement().getAnnotation(PasswordValidation.class).min();
                String passwordPattern = fieldValidationInfo.getElement().getAnnotation(PasswordValidation.class).pattern();
                CodeBlock passwordValidationCodeBlock = CodeBlock.builder()
                        .add("\n")
                        .addStatement(
                                "$T passwordField = $N.$N",
                                fieldValidationInfo.getTypeName(),
                                "target",
                                fieldValidationInfo.getName()
                        )
                        .add("\n")
                        .add("{")
                        .add("\n")
                        .indent()
                        .addStatement("int min = $L", minPasswordLength)
                        .addStatement("String pattern = $S", passwordPattern)
                        .add(createErrorMessageDeclarationCode(fieldValidationInfo.getId()))
                        .addStatement(
                                "this.$N.addValidator(new $T($N, $N, $N, $N))",
                                "validationSet",
                                PASSWORD_VALIDATOR,
                                "passwordField",
                                "min",
                                "pattern",
                                "errorMessage"
                        )
                        .unindent()
                        .add("}")
                        .add("\n")
                        .build();

                constructorBuilder.addCode(passwordValidationCodeBlock);
                break;
            case CONFIRM_PASSWORD_ANNOTATION:
                CodeBlock confirmPasswordValidationCodeBlock = CodeBlock.builder()
                        .add("\n")
                        .add("{")
                        .add("\n")
                        .indent()
                        .add(createElementDeclarationCode(fieldValidationInfo))
                        .add(createErrorMessageDeclarationCode(fieldValidationInfo.getId()))
                        .addStatement(
                                "this.$N.addValidator(new $T($N, $N, $N))",
                                "validationSet",
                                CONFIRM_PASSWORD_VALIDATOR,
                                "passwordField",
                                fieldValidationInfo.getName(),
                                "errorMessage"
                        )
                        .unindent()
                        .add("}")
                        .add("\n")
                        .build();

                constructorBuilder.addCode(confirmPasswordValidationCodeBlock);
                break;
        }
    }

    private static CodeBlock createElementDeclarationCode(FieldValidationInfo fieldValidationInfo) {
        return CodeBlock.builder()
                .addStatement(
                        "$T $N = $N.$N",
                        fieldValidationInfo.getTypeName(),
                        fieldValidationInfo.getName(),
                        "target",
                        fieldValidationInfo.getName()
                )
                .build();
    }

    private static CodeBlock createErrorMessageDeclarationCode(Id id) {
        return CodeBlock.builder()
                .addStatement(
                        "String errorMessage = $N.getString($L)",
                        "target",
                        id.getCode()
                )
                .build();
    }

    private static MethodSpec createValidateMethod() {
        return MethodSpec.methodBuilder("validateFields")
                .addAnnotation(UI_THREAD)
                .addAnnotation(OVERRIDE)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return this.$N.$N()", "validationSet", "isValid")
                .returns(TypeName.BOOLEAN)
                .build();
    }

    private static MethodSpec createClearValidationMethod() {
        return MethodSpec.methodBuilder("clearValidations")
                .addAnnotation(UI_THREAD)
                .addAnnotation(OVERRIDE)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.$N.$N()", "validationSet", "clearValidators")
                .build();
    }

}