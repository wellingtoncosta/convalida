package convalida.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.Set;

import javax.lang.model.element.Modifier;

/**
 * @author  Wellington Costa on 19/06/2017.
 */
class JavaFiler {

    private static final ClassName UI_THREAD = ClassName.get("android.support.annotation", "UiThread");
    private static final ClassName OVERRIDE = ClassName.get("java.lang", "Override");
    private static final ClassName VALIDATOR = ClassName.get("convalida.library", "Validator");

    static JavaFile cookJava(TargetInfo targetInfo, Set<FieldValidation> fields) {
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addAnnotation(UI_THREAD)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(targetInfo.getTypeName(), "target")
                .addStatement("this.$N = $N", "target", "target")
                .build();

        TypeSpec classValidator = TypeSpec.classBuilder(targetInfo.getClassName())
                .addSuperinterface(VALIDATOR)
                .addModifiers(Modifier.PUBLIC)
                .addField(targetInfo.getTypeName(), "target", Modifier.PRIVATE)
                .addMethod(constructor)
                .addMethod(createValidateMethod(fields))
                .addMethod(createClearValidationMethod(fields))
                .build();


        return JavaFile.builder(targetInfo.getPackageName(), classValidator)
                .addFileComment("Generated code from Convalida. Do not modify!")
                .build();
    }

    private static MethodSpec createValidateMethod(Set<FieldValidation> fields) {
        MethodSpec.Builder validateMethodBuilder = MethodSpec.methodBuilder("validate")
                .addAnnotation(UI_THREAD)
                .addAnnotation(OVERRIDE)
                .addModifiers(Modifier.PUBLIC);

        for (FieldValidation field : fields) {
            validateMethodBuilder.addStatement("this.$N.$N.setErrorEnabled(true)", "target", field.getName());
            validateMethodBuilder.addStatement("this.$N.$N.setError($S)", "target", field.getName(), "Field required");
        }

        return validateMethodBuilder.build();
    }

    private static MethodSpec createClearValidationMethod(Set<FieldValidation> fields) {
        MethodSpec.Builder clearMethodBuilder = MethodSpec.methodBuilder("clear")
                .addAnnotation(UI_THREAD)
                .addAnnotation(OVERRIDE)
                .addModifiers(Modifier.PUBLIC);

        for (FieldValidation field : fields) {
            clearMethodBuilder.addStatement("this.$N.$N.setErrorEnabled(false)", "target", field.getName());
            clearMethodBuilder.addStatement("this.$N.$N.setError(null)", "target", field.getName());
        }

        return clearMethodBuilder.build();
    }

}
