package convalida.compiler;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.stream.Collectors;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import convalida.annotations.LengthValidation;
import convalida.annotations.PasswordValidation;
import convalida.annotations.PatternValidation;
import convalida.compiler.internal.Id;
import convalida.compiler.internal.ValidationClass;
import convalida.compiler.internal.ValidationField;

/**
 * @author Wellington Costa on 19/06/2017.
 */
class JavaFiler {

    static JavaFile cookJava(ValidationClass validationClass) {
        TypeSpec.Builder validationClassBuilder = TypeSpec.classBuilder(validationClass.getClassName())
                .addSuperinterface(Constants.VALIDATOR)
                .addModifiers(Modifier.PUBLIC)
                .addField(Constants.VALIDATOR_SET, "validationSet", Modifier.PRIVATE);

        validationClassBuilder.addMethod(createConstructor(validationClass, validationClassBuilder));
        validationClassBuilder.addMethod(createValidateMethod());
        validationClassBuilder.addMethod(createClearValidationMethod());

        return JavaFile.builder(validationClass.getPackageName(), validationClassBuilder.build())
                .addFileComment("Generated code from Convalida. Do not modify!")
                .build();
    }

    private static MethodSpec createConstructor(ValidationClass validationClass, TypeSpec.Builder validationClassBuilder) {
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addAnnotation(Constants.UI_THREAD)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(validationClass.getTypeName(), "target")
                .addStatement("this.$N = new $T()", "validationSet", Constants.VALIDATOR_SET);

        chooseValidationStrategy(constructorBuilder, validationClass);

        createValidateOnClickCodeBlock(validationClass, constructorBuilder, validationClassBuilder);
        createClearValidationsOnClickCodeBlock(validationClass, constructorBuilder, validationClassBuilder);

        return constructorBuilder.build();
    }

    private static void chooseValidationStrategy(MethodSpec.Builder constructorBuilder, ValidationClass validationClass) {
        validationClass.getFields()
                .forEach(validationField -> {
                    String annotationClass = validationField.getAnnotationClass();

                    switch (annotationClass) {
                        case Constants.NOT_EMPTY_ANNOTATION:
                            createNotEmptyValidationCodeBlock(constructorBuilder, validationField);
                            break;
                        case Constants.EMAIL_ANNOTATION:
                            createEmailValidationCodeBlock(constructorBuilder, validationField);
                            break;
                        case Constants.PATTERN_ANNOTATION:
                            createPatternValidationCodeBlock(constructorBuilder, validationField);
                            break;
                        case Constants.LENGTH_ANNOTATION:
                            createLengthValidationCodeBlock(constructorBuilder, validationField);
                            break;
                        case Constants.ONLY_NUMBER_ANNOTATION:
                            createOnlyNumberValidationCodeBlock(constructorBuilder, validationField);
                            break;
                        case Constants.PASSWORD_ANNOTATION:
                            createPasswordValidationCodeBlock(constructorBuilder, validationField);
                            break;
                        case Constants.CONFIRM_PASSWORD_ANNOTATION:
                            ValidationField passwordValidationField = validationClass.getFields()
                                    .stream()
                                    .filter(field -> field.getAnnotationClass().equals(Constants.PASSWORD_ANNOTATION))
                                    .collect(Collectors.toList())
                                    .get(0);

                            createConfirmPasswordValidationCodeBlock(constructorBuilder, passwordValidationField, validationField);
                            break;
                    }
                });
    }

    private static void createNotEmptyValidationCodeBlock(MethodSpec.Builder constructorBuilder, ValidationField validationField) {
        CodeBlock notEmptyValidationCodeBlock = CodeBlock.builder()
                .add("\n")
                .add("{")
                .add("\n")
                .indent()
                .add(createElementDeclarationCode(validationField))
                .add(createErrorMessageDeclarationCode(validationField.getId()))
                .addStatement(
                        "this.$N.addValidator(new $T($N, $N))",
                        "validationSet",
                        Constants.NOT_EMPTY_VALIDATOR,
                        validationField.getName(),
                        "errorMessage"
                )
                .unindent()
                .add("}")
                .add("\n")
                .build();

        constructorBuilder.addCode(notEmptyValidationCodeBlock);
    }

    private static void createEmailValidationCodeBlock(MethodSpec.Builder constructorBuilder, ValidationField validationField) {
        CodeBlock emailValidationCodeBlock = CodeBlock.builder()
                .add("\n")
                .add("{")
                .add("\n")
                .indent()
                .add(createElementDeclarationCode(validationField))
                .add(createErrorMessageDeclarationCode(validationField.getId()))
                .addStatement(
                        "this.$N.addValidator(new $T($N, $N))",
                        "validationSet",
                        Constants.EMAIL_VALIDATOR,
                        validationField.getName(),
                        "errorMessage"
                )
                .unindent()
                .add("}")
                .add("\n")
                .build();

        constructorBuilder.addCode(emailValidationCodeBlock);
    }

