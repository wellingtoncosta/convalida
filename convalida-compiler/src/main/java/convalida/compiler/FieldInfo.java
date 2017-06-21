package convalida.compiler;

import com.squareup.javapoet.TypeName;
import java.lang.annotation.Annotation;
import javax.lang.model.element.Element;

/**
 * @author Wellington Costa on 19/06/2017.
 */
class FieldInfo {

    private Element element;
    private String name;
    private TypeName typeName;
    private String annotationClass;

    FieldInfo(Element element, String annotationClass) {
        this.element = element;
        this.name = element.getSimpleName().toString();
        this.typeName = TypeName.get(element.asType());
        this.annotationClass = annotationClass;
    }

    public Element getElement() {
        return element;
    }

    String getName() {
        return name;
    }

    TypeName getTypeName() {
        return typeName;
    }

    public String getAnnotationClass() {
        return annotationClass;
    }
}
