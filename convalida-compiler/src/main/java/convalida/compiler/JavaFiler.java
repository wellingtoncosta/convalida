package convalida.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Set;

import javax.lang.model.element.Modifier;

import convalida.annotations.EmailValidation;
import convalida.annotations.NotEmptyValidation;
import convalida.annotations.PasswordValidation;
import convalida.annotations.PatternValidation;

/**
 * @author  Wellington Costa on 19/06/2017.
 */
class JavaFiler {

    private static final ClassName UI_THREAD            = ClassName.get("android.support.annotation", "UiThread");
    private static final ClassName OVERRIDE             = ClassName.get("java.lang", "Override");

    private static final ClassName VALIDATOR            = ClassName.get("convalida.library", "ConvalidaValidator");
    private static final ClassName VALIDATION_SET       = ClassName.get("convalida.library.validation", "ValidationSet");

    private static final String NOT_EMPTY_ANNOTATION    = "convalida.annotations.NotEmptyValidation";
    private static final String EMAIL_ANNOTATION        = "convalida.annotations.EmailValidation";
    private static final String PASSWORD_ANNOTATION     = "convalida.annotations.PasswordValidation";
    private static final String PATTERN_ANNOTATION      = "convalida.annotations.PatternValidation";

    private static final String VALIDATORS_PACKAGE      = "convalida.library.validation.validator";

    private static final ClassName NOT_EMPTY_VALIDATOR  = ClassName.get(VALIDATORS_PACKAGE, "NotEmptyValidator");
    private static final ClassName EMAIL_VALIDATOR      = ClassName.get(VALIDATORS_PACKAGE, "EmailValidator");
    private static final ClassName PASSWORD_VALIDATOR   = ClassName.get(VALIDATORS_PACKAGE, "PasswordValidator");
    private static final ClassName PATTERN_VALIDATOR    = ClassName.get(VALIDATORS_PACKAGE, "PatternValidator");

    static JavaFile cookJava(TargetInfo targetInfo, Set<FieldInfo> fieldInfos) {
        TypeSpec classValidator = TypeSpec.classBuilder(targetInfo.getClassName())
                .addSuperinterface(VALIDATOR)
                .addModifiers(Modifier.PUBLIC)
                .addField(targetInfo.getTypeName(), "target", Modifier.PRIVATE)
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
                .addStatement("this.$N = $N", "target", "target")
                .addStatement("this.$N = new $T()", "validationSet", VALIDATION_SET);

        for (FieldInfo fieldInfo : fieldInfos) {
            addValidatorStrategy(constructorBuilder, fieldInfo);
        }

        return constructorBuilder.build();
    }

    private static void addValidatorStrategy(MethodSpec.Builder constructorBuilder, FieldInfo fieldInfo) {
        String annotationClass = fieldInfo.getAnnotationClass();

        switch (annotationClass) {
            case NOT_EMPTY_ANNOTATION:
                constructorBuilder.addStatement(
                        "this.$N.addValidator(new $T(this.$N.$N, $S))",
                        "validationSet",
                        NOT_EMPTY_VALIDATOR,
                        "target",
                        fieldInfo.getName(),
                        fieldInfo.getElement().getAnnotation(NotEmptyValidation.class).errorMessage()
                );
                break;
            case EMAIL_ANNOTATION:
                constructorBuilder.addStatement(
                        "this.$N.addValidator(new $T(this.$N.$N, $S))",
                        "validationSet",
                        EMAIL_VALIDATOR,
                        "target",
                        fieldInfo.getName(),
                        fieldInfo.getElement().getAnnotation(EmailValidation.class).errorMessage()
                );
                break;
            case PASSWORD_ANNOTATION:
                constructorBuilder.addStatement(
                        "this.$N.addValidator(new $T(this.$N.$N, $S))",
                        "validationSet",
                        PASSWORD_VALIDATOR,
                        "target",
                        fieldInfo.getName(),
                        fieldInfo.getElement().getAnnotation(PasswordValidation.class).errorMessage()
                );
                break;
            case PATTERN_ANNOTATION:
                constructorBuilder.addStatement(
                        "this.$N.addValidator(new $T(this.$N.$N, $S, $S))",
                        "validationSet",
                        PATTERN_VALIDATOR,
                        "target",
                        fieldInfo.getName(),
                        fieldInfo.getElement().getAnnotation(PatternValidation.class).errorMessage(),
                        fieldInfo.getElement().getAnnotation(PatternValidation.class).pattern()
                );
                break;
        }
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