    private static void createPatternValidationCodeBlock(MethodSpec.Builder constructorBuilder, ValidationField validationField) {
        CodeBlock patternValidationCodeBlock = CodeBlock.builder()
                .add("\n")
                .add("{")
                .add("\n")
                .indent()
                .add(createElementDeclarationCode(validationField))
                .add(createErrorMessageDeclarationCode(validationField.getId()))
                .addStatement(
                        "String pattern = $S",
                        validationField.getElement().getAnnotation(PatternValidation.class).pattern()
                )
                .addStatement(
                        "this.$N.addValidator(new $T($N, $N, $N))",
                        "validationSet",
                        Constants.PATTERN_VALIDATOR,
                        validationField.getName(),
                        "errorMessage",
                        "pattern"
                )
                .unindent()
                .add("}")
                .add("\n")
                .build();

        constructorBuilder.addCode(patternValidationCodeBlock);
    }

    private static void createLengthValidationCodeBlock(MethodSpec.Builder constructorBuilder, ValidationField validationField) {
        int minLength = validationField.getElement().getAnnotation(LengthValidation.class).min();
        int maxLength = validationField.getElement().getAnnotation(LengthValidation.class).max();
        CodeBlock lengthValidationCodeBlock = CodeBlock.builder()
                .add("\n")
                .add("{")
                .add("\n")
                .indent()
                .add(createElementDeclarationCode(validationField))
                .addStatement("int minLength = $L", minLength)
                .addStatement("int maxLength = $L", maxLength)
                .add(createErrorMessageDeclarationCode(validationField.getId()))
                .addStatement(
                        "this.$N.addValidator(new $T($N, $N, $N, $N))",
                        "validationSet",
                        Constants.LENGTH_VALIDATOR,
                        validationField.getName(),
                        "minLength",
                        "maxLength",
                        "errorMessage"
                )
                .unindent()
                .add("}")
                .add("\n")
                .build();

        constructorBuilder.addCode(lengthValidationCodeBlock);
    }

    private static void createOnlyNumberValidationCodeBlock(MethodSpec.Builder constructorBuilder, ValidationField validationField) {
        CodeBlock numericOnlyValidationCodeBlock = CodeBlock.builder()
                .add("\n")
                .add("{")
                .add("\n")
                .indent()
                .add(createElementDeclarationCode(validationField))
                .add(createErrorMessageDeclarationCode(validationField.getId()))
                .addStatement(
                        "this.$N.addValidator(new $T($N, $N))",
                        "validationSet",
                        Constants.ONLY_NUMBER_VALIDATOR,
                        validationField.getName(),
                        "errorMessage"
                )
                .unindent()
                .add("}")
                .add("\n")
                .build();

        constructorBuilder.addCode(numericOnlyValidationCodeBlock);
    }

    private static void createPasswordValidationCodeBlock(MethodSpec.Builder constructorBuilder, ValidationField validationField) {
        int minPasswordLength = validationField.getElement().getAnnotation(PasswordValidation.class).min();
        String passwordPattern = validationField.getElement().getAnnotation(PasswordValidation.class).pattern();
        CodeBlock passwordValidationCodeBlock = CodeBlock.builder()
                .add("\n")
                .add("{")
                .add("\n")
                .indent()
                .add(createElementDeclarationCode(validationField))
                .addStatement("int min = $L", minPasswordLength)
                .addStatement("String pattern = $S", passwordPattern)
                .add(createErrorMessageDeclarationCode(validationField.getId()))
                .addStatement(
                        "this.$N.addValidator(new $T($N, $N, $N, $N))",
                        "validationSet",
                        Constants.PASSWORD_VALIDATOR,
                        validationField.getName(),
                        "min",
                        "pattern",
                        "errorMessage"
                )
                .unindent()
                .add("}")
                .add("\n")
                .build();

        constructorBuilder.addCode(passwordValidationCodeBlock);
    }

    private static void createConfirmPasswordValidationCodeBlock(
            MethodSpec.Builder constructorBuilder,
            ValidationField passwordField,
            ValidationField confirmPasswordField) {

        CodeBlock confirmPasswordValidationCodeBlock = CodeBlock.builder()
                .add("\n")
                .add("{")
                .add("\n")
                .indent()
                .add(createElementDeclarationCode(passwordField))
                .add(createElementDeclarationCode(confirmPasswordField))
                .add(createErrorMessageDeclarationCode(confirmPasswordField.getId()))
                .addStatement(
                        "this.$N.addValidator(new $T($N, $N, $N))",
                        "validationSet",
                        Constants.CONFIRM_PASSWORD_VALIDATOR,
                        passwordField.getName(),
                        confirmPasswordField.getName(),
                        "errorMessage"
                )
                .unindent()
                .add("}")
                .add("\n")
                .build();

        constructorBuilder.addCode(confirmPasswordValidationCodeBlock);
    }

