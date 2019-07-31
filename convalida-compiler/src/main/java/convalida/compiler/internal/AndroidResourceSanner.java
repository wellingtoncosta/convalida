package convalida.compiler.internal;

import com.squareup.javapoet.ClassName;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import convalida.compiler.internal.scanners.IdScanner;
import convalida.compiler.internal.scanners.RClassScanner;

/**
 * @author Wellington Costa on 31/07/2019.
 */
public final class AndroidResourceSanner {

    private final Elements elementUtils;

    private final Types typeUtils;

    private final Map<QualifiedId, Id> symbols;

    private final Trees trees;

    private final Set<Class<? extends Annotation>> supportedAnnotations;

    private static boolean initialized = false;

    private AndroidResourceSanner(
            ProcessingEnvironment processingEnvironment,
            Set<Class<? extends Annotation>> supportedAnnotations
    ) {
        this.elementUtils = processingEnvironment.getElementUtils();
        this.typeUtils = processingEnvironment.getTypeUtils();
        this.symbols = new LinkedHashMap<>();
        this.supportedAnnotations = supportedAnnotations;
        this.trees = initTrees(processingEnvironment);
    }

    private static AndroidResourceSanner scanner;

    public static void init(
            ProcessingEnvironment processingEnvironment,
            Set<Class<? extends Annotation>> supportedAnnotations
    ) {
        if (initialized) {
            throw new IllegalStateException("Cannot call init more than once.");
        } else {
            Objects.requireNonNull(
                    processingEnvironment,
                    "processingEnvironment parameter cannot be null."
            );

            Objects.requireNonNull(
                    supportedAnnotations,
                    "supportedAnnotations parameter cannot be null."
            );

            initialized = true;

            scanner = new AndroidResourceSanner(processingEnvironment, supportedAnnotations);
        }
    }

    private Trees initTrees(ProcessingEnvironment processingEnvironment) {
        try {
            return Trees.instance(processingEnvironment);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public static QualifiedId elementToQualifiedId(Element element, int id) {
        return new QualifiedId(scanner.elementUtils.getPackageOf(element).getQualifiedName().toString(), id);
    }

    public static Id getId(QualifiedId qualifiedId) {
        if (scanner.symbols.get(qualifiedId) == null) {
            scanner.symbols.put(qualifiedId, new Id(qualifiedId.id));
        }
        return scanner.symbols.get(qualifiedId);
    }

    private static AnnotationMirror getMirror(
            Element element,
            Class<? extends Annotation> annotation
    ) {
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            if (annotationMirror.getAnnotationType().toString().equals(annotation.getCanonicalName())) {
                return annotationMirror;
            }
        }
        return null;
    }

    public static void scanForRClasses(RoundEnvironment env) {
        if (scanner.trees == null) return;

        RClassScanner rClassScanner = new RClassScanner();

        for (Class<? extends Annotation> annotation : scanner.supportedAnnotations) {
            for (Element element : env.getElementsAnnotatedWith(annotation)) {
                JCTree tree = (JCTree) scanner.trees.getTree(element, getMirror(element, annotation));
                if (tree != null) { // tree can be null if the references are compiled types and not source
                    String respectivePackageName = scanner.elementUtils.getPackageOf(element).getQualifiedName().toString();
                    rClassScanner.setCurrentPackageName(respectivePackageName);
                    tree.accept(rClassScanner);
                }
            }
        }

        for (Map.Entry<String, Set<String>> packageNameToRClassSet : rClassScanner.getRClasses().entrySet()) {
            String respectivePackageName = packageNameToRClassSet.getKey();
            for (String rClass : packageNameToRClassSet.getValue()) {
                parseRClass(respectivePackageName, rClass);
            }
        }
    }

    private static void parseRClass(String respectivePackageName, String rClass) {
        Element element;

        try {
            element = scanner.elementUtils.getTypeElement(rClass);
        } catch (MirroredTypeException mte) {
            element = scanner.typeUtils.asElement(mte.getTypeMirror());
        }

        JCTree tree = (JCTree) scanner.trees.getTree(element);
        if (tree != null) {
            IdScanner idScanner = new IdScanner(scanner.symbols, scanner.elementUtils.getPackageOf(element)
                    .getQualifiedName().toString(), respectivePackageName);
            tree.accept(idScanner);
        } else {
            parseCompiledR(respectivePackageName, (TypeElement) element);
        }
    }

    private static void parseCompiledR(String respectivePackageName, TypeElement rClass) {
        for (Element element : rClass.getEnclosedElements()) {
            String innerClassName = element.getSimpleName().toString();
            if (innerClassName.equals("string")) {
                for (Element enclosedElement : element.getEnclosedElements()) {
                    if (enclosedElement instanceof VariableElement) {
                        VariableElement variableElement = (VariableElement) enclosedElement;
                        Object value = variableElement.getConstantValue();

                        if (value instanceof Integer) {
                            int id = (Integer) value;
                            ClassName rClassName = ClassName.get(
                                    scanner.elementUtils.getPackageOf(variableElement).toString(),
                                    "R",
                                    innerClassName
                            );

                            String resourceName = variableElement.getSimpleName().toString();

                            QualifiedId qualifiedId = new QualifiedId(respectivePackageName, id);

                            scanner.symbols.put(qualifiedId, new Id(id, rClassName, resourceName));
                        }
                    }
                }
            }
        }
    }

}
