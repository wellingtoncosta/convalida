package convalida.compiler;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * @author Wellington Costa on 19/06/2017.
 */
class TargetInfo {

    private String packageName;
    private String className;
    private TypeName typeName;
    private TypeElement typeElement;

    TargetInfo(Element element, Elements elements) {
        this.packageName = elements.getPackageOf(element).toString();
        this.className = element.getSimpleName().toString() + "_Validation";
        this.typeName = TypeName.get(element.asType());
        this.typeElement = (TypeElement) element;
    }

    String getPackageName() {
        return packageName;
    }

    String getClassName() {
        return className;
    }

    TypeName getTypeName() {
        return typeName;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }
}
