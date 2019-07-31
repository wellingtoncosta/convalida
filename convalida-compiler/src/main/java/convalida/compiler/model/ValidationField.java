package convalida.compiler.model;

import java.lang.annotation.Annotation;

import javax.lang.model.element.Element;

import convalida.compiler.internal.Id;

/**
 * @author Wellington Costa on 27/09/2017.
 */
public class ValidationField {

    public final Element element;
    public final String name;
    public final String annotationClassName;
    public final Id id;
    public final boolean autoDismiss;

    public ValidationField(Element element, Class<? extends Annotation> annotationClassName, Id id, boolean autoDismiss) {
        this.element = element;
        this.name = element.getSimpleName().toString();
        this.annotationClassName = annotationClassName.getCanonicalName();
        this.id = id;
        this.autoDismiss = autoDismiss;
    }

}