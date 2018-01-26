package convalida.compiler.internal;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;

/**
 * @author Wellington Costa on 27/09/2017.
 */

public class ValidationField {

    public final Element element;
    public final String name;
    public final TypeName typeName;
    public final String annotationClass;
    public final Id id;

    public ValidationField(Element element, String annotationClass, Id id) {
        this.element = element;
        this.name = element.getSimpleName().toString();
        this.typeName = TypeName.get(element.asType());
        this.annotationClass = annotationClass;
        this.id = id;
    }

}