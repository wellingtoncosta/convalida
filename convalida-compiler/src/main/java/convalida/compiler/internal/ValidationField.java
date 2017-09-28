package convalida.compiler.internal;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;

/**
 * @author Wellington Costa on 27/09/2017.
 */

public class ValidationField {

    private Element element;
    private String name;
    private TypeName typeName;
    private String annotationClass;
    private Id id;

    public ValidationField(Element element, String annotationClass, Id id) {
        this.element = element;
        this.name = element.getSimpleName().toString();
        this.typeName = TypeName.get(element.asType());
        this.annotationClass = annotationClass;
        this.id = id;
    }

    public Element getElement() {
        return element;
    }

    public String getName() {
        return name;
    }

    public TypeName getTypeName() {
        return typeName;
    }

    public String getAnnotationClass() {
        return annotationClass;
    }

    public Id getId() {
        return id;
    }
}