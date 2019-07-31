package convalida.compiler.model;

import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * @author Wellington Costa on 27/09/2017.
 */
public class ValidationClass {

    public final String className;
    public final String packageName;
    public final TypeName typeName;
    public final TypeElement typeElement;
    public final List<ValidationField> fields;

    private Element validateButton;
    private Element onValidationSuccessMethod;
    private Element onValidationErrorMethod;
    private Element clearValidationsButton;

    public ValidationClass(Element element, Elements elements) {
        this.packageName = elements.getPackageOf(element).toString();
        this.className = element.getSimpleName().toString() + "FieldsValidation";
        this.typeName = TypeName.get(element.asType());
        this.typeElement = (TypeElement) element;
        this.fields = new ArrayList<>();
    }

    public void addField(ValidationField field) {
        fields.add(field);
    }

    public Element getValidateButton() {
        return validateButton;
    }

    public void setValidateButton(Element validateButton) {
        this.validateButton = validateButton;
    }

    public Element getOnValidationSuccessMethod() {
        return onValidationSuccessMethod;
    }

    public void setOnValidationSuccessMethod(Element onValidationSuccessMethod) {
        this.onValidationSuccessMethod = onValidationSuccessMethod;
    }

    public Element getOnValidationErrorMethod() {
        return onValidationErrorMethod;
    }

    public void setOnValidationErrorMethod(Element onValidationErrorMethod) {
        this.onValidationErrorMethod = onValidationErrorMethod;
    }

    public Element getClearValidationsButton() {
        return clearValidationsButton;
    }

    public void setClearValidationsButton(Element clearValidationsButton) {
        this.clearValidationsButton = clearValidationsButton;
    }

}