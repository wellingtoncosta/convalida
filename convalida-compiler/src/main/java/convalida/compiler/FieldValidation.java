package convalida.compiler;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;

/**
 * @author Wellington Costa on 19/06/2017.
 */
class FieldValidation {

    private Element element;
    private String name;
    private TypeName type;

    FieldValidation(Element element) {
        this.element = element;
        this.name = element.getSimpleName().toString();
        this.type = TypeName.get(element.asType());
    }

    public Element getElement() {
        return element;
    }

    String getName() {
        return name;
    }

    TypeName getType() {
        return type;
    }

}
