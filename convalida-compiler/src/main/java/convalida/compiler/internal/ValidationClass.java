package convalida.compiler.internal;

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

    private String className;
    private String packageName;
    private TypeName typeName;
    private TypeElement typeElement;
    private List<ValidationField> fields;
    private Element validateButton;
    private Element onValidationSuccessMethod;
    private Element onValidationErrorMethod;
    private Element clearValidationsButton;

    public ValidationClass(Element element, Elements elements) {
        this.packageName = elements.getPackageOf(element).toString();
        this.className = element.getSimpleName().toString() + "_Validation";
        this.typeName = TypeName.get(element.asType());
        this.typeElement = (TypeElement) element;
        this.fields = new ArrayList<>();
    }

    public void addField(ValidationField field) {
        fields.add(field);
    }

    public String getClassName() {
        return className;
    }

    public String getPackageName() {
        return packageName;
    }

    public TypeName getTypeName() {
        return typeName;
    }

    public TypeElement getTypeElement() {
        return typeElement;
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

    public List<ValidationField> getFields() {
        return fields;
    }
}