    private static CodeBlock createElementDeclarationCode(ValidationField validationField) {
        return CodeBlock.builder()
                .addStatement(
                        "$T $N = $N.$N",
                        validationField.getTypeName(),
                        validationField.getName(),
                        "target",
                        validationField.getName()
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
                .addAnnotation(Constants.UI_THREAD)
                .addAnnotation(Constants.OVERRIDE)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return this.$N.$N()", "validationSet", "isValid")
                .returns(TypeName.BOOLEAN)
                .build();
    }

    private static MethodSpec createClearValidationMethod() {
        return MethodSpec.methodBuilder("clearValidations")
                .addAnnotation(Constants.UI_THREAD)
                .addAnnotation(Constants.OVERRIDE)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.$N.$N()", "validationSet", "clearValidators")
                .build();
    }

    private static void createValidateOnClickCodeBlock(
            ValidationClass validationClass,
            MethodSpec.Builder methodSpecBuilder,
            TypeSpec.Builder validationClassBuilder) {
        Element element = validationClass.getValidateButton();

        if (element != null) {
            createValidateOnClickMethod(validationClass, validationClassBuilder);

            methodSpecBuilder.addCode(CodeBlock.builder()
                    .add("\n")
                    .addStatement(
                            "$N.$N.setOnClickListener(validateOnClickListener($N))",
                            "target",
                            element.getSimpleName().toString(),
                            "target"
                    )
                    .build());
        }
    }

    private static void createValidateOnClickMethod(ValidationClass validationClass, TypeSpec.Builder validationClassBuilder) {
        validationClassBuilder.addMethod(MethodSpec.methodBuilder("validateOnClickListener")
                .addAnnotation(Constants.UI_THREAD)
                .addModifiers(Modifier.PRIVATE)
                .addParameter(ParameterSpec.builder(
                        validationClass.getTypeName(),
                        "target",
                        Modifier.FINAL
                        ).build()
                )
                .addCode(
                        "return new $T() {",
                        Constants.VIEW_ONCLICK_LISTENER
                )
                .addCode("\n")
                .addCode(CodeBlock.builder().indent().build())
                .addCode(
                        "@$T public void onClick($T view) {",
                        Constants.OVERRIDE,
                        Constants.VIEW
                )
                .addCode("\n")
                .addCode(CodeBlock.builder().indent().build())
                .addCode("if(validateFields()) {")
                .addCode("\n")
                .addCode(CodeBlock.builder().indent().build())
                .addCode(
                        "target.$N();",
                        validationClass.getOnValidationSuccessMethod().getSimpleName()
                )
                .addCode("\n")
                .addCode(CodeBlock.builder().unindent().build())
                .addCode("} else {")
                .addCode("\n")
                .addCode(CodeBlock.builder().indent().build())
                .addCode(
                        "target.$N();",
                        validationClass.getOnValidationErrorMethod().getSimpleName()
                )
                .addCode("\n")
                .addCode(CodeBlock.builder().unindent().build())
                .addCode("}")
                .addCode("\n")
                .addCode(CodeBlock.builder().unindent().build())
                .addCode("}")
                .addCode("\n")
                .addCode(CodeBlock.builder().unindent().build())
                .addCode("};")
                .addCode("\n")
                .returns(Constants.VIEW_ONCLICK_LISTENER).build());
    }

    private static void createClearValidationsOnClickCodeBlock(
            ValidationClass validationClass,
            MethodSpec.Builder methodSpecBuilder,
            TypeSpec.Builder validationClassBuilder) {
        Element element = validationClass.getClearValidationsButton();

        if (element != null) {
            createClearValidationsOnClickMethod(validationClass, validationClassBuilder);

            methodSpecBuilder.addCode(CodeBlock.builder()
                    .addStatement(
                            "$N.$N.setOnClickListener(clearValidationsOnClickListener(target))",
                            "target",
                            element.getSimpleName().toString()
                    )
                    .build());
        }
    }

    private static void createClearValidationsOnClickMethod(ValidationClass validationClass, TypeSpec.Builder validationClassBuilder) {
        validationClassBuilder.addMethod(MethodSpec.methodBuilder("clearValidationsOnClickListener")
                .addAnnotation(Constants.UI_THREAD)
                .addModifiers(Modifier.PRIVATE)
                .addParameter(ParameterSpec.builder(
                        validationClass.getTypeName(),
                        "target",
                        Modifier.FINAL
                        ).build()
                )
                .addCode(
                        "return new $T() {",
                        Constants.VIEW_ONCLICK_LISTENER
                )
                .addCode("\n")
                .addCode(CodeBlock.builder().indent().build())
                .addCode(
                        "@$T public void onClick($T view) {",
                        Constants.OVERRIDE,
                        Constants.VIEW
                )
                .addCode("\n")
                .addCode(CodeBlock.builder().indent().build())
                .addCode("clearValidations();")
                .addCode("\n")
                .addCode(CodeBlock.builder().unindent().build())
                .addCode("}")
                .addCode("\n")
                .addCode(CodeBlock.builder().unindent().build())
                .addCode("};")
                .addCode("\n")
                .returns(Constants.VIEW_ONCLICK_LISTENER).build());
    }

}