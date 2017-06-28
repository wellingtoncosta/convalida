package convalida.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Set;

import javax.lang.model.element.Modifier;

import convalida.annotations.ConfirmPasswordValidation;
import convalida.annotations.EmailValidation;
import convalida.annotations.NotEmptyValidation;
import convalida.annotations.PasswordValidation;
import convalida.annotations.PatternValidation;

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
    private static final String PASSWORD_ANNOTATION         = "convalida.annotations.PasswordValidation";
    private static final String CONFIRM_PASSWORD_ANNOTATION = "convalida.annotations.ConfirmPasswordValidation";

    private static final String VALIDATORS_PACKAGE      = "convalida.library.validation.validator";

    private static final ClassName NOT_EMPTY_VALIDATOR          = ClassName.get(VALIDATORS_PACKAGE, "NotEmptyValidator");
    private static final ClassName EMAIL_VALIDATOR              = ClassName.get(VALIDATORS_PACKAGE, "EmailValidator");
    private static final ClassName PATTERN_VALIDATOR            = ClassName.get(VALIDATORS_PACKAGE, "PatternValidator");
    private static final ClassName PASSWORD_VALIDATOR           = ClassName.get(VALIDATORS_PACKAGE, "PasswordValidator");
    private static final ClassName CONFIRM_PASSWORD_VALIDATOR   = ClassName.get(VALIDATORS_PACKAGE, "ConfirmPasswordValidator");


    static JavaFile cookJava(TargetInfo targetInfo, Set<FieldInfo> fieldInfos) {
        TypeSpec classValidator = TypeSpec.classBuilder(targetInfo.getClassName())
                .addSuperinterface(VALIDATOR)
                .addModifiers(Modifier.PUBLIC)
                .addField(VALIDATION_SET, "validationSet", Modifier.PRIVATE)
                .addMethod(createConstructor(targetInfo, fieldInfos))
                .addMethod(createValidateMethod())
                .addMethod(createClearValidationMethod())
                .build();

        return JavaFile.builder(targetInfo.getPackageName(), classValidator)
                .addFileComment("Generated code from Convalida. Do not modify!")
                .build();
    }

    private static MethodSpec createConstructor(TargetInfo targetInfo, Set<FieldInfo> fieldInfos) {
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addAnnotation(UI_THREAD)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(targetInfo.getTypeName(), "target")
                .addStatement("this.$N = new $T()", "validationSet", VALIDATION_SET);

        for (FieldInfo fieldInfo : fieldInfos) {
            chooseValidationStrategy(constructorBuilder, fieldInfo);
        }

        return constructorBuilder.build();
    }

    private static void chooseValidationStrategy(MethodSpec.Builder constructorBuilder, FieldInfo fieldInfo) {
        String annotationClass = fieldInfo.getAnnotationClass();

        switch (annotationClass) {
            case NOT_EMPTY_ANNOTATION:
                int notEmptyValidationErrorMessage = fieldInfo.getElement().getAnnotation(NotEmptyValidation.class).value();
                CodeBlock notEmptyValidationCodeBlock = CodeBlock.builder()
                        .add("\n")
                        .add("{")
                        .add("\n")
                        .indent()
                        .add(createElementDeclarationCode(fieldInfo))
                        .add(createErrorMessageDeclarationCode(notEmptyValidationErrorMessage))
                        .addStatement(
                                "this.$N.addValidator(new $T($N, $N))",
                                "validationSet",
                                NOT_EMPTY_VALIDATOR,
                                fieldInfo.getName(),
                                "errorMessage"
                        )
                        .unindent()
                        .add("}")
                        .add("\n")
                        .build();

                constructorBuilder.addCode(notEmptyValidationCodeBlock);
                break;
            case EMAIL_ANNOTATION:
                int emailValidationErrorMessage = fieldInfo.getElement().getAnnotation(EmailValidation.class).value();
                CodeBlock emailValidationCodeBlock = CodeBlock.builder()
                        .add("\n")
                        .add("{")
                        .add("\n")
                        .indent()
                        .add(createElementDeclarationCode(fieldInfo))
                        .add(createErrorMessageDeclarationCode(emailValidationErrorMessage))
                        .addStatement(
                                "this.$N.addValidator(new $T($N, $N))",
                                "validationSet",
                                EMAIL_VALIDATOR,
                                fieldInfo.getName(),
                                "errorMessage"
                        )
                        .unindent()
                        .add("}")
                        .add("\n")
                        .build();

                constructorBuilder.addCode(emailValidationCodeBlock);
                break;
            case PATTERN_ANNOTATION:
                int patternValidationErrorMessage = fieldInfo.getElement().getAnnotation(PatternValidation.class).errorMessage();
                CodeBlock patternValidationCodeBlock = CodeBlock.builder()
                        .add("\n")
                        .add("{")
                        .add("\n")
                        .indent()
                        .add(createElementDeclarationCode(fieldInfo))
                        .add(createErrorMessageDeclarationCode(patternValidationErrorMessage))
                        .addStatement(
                                "String pattern = $S",
                                fieldInfo.getElement().getAnnotation(PatternValidation.class).pattern()
                        )
                        .addStatement(
                                "this.$N.addValidator(new $T($N, $N, $N))",
                                "validationSet",
                                PATTERN_VALIDATOR,
                                fieldInfo.getName(),
                                "errorMessage",
                                "pattern"
                        )
                        .unindent()
                        .add("}")
                        .add("\n")
                        .build();

                constructorBuilder.addCode(patternValidationCodeBlock);
                break;
            case PASSWORD_ANNOTATION:
                int passwordValidationErrorMessage = fieldInfo.getElement().getAnnotation(PasswordValidation.class).value();
                CodeBlock passwordValidationCodeBlock = CodeBlock.builder()
                        .add("\n")
                        .addStatement(
                                "$T passwordElement = $N.$N",
                                fieldInfo.getTypeName(),
                                "target",
                                fieldInfo.getName()
                        )
                        .add("\n")
                        .add("{")
                        .add("\n")
                        .indent()
                        .add(createErrorMessageDeclarationCode(passwordValidationErrorMessage))
                        .addStatement(
                                "this.$N.addValidator(new $T($N, $N))",
                                "validationSet",
                                PASSWORD_VALIDATOR,
                                "passwordElement",
                                "errorMessage"
                        )
                        .unindent()
                        .add("}")
                        .add("\n")
                        .build();

                constructorBuilder.addCode(passwordValidationCodeBlock);
                break;
            case CONFIRM_PASSWORD_ANNOTATION:
                int confirmPasswordValidationErrorMessage = fieldInfo.getElement().getAnnotation(ConfirmPasswordValidation.class).value();
                CodeBlock confirmPasswordValidationCodeBlock = CodeBlock.builder()
                        .add("\n")
                        .add("{")
                        .add("\n")
                        .indent()
                        .add(createElementDeclarationCode(fieldInfo))
                        .add(createErrorMessageDeclarationCode(confirmPasswordValidationErrorMessage))
                        .addStatement(
                                "this.$N.addValidator(new $T($N, $N, $N))",
                                "validationSet",
                                CONFIRM_PASSWORD_VALIDATOR,
                                "passwordElement",
                                fieldInfo.getName(),
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

    private static CodeBlock createElementDeclarationCode(FieldInfo fieldInfo) {
        return CodeBlock.builder()
                .addStatement(
                        "$T $N = $N.$N",
                        fieldInfo.getTypeName(),
                        fieldInfo.getName(),
                        "target",
                        fieldInfo.getName()
                )
                .build();
    }

    private static CodeBlock createErrorMessageDeclarationCode(int errorMessage) {
        return CodeBlock.builder()
                .addStatement(
                        "String errorMessage = $N.getString($L)",
                        "target",
                        errorMessage
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