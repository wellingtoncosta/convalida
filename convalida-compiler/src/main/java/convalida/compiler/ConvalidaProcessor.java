package convalida.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;

import convalida.annotations.EmailValidation;
import convalida.annotations.NotEmptyValidation;
import convalida.annotations.PasswordValidation;

/**
 * @author Wellington Costa on 13/06/2017.
 */
@AutoService(Processor.class)
public class ConvalidaProcessor extends AbstractProcessor {

    private static final ClassName UI_THREAD = ClassName.get("android.support.annotation", "UiThread");

    //private static final ClassName TEXT_INPUT_LAYOUT = ClassName.get("android.support.design.widget", "TextInputLayout");
    //private static final ClassName EDIT_TEXT = ClassName.get("android.widget", "EditText");

    private Elements elements;
    private Messager messager;
    private Filer filer;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        this.elements = processingEnvironment.getElementUtils();
        this.messager = processingEnvironment.getMessager();
        this.filer = processingEnvironment.getFiler();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supportedAnnotations = new HashSet<>();

        supportedAnnotations.add(NotEmptyValidation.class.getCanonicalName());
        supportedAnnotations.add(EmailValidation.class.getCanonicalName());
        supportedAnnotations.add(PasswordValidation.class.getCanonicalName());

        return supportedAnnotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {
        System.out.println("Processing...");

        for (Element element : roundEnvironment.getElementsAnnotatedWith(NotEmptyValidation.class)) {
            try {
                JavaFile javaFile = cookJava(element);
                javaFile.writeTo(filer);
            } catch (Exception e) {
                error(element, "Couldn't generate validation class");
            }
        }

        return false;
    }

    private JavaFile cookJava(Element element) {
        Element parentElement = element.getEnclosingElement();
        TypeName parentTypeName = TypeName.get(parentElement.asType());

        String packageName = elements.getPackageOf(element).toString();
        String className = parentElement.getSimpleName().toString() + "_Validation";

        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addAnnotation(UI_THREAD)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.$N = $N", "target", "target")
                .build();

        TypeSpec classValidator = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addField(parentTypeName, "target", Modifier.PRIVATE)
                .addMethod(constructor)
                .build();


        return JavaFile.builder(packageName, classValidator)
                .addFileComment("Generated code from Convalida. Do not modify!")
                .build();
    }

    private void error(Element element, String message, Object... args) {
        printMessage(Kind.ERROR, element, message, args);
    }

    private void printMessage(Kind kind, Element element, String message, Object[] args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }

        messager.printMessage(kind, message, element);
    }
}